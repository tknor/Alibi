package cz.tomas.alibi.config;

import cz.tomas.alibi.common.controller.ItemController;
import cz.tomas.alibi.common.controller.PersonController;
import cz.tomas.alibi.common.domain.Person;
import cz.tomas.alibi.common.entity.ItemEntity;
import cz.tomas.alibi.common.service.ItemService;
import cz.tomas.alibi.common.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({PersonController.class, ItemController.class})
@Import(SecurityConfig.class)
class SecurityConfigTest {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_ADMIN = "Bearer admin-token";
    private static final String BEARER_TOKEN_MEMBER = "Bearer member-token";

    private static final String PERSON_JSON = """
            {
              "name": "Jane",
              "phone": "123"
            }
            """;

    private static final String ITEM_JSON = """
            {
              "label": "Radio",
              "category": "TOOL"
            }
            """;

    @Autowired
    MockMvc mvc;

    @MockitoBean
    JwtDecoder jwtDecoder;

    @MockitoBean
    PersonService personService;

    @MockitoBean
    ItemService itemService;

    @BeforeEach
    void tokens() {
        when(jwtDecoder.decode("admin-token")).thenReturn(token("ADMIN"));
        when(jwtDecoder.decode("member-token")).thenReturn(token("CREW_MEMBER"));
    }

    @Test
    void listingItemsDoesNotRequireAuthentication() throws Exception {

        mvc.perform(get("/api/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isOk());
    }

    @Test
    void anonymousWritesRequireAuthentication() throws Exception {

        mvc.perform(post("/api/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PERSON_JSON))
                .andExpect(status().isUnauthorized());

        mvc.perform(post("/api/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ITEM_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void nonAdminCannotCreatePeopleOrItems() throws Exception {


        mvc.perform(post("/api/person")
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_MEMBER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PERSON_JSON))
                .andExpect(status().isForbidden());

        mvc.perform(post("/api/item")
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_MEMBER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ITEM_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanCreatePersonFromJson() throws Exception {
        when(personService.createPerson(any())).thenReturn(Person.builder().name("Jane").phone("123").build());

        mvc.perform(post("/api/person")
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PERSON_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane"));

        verify(personService).createPerson(new cz.tomas.alibi.common.dto.CreatePersonCommand("Jane", "123"));
    }

    @Test
    void adminCanCreateItemFromJson() throws Exception {
        when(itemService.createItem(any())).thenAnswer(invocation -> invocation.getArgument(0, ItemEntity.class));

        mvc.perform(post("/api/item")
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ITEM_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("Radio"));
    }

    private Jwt token(String role) {
        return Jwt.withTokenValue(role)
                .header("arbitrary", "none")
                .subject("test-user")
                .claim("realm_access", Map.of("roles", List.of(role)))
                .build();
    }
}

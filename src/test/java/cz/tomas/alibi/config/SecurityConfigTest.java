package cz.tomas.alibi.config;

import cz.tomas.alibi.common.controller.ItemController;
import cz.tomas.alibi.common.controller.OperationController;
import cz.tomas.alibi.common.controller.PersonController;
import cz.tomas.alibi.common.domain.ItemCategory;
import cz.tomas.alibi.common.dto.AddCrewMemberCommand;
import cz.tomas.alibi.common.dto.AddCrewMemberRequest;
import cz.tomas.alibi.common.dto.CreateOperationCommand;
import cz.tomas.alibi.common.dto.CreateOperationRequest;
import cz.tomas.alibi.common.dto.CreatePersonRequest;
import cz.tomas.alibi.common.dto.ItemDto;
import cz.tomas.alibi.common.entity.Item;
import cz.tomas.alibi.common.entity.Operation;
import cz.tomas.alibi.common.entity.Person;
import cz.tomas.alibi.common.service.ItemService;
import cz.tomas.alibi.common.service.OperationCommandService;
import cz.tomas.alibi.common.service.OperationQueryService;
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
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({PersonController.class, ItemController.class, OperationController.class})
@Import(SecurityConfig.class)
class SecurityConfigTest {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_ADMIN = "Bearer admin-token";
    private static final String BEARER_TOKEN_MANAGER = "Bearer manager-token";
    private static final String BEARER_TOKEN_MEMBER = "Bearer member-token";

    private static final UUID OPERATION_ID = UUID.fromString("10000000-0000-0000-0000-000000000001");
    private static final UUID PERSON_ID = UUID.fromString("20000000-0000-0000-0000-000000000002");
    private static final UUID ITEM_ID = UUID.fromString("30000000-0000-0000-0000-000000000003");

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    JwtDecoder jwtDecoder;

    @MockitoBean
    PersonService personService;

    @MockitoBean
    ItemService itemService;

    @MockitoBean
    OperationQueryService operationQueryService;

    @MockitoBean
    OperationCommandService operationCommandService;

    @BeforeEach
    void tokens() {
        when(jwtDecoder.decode("admin-token")).thenReturn(token("ADMIN"));
        when(jwtDecoder.decode("manager-token")).thenReturn(token("CREW_MANAGER"));
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
                        .content(objectMapper.writeValueAsString(createPersonRequest())))
                .andExpect(status().isUnauthorized());

        mvc.perform(post("/api/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequest())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void nonAdminCannotCreatePeopleOrItems() throws Exception {
        mvc.perform(post("/api/person")
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_MANAGER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPersonRequest())))
                .andExpect(status().isForbidden());

        mvc.perform(post("/api/item")
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_MANAGER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequest())))
                .andExpect(status().isForbidden());
    }

    @Test
    void listingPeopleRequiresManagerOrAdminRole() throws Exception {
        when(personService.getAllPersons()).thenReturn(List.of());

        mvc.perform(get("/api/person"))
                .andExpect(status().isUnauthorized());

        mvc.perform(get("/api/person")
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_MEMBER))
                .andExpect(status().isForbidden());

        mvc.perform(get("/api/person")
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_MANAGER))
                .andExpect(status().isOk());

        mvc.perform(get("/api/person")
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_ADMIN))
                .andExpect(status().isOk());
    }

    @Test
    void readingPersonDetailsRequiresManagerOrAdminRole() throws Exception {
        when(personService.getPerson(PERSON_ID)).thenReturn(person());

        mvc.perform(get("/api/person/{personId}", PERSON_ID)
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_MEMBER))
                .andExpect(status().isForbidden());

        mvc.perform(get("/api/person/{personId}", PERSON_ID)
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_MANAGER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationCodeNames").isArray());

        mvc.perform(get("/api/person/{personId}", PERSON_ID)
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_ADMIN))
                .andExpect(status().isOk());
    }

    @Test
    void readingOperationsRequiresManagerOrAdminRole() throws Exception {
        when(operationQueryService.getAllOperations()).thenReturn(List.of());
        when(operationQueryService.getOperation(OPERATION_ID)).thenReturn(operation());

        mvc.perform(get("/api/operation"))
                .andExpect(status().isUnauthorized());

        mvc.perform(get("/api/operation/{operationId}", OPERATION_ID)
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_MEMBER))
                .andExpect(status().isForbidden());

        mvc.perform(get("/api/operation/{operationId}", OPERATION_ID)
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_MANAGER))
                .andExpect(status().isOk());

        mvc.perform(get("/api/operation/{operationId}", OPERATION_ID)
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_ADMIN))
                .andExpect(status().isOk());
    }

    @Test
    void onlyManagerCanCreateOperationsOrChangeCrews() throws Exception {
        when(operationCommandService.createOperation(createOperationCommand())).thenReturn(operation());
        when(operationCommandService.addCrewMember(addCrewMemberCommand())).thenReturn(operation());
        when(operationCommandService.removeCrewMember(OPERATION_ID, PERSON_ID)).thenReturn(operation());

        mvc.perform(post("/api/operation")
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOperationRequest())))
                .andExpect(status().isForbidden());

        mvc.perform(post("/api/operation/{operationId}/crew-member", OPERATION_ID)
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_MEMBER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addCrewMemberRequest())))
                .andExpect(status().isForbidden());

        mvc.perform(delete("/api/operation/{operationId}/crew-member/{personId}", OPERATION_ID, PERSON_ID)
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_ADMIN))
                .andExpect(status().isForbidden());

        mvc.perform(post("/api/operation")
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_MANAGER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOperationRequest())))
                .andExpect(status().isOk());

        mvc.perform(post("/api/operation/{operationId}/crew-member", OPERATION_ID)
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_MANAGER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addCrewMemberRequest())))
                .andExpect(status().isOk());

        mvc.perform(delete("/api/operation/{operationId}/crew-member/{personId}", OPERATION_ID, PERSON_ID)
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_MANAGER))
                .andExpect(status().isOk());
    }

    @Test
    void adminCanCreatePersonFromJson() throws Exception {
        when(personService.createPerson(any())).thenReturn(person());

        mvc.perform(post("/api/person")
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPersonRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"));

        verify(personService).createPerson(createPersonRequest());
    }

    @Test
    void adminCanCreateItemFromJson() throws Exception {
        when(itemService.createItem(any())).thenReturn(item());

        mvc.perform(post("/api/item")
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("Radio"));
    }

    private CreatePersonRequest createPersonRequest() {
        return new CreatePersonRequest("Jane Doe", "123");
    }

    private ItemDto itemRequest() {
        return new ItemDto("Radio", "TOOL");
    }

    private CreateOperationRequest createOperationRequest() {
        return new CreateOperationRequest("Operation Harambe", 5);
    }

    private CreateOperationCommand createOperationCommand() {
        return new CreateOperationCommand("Operation Harambe", 5);
    }

    private AddCrewMemberRequest addCrewMemberRequest() {
        return new AddCrewMemberRequest(PERSON_ID);
    }

    private AddCrewMemberCommand addCrewMemberCommand() {
        return new AddCrewMemberCommand(OPERATION_ID, PERSON_ID);
    }

    private Jwt token(String role) {
        return Jwt.withTokenValue(role)
                .header("arbitrary", "none")
                .subject("test-user")
                .claim("realm_access", Map.of("roles", List.of(role)))
                .build();
    }

    private Operation operation() {
        return Operation.builder()
                .id(OPERATION_ID)
                .codeName("Operation Harambe")
                .crewSizeLimit(5)
                .build();
    }

    private Person person() {
        return Person.builder()
                .id(PERSON_ID)
                .name("Jane Doe")
                .phone("123")
                .build();
    }

    private Item item() {
        return Item.builder()
                .id(ITEM_ID)
                .label("Radio")
                .category(ItemCategory.TOOL)
                .build();
    }
}

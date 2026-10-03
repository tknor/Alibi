package cz.tomas.alibi.common.controller;

import cz.tomas.alibi.common.dto.AddCrewMemberCommand;
import cz.tomas.alibi.common.dto.AddCrewMemberRequest;
import cz.tomas.alibi.common.dto.OperationSummaryDto;
import cz.tomas.alibi.common.dtomapper.OperationDtoMapper;
import cz.tomas.alibi.common.dtomapper.PersonDtoMapper;
import cz.tomas.alibi.common.exception.ApiExceptionHandler;
import cz.tomas.alibi.common.exception.CrewManagementException;
import cz.tomas.alibi.common.exception.ResourceNotFoundException;
import cz.tomas.alibi.common.service.OperationCommandService;
import cz.tomas.alibi.common.service.OperationQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OperationController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({ApiExceptionHandler.class, PersonDtoMapper.class, OperationDtoMapper.class})
class OperationControllerTest {

    private static final UUID OPERATION_ID = UUID.fromString("10000000-0000-0000-0000-000000000001");
    private static final UUID PERSON_ID = UUID.fromString("20000000-0000-0000-0000-000000000002");

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    OperationQueryService operationQueryService;

    @MockitoBean
    OperationCommandService operationCommandService;

    @Test
    void operationCollectionReturnsOccupancySummary() throws Exception {
        when(operationQueryService.getAllOperations()).thenReturn(List.of(
                new OperationSummaryDto(OPERATION_ID, "Operation Harambe", 2, 5)
        ));

        mvc.perform(get("/api/operation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(OPERATION_ID.toString()))
                .andExpect(jsonPath("$[0].crewSize").value(2))
                .andExpect(jsonPath("$[0].crewSizeLimit").value(5));
    }

    @Test
    void missingOperationReturnsNotFoundProblem() throws Exception {
        when(operationQueryService.getOperation(OPERATION_ID))
                .thenThrow(new ResourceNotFoundException("Operation was not found."));

        mvc.perform(get("/api/operation/{operationId}", OPERATION_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Resource not found"))
                .andExpect(jsonPath("$.detail").value("Operation was not found."))
                .andExpect(jsonPath("$.instance").value("/api/operation/" + OPERATION_ID));
    }

    @Test
    void crewRuleViolationReturnsConflictProblem() throws Exception {
        AddCrewMemberCommand command = new AddCrewMemberCommand(OPERATION_ID, PERSON_ID);
        AddCrewMemberRequest request = new AddCrewMemberRequest(PERSON_ID);
        when(operationCommandService.addCrewMember(command))
                .thenThrow(new CrewManagementException("Crew size limit has been reached."));

        mvc.perform(post("/api/operation/{operationId}/crew-member", OPERATION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Crew management conflict"))
                .andExpect(jsonPath("$.detail").value("Crew size limit has been reached."));
    }
}

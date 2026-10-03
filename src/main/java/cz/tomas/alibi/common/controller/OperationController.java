package cz.tomas.alibi.common.controller;

import cz.tomas.alibi.common.dto.AddCrewMemberCommand;
import cz.tomas.alibi.common.dto.AddCrewMemberRequest;
import cz.tomas.alibi.common.dto.CreateOperationCommand;
import cz.tomas.alibi.common.dto.CreateOperationRequest;
import cz.tomas.alibi.common.dto.OperationDetailDto;
import cz.tomas.alibi.common.dto.OperationSummaryDto;
import cz.tomas.alibi.common.dtomapper.OperationDtoMapper;
import cz.tomas.alibi.common.service.OperationCommandService;
import cz.tomas.alibi.common.service.OperationQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/operation")
public class OperationController {

    private final OperationQueryService operationQueryService;
    private final OperationCommandService operationCommandService;
    private final OperationDtoMapper operationDtoMapper;

    @GetMapping
    public List<OperationSummaryDto> getAllOperations() {
        return operationQueryService.getAllOperations();
    }

    @GetMapping("/{operationId}")
    public OperationDetailDto getOperation(@PathVariable UUID operationId) {
        return operationDtoMapper.toOperationDetailDto(operationQueryService.getOperation(operationId));
    }

    @PostMapping
    public OperationDetailDto createOperation(@Valid @RequestBody CreateOperationRequest request) {
        CreateOperationCommand command = new CreateOperationCommand(
                request.codeName(),
                request.crewSizeLimit()
        );

        return operationDtoMapper.toOperationDetailDto(operationCommandService.createOperation(command));
    }

    @PostMapping("/{operationId}/crew-member")
    public OperationDetailDto addCrewMember(
            @PathVariable UUID operationId,
            @Valid @RequestBody AddCrewMemberRequest request
    ) {
        AddCrewMemberCommand command = new AddCrewMemberCommand(operationId, request.personId());

        return operationDtoMapper.toOperationDetailDto(operationCommandService.addCrewMember(command));
    }

    @DeleteMapping("/{operationId}/crew-member/{personId}")
    public OperationDetailDto removeCrewMember(
            @PathVariable UUID operationId,
            @PathVariable UUID personId
    ) {
        return operationDtoMapper.toOperationDetailDto(operationCommandService.removeCrewMember(operationId, personId));
    }
}

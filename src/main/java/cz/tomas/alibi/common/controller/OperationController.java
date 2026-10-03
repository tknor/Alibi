package cz.tomas.alibi.common.controller;

import cz.tomas.alibi.common.dto.AddCrewMemberCommand;
import cz.tomas.alibi.common.dto.AddCrewMemberRequest;
import cz.tomas.alibi.common.dto.CreateOperationCommand;
import cz.tomas.alibi.common.dto.CreateOperationRequest;
import cz.tomas.alibi.common.dto.OperationDetailDto;
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

    // TODO implement projection: OperationSummaryDto with id, codeName, (crewMembersCount / crewSizeLimit) as occupancy
    @GetMapping
    public List<OperationDetailDto> getAllOperations() {
        return operationQueryService.getAllOperations().stream()
                .map(OperationDtoMapper::toOperationDto)
                .toList();
    }

    @GetMapping("/{operationId}")
    public OperationDetailDto getOperation(@PathVariable UUID operationId) {
        return OperationDtoMapper.toOperationDto(operationQueryService.getOperation(operationId));
    }

    @PostMapping
    public OperationDetailDto createOperation(@Valid @RequestBody CreateOperationRequest request) {
        CreateOperationCommand command = new CreateOperationCommand(
                request.codeName(),
                request.crewSizeLimit()
        );

        return OperationDtoMapper.toOperationDto(operationCommandService.createOperation(command));
    }

    @PostMapping("/{operationId}/crew-member")
    public OperationDetailDto addCrewMember(
            @PathVariable UUID operationId,
            @Valid @RequestBody AddCrewMemberRequest request
    ) {
        AddCrewMemberCommand command = new AddCrewMemberCommand(operationId, request.personId());

        return OperationDtoMapper.toOperationDto(
                operationCommandService.addCrewMember(command)
        );
    }

    @DeleteMapping("/{operationId}/crew-member/{personId}")
    public OperationDetailDto removeCrewMember(
            @PathVariable UUID operationId,
            @PathVariable UUID personId
    ) {
        return OperationDtoMapper.toOperationDto(
                operationCommandService.removeCrewMember(operationId, personId)
        );
    }
}

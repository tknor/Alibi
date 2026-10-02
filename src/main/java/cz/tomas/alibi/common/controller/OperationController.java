package cz.tomas.alibi.common.controller;

import cz.tomas.alibi.common.dto.AddCrewMemberCommand;
import cz.tomas.alibi.common.dto.CreateOperationCommand;
import cz.tomas.alibi.common.dto.OperationDetailDto;
import cz.tomas.alibi.common.dtomapper.OperationDtoMapper;
import cz.tomas.alibi.common.entity.Operation;
import cz.tomas.alibi.common.service.OperationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/operation")
public class OperationController {

    private final OperationService operationService;

    // TODO implement projection: OperationSummaryDto with id, codeName, (crewMembersCount / crewSizeLimit) as occupancy
    @GetMapping
    public List<OperationDetailDto> getAllOperations() {
        return operationService.getAllOperationsFull().stream()
                .map(OperationDtoMapper::toOperationDto)
                .toList();
    }

    @GetMapping("/{operationId}")
    public OperationDetailDto getOperation(@PathVariable UUID operationId) {
        return OperationDtoMapper.toOperationDto(operationService.getOperation(operationId));
    }

    @PostMapping
    public OperationDetailDto createOperation(@Valid @RequestBody CreateOperationCommand command) {
        Operation operation = OperationDtoMapper.toOperationEntity(command);
        return OperationDtoMapper.toOperationDto(operationService.createOperation(operation));
    }

    @PostMapping("/{operationId}/crew-member")
    public OperationDetailDto addCrewMember(
            @PathVariable UUID operationId,
            @Valid @RequestBody AddCrewMemberCommand command
    ) {
        return OperationDtoMapper.toOperationDto(
                operationService.addCrewMember(operationId, command.personId())
        );
    }

    @DeleteMapping("/{operationId}/crew-member/{personId}")
    public OperationDetailDto removeCrewMember(
            @PathVariable UUID operationId,
            @PathVariable UUID personId
    ) {
        return OperationDtoMapper.toOperationDto(
                operationService.removeCrewMember(operationId, personId)
        );
    }
}

package cz.tomas.alibi.common.dtomapper;

import cz.tomas.alibi.common.dto.OperationDetailDto;
import cz.tomas.alibi.common.dto.OperationSummaryDto;
import cz.tomas.alibi.common.entity.Operation;
import cz.tomas.alibi.common.repository.OperationSummaryProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OperationDtoMapper {

    private final PersonDtoMapper personDtoMapper;

    public OperationDetailDto toOperationDetailDto(Operation operation) {
        return new OperationDetailDto(
                operation.getId(),
                operation.getCodeName(),
                operation.getCrewSizeLimit(),
                operation.getCrewMembers().stream()
                        .map(crewMember -> personDtoMapper.toPersonSummaryDto(crewMember.getPerson()))
                        .toList()
        );
    }

    public OperationSummaryDto toOperationSummaryDto(OperationSummaryProjection operation) {
        return new OperationSummaryDto(
                operation.getId(),
                operation.getCodeName(),
                Math.toIntExact(operation.getCrewSize()),
                operation.getCrewSizeLimit()
        );
    }
}

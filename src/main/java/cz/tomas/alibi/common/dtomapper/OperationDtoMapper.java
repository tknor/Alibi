package cz.tomas.alibi.common.dtomapper;

import cz.tomas.alibi.common.dto.CrewMemberDto;
import cz.tomas.alibi.common.dto.OperationDetailDto;
import cz.tomas.alibi.common.entity.CrewMember;
import cz.tomas.alibi.common.entity.Operation;

// TODO stop using static methods (for better testability) possibly use MapStruct instead
public final class OperationDtoMapper {

    private OperationDtoMapper() {
    }

    public static OperationDetailDto toOperationDto(Operation operation) {
        return new OperationDetailDto(
                operation.getId(),
                operation.getCodeName(),
                operation.getCrewSizeLimit(),
                operation.getCrewMembers().stream()
                        .map(OperationDtoMapper::toCrewMemberDto)
                        .toList()
        );
    }

    private static CrewMemberDto toCrewMemberDto(CrewMember crewMember) {
        return new CrewMemberDto(
                crewMember.getPerson().getId(),
                crewMember.getPerson().getName(),
                crewMember.getPerson().getPhone()
        );
    }
}

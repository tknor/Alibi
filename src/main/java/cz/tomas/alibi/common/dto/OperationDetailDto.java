package cz.tomas.alibi.common.dto;

import java.util.List;
import java.util.UUID;

public record OperationDetailDto(
        UUID id,
        String codeName,
        Integer crewSizeLimit,
        List<CrewMemberDto> crewMembers
) {
}

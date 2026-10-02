package cz.tomas.alibi.common.dto;

import java.util.UUID;

// TODO remove this DTO, use PersonSummaryDto instead
public record CrewMemberDto(
        UUID personId,
        String name,
        String phone
) {
}

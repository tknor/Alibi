package cz.tomas.alibi.common.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddCrewMemberRequest(
        @NotNull UUID personId
) {
}

package cz.tomas.alibi.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateOperationCommand(
        @NotBlank String codeName,
        @Positive Integer crewSizeLimit
) {
}

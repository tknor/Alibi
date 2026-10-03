package cz.tomas.alibi.common.dto;

import cz.tomas.alibi.common.validation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;

public record CreatePersonRequest(
        @NotBlank String name,
        @ValidPhoneNumber
        String phone
) {
}

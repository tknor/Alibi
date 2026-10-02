package cz.tomas.alibi.common.dto;

import jakarta.validation.constraints.NotBlank;

// TODO add custom validation for phone number ensuring "+" format and mapping that removes whitespace, if the number is at all present
public record CreatePersonCommand(
        @NotBlank String name,
        String phone
) {
}

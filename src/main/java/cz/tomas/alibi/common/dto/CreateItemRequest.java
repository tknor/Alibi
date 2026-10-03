package cz.tomas.alibi.common.dto;

import cz.tomas.alibi.common.domain.ItemCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateItemRequest(
        @NotBlank String label,
        @NotNull ItemCategory category
) {
}

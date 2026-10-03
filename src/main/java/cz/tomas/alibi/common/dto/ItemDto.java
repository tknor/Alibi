package cz.tomas.alibi.common.dto;

import cz.tomas.alibi.common.domain.ItemCategory;

import java.util.UUID;

public record ItemDto(
        UUID id,
        String label,
        ItemCategory category
) {
}

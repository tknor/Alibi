package cz.tomas.alibi.common.dtomapper;

import cz.tomas.alibi.common.domain.ItemCategory;
import cz.tomas.alibi.common.dto.ItemDto;
import cz.tomas.alibi.common.entity.ItemEntity;

public class ItemDtoMapper {

    public static ItemDto toItemDto(ItemEntity itemEntity) {
        return new ItemDto(itemEntity.getLabel(), itemEntity.getCategory().toString());
    }

    public static ItemEntity toItemEntity(ItemDto itemDto) {
        return ItemEntity.builder()
                .label(itemDto.label())
                .category(ItemCategory.valueOf(itemDto.category()))
                .build();
    }
}

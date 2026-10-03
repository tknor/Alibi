package cz.tomas.alibi.common.dtomapper;

import cz.tomas.alibi.common.domain.ItemCategory;
import cz.tomas.alibi.common.dto.ItemDto;
import cz.tomas.alibi.common.entity.Item;

// TODO stop using static methods (for better testability)
// TODO implement the mappers according to best practices
public class ItemDtoMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getLabel(), item.getCategory().toString());
    }

    public static Item toItemEntity(ItemDto itemDto) {
        return Item.builder()
                .label(itemDto.label())
                .category(ItemCategory.valueOf(itemDto.category()))
                .build();
    }
}

package cz.tomas.alibi.common.dtomapper;

import cz.tomas.alibi.common.dto.CreateItemRequest;
import cz.tomas.alibi.common.dto.ItemDto;
import cz.tomas.alibi.common.entity.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemDtoMapper {

    public ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(), item.getLabel(), item.getCategory());
    }

    public Item toItemEntity(CreateItemRequest request) {
        return Item.builder()
                .label(request.label())
                .category(request.category())
                .build();
    }
}

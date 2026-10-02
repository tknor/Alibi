package cz.tomas.alibi.common.controller;

import cz.tomas.alibi.common.dto.ItemDto;
import cz.tomas.alibi.common.dtomapper.ItemDtoMapper;
import cz.tomas.alibi.common.entity.ItemEntity;
import cz.tomas.alibi.common.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/item")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getAllItems() {
        List<ItemEntity> items = itemService.getAllItems();

        return items.stream()
                .map(ItemDtoMapper::toItemDto)
                .toList();
    }

    // TODO command or DTO
    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto dto) {
        ItemEntity created = itemService.createItem(ItemDtoMapper.toItemEntity(dto));
        return ItemDtoMapper.toItemDto(created);
    }
}

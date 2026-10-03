package cz.tomas.alibi.common.controller;

import cz.tomas.alibi.common.domain.ItemCategory;
import cz.tomas.alibi.common.dto.CreateItemRequest;
import cz.tomas.alibi.common.dto.ItemDto;
import cz.tomas.alibi.common.dto.PageResponse;
import cz.tomas.alibi.common.dtomapper.ItemDtoMapper;
import cz.tomas.alibi.common.entity.Item;
import cz.tomas.alibi.common.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/item")
public class ItemController {

    private final ItemService itemService;
    private final ItemDtoMapper itemDtoMapper;

    @GetMapping
    public PageResponse<ItemDto> getItems(
            @RequestParam(required = false) ItemCategory category,
            @PageableDefault(size = 20, sort = "label") Pageable pageable
    ) {
        Page<ItemDto> items = itemService.getItems(category, pageable)
                .map(itemDtoMapper::toItemDto);

        return PageResponse.from(items);
    }

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody CreateItemRequest request) {
        Item created = itemService.createItem(itemDtoMapper.toItemEntity(request));
        return itemDtoMapper.toItemDto(created);
    }
}

package cz.tomas.alibi.common.service;

import cz.tomas.alibi.common.domain.ItemCategory;
import cz.tomas.alibi.common.entity.Item;
import cz.tomas.alibi.common.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public Page<Item> getItems(ItemCategory category, Pageable pageable) {
        if (category == null) {
            return itemRepository.findAll(pageable);
        }
        return itemRepository.findByCategory(category, pageable);
    }

    public Item createItem(Item item) {
        return itemRepository.save(item);
    }
}

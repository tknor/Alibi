package cz.tomas.alibi.common.service;

import cz.tomas.alibi.common.entity.ItemEntity;
import cz.tomas.alibi.common.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public List<ItemEntity> getAllItems() {
        return itemRepository.findAll();
    }

    public ItemEntity createItem(ItemEntity itemEntity) {
        return itemRepository.save(itemEntity);
    }
}

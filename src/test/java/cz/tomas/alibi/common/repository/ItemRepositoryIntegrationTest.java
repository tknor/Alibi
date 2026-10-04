package cz.tomas.alibi.common.repository;

import cz.tomas.alibi.AbstractIntegrationTest;
import cz.tomas.alibi.common.domain.ItemCategory;
import cz.tomas.alibi.common.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
class ItemRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    void itemsCanBeFilteredAndPagedByCategory() {
        itemRepository.saveAll(List.of(
                Item.builder().label("Radio").category(ItemCategory.TOOL).build(),
                Item.builder().label("Jacket").category(ItemCategory.WEARABLE).build()
        ));

        var page = itemRepository.findByCategory(ItemCategory.TOOL, PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
        assertEquals("Radio", page.getContent().getFirst().getLabel());
    }
}

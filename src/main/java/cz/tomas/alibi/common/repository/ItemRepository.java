package cz.tomas.alibi.common.repository;

import cz.tomas.alibi.common.domain.ItemCategory;
import cz.tomas.alibi.common.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {

    Page<Item> findByCategory(ItemCategory category, Pageable pageable);
}

package cz.tomas.alibi.common.repository;

import cz.tomas.alibi.common.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {
}

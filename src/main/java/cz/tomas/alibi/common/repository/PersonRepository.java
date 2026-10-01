package cz.tomas.alibi.common.repository;

import cz.tomas.alibi.common.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PersonRepository extends JpaRepository<PersonEntity, UUID> {

}

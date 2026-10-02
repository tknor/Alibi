package cz.tomas.alibi.common.repository;

import cz.tomas.alibi.common.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {

}

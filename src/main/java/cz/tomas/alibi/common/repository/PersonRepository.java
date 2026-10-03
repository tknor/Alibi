package cz.tomas.alibi.common.repository;

import cz.tomas.alibi.common.entity.Person;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {

    @EntityGraph(attributePaths = {"crewMembers", "crewMembers.operation"})
    @Query("select distinct person from Person person where person.id = :id")
    Optional<Person> findWithOperationsById(UUID id);
}

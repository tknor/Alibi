package cz.tomas.alibi;

import cz.tomas.alibi.common.entity.Operation;
import cz.tomas.alibi.common.entity.Person;
import cz.tomas.alibi.common.repository.OperationRepository;
import cz.tomas.alibi.common.repository.PersonRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
class PersonRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    OperationRepository operationRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    void personDetailQueryFetchesOperationAssociations() {
        Person person = personRepository.save(Person.builder().name("Jane Doe").phone("123").build());
        Operation operation = Operation.builder().codeName("Operation Harambe").build();
        operation.addCrewMember(person);
        operationRepository.saveAndFlush(operation);
        entityManager.clear();

        Person loaded = personRepository.findWithOperationsById(person.getId()).orElseThrow();

        assertTrue(loaded.getCrewMembers().stream()
                .anyMatch(member -> member.getOperation().getCodeName().equals("Operation Harambe")));
    }
}

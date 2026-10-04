package cz.tomas.alibi.common.repository;

import cz.tomas.alibi.AbstractIntegrationTest;
import cz.tomas.alibi.common.entity.Operation;
import cz.tomas.alibi.common.entity.Person;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
class OperationRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    OperationRepository operationRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    void operationSummaryCountsCrewWithoutLoadingDetailGraph() {
        Person person = personRepository.save(Person.builder().name("Jane Doe").phone("123").build());
        Operation operation = Operation.builder()
                .codeName("Operation Harambe")
                .crewSizeLimit(5)
                .build();
        operation.addCrewMember(person);
        operationRepository.saveAndFlush(operation);
        entityManager.clear();

        OperationSummaryProjection summary = operationRepository.findAllSummaries().stream()
                .filter(candidate -> candidate.getId().equals(operation.getId()))
                .findFirst()
                .orElseThrow();

        assertEquals("Operation Harambe", summary.getCodeName());
        assertEquals(1, summary.getCrewSize());
        assertEquals(5, summary.getCrewSizeLimit());
    }
}

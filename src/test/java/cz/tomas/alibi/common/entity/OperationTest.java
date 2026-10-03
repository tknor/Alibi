package cz.tomas.alibi.common.entity;

import cz.tomas.alibi.common.exception.CrewManagementException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OperationTest {

    private static final UUID PERSON_ID = UUID.randomUUID();

    @Test
    void preventsDuplicateCrewAssignments() {
        Operation operation = operation(2);
        Person person = person(PERSON_ID);

        operation.addCrewMember(person);

        assertThrows(
                CrewManagementException.class,
                () -> operation.addCrewMember(person)
        );
    }

    @Test
    void enforcesCrewSizeLimit() {
        Operation operation = operation(1);
        operation.addCrewMember(person(PERSON_ID));

        assertThrows(
                CrewManagementException.class,
                () -> operation.addCrewMember(person(UUID.randomUUID()))
        );
    }

    @Test
    void removesAssignedCrewMember() {
        Operation operation = operation(null);
        operation.addCrewMember(person(PERSON_ID));

        operation.removeCrewMember(PERSON_ID);

        assertEquals(0, operation.getCrewMembers().size());
    }

    @Test
    void rejectsRemovingUnassignedPerson() {
        assertThrows(
                CrewManagementException.class,
                () -> operation(null).removeCrewMember(PERSON_ID)
        );
    }

    private Operation operation(Integer crewSizeLimit) {
        return Operation.builder()
                .codeName("Operation Harambe")
                .crewSizeLimit(crewSizeLimit)
                .build();
    }

    private Person person(UUID id) {
        return Person.builder()
                .id(id)
                .name("Jane Doe")
                .build();
    }
}

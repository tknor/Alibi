package cz.tomas.alibi.common.dtomapper;

import cz.tomas.alibi.common.dto.CreatePersonRequest;
import cz.tomas.alibi.common.dto.PersonDetailDto;
import cz.tomas.alibi.common.dto.PersonSummaryDto;
import cz.tomas.alibi.common.entity.CrewMember;
import cz.tomas.alibi.common.entity.Operation;
import cz.tomas.alibi.common.entity.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonDtoMapper {

    public PersonSummaryDto toPersonSummaryDto(Person person) {
        return new PersonSummaryDto(person.getId(), person.getName());
    }

    public PersonDetailDto toPersonDetailDto(Person person) {
        return new PersonDetailDto(
                person.getId(),
                person.getName(),
                person.getPhone(),
                person.getCrewMembers().stream()
                        .map(CrewMember::getOperation)
                        .map(Operation::getCodeName)
                        .sorted()
                        .toList()
        );
    }

    public Person toPersonEntity(CreatePersonRequest request) {
        return Person.builder()
                .name(request.name())
                .phone(normalizePhone(request.phone()))
                .build();
    }

    private String normalizePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return null;
        }
        return phone.replaceAll("\\s+", "");
    }
}

package cz.tomas.alibi.common.dtomapper;

import cz.tomas.alibi.common.dto.PersonSummaryDto;
import cz.tomas.alibi.common.dto.PersonDetailDto;
import cz.tomas.alibi.common.entity.Person;

// TODO stop using static methods (for better testability)
public class PersonDtoMapper {

    public static PersonSummaryDto toPersonSummaryDto(Person person) {
        return new PersonSummaryDto(person.getId(), person.getName());
    }

    public static PersonDetailDto toPersonDetailDto(Person person) {
        return new PersonDetailDto(person.getId(), person.getName(), person.getPhone());
    }

    public static Person toPersonEntity(PersonDetailDto personDetailDto) {
        Person person = new Person();
        person.setId(personDetailDto.id());
        person.setName(personDetailDto.name());
        person.setPhone(personDetailDto.phone());
        return person;
    }
}

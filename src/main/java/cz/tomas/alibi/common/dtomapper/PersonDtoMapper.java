package cz.tomas.alibi.common.dtomapper;

import cz.tomas.alibi.common.domain.Person;
import cz.tomas.alibi.common.dto.PersonSummaryDto;
import cz.tomas.alibi.common.dto.PersonDetailDto;

public class PersonDtoMapper {

    public static PersonSummaryDto toPersonSummaryDto(Person person) {
        return new PersonSummaryDto(person.getName());
    }

    public static PersonDetailDto toPersonDetailDto(Person person) {
        return new PersonDetailDto(person.getName(), person.getPhone());
    }
}

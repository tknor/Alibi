package cz.tomas.alibi.common.dtomapper;

import cz.tomas.alibi.common.dto.PersonSummaryDto;
import cz.tomas.alibi.common.dto.PersonDetailDto;
import cz.tomas.alibi.common.entity.PersonEntity;

public class PersonDtoMapper {

    public static PersonSummaryDto toPersonSummaryDto(PersonEntity person) {
        return new PersonSummaryDto(person.getName());
    }

    public static PersonDetailDto toPersonDetailDto(PersonEntity person) {
        return new PersonDetailDto(person.getName(), person.getPhone());
    }

    public static PersonEntity toPersonEntity(PersonDetailDto personDetailDto) {
        PersonEntity personEntity = new PersonEntity();
        personEntity.setName(personDetailDto.name());
        personEntity.setPhone(personDetailDto.phone());
        return personEntity;
    }
}

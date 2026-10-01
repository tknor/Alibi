package cz.tomas.alibi.common.jpamapper;

import cz.tomas.alibi.common.domain.Person;
import cz.tomas.alibi.common.entity.PersonEntity;

public class PersonJpaMapper {

    public static Person toDomainObject(PersonEntity entity) {
        return Person.builder()
                .name(entity.getName())
                .phone(entity.getPhone())
                .build();
    }

    public static PersonEntity toEntity(Person person) {
        PersonEntity entity = new PersonEntity();
        entity.setName(person.getName());
        entity.setPhone(person.getPhone());
        return entity;
    }
}

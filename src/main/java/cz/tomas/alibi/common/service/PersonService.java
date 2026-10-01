package cz.tomas.alibi.common.service;

import cz.tomas.alibi.common.domain.Person;
import cz.tomas.alibi.common.dto.CreatePersonCommand;
import cz.tomas.alibi.common.entity.PersonEntity;
import cz.tomas.alibi.common.jpamapper.PersonJpaMapper;
import cz.tomas.alibi.common.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PersonService {

    private final PersonRepository personRepository;

    public List<Person> getAllPersons() {
        return personRepository.findAll().stream()
                .map(PersonJpaMapper::toDomainObject)
                .toList();
    }

    public Person createPerson(CreatePersonCommand command) {

        Person candidate = Person.builder()
                .name(command.name())
                .phone(command.phone())
                .build();

        PersonEntity entity = PersonJpaMapper.toEntity(candidate);

        PersonEntity created = personRepository.save(entity);

        return PersonJpaMapper.toDomainObject(created);
    }
}

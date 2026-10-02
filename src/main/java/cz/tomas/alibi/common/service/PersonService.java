package cz.tomas.alibi.common.service;

import cz.tomas.alibi.common.dto.CreatePersonCommand;
import cz.tomas.alibi.common.entity.Person;
import cz.tomas.alibi.common.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PersonService {

    private final PersonRepository personRepository;

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    // TODO not sure if it is good CQRS practice
    public Person createPerson(CreatePersonCommand command) {

        Person candidate = Person.builder()
                .name(command.name())
                .phone(command.phone())
                .build();

        return personRepository.save(candidate);
    }
}

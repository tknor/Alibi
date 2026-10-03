package cz.tomas.alibi.common.service;

import cz.tomas.alibi.common.dto.CreatePersonRequest;
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

    public Person createPerson(CreatePersonRequest request) {

        Person candidate = Person.builder()
                .name(request.name())
                .phone(request.phone())
                .build();

        return personRepository.save(candidate);
    }
}

package cz.tomas.alibi.common.service;

import cz.tomas.alibi.common.dto.CreatePersonRequest;
import cz.tomas.alibi.common.dtomapper.PersonDtoMapper;
import cz.tomas.alibi.common.entity.Person;
import cz.tomas.alibi.common.exception.ResourceNotFoundException;
import cz.tomas.alibi.common.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonDtoMapper personDtoMapper;

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Person createPerson(CreatePersonRequest request) {
        return personRepository.save(personDtoMapper.toPersonEntity(request));
    }

    public Person getPerson(UUID personId) {
        return personRepository.findWithOperationsById(personId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Person %s was not found.".formatted(personId)
                ));
    }
}

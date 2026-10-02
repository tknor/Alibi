package cz.tomas.alibi.common.service;

import cz.tomas.alibi.common.dto.CreatePersonCommand;
import cz.tomas.alibi.common.entity.PersonEntity;
import cz.tomas.alibi.common.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PersonService {

    private final PersonRepository personRepository;

    public List<PersonEntity> getAllPersons() {
        return personRepository.findAll();
    }

    public PersonEntity createPerson(CreatePersonCommand command) {

        PersonEntity candidate = PersonEntity.builder()
                .name(command.name())
                .phone(command.phone())
                .build();

        return personRepository.save(candidate);
    }
}

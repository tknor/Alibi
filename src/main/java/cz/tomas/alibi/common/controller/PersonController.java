package cz.tomas.alibi.common.controller;

import cz.tomas.alibi.common.dto.CreatePersonRequest;
import cz.tomas.alibi.common.dto.PersonDetailDto;
import cz.tomas.alibi.common.dto.PersonSummaryDto;
import cz.tomas.alibi.common.dtomapper.PersonDtoMapper;
import cz.tomas.alibi.common.entity.Person;
import cz.tomas.alibi.common.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/person")
public class PersonController {

    private final PersonService personService;
    private final PersonDtoMapper personDtoMapper;

    @GetMapping
    public List<PersonSummaryDto> getAllPersons() {
        List<Person> persons = personService.getAllPersons();

        return persons.stream()
                .map(personDtoMapper::toPersonSummaryDto)
                .toList();
    }

    @GetMapping("/{personId}")
    public PersonDetailDto getPerson(@PathVariable UUID personId) {
        return personDtoMapper.toPersonDetailDto(personService.getPerson(personId));
    }

    @PostMapping
    public PersonDetailDto createPerson(@Valid @RequestBody CreatePersonRequest request) {
        Person person = personService.createPerson(request);
        return personDtoMapper.toPersonDetailDto(person);
    }
}

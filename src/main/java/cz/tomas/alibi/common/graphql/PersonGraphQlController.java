package cz.tomas.alibi.common.graphql;

import cz.tomas.alibi.common.entity.Person;
import cz.tomas.alibi.common.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class PersonGraphQlController {

    private final PersonService personService;

    @QueryMapping
    public List<GraphQlPerson> people() {
        return personService.getAllPersons().stream()
                .map(GraphQlPerson::from)
                .toList();
    }

    public record GraphQlPerson(UUID id, String name, String phone) {

        private static GraphQlPerson from(Person person) {
            return new GraphQlPerson(person.getId(), person.getName(), person.getPhone());
        }
    }
}

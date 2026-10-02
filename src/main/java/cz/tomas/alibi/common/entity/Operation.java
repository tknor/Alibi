package cz.tomas.alibi.common.entity;

import cz.tomas.alibi.common.exception.CrewManagementException;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "operation")
public class Operation {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    private UUID id;

    @Column(name = "code_name", nullable = false)
    private String codeName;

    @Column(name = "crew_size_limit")
    private Integer crewSizeLimit;

    @Builder.Default
    @OneToMany(mappedBy = "operation", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CrewMember> crewMembers = new ArrayList<>();

    public void addCrewMember(Person person) {

        boolean alreadyAssigned = crewMembers.stream()
                .map(CrewMember::getPerson)
                .map(Person::getId)
                .anyMatch(personId -> Objects.equals(personId, person.getId()));

        if (alreadyAssigned) {
            throw new CrewManagementException("Crew member is already part of the crew.");
        }

        if (crewSizeLimit != null && crewMembers.size() >= crewSizeLimit) {
            throw new CrewManagementException("Crew size limit has been reached.");
        }

        crewMembers.add(CrewMember.builder()
                .operation(this)
                .person(person)
                .build());
    }

    public void removeCrewMember(UUID personId) {

        CrewMember crewMember = crewMembers.stream()
                .filter(member -> Objects.equals(member.getPerson().getId(), personId))
                .findFirst()
                .orElseThrow(() -> new CrewManagementException("Crew member is not part of the crew."));

        if (!crewMembers.remove(crewMember)) {
            throw new CrewManagementException("Crew member is not part of the crew.");
        }
    }
}

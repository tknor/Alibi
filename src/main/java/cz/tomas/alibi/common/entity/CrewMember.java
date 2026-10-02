package cz.tomas.alibi.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(
        name = "crew_member",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_crew_member_operation_person",
                columnNames = {"operation_id", "person_id"}
        )
)
public class CrewMember {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_id", nullable = false)
    private Operation operation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
}

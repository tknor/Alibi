package cz.tomas.alibi.common.repository;

import cz.tomas.alibi.common.entity.Operation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OperationRepository extends JpaRepository<Operation, UUID> {

    @Query("""
            select operation.id as id,
                   operation.codeName as codeName,
                   count(crewMember.id) as crewSize,
                   operation.crewSizeLimit as crewSizeLimit
            from Operation operation
            left join operation.crewMembers crewMember
            group by operation.id, operation.codeName, operation.crewSizeLimit
            """)
    List<OperationSummaryProjection> findAllSummaries();

    @EntityGraph(attributePaths = {"crewMembers", "crewMembers.person"})
    @Query("select operation from Operation operation where operation.id = :id")
    Optional<Operation> findWithCrewMembersById(UUID id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select operation from Operation operation where operation.id = :id")
    Optional<Operation> findByIdForUpdate(UUID id);
}

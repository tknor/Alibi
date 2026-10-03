package cz.tomas.alibi.common.service;

import cz.tomas.alibi.common.entity.Operation;
import cz.tomas.alibi.common.exception.ResourceNotFoundException;
import cz.tomas.alibi.common.repository.OperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OperationQueryService {

    private final OperationRepository operationRepository;

    public List<Operation> getAllOperations() {
        return operationRepository.findAllWithCrewMembers();
    }

    public Operation getOperation(UUID operationId) {
        return operationRepository.findWithCrewMembersById(operationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Operation %s was not found.".formatted(operationId)
                ));
    }
}

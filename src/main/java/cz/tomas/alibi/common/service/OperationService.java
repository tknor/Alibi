package cz.tomas.alibi.common.service;

import cz.tomas.alibi.common.entity.Operation;
import cz.tomas.alibi.common.entity.Person;
import cz.tomas.alibi.common.exception.ResourceNotFoundException;
import cz.tomas.alibi.common.repository.OperationRepository;
import cz.tomas.alibi.common.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OperationService {

    private final OperationRepository operationRepository;
    private final PersonRepository personRepository;

    @Transactional(readOnly = true)
    public List<Operation> getAllOperationsFull() {
        return operationRepository.findAllWithCrewMembers();
    }

    @Transactional(readOnly = true)
    public Operation getOperation(UUID operationId) {
        return operationRepository.findWithCrewMembersById(operationId)
                .orElseThrow(() -> operationNotFound(operationId));
    }

    @Transactional
    public Operation createOperation(Operation operation) {
        return operationRepository.save(operation);
    }

    // TODO call a dummy audit service which could log the attempt of adding and removing crew members (using new transaction so the attempt is always logged) and call it to log the success of the operation (in the same transaction so that the success is logged only when nothing goes wrong)
    @Transactional
    public Operation addCrewMember(UUID operationId, UUID personId) {
        Operation operation = getOperationForUpdate(operationId);

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> personNotFound(personId));

        operation.addCrewMember(person);
        return operationRepository.save(operation);
    }

    @Transactional
    public Operation removeCrewMember(UUID operationId, UUID personId) {
        Operation operation = getOperationForUpdate(operationId);
        operation.removeCrewMember(personId);
        return operationRepository.save(operation);
    }

    private Operation getOperationForUpdate(UUID operationId) {

        // Lock the operation for update to prevent concurrent modifications
        operationRepository.findByIdForUpdate(operationId)
                .orElseThrow(() -> operationNotFound(operationId));

        // Load the needed entity graph
        return operationRepository.findWithCrewMembersById(operationId)
                .orElseThrow(() -> operationNotFound(operationId));
    }

    private ResourceNotFoundException operationNotFound(UUID operationId) {
        return new ResourceNotFoundException("Operation %s was not found.".formatted(operationId));
    }

    private ResourceNotFoundException personNotFound(UUID personId) {
        return new ResourceNotFoundException("Person %s was not found.".formatted(personId));
    }
}

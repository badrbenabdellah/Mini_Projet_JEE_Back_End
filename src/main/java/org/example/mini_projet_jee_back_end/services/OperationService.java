package org.example.mini_projet_jee_back_end.services;

import org.example.mini_projet_jee_back_end.entities.Operation;
import org.example.mini_projet_jee_back_end.repositories.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OperationService {
    private final OperationRepository operationRepository;

    @Autowired
    public OperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public Operation saveOperation(Operation operation) {
        return operationRepository.save(operation);
    }

    public Operation updateOperation(Operation operation) {
        return operationRepository.save(operation);
    }

    public Operation getOperationById(Long id) {
        return operationRepository.findById(id).orElse(null);
    }

    public void deleteOperation(Long id) {
        operationRepository.deleteById(id);
    }

    public List<Operation> getAllOperations() {
        return operationRepository.findAll();
    }

    public int countAllOperationsByDate(LocalDate dateOperation) {
        return operationRepository.countAllByDateOperation(dateOperation);
    }

    public List<Operation> getOperationsByType(Class<? extends Operation> type) {
        return operationRepository.findByType(type);
    }
}

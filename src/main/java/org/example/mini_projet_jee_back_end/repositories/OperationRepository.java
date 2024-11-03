package org.example.mini_projet_jee_back_end.repositories;

import org.example.mini_projet_jee_back_end.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    public int countAllByDateOperation(LocalDate dateOperation);
}
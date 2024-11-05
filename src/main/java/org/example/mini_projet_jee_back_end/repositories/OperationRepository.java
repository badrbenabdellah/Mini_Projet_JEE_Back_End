package org.example.mini_projet_jee_back_end.repositories;

import org.example.mini_projet_jee_back_end.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    int countAllByDateOperation(LocalDate dateOperation);
    @Query("SELECT o FROM Operation o WHERE TYPE(o) = :type")
    List<Operation> findByType(@Param("type") Class<? extends Operation> type);
}
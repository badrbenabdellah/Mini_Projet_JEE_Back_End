package org.example.mini_projet_jee_back_end.repositories;

import org.example.mini_projet_jee_back_end.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Page<Client> findAll(Pageable pageable);
    int countByJoinDateBetween(LocalDate startDate, LocalDate endDate);
}

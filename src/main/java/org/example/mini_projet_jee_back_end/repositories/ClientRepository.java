package org.example.mini_projet_jee_back_end.repositories;

import org.example.mini_projet_jee_back_end.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Page<Client> findAll(Pageable pageable);
    int countByJoinDateBetween(LocalDate startDate, LocalDate endDate);

}

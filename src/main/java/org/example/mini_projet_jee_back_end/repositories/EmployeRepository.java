package org.example.mini_projet_jee_back_end.repositories;

import org.example.mini_projet_jee_back_end.entities.Employe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeRepository extends JpaRepository<Employe, Long> {
    Employe findEmployeByEmail(String email);
    Employe findEmployeByEmailAndPassword(String email, String password);
}

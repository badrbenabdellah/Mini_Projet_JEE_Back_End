package org.example.mini_projet_jee_back_end.repositories;

import org.example.mini_projet_jee_back_end.entities.Groupe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupeRepository extends JpaRepository<Groupe, Long> {
}
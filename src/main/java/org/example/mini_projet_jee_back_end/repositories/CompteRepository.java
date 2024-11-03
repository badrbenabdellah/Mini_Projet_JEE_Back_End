package org.example.mini_projet_jee_back_end.repositories;

import org.example.mini_projet_jee_back_end.entities.Client;
import org.example.mini_projet_jee_back_end.entities.Compte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompteRepository extends JpaRepository<Compte, Long> {
    public Compte findCompteByClient(Client client);
}
package org.example.mini_projet_jee_back_end.services;

import org.example.mini_projet_jee_back_end.entities.Client;
import org.example.mini_projet_jee_back_end.entities.Compte;
import org.example.mini_projet_jee_back_end.repositories.CompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompteService {
    private final CompteRepository compteRepository;

    @Autowired
    public CompteService(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    public Compte saveCompte(Compte compte) {
        return compteRepository.save(compte);
    }

    public Compte updateCompte(Compte compte) {
        return compteRepository.save(compte);
    }

    public Compte getCompteById(Long id) {
        return compteRepository.findById(id).orElse(null);
    }

    public void deleteCompte(Long id) {
        compteRepository.deleteById(id);
    }

    public List<Compte> getAllComptes() {
        return compteRepository.findAll();
    }

    public Compte getCompteByClient(Client client) {
        return compteRepository.findCompteByClient(client);
    }

    public int countAllAccounts() {
        return (int) compteRepository.count();
    }

}

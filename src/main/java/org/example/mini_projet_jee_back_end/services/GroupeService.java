package org.example.mini_projet_jee_back_end.services;

import org.example.mini_projet_jee_back_end.entities.Groupe;
import org.example.mini_projet_jee_back_end.repositories.GroupeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupeService {
    private final GroupeRepository groupeRepository;

    @Autowired
    public GroupeService(GroupeRepository groupeRepository) {
        this.groupeRepository = groupeRepository;
    }

    public Groupe saveGroupe(Groupe groupe) {
        return groupeRepository.save(groupe);
    }

    public Groupe updateGroupe(Groupe groupe) {
        return groupeRepository.save(groupe);
    }

    public Groupe getGroupeById(Long id) {
        return groupeRepository.findById(id).orElse(null);
    }

    public void deleteGroupe(Long id) {
        groupeRepository.deleteById(id);
    }

    public List<Groupe> getAllGroupes() {
        return groupeRepository.findAll();
    }
}

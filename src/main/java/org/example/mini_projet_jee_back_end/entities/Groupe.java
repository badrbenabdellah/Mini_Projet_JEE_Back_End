package org.example.mini_projet_jee_back_end.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Groupe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numGroupe;
    private String nomGroupe;

    @OneToMany(mappedBy = "groupe")
    @JsonIgnore
    private List<Employe> employes;

    public Groupe() {
    }

    public Groupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
        this.employes = new ArrayList<>();
    }

    public void setNumGroupe(Long numGroupe) {
        this.numGroupe = numGroupe;
    }

    public Long getNumGroupe() {
        return numGroupe;
    }

    public void setNomGroupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }

    public String getNomGroupe() {
        return nomGroupe;
    }

    public void setEmployes(Employe employes) {
        this.employes.add(employes);
    }

    public List<Employe> getEmployes() {
        return employes;
    }

    public void addEmploye(Employe employe) {
        this.employes.add(employe);
        employe.setGroupe(this);
    }
}

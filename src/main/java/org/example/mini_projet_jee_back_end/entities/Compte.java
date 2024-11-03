package org.example.mini_projet_jee_back_end.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE_CPTE", discriminatorType = DiscriminatorType.STRING, length = 2)
public abstract class Compte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numCompte;

    private LocalDate dateCreation;
    private double solde;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "compte", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Operation> operations = new ArrayList<>();

    public Compte() {}

    public Compte(LocalDate dateCreation, double solde) {
        this.dateCreation = dateCreation;
        this.solde = solde;
    }

    // Getters et setters
    public Long getNumCompte() { return numCompte; }
    public void setNumCompte(Long numCompte) { this.numCompte = numCompte; }

    public LocalDate getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDate dateCreation) { this.dateCreation = dateCreation; }

    public double getSolde() { return solde; }
    public void setSolde(double solde) { this.solde = solde; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public List<Operation> getOperations() { return operations; }

    public void addOperation(Operation operation) {
        operations.add(operation);
        operation.setCompte(this); // Assurer la relation bidirectionnelle
    }

    public void removeOperation(Operation operation) {
        operations.remove(operation);
        operation.setCompte(null); // Assurer la relation bidirectionnelle
    }
}

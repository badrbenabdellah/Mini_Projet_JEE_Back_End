package org.example.mini_projet_jee_back_end.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TYPE_OP",discriminatorType=DiscriminatorType.STRING,length=2)
public abstract class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numOperation;
    private LocalDate dateOperation;
    private double montant;
    private String status;

    @ManyToOne
    @JoinColumn(name = "employe_id")
    private Employe employe;

    @ManyToOne
    @JoinColumn(name = "num_cpte", nullable = false)
    private Compte compte;


    public Operation() {
    }

    public Operation(LocalDate dateOperation, double montant) {
        this.dateOperation = dateOperation;
        this.montant = montant;
    }

    public void setNumOperation(Long numOperation) {
        this.numOperation = numOperation;
    }

    public Long getNumOperation() {
        return numOperation;
    }

    public void setDateOperation(LocalDate dateOperation) {
        this.dateOperation = dateOperation;
    }

    public LocalDate getDateOperation() {
        return dateOperation;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public double getMontant() {
        return montant;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}

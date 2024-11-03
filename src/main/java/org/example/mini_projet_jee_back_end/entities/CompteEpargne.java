package org.example.mini_projet_jee_back_end.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("CE")
public class CompteEpargne extends Compte {

    private double taux;

    public CompteEpargne() {}

    public CompteEpargne(LocalDate dateCreation, double solde, double taux) {
        super(dateCreation, solde);
        this.taux = taux;
    }

    public double getTaux() { return taux; }
    public void setTaux(double taux) { this.taux = taux; }
}

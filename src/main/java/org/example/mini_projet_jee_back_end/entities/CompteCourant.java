package org.example.mini_projet_jee_back_end.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("CC")
public class CompteCourant extends Compte {

    private double decouvert;

    private double decouvertInitial;

    public CompteCourant() {}

    public CompteCourant(LocalDate dateCreation, double solde, double decouvert) {
        super(dateCreation, solde);
        this.decouvert = decouvert;
        this.decouvertInitial = decouvert;
    }

    public double getDecouvert() { return decouvert; }
    public void setDecouvert(double decouvert) { this.decouvert = decouvert; }

    public double getDecouvertInitial() { return decouvertInitial; }

    public void setDecouvertInitial(double decouvertInitial) { this.decouvertInitial = decouvertInitial; }
}

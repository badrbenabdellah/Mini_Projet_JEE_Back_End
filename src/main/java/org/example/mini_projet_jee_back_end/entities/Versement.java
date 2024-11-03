package org.example.mini_projet_jee_back_end.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("VE")
public class Versement extends Operation {
    public Versement() {
    }

    public Versement(LocalDate dateOperation, double montant) {
        super(dateOperation, montant);
    }
}

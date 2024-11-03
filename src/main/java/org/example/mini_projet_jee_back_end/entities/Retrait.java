package org.example.mini_projet_jee_back_end.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("RE")
public class Retrait extends Operation {
    public Retrait() {
    }

    public Retrait(LocalDate dateOperation, double montant) {
        super(dateOperation, montant);
    }
}

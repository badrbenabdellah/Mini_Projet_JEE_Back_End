package org.example.mini_projet_jee_back_end.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.mini_projet_jee_back_end.entities.*;
import org.example.mini_projet_jee_back_end.services.ClientService;
import org.example.mini_projet_jee_back_end.services.CompteService;
import org.example.mini_projet_jee_back_end.services.EmployeService;
import org.example.mini_projet_jee_back_end.services.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
public class OperationController {
    @Autowired
    private OperationService operationService;

    @Autowired
    private CompteService compteService;

    @Autowired
    private EmployeService employeService;

    @Autowired
    private ClientService clientService;

    @PostMapping("/api/v1/operation")
    public ResponseEntity<Map<String, Object>> saveOperation(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        String operationType = request.getParameter("operationType");
        Long compteId = Long.parseLong(request.getParameter("compteId"));
        double montant = Double.parseDouble(request.getParameter("montant"));
        Long employeID = Long.parseLong(request.getParameter("employeID"));

        // Validation du type d'opération
        if (operationType == null || (!operationType.equals("retrait") && !operationType.equals("versement"))) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Type d'opération invalide. Utilisez 'retrait' ou 'versement'.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Compte compte = compteService.getCompteById(compteId);
        Employe employe = employeService.getEmployeById(employeID);
        Operation newOperation = operationType.equals("retrait") ? new Retrait(LocalDate.now(), montant) : new Versement(LocalDate.now(), montant);

        boolean operationSuccessful = false;
        String errorMessage = null;

        // Gestion du retrait
        if (operationType.equals("retrait")) {
            operationSuccessful = handleRetraitOperation(compte, montant, newOperation);
            errorMessage = "Solde insuffisant pour effectuer le retrait.";
        }
        // Gestion du versement
        else {
            handleVersementOperation(compte, montant);
            operationSuccessful = true;
        }

        // Mise à jour des détails de l'opération et ajout à l'employé et au compte
        newOperation.setStatus(operationSuccessful ? "success" : "failed");
        newOperation.setEmploye(employe);
        newOperation.setCompte(compte);
        employe.addOperations(newOperation);
        compte.addOperation(newOperation);

        // Sauvegarde unique de l'opération après toutes les mises à jour
        if (operationSuccessful) {
            //compteService.updateCompte(compte);
            operationService.saveOperation(newOperation); // Sauvegarde ici, une seule fois
            response.put("code", 1);
            response.put("data", newOperation);
            response.put("message", "Opération effectuée avec succès.");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    private boolean handleRetraitOperation(Compte compte, double montant, Operation operation) {
        if (compte instanceof CompteCourant) {
            CompteCourant cc = (CompteCourant) compte;
            double soldeDisponible = cc.getSolde() + cc.getDecouvert();

            // Vérifie si le retrait dépasse le solde disponible
            if (montant > soldeDisponible) return false;

            // Débit du montant : d'abord sur le solde, puis sur le découvert
            if (montant <= cc.getSolde()) {
                cc.setSolde(cc.getSolde() - montant);
            } else {
                double montantRestant = montant - cc.getSolde();
                cc.setSolde(0);
                cc.setDecouvert(cc.getDecouvert() - montantRestant);
            }
        } else if (compte instanceof CompteEpargne) {
            CompteEpargne ce = (CompteEpargne) compte;

            // Vérifie si le solde est suffisant pour le retrait
            if (montant > ce.getSolde()) return false;

            ce.setSolde(ce.getSolde() - montant);
        }
        return true;
    }

    private void handleVersementOperation(Compte compte, double montant) {
        if (compte instanceof CompteCourant) {
            CompteCourant cc = (CompteCourant) compte;

            // Remboursement du découvert avant de créditer le solde
            double montantPourDecouvert = cc.getDecouvertInitial() - cc.getDecouvert();
            if (montant <= montantPourDecouvert) {
                cc.setDecouvert(cc.getDecouvert() + montant);
            } else {
                cc.setDecouvert(cc.getDecouvertInitial());
                cc.setSolde(cc.getSolde() + (montant - montantPourDecouvert));
            }
        } else if (compte instanceof CompteEpargne) {
            CompteEpargne ce = (CompteEpargne) compte;

            // Versement direct dans le solde pour le Compte Épargne
            ce.setSolde(ce.getSolde() + montant);
        }
    }

}

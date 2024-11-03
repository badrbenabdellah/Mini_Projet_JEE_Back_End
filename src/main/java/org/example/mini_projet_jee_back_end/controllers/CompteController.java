package org.example.mini_projet_jee_back_end.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.mini_projet_jee_back_end.entities.Client;
import org.example.mini_projet_jee_back_end.entities.Compte;
import org.example.mini_projet_jee_back_end.entities.CompteCourant;
import org.example.mini_projet_jee_back_end.entities.CompteEpargne;
import org.example.mini_projet_jee_back_end.services.ClientService;
import org.example.mini_projet_jee_back_end.services.CompteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CompteController {
    @Autowired
    private CompteService compteService;

    @Autowired
    private ClientService clientService;

    @PostMapping("/api/v1/compte")
    public ResponseEntity<Map<String, Object>> saveCompte(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        String accountType = request.getParameter("accountType");
        Long clientId = null;
        double solde = 0;
        double decouvert = 0;
        double taux = 0;

        try {
            if (request.getParameter("decouvert") != null) {
                decouvert = Double.parseDouble(request.getParameter("decouvert"));
            }
            if (request.getParameter("taux") != null) {
                taux = Double.parseDouble(request.getParameter("taux"));
            }
            if(request.getParameter("solde") != null){
                solde = Double.parseDouble(request.getParameter("solde"));
            }
            if (request.getParameter("clientId") != null) {
                clientId = Long.parseLong(request.getParameter("clientId"));
            }
        } catch (NumberFormatException e) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Invalid parameter format for 'decouvert' or 'taux'");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if(accountType == null){
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Type de compte manquant");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Compte newCompte;
        if (accountType.equals("cc")) {
            newCompte = new CompteCourant(LocalDate.now(), solde, decouvert);
        } else {
            newCompte = new CompteEpargne(LocalDate.now(), solde, taux);
        }

        Client client = clientService.getClientById(clientId);

        if(client == null){
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Client not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        newCompte.setClient(client);
        Compte savedCompte = compteService.saveCompte(newCompte);
        if (savedCompte == null) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Compte could not be saved");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        response.put("code", 1);
        response.put("data", savedCompte);
        response.put("message", "Compte created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/v1/compte")
    public ResponseEntity<Map<String, Object>> getComptes() {
        Map<String, Object> response = new HashMap<>();
        List<Compte> comptes = compteService.getAllComptes();

        if(comptes.isEmpty()){
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "No compte found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("code", 1);
        response.put("data", comptes);
        response.put("message", "List of comptes");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/api/v1/compte/{id}")
    public ResponseEntity<Map<String, Object>> getCompteById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        Compte compte = compteService.getCompteById(id);
        if(compte == null){
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Compte not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("code", 1);
        response.put("data", compte);
        response.put("message", "Compte found");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/api/v1/compte/{id}")
    public ResponseEntity<Map<String, Object>> deleteCompte(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Compte compte = compteService.getCompteById(id);

        if(compte == null){
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Compte not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        compteService.deleteCompte(id);
        response.put("code", 1);
        response.put("data", null);
        response.put("message", "Compte deleted successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/api/v1/compte/{id}")
    public ResponseEntity<Map<String, Object>> updateCompte(@PathVariable Long id, @RequestBody Compte compte) {
        Map<String, Object> response = new HashMap<>();
        Compte existingCompte = compteService.getCompteById(id);

        if(existingCompte == null){
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Compte not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Compte updatedCompte = compteService.updateCompte(compte);
        if(updatedCompte == null){
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Compte could not be updated");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.put("code", 1);
        response.put("data", updatedCompte);
        response.put("message", "Compte updated successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

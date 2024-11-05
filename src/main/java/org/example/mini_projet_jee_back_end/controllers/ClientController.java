package org.example.mini_projet_jee_back_end.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.mini_projet_jee_back_end.entities.Client;
import org.example.mini_projet_jee_back_end.entities.Compte;
import org.example.mini_projet_jee_back_end.entities.CompteCourant;
import org.example.mini_projet_jee_back_end.entities.CompteEpargne;
import org.example.mini_projet_jee_back_end.services.ClientService;
import org.example.mini_projet_jee_back_end.services.CompteService;
import org.example.mini_projet_jee_back_end.services.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ClientController {
    @Autowired
    private  ClientService clientService;
    @Autowired
    private  CompteService compteService;
    @Autowired
    private OperationService operationService;



    @PostMapping("/api/v1/client")
    public ResponseEntity<Map<String, Object>> saveClient(HttpServletRequest request, @RequestBody Client client) {
        Map<String, Object> response = new HashMap<>();

        // Génération de l'ID et de la date d'adhésion du client
        //client.setCodeClient(Generator.generateID("client"));
        client.setJoinDate(LocalDate.now());

        // Récupération et validation des paramètres
        String accountType = request.getParameter("account_type");
        double decouvert = request.getParameter("decouvert") != null ? Double.parseDouble(request.getParameter("decouvert")) : 0;
        double taux = request.getParameter("taux") != null ? Double.parseDouble(request.getParameter("taux")) : 0;
        double solde = request.getParameter("solde") != null ? Double.parseDouble(request.getParameter("solde")) : 0;

        if (accountType == null || (!accountType.equals("cc") && !accountType.equals("ce"))) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Account type is missing or invalid. Use 'cc' for CompteCourant or 'ce' for CompteEpargne.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        /*try {
            if (request.getParameter("decouvert") != null) {
                decouvert = Double.parseDouble(request.getParameter("decouvert"));
            }
            if (request.getParameter("taux") != null) {
                taux = Double.parseDouble(request.getParameter("taux"));
            }
            if(request.getParameter("solde") != null){
                solde = Double.parseDouble(request.getParameter("solde"));
            }
        } catch (NumberFormatException e) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Invalid parameter format for 'decouvert' or 'taux'");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }*/

        // Sauvegarde du client
        Client newClient = clientService.saveClient(client);
        if (newClient == null) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Client could not be saved");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        // Création et sauvegarde du compte
        Compte newCompte;
        if (accountType.equals("cc")) {
            newCompte = new CompteCourant(LocalDate.now(), solde, decouvert);
        } else {
            newCompte = new CompteEpargne(LocalDate.now(), solde, taux);
        }

        newClient.addCompte(newCompte);
        Compte savedCompte = compteService.saveCompte(newCompte); // Utilisation du service pour sauvegarder le compte

        if (savedCompte == null) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Account could not be saved");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        // Construction de la réponse
        response.put("code", 1);
        response.put("data", Map.of(
                "codeClient", newClient.getCodeClient(),
                "full_name", newClient.getFullname(),
                "adresse", newClient.getAddress(),
                "telephone", newClient.getPhone(),
                "email", newClient.getEmail(),
                "account_type", accountType,
                "decouvert", decouvert,
                "taux", taux,
                "solde", solde
        ));
        response.put("message", "Client and account saved successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /*@GetMapping("/api/v1/client")
    public ResponseEntity<Map<String, Object>> getClients(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        int limit = request.getParameter("limit") != null ? Integer.parseInt(request.getParameter("limit")) : 0;
        List<Client> clients;
        if(limit == 0){
            clients = clientService.getAllClients();
        }else{
            clients = clientService.getClientByLimit(limit);
        }

        if (clients.isEmpty()) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "No clients found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("code", 1);
        response.put("data", clients);
        response.put("message", "Clients found");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }*/

    @GetMapping("/api/v1/client")
    public ResponseEntity<Map<String, Object>> getClients(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        int limit = request.getParameter("limit") != null ? Integer.parseInt(request.getParameter("limit")) : 0;
        List<Client> clients;

        if (limit == 0) {
            clients = clientService.getAllClients();
        } else {
            clients = clientService.getClientByLimit(limit);
        }

        if (clients.isEmpty()) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "No clients found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Structure pour stocker les données des clients enrichies
        List<Map<String, Object>> enrichedClients = new ArrayList<>();
        for (Client client : clients) {
            Map<String, Object> clientData = new HashMap<>();
            clientData.put("id", client.getCodeClient());
            clientData.put("fullname", client.getFullname());
            clientData.put("email", client.getEmail());
            clientData.put("phone", client.getPhone());
            clientData.put("address", client.getAddress());
            clientData.put("joinDate", client.getJoinDate());


            // Calcul de la somme des soldes
            double totalBalance = client.getComptes().stream().mapToDouble(Compte::getSolde).sum();
            clientData.put("totalBalance", totalBalance);

            // Types de comptes
            Set<String> accountTypes = client.getComptes().stream()
                    .map(compte -> compte instanceof CompteCourant ? "cc" : "ce")
                    .collect(Collectors.toSet());
            clientData.put("accountTypes", accountTypes);
            enrichedClients.add(clientData);
        }

        // Ajout des métadonnées
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("totalClients", clientService.countAllClients());
        metadata.put("totalAccounts", compteService.countAllAccounts());
        metadata.put("operationsToday", operationService.countAllOperationsByDate(LocalDate.now()));
        metadata.put("newClientsThisMonth", clientService.countNewClientsThisMonth());

        response.put("code", 1);
        response.put("data", enrichedClients);
        response.put("message", "Clients found");
        response.put("metadata", metadata);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/api/v1/client/{id}")
    public ResponseEntity<Map<String, Object>> getClientById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Client client = clientService.getClientById(id);

        if (client == null) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Client not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("code", 1);
        response.put("data", client);
        response.put("message", "Client found");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/api/v1/client/{id}")
    public ResponseEntity<Map<String, Object>> deleteClient(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Client client = clientService.getClientById(id);

        if (client == null) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Client not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        clientService.deleteClient(id);
        response.put("code", 1);
        response.put("data", null);
        response.put("message", "Client deleted successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/api/v1/client/{id}")
    public ResponseEntity<Map<String, Object>> updateClient(@PathVariable Long id, @RequestBody Client client) {
        Map<String, Object> response = new HashMap<>();
        Client existingClient = clientService.getClientById(id);

        if (existingClient == null) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Client not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Vérifiez chaque champ avant de mettre à jour
        if (client.getFullname() != null) {
            existingClient.setFullname(client.getFullname());
        }
        if (client.getEmail() != null) {
            existingClient.setEmail(client.getEmail());
        }
        if (client.getPhone() != null) {
            existingClient.setPhone(client.getPhone());
        }
        if (client.getAddress() != null) {
            existingClient.setAddress(client.getAddress());
        }

        existingClient.setJoinDate(existingClient.getJoinDate());

        Client updatedClient = clientService.saveClient(existingClient);

        response.put("code", 1);
        response.put("data", updatedClient);
        response.put("message", "Client updated successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/api/v1/client/limit")
    public ResponseEntity<Map<String, Object>> getClientByLimit(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        int limit = request.getParameter("limit") != null ? Integer.parseInt(request.getParameter("limit")) : 0;
        List<Client> clients = clientService.getClientByLimit(limit);

        if (clients.isEmpty()) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "No clients found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("code", 1);
        response.put("data", clients);
        response.put("message", "Clients found");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

package org.example.mini_projet_jee_back_end.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.mini_projet_jee_back_end.entities.Employe;
import org.example.mini_projet_jee_back_end.entities.Groupe;
import org.example.mini_projet_jee_back_end.services.EmployeService;
import org.example.mini_projet_jee_back_end.services.GroupeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class EmployeController {

    @Autowired
    private EmployeService employeService;

    @Autowired
    private GroupeService groupeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/api/v1/employe")
    public ResponseEntity<Map<String, Object>> saveEmploye(HttpServletRequest request, @RequestBody Employe employe) {
        Map<String, Object> response = new HashMap<>();
        Long employe_sup_id = request.getParameter("employe_sup_id") != null ? Long.parseLong(request.getParameter("employe_sup_id")) : null;
        Long groupe_id = request.getParameter("groupe_id") != null ? Long.parseLong(request.getParameter("groupe_id")) : null;
        employe.setJoinDate(LocalDate.now());
        String hashedPassword = passwordEncoder.encode(employe.getPassword());
        employe.setPassword(hashedPassword);
        if (employe_sup_id != null) {
            Employe employe_sup = employeService.getEmployeById(employe_sup_id);
            if (employe_sup == null) {
                response.put("code", -1);
                response.put("data", null);
                response.put("message", "Employé sup non trouvé");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            employe.setEmploye_sup(employe_sup);
        }
        if (groupe_id != null) {
            Groupe groupe = groupeService.getGroupeById(groupe_id);
            if (groupe == null) {
                response.put("code", -1);
                response.put("data", null);
                response.put("message", "Groupe non trouvé");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            employe.setGroupe(groupe);
        }

        employeService.saveEmploye(employe);

        response.put("code", 1);
        response.put("data", employe);
        response.put("message", "Employé créé avec succès");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/api/v1/employe/{superviseurId}/subordonnees")
    public ResponseEntity<Map<String, Object>> addSubordonne(HttpServletRequest request ,@PathVariable Long superviseurId) {
        Map<String, Object> response = new HashMap<>();
        Long subordonneId = request.getParameter("subordonneId") != null ? Long.parseLong(request.getParameter("subordonneId")) : null;

        // Récupérer l'employé subordonné par son ID
        if(subordonneId == null) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "ID de subordonné manquant");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Employe subordonne = employeService.getEmployeById(subordonneId);

        // Récupérer l'employé superviseur par son ID
        Employe superviseur = employeService.getEmployeById(superviseurId);
        if (superviseur == null) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Employé superviseur non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Associer le subordonné au superviseur
        subordonne.setEmploye_sup(superviseur);

        // Sauvegarder le subordonné en base de données
        employeService.updateEmploye(subordonne);

        response.put("code", 1);
        response.put("data", subordonne);
        response.put("message", "Subordonné ajouté avec succès");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/v1/employe/{id}/collaborators")
    public ResponseEntity<Map<String, Object>> getCollaboratorData(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Employe employe = employeService.getEmployeById(id);

        if (employe == null) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Employé non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        List<Map<String, Object>> collaboratorData = new ArrayList<>();
        for (Employe collaborator : employe.getEmployes()) {
            Map<String, Object> collaboratorsInfo = new HashMap<>();
            collaboratorsInfo.put("id", collaborator.getCodeEmploye());
            collaboratorsInfo.put("fullname", collaborator.getFullname());
            collaboratorsInfo.put("email", collaborator.getEmail());
            collaboratorsInfo.put("phone", collaborator.getPhone());
            collaboratorsInfo.put("address", collaborator.getAddress());
            collaboratorsInfo.put("role", collaborator.getRole());
            collaboratorData.add(collaboratorsInfo);
        }

        if(collaboratorData.isEmpty()) {
            response.put("code", -1);
            response.put("data", collaboratorData);
            response.put("message", "Aucun collaborateur trouvé");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        response.put("code", 1);
        response.put("data", collaboratorData);
        response.put("message", "Liste des collaborateurs");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/api/v1/employe/{id}")
    public ResponseEntity<Map<String, Object>> deleteEmploye(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Employe employe = employeService.getEmployeById(id);

        if (employe == null) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Employé non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        employeService.deleteEmploye(id);

        response.put("code", 1);
        response.put("data", null);
        response.put("message", "Employé supprimé avec succès");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/api/v1/employe")
    public ResponseEntity<Map<String, Object>> getEmployes(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        List<Employe> employes = employeService.getAllEmployes();
        if (employes.isEmpty()) {
            response.put("code", 0);
            response.put("data", null);
            response.put("message", "Aucun employé trouvé");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }
        response.put("code", 1);
        response.put("data", employes);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/api/v1/employe/{id}")
    public ResponseEntity<Map<String, Object>> getEmployeById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Employe employe = employeService.getEmployeById(id);
        if (employe == null) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Employé non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("code", 1);
        response.put("data", employe);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("api/v1/auth/employe")
    public ResponseEntity<Map<String, Object>> getEmployeByEmail(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> body = new HashMap<>();
        body.put("email", request.getParameter("email"));
        body.put("password", request.getParameter("password"));
        if (!body.containsKey("email") || !body.containsKey("password")) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Email et/ou mot de passe manquant");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Employe employe = employeService.getEmployeByEmail(body.get("email"));
        if (employe == null) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Employé non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (!passwordEncoder.matches(body.get("password"), employe.getPassword())) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Password or email incorrect");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        response.put("code", 1);
        response.put("data", employe);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}

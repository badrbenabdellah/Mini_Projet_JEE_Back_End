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
        Long employe_sub_id = request.getParameter("employe_sub_id") != null ? Long.parseLong(request.getParameter("employe_sub_id")) : null;
        Long groupe_id = request.getParameter("groupe_id") != null ? Long.parseLong(request.getParameter("groupe_id")) : null;
        employe.setJoinDate(LocalDate.now());
        String hashedPassword = passwordEncoder.encode(employe.getPassword());
        employe.setPassword(hashedPassword);
        if (employe_sub_id != null) {
            Employe employe_sub = employeService.getEmployeById(employe_sub_id);
            if (employe_sub == null) {
                response.put("code", -1);
                response.put("data", null);
                response.put("message", "Employé subordonné non trouvé");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            employe.setEmploye_sup(employe_sub);
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

    @GetMapping("/api/v1/employe/{id}/subordonnees")
    public ResponseEntity<Map<String, Object>> getSubordonnees(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Employe employe = employeService.getEmployeById(id);

        if (employe == null) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Employé non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        List<Map<String, Object>> subordonneesData = new ArrayList<>();
        for (Employe subordonne : employe.getEmployes()) {
            Map<String, Object> subordonneInfo = new HashMap<>();
            subordonneInfo.put("id", subordonne.getCodeEmploye());
            subordonneInfo.put("fullname", subordonne.getFullname());
            subordonneInfo.put("email", subordonne.getEmail());
            subordonneInfo.put("phone", subordonne.getPhone());
            subordonneInfo.put("address", subordonne.getAddress());
            subordonneInfo.put("role", subordonne.getRole());
            subordonneesData.add(subordonneInfo);
        }

        response.put("code", 1);
        response.put("data", subordonneesData);
        response.put("message", subordonneesData.isEmpty() ? "Aucun subordonné trouvé" : "Liste des subordonnés");
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


}

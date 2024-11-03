package org.example.mini_projet_jee_back_end.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.mini_projet_jee_back_end.entities.Groupe;
import org.example.mini_projet_jee_back_end.services.GroupeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GroupeController {
    @Autowired
    private GroupeService groupeService;

    @PostMapping("/api/v1/groupe")
    public ResponseEntity<Map<String, Object>> saveGroupe(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        String groupName = request.getParameter("groupName");
        if(groupName == null){
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Nom de groupe manquant");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Groupe groupe = new Groupe(groupName);
        groupeService.saveGroupe(groupe);

        response.put("code", 1);
        response.put("data", groupe);
        response.put("message", "Groupe créé avec succès");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/api/v1/groupe/{id}")
    public ResponseEntity<Map<String, Object>> deleteGroupe(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        if (id == null) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "ID de groupe manquant");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Groupe groupe = groupeService.getGroupeById(id);
        if (groupe == null) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Groupe non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        groupeService.deleteGroupe(id);
        response.put("code", 1);
        response.put("data", null);
        response.put("message", "Groupe supprimé avec succès");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/api/v1/groupe")
    public ResponseEntity<Map<String, Object>> getAllGroupes() {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 1);
        response.put("data", groupeService.getAllGroupes());
        response.put("message", "Liste des groupes");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/api/v1/groupe/{id}")
    public ResponseEntity<Map<String, Object>> getGroupeById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Groupe groupe = groupeService.getGroupeById(id);
        if (groupe == null) {
            response.put("code", -1);
            response.put("data", null);
            response.put("message", "Groupe non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("code", 1);
        response.put("data", groupe);
        response.put("message", "Groupe trouvé");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

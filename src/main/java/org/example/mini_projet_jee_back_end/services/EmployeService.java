package org.example.mini_projet_jee_back_end.services;

import org.example.mini_projet_jee_back_end.entities.Employe;
import org.example.mini_projet_jee_back_end.repositories.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeService {
    private final EmployeRepository employeRepository;

    @Autowired
    public EmployeService(EmployeRepository employeRepository) {
        this.employeRepository = employeRepository;
    }

    public Employe saveEmploye(Employe employe) {
        return employeRepository.save(employe);
    }

    public Employe updateEmploye(Employe employe) {
        return employeRepository.save(employe);
    }

    public Employe getEmployeById(Long id) {
        return employeRepository.findById(id).orElse(null);
    }

    public void deleteEmploye(Long id) {
        employeRepository.deleteById(id);
    }

    public List<Employe> getAllEmployes() {
        return employeRepository.findAll();
    }

}

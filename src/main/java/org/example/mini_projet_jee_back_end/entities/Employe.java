package org.example.mini_projet_jee_back_end.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Employe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codeEmploye;
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private String password;
    private String role;
    private LocalDate joinDate;

    @OneToMany(mappedBy = "employe" , cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Operation> operations;

    @OneToMany(mappedBy = "employe_sup", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Employe> employes;

    @ManyToOne
    @JoinColumn(name = "employe_sup_id")
    private Employe employe_sup;

    @ManyToOne
    @JoinColumn(name = "groupes_id")
    private Groupe groupe;

    public Employe() {
    }

    public Employe(String fullname, String email, String phone, String address, String password, String role, LocalDate joinDate) {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.role = role;
        this.joinDate = joinDate;
        this.operations = new ArrayList<>();
        this.employes = new ArrayList<>();
    }

    public void setCodeEmploye(Long codeEmploye) {
        this.codeEmploye = codeEmploye;
    }

    public Long getCodeEmploye() {
        return codeEmploye;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setEmployes(Employe employe) {
        this.employes.add(employe);
    }

    public List<Employe> getEmployes() {
        return employes;
    }

    public void setEmploye_sup(Employe employe_sup) {
        this.employe_sup = employe_sup;
    }

    public Employe getEmploye_sup() {
        return employe_sup;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public void addOperations(Operation operation) {
        this.operations.add(operation);
        operation.setEmploye(this);
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void addEmployes(Employe employe) {
        this.employes.add(employe);
        employe.setEmploye_sup(this);
    }
}

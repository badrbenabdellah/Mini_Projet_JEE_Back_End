package org.example.mini_projet_jee_back_end.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codeClient;

    private String fullname;
    private String email;
    private String phone;
    private String address;
    private LocalDate joinDate;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Compte> comptes = new ArrayList<>();

    public Client() {}

    public Client(String fullname, String email, String phone, String address, LocalDate joinDate) {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.joinDate = joinDate;
    }

    // Getters et setters
    public Long getCodeClient() { return codeClient; }
    public void setCodeClient(Long codeClient) { this.codeClient = codeClient; }

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }

    public List<Compte> getComptes() { return comptes; }

    public void addCompte(Compte compte) {
        comptes.add(compte);
        compte.setClient(this); // Assurer la relation bidirectionnelle
    }

    public void removeCompte(Compte compte) {
        comptes.remove(compte);
        compte.setClient(null); // Assurer la relation bidirectionnelle
    }
}

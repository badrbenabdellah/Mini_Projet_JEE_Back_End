package org.example.mini_projet_jee_back_end.services;

import org.example.mini_projet_jee_back_end.entities.Client;
import org.example.mini_projet_jee_back_end.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public Client updateClient(Client client) {
        return clientRepository.save(client);
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public List<Client> getClientByLimit(int limit) {
        return clientRepository.findAll(PageRequest.of(0, limit)).toList();
    }

    public int countAllClients() {
        return (int) clientRepository.count();
    }

    public int countNewClientsThisMonth() {
        YearMonth currentMonth = YearMonth.now();
        LocalDate startDate = currentMonth.atDay(1);           // Premier jour du mois
        LocalDate endDate = currentMonth.atEndOfMonth();       // Dernier jour du mois

        return clientRepository.countByJoinDateBetween(startDate, endDate);
    }
}

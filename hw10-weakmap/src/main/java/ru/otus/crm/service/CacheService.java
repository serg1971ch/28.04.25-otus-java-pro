package ru.otus.crm.service;

import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public interface CacheService {
    Client saveClient(Client client);

    Client update(Client client);

    Optional<Client> getClient(long id);

    List<Client> findAll();

    void saveAll(List<Client> clientList);

    boolean isConsistent();
}

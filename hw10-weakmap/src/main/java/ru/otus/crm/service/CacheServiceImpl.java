package ru.otus.crm.service;

import ru.otus.cachehw.MyCache;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class CacheServiceImpl implements CacheService {

    private final MyCache<Long, Client> myCache;

    public CacheServiceImpl(MyCache<Long, Client> myCache) {
        this.myCache = myCache;
    }

    public Client saveClient(Client client) {
        myCache.put(client.getId(), client);
        return client;
    }

    public Client update(Client client) {
        return myCache.update(client.getId(), client);
    }

    public Optional<Client> getClient(long id) {
        return Optional.ofNullable(myCache.get(id));
    }

    public List<Client> findAll() {
        return myCache.findAll();
    }

    public void saveAll(List<Client> clientList) {
        if(myCache.getMaxSize() < clientList.size()) {
            myCache.clearCache();
            clientList.forEach(client -> myCache.put(client.getId(),client));
        }
    }

    @Override
    public boolean isConsistent() {
        return myCache.getResetCount() == 0;
    }
}

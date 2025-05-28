package ru.otus.hw02collections;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    private final Map<Customer, String> customers = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        var entry = ((TreeMap<Customer, String>) customers).firstEntry();
        return copyOrNull(entry);
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        var entry = ((TreeMap<Customer, String>) customers).higherEntry(customer);
        return copyOrNull(entry);
    }

    public void add(Customer customer, String data) {
        customers.put(customer, data);
    }

    private Map.Entry<Customer, String> copyOrNull(Map.Entry<Customer, String> entry) {
        if (entry != null) return new AbstractMap.SimpleEntry<>(new Customer(entry.getKey()), entry.getValue());
        return null;
    }
}

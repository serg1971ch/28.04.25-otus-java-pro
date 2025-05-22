package ru.otus.hw02collections;

import java.util.ArrayDeque;
import java.util.Deque;
import ru.otus.hw02collections.Customer;

public class CustomerReverseOrder {

    private final Deque<Customer> customers = new ArrayDeque<>();

    public void add(Customer customer) {
        customers.add(customer);
    }

    public Customer take() {
        return customers.pollLast();
    }
}

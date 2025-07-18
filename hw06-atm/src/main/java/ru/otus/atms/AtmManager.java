package ru.otus.atms;

import ru.otus.domain.Banknote;
import ru.otus.exceptions.AtmModificationException;
import ru.otus.factories.AtmBalance;

import java.util.List;

public class AtmManager {
    private final AtmBalance balance;
    private final CashDispenser dispenser;
    private final CashAcceptor acceptor;

    public AtmManager(AtmBalance balance) {
        this.balance = balance;
        this.dispenser = new CashDispenser(balance);
        this.acceptor = new CashAcceptor(balance);
    }

    public int getBalance() {
        return balance.calculateTotalBalance();
    }

    public List<Banknote> withdraw(int amount) throws AtmModificationException {
        return dispenser.dispense(amount);
    }

    public int deposit(int amount) throws AtmModificationException {
        return acceptor.accept(amount);
    }
}

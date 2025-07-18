package ru.otus.atms;

import ru.otus.cells.Cell;
import ru.otus.domain.Banknote;
import ru.otus.exceptions.AtmModificationException;
import ru.otus.factories.AtmBalance;

import java.util.ArrayList;
import java.util.List;

public class CashDispenser {
    private final AtmBalance balance;

    public CashDispenser(AtmBalance balance) {
        this.balance = balance;
    }

    public List<Banknote> dispense(int amount) throws AtmModificationException {
        return getBanknotes(amount, balance);
    }

    private List<Banknote> getBanknotes(int amount, AtmBalance balance) {

    }
}
package ru.otus.atms;

import org.jetbrains.annotations.NotNull;
import ru.otus.cells.Cell;
import ru.otus.domain.Banknote;
import ru.otus.exceptions.AtmModificationException;
import ru.otus.factories.AtmBalance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CashAcceptor {
    private final AtmBalance balance;

    public CashAcceptor(AtmBalance balance) {
        this.balance = balance;
    }

    public List<Banknote> accept(int amount) throws AtmModificationException {
        return getBanknotes(amount, balance);
    }

    @NotNull
    static List<Banknote> getBanknotes(int amount, AtmBalance balance) throws AtmModificationException {
        if (amount <= 0) {
            throw new AtmModificationException("Amount must be positive");
        }
        if (amount % Banknote.FIFTY.getValue() != 0) {
            throw new AtmModificationException("Amount must be multiple of 50");
        }
        if (amount > balance.calculateTotalBalance()) {
            throw new AtmModificationException("Insufficient funds");
        }

        List<Banknote> result = new ArrayList<>();
        for (Map.Entry<Banknote, Cell> entry : balance.getCells().entrySet()) {
            while (amount >= entry.getKey().getValue() && entry.getValue().size() > 0) {
                Banknote banknote = entry.getValue().getBanknote();
                result.add(banknote);
            }
        }
        return result;
    }
}


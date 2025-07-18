package ru.otus.cells.impl;

import ru.otus.cells.Cell;
import ru.otus.domain.Banknote;

import java.util.List;
import java.util.NoSuchElementException;

public class BasicCell implements Cell {
    private final List<Banknote> banknotes;
    private final int nominal;
    private int cachedBalance;

    public BasicCell(final List<Banknote> banknotes, final int nominal) {
        this.banknotes = banknotes;
        this.nominal = nominal;
        this.cachedBalance = calculateBalance();
    }

    @Override
    public Banknote getBanknote() throws NoSuchElementException {
        if (banknotes.isEmpty()) {
            throw new NoSuchElementException("Cell is empty");
        }

        Banknote banknote = banknotes.remove(banknotes.size() - 1);
        cachedBalance -= nominal;
        return banknote;
    }

    @Override
    public void addBanknote(final Banknote banknote) throws IllegalArgumentException {
        if (banknote == null) {
            throw new IllegalArgumentException("Banknote cannot be null");
        }
        if (banknote.getValue() != nominal) {
            throw new IllegalArgumentException("Invalid nominal: " + banknote.getValue());
        }
        banknotes.add(banknote);
        cachedBalance += nominal;
    }

    @Override
    public int calculateBalance() {
        return cachedBalance;
    }

    @Override
    public int size() {
        return banknotes.size();
    }
}

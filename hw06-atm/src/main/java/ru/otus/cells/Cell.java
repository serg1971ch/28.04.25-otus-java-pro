package ru.otus.cells;

import ru.otus.domain.Banknote;
import ru.otus.exceptions.CellOperationException;

public interface Cell<T extends Banknote> {
    T extract() throws CellOperationException;

    void insertBanknote(T banknote) throws CellOperationException;

    int getBanknotesAmount();

    Integer getDenomination();
}

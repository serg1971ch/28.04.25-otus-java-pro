package ru.otus.cells.factory;

import ru.otus.cells.Cell;
import ru.otus.domain.Banknote;
import ru.otus.domain.Currency;

public abstract class CellCreator<T extends Banknote> {
    protected static final int DEFAULT_CELL_SIZE = 2500;

    public abstract Cell<T> create(Integer denomination, Currency currency);
}

package ru.otus.cells.factory;

import ru.otus.cells.Cell;
import ru.otus.cells.CellBanknote;
import ru.otus.domain.Banknote;
import ru.otus.domain.Currency;

public class CellBanknoteCreator extends CellCreator<Banknote> {
    @Override
    public Cell<Banknote> create(Integer denomination, Currency currency) {
        return new CellBanknote(denomination, currency, DEFAULT_CELL_SIZE);
    }
}

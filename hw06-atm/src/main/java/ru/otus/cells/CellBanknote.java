package ru.otus.cells;

import java.util.ArrayDeque;
import java.util.Deque;
import ru.otus.domain.Banknote;
import ru.otus.domain.Currency;
import ru.otus.exceptions.CellOperationException;

public class CellBanknote implements Cell<Banknote> {

    private final Deque<Banknote> banknotes;
    private final Integer denomination;
    private final Currency currency;
    private final int cellSize;

    public CellBanknote(Integer denomination, Currency currency, final int cellSize) {
        this.denomination = denomination;
        this.currency = currency;
        this.banknotes = new ArrayDeque<>();
        this.cellSize = cellSize;
    }

    /* ATM достает их строго по одной, т.к. выполняет проверку на слипшиеся купюры */
    @Override
    public Banknote extract() throws CellOperationException {
        if (this.getBanknotesAmount() > 0) return banknotes.pop();
        throw new CellOperationException("Unable to receive a banknote because the cell with is empty");
    }

    @Override
    public void insertBanknote(Banknote banknote) throws CellOperationException {
        if (cellSize + 1 < this.getBanknotesAmount()) {
            throw new CellOperationException("It is impossible to add banknotes, because cell is full");
        }
        if (checkInconsistency(banknote)) {
            throw new CellOperationException(
                    "Unable to insert banknote into current cell! Inconsistency between banknote and destination");
        }
        banknotes.push(banknote);
    }

    private boolean checkInconsistency(Banknote banknote) {
        return this.currency != banknote.currency() || this.denomination.compareTo(banknote.denomination()) != 0;
    }

    @Override
    public int getBanknotesAmount() {
        return banknotes.size();
    }

    @Override
    public Integer getDenomination() {
        if (getBanknotesAmount() == 0) return 0;
        return banknotes.peek().denomination();
    }
}

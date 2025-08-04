package ru.otus.atm.factory;

import java.util.List;
import ru.otus.domain.Banknote;
import ru.otus.domain.Currency;
import ru.otus.exceptions.AtmInteractionException;

public interface Atm {
    List<Banknote> requestMoney(Currency currency, String amount) throws AtmInteractionException;

    void insertMoney(List<Banknote> banknotes);

    Integer getBalanceForCurrency(Currency currency);
}

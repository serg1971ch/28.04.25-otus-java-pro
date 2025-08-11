package ru.otus.atm;

import java.util.HashMap;
import java.util.List;
import ru.otus.atm.factory.Atm;
import ru.otus.atm.factory.AtmImpl;
import ru.otus.cells.factory.CellBanknoteCreator;
import ru.otus.cluster.ClusterManager;
import ru.otus.cluster.factory.ClusterCellBanknoteCreator;
import ru.otus.cluster.factory.ClusterCreator;
import ru.otus.domain.Banknote;
import ru.otus.domain.Currency;

public class AtmCreatorDefault extends AtmCreator {
    private final ClusterCreator<Banknote> clusterCreator;
    private final List<Currency> currencyList;

    public AtmCreatorDefault(List<Currency> currencyList) {
        this.clusterCreator = new ClusterCellBanknoteCreator(new CellBanknoteCreator());
        this.currencyList = currencyList;
    }

    @Override
    public Atm create() {
        HashMap<Currency, ClusterManager<Banknote>> atmCurrencyClusters = new HashMap<>();
        for (var currency : currencyList) {
            atmCurrencyClusters.put(currency, clusterCreator.create(currency));
        }
        return new AtmImpl(atmCurrencyClusters);
    }
}

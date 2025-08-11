package ru.otus.cluster.factory;

import static java.util.stream.Collectors.toMap;

import java.util.Map;
import ru.otus.cells.Cell;
import ru.otus.cells.factory.CellCreator;
import ru.otus.cluster.ClusterManager;
import ru.otus.cluster.ClusterManagerImpl;
import ru.otus.domain.Banknote;
import ru.otus.domain.Currency;

public class ClusterCellBanknoteCreator extends ClusterCreator<Banknote> {
    private final CellCreator<Banknote> cellCreator;

    public ClusterCellBanknoteCreator(CellCreator<Banknote> cellCreator) {
        this.cellCreator = cellCreator;
    }

    @Override
    public ClusterManager<Banknote> create(Currency currency) {
        return new ClusterManagerImpl(currency, initCellHolder(currency));
    }

    private Map<Integer, Cell<Banknote>> initCellHolder(Currency currency) {
        return currency.getAcceptableDenominations().stream()
                .collect(toMap(e -> e, e -> cellCreator.create(e, currency)));
    }
}

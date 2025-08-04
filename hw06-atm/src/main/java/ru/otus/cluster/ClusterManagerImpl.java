package ru.otus.cluster;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ru.otus.cells.Cell;
import ru.otus.domain.Banknote;
import ru.otus.domain.Currency;
import ru.otus.exceptions.CellOperationException;
import ru.otus.exceptions.ClusterOperationException;

public class ClusterManagerImpl implements ClusterManager<Banknote> {
    private final Currency clusterCurrency;
    private final Map<Integer, Cell<Banknote>> cellHolder;

    public ClusterManagerImpl(Currency clusterCurrency, Map<Integer, Cell<Banknote>> cellHolder) {
        this.clusterCurrency = clusterCurrency;
        this.cellHolder = cellHolder;
    }

    @Override
    public void putBanknote(List<Banknote> banknotes) throws ClusterOperationException {
        for (var banknote : banknotes) {
            Cell<Banknote> cell = cellHolder.get(banknote.denomination());
            try {
                cell.insertBanknote(banknote);
            } catch (CellOperationException exception) {
                throw new ClusterOperationException(
                        "An error occurred while trying to insert into a cell: " + exception.getMessage(), exception);
            }
        }
    }

    @Override
    public List<Banknote> requestBanknote(Integer nominal, int amount) throws ClusterOperationException {
        List<Banknote> result = new ArrayList<>();
        Cell<Banknote> banknoteCell = cellHolder.get(nominal);
        try {
            for (int i = 0; i < amount; i++) {
                result.add(banknoteCell.extract());
            }
        } catch (CellOperationException exception) {
            throw new ClusterOperationException(
                    "An error occurred while trying to extract from a cell" + exception.getMessage(), exception);
        }
        return result;
    }

    @Override
    public int getBanknoteAmountFromCell(Integer denomination) {
        return cellHolder.get(denomination).getBanknotesAmount();
    }

    @Override
    public Integer getTotalSumFromCluster() {
        int result = 0;
        List<Cell<Banknote>> clustersCells = new ArrayList<>(cellHolder.values());
        for (var cell : clustersCells) {
            result += cell.getBanknotesAmount() * cell.getDenomination();
        }
        return result;
    }

    @Override
    public Currency getClusterCurrency() {
        return clusterCurrency;
    }
}

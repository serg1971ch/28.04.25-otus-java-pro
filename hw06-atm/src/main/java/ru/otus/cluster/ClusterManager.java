package ru.otus.cluster;

import java.util.List;
import ru.otus.domain.Banknote;
import ru.otus.domain.Currency;
import ru.otus.exceptions.ClusterOperationException;

public interface ClusterManager<T extends Banknote> {
    void putBanknote(List<Banknote> banknotes) throws ClusterOperationException;

    List<T> requestBanknote(Integer nominal, int amount) throws ClusterOperationException;

    int getBanknoteAmountFromCell(Integer denomination);

    Integer getTotalSumFromCluster();

    Currency getClusterCurrency();
}

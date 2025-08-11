package ru.otus.cluster.factory;

import ru.otus.cluster.ClusterManager;
import ru.otus.domain.Banknote;
import ru.otus.domain.Currency;

public abstract class ClusterCreator<T extends Banknote> {
    public abstract ClusterManager<T> create(Currency currency);
}

package ru.otus.atm.factory;

import static java.lang.Math.toIntExact;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.util.*;
import java.util.function.Function;
import ru.otus.cluster.ClusterManager;
import ru.otus.domain.Banknote;
import ru.otus.domain.Currency;
import ru.otus.exceptions.AtmInteractionException;
import ru.otus.exceptions.ClusterOperationException;

public class AtmImpl implements Atm {
    private final HashMap<Currency, ClusterManager<Banknote>> atmCurrencyClusters;

    private record RequestedBanknote(Integer denomination, int count) {}

    public AtmImpl(HashMap<Currency, ClusterManager<Banknote>> atmCurrencyClusters) {
        this.atmCurrencyClusters = atmCurrencyClusters;
    }

    @Override
    public List<Banknote> requestMoney(Currency currency, String amount) throws AtmInteractionException {
        List<Integer> denominations = splitRequestedIntoBanknotes(currency, amount);
        List<RequestedBanknote> requestedBanknoteByCount = groupBanknotes(denominations);

        var cluster = atmCurrencyClusters.get(currency);
        long flags = 0;
        for (var requested : requestedBanknoteByCount) {
            flags += checkBanknotesSufficiency(cluster, requested) ? 1 : 0;
        }

        List<Banknote> out = new ArrayList<>();
        if (flags == requestedBanknoteByCount.size()) {
            for (var requested : requestedBanknoteByCount) {
                try {
                    out.addAll(cluster.requestBanknote(requested.denomination, requested.count));
                } catch (ClusterOperationException e) {
                    throw new AtmInteractionException("Error receiving banknotes from the cluster", e);
                }
            }
        } else {
            throw new AtmInteractionException("There is not enough banknotes in the ATM");
        }
        return out;
    }

    private boolean checkBanknotesSufficiency(ClusterManager<Banknote> cluster, RequestedBanknote banknoteRecord) {
        return cluster.getBanknoteAmountFromCell(banknoteRecord.denomination) >= banknoteRecord.count;
    }

    private static List<RequestedBanknote> groupBanknotes(List<Integer> banknotes) {
        Map<Integer, Long> frequencyMap = banknotes.stream().collect(groupingBy(Function.identity(), counting()));

        List<RequestedBanknote> result = new ArrayList<>();
        for (var entry : frequencyMap.entrySet()) {
            result.add(new RequestedBanknote(entry.getKey(), toIntExact(entry.getValue())));
        }
        return result;
    }

    private static List<Integer> splitRequestedIntoBanknotes(Currency currency, String amount) {
        List<Integer> requested = new LinkedList<>();
        int amountB = Integer.parseInt(amount);
        while (amountB > 0) {
            Integer nearest = findNearest(currency, amountB);
            requested.add(nearest);
            amountB = amountB - nearest;
        }
        return requested;
    }

    private static Integer findNearest(Currency currency, Integer digitNumber) {
        NavigableSet<Integer> set = new TreeSet<>(currency.getAcceptableDenominations());
        return set.floor(digitNumber);
    }

    @Override
    public void insertMoney(List<Banknote> banknotes) {
        if (oneCurrencyCheck(banknotes)) {
            ClusterManager<Banknote> banknoteClusterManager =
                    atmCurrencyClusters.get(banknotes.get(0).currency());
            try {
                banknoteClusterManager.putBanknote(banknotes);
            } catch (ClusterOperationException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Banknotes of different denominations!");
        }
    }

    private boolean oneCurrencyCheck(List<Banknote> banknotes) {
        if (banknotes == null || banknotes.size() == 0) return false;
        Currency currency = banknotes.get(0).currency();
        for (var banknote : banknotes) {
            if (banknote.currency() != currency) return false;
        }
        return true;
    }

    @Override
    public Integer getBalanceForCurrency(Currency currency) {
        return atmCurrencyClusters.get(currency).getTotalSumFromCluster();
    }
}

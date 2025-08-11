package ru.otus.domain;

import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum Currency {
    RUB(convertToSet(100, 200, 500, 1000, 2000, 5000)),
    EUR(convertToSet(5, 10, 20, 50, 100, 200, 500));

    private static final Map<Currency, Set<Integer>> ACCEPTABLE_DENOMINATION_FOR_CURRENCY = new HashMap<>();
    ;

    static {
        for (var currency : values()) {
            ACCEPTABLE_DENOMINATION_FOR_CURRENCY.put(currency, currency.denomination);
        }
    }

    final Set<Integer> denomination;

    Currency(Set<Integer> denomination) {
        this.denomination = unmodifiableSet(denomination);
    }

    private static Set<Integer> convertToSet(int... denomination) {
        return stream(denomination).boxed().collect(toSet());
    }

    public Set<Integer> getAcceptableDenominations() {
        return this.denomination;
    }

    public static Set<Integer> getAcceptableDenominations(Currency currency) {
        return ACCEPTABLE_DENOMINATION_FOR_CURRENCY.get(currency);
    }
}

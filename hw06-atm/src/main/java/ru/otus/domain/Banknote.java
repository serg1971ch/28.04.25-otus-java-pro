package ru.otus.domain;

import static java.util.Objects.requireNonNull;

public record Banknote(Integer denomination, Currency currency) {
    public Banknote {
        requireNonNull(currency);
        requireNonNull(denomination);
        if (!currency.getAcceptableDenominations().contains(denomination)) throw new IllegalArgumentException();
    }
}

package ru.otus.cells;

import ru.otus.domain.Banknote;

import java.util.NoSuchElementException;

public interface Cell {
	Banknote getBanknote() throws NoSuchElementException;

	void addBanknote(final Banknote banknote) throws IllegalArgumentException;

	int calculateBalance();

	int size();
}

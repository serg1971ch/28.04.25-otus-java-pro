package ru.otus.factories;

import ru.otus.cells.Cell;
import ru.otus.cells.impl.BasicCell;
import ru.otus.domain.Banknote;

import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AtmBalance {
    private final Map<Banknote, Cell> cells;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private int totalBalance;

    public AtmBalance() {
        // Инициализация ячеек для каждого номинала
        cells = new TreeMap<>((b1, b2) -> Integer.compare(b2.getValue(), b1.getValue()));
        for (Banknote banknote : Banknote.values()) {
            cells.put(banknote, new BasicCell(new ArrayList<>(), banknote.getValue()));
        }
        totalBalance = 0;
    }

    public synchronized void updateTotalBalance() {
        lock.writeLock().lock();
        try {
            totalBalance = cells.values().stream()
                    .mapToInt(Cell::calculateBalance)
                    .sum();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int calculateTotalBalance() {
        lock.readLock().lock();
        try {
            return totalBalance;
        } finally {
            lock.readLock().unlock();
        }
    }

    public Map<Banknote, Cell> getCells() {
        lock.readLock().lock();
        try {
            return new TreeMap<>(cells);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Cell getCell(Banknote banknote) {
        lock.readLock().lock();
        try {
            return cells.get(banknote);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void addCell(Banknote banknote, Cell cell) {
        lock.writeLock().lock();
        try {
            if (cells.containsKey(banknote)) {
                throw new IllegalArgumentException("Cell for this banknote already exists");
            }
            cells.put(banknote, cell);
            updateTotalBalance();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeCell(Banknote banknote) {
        lock.writeLock().lock();
        try {
            if (!cells.containsKey(banknote)) {
                throw new NoSuchElementException("Cell for this banknote does not exist");
            }
            cells.remove(banknote);
            updateTotalBalance();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String toString() {
        lock.readLock().lock();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("AtmBalance{\n");
            for (Map.Entry<Banknote, Cell> entry : cells.entrySet()) {
                sb.append("  ")
                        .append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue())
                        .append("\n");
            }
            sb.append("  totalBalance=")
                    .append(totalBalance)
                    .append("\n}");
            return sb.toString();
        } finally {
            lock.readLock().unlock();
        }
    }
}

package ru.otus.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private final Map<K, V> cache = new WeakHashMap<>(10, 1);
    private static final int MAX_SIZE = 15;
    private static int resetCount = 0;
    private final List<HwListener<K, V>> listeners = new ArrayList<>();
    @Override
    public void put(K key, V value) {
        if (cache.size() > MAX_SIZE) {
            System.gc();
            resetCount++;
        }
        cache.put(key, value);
        notify(key, value, "put");
    }

    @Override
    public void remove(K key) {
        var value = cache.get(key);
        cache.remove(key);
        notify(key, value, "remove");
    }

    @Override
    public V get(K key) {
        var value = cache.get(key);
        notify(key, value, "get");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    @Override
    public List<V> findAll() {
        return cache.values().stream().toList();
    }

    @Override
    public V update(K key, V value) {
        cache.computeIfPresent(key, (k, v) -> v = value);
        return cache.get(key);
    }

    @Override
    public void clearCache() {
        cache.clear();
        resetCount = 0;
    }

    @Override
    public int getResetCount() {
        return resetCount;
    }

    @Override
    public int getMaxSize() {
        return MAX_SIZE;
    }

    private void notify(K key, V value, String action) {
        listeners.forEach(listener -> listener.notify(key, value, action));
    }
}

package ru.otus.cachehw;

import java.util.List;

public interface HwCache<K, V> {

    void put(K key, V value);

    void remove(K key);

    V get(K key);

    void addListener(HwListener<K, V> listener);

    void removeListener(HwListener<K, V> listener);

    List<V> findAll();

    V update(K key, V value);

    void clearCache();

    int getResetCount();

    int getMaxSize();
}

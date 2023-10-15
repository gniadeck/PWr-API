package dev.wms.pwrapi.utils.map;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;

/**
 * A thread-safe wrapper of {@link ConcurrentHashMap}
 * extended to automatically remove keys if they were not used
 * for a specified amount of time.
 * <p>
 * The expiration of keys is managed by the {@link #cacheTTL} variable,
 * which specifies the amount of time in seconds after which
 * an entry will be automatically removed from the map if it hasn't been
 * accessed. The cacheTTL can be set using the constructor or the
 * {@link #setCacheTTL(int)} method.
 * </p>
 */
public class ExpirationCache<K, V> {

    @Value("${concurrent-expiry-hash-map.cache-ttl-in-secs}")
    private int cacheTTL;

    private final Map<K, TimestampedContainer<V>> cache;

    public ExpirationCache() {
        this.cache = new ConcurrentHashMap<>();
    }

    /**
     * @param cacheTTL the time in seconds after which an entry will be removed if it was not used.
     */
    public ExpirationCache(int cacheTTL) {
        this();
        if (cacheTTL <= 0) {
            throw new IllegalArgumentException("cacheTTL must be greater than 0");
        }
        this.cacheTTL = cacheTTL;
    }

    @Scheduled(fixedDelayString = "${concurrent-expiry-hash-map.cache-clear-interval-in-secs}000")
    protected void removeExpiredEntriesTask() {
        removeExpiredEntries();
    }

    public boolean containsKey(@NotNull K key) {
        TimestampedContainer<V> dataFromCache = cache.get(key);

        if (dataFromCache == null) {
            return false;
        }

        dataFromCache.setTimestampToNow();
        return true;
    }

    public V get(@NotNull K key) {
        return cache.get(key).getDataAndUpdateTimestamp();
    }

    public V put(@NotNull K key, @NotNull V value) {
        return put(key, value, LocalDateTime.now());
    }

    protected V put(@NotNull K key, @NotNull V value, @NotNull LocalDateTime timestamp) {
        TimestampedContainer<V> prevValue = cache.put(key, new TimestampedContainer<>(value, timestamp));
        return prevValue == null ? null : prevValue.getData();
    }

    public V remove(@NotNull K key) {
        TimestampedContainer<V> prevValue = cache.remove(key);
        return prevValue == null ? null : prevValue.getData();
    }

    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        m.forEach((key, value) -> put(key, value, LocalDateTime.now()));
    }

    public void clear() {
        cache.clear();
    }

    public int size() {
        return cache.size();
    }

    public boolean isEmpty(){
        return cache.isEmpty();
    }

    public boolean remove(@NotNull K key, @NotNull V value) {
        TimestampedContainer<V> dataFromCache = cache.get(key);

        if(dataFromCache == null || !dataFromCache.getData().equals(value)) {
            return false;
        }

        return cache.remove(key, dataFromCache);
    }

    public V replace(@NotNull K key, @NotNull V value) {
        TimestampedContainer<V> prevData = cache.replace(key, new TimestampedContainer<>(value));
        return prevData == null ? null : prevData.getData();
    }

    public V computeIfAbsent(@NotNull K key, Function<? super K, ? extends V> mappingFunction) {
        if(cache.containsKey(key)) {
            return cache.get(key).getDataAndUpdateTimestamp(LocalDateTime.now());
        }

        V computedValue = mappingFunction.apply(key);
        cache.put(key, new TimestampedContainer<>(computedValue));
        return computedValue;
    }

    public V putIfAbsent(@NotNull K key, @NotNull V value) {
        if(cache.containsKey(key)) {
            return cache.get(key).getDataAndUpdateTimestamp(LocalDateTime.now());
        }

        cache.put(key, new TimestampedContainer<>(value));
        return value;
    }

    private void removeExpiredEntries() {
        if (cache.isEmpty()) {
            return;
        }

        LocalDateTime currentTime = LocalDateTime.now();

        cache.entrySet().removeIf(entry -> {
            TimestampedContainer<V> data = entry.getValue();
            LocalDateTime lastRequestTime = data.getTimestamp();
            long secondsSinceLastRequest = lastRequestTime.until(currentTime, ChronoUnit.SECONDS);

            return secondsSinceLastRequest > cacheTTL;
        });
    }

    public int getCacheTTL() {
        return cacheTTL;
    }

    /**
     * @param cacheTTL the time in seconds after which an entry will be removed if it was not used.
     */
    public void setCacheTTL(int cacheTTL) {
        if (cacheTTL <= 0) {
            throw new IllegalArgumentException("cacheTTL must be greater than 0");
        }

        this.cacheTTL = cacheTTL;
    }
}
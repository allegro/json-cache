package pl.allegro.tech.jsoncache;

public interface EntityCache<K, V> {

    V get(K key);

    V put(K key, V value);

    <X extends Exception> V computeIfAbsent(K key, CacheLoader<? super K, ? extends V, ? extends X> loader) throws X;

    long size();

    void invalidateAll();

}

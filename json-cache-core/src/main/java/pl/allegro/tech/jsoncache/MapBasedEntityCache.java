package pl.allegro.tech.jsoncache;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MapBasedEntityCache<K, V> implements EntityCache<K, V> {

    private final Map<K, V> map;

    public MapBasedEntityCache() {
        this(HashMap::new);
    }

    public MapBasedEntityCache(Supplier<? extends Map<K, V>> mapSupplier) {
        this.map = mapSupplier.get();
    }

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public <X extends Exception> V computeIfAbsent(
            K key,
            CacheLoader<? super K, ? extends V, ? extends X> loader
    ) throws X {
        try {
            return map.computeIfAbsent(key, loader.asFunction());
        } catch (CacheLoadingException ex) {
            throw ex.<X>getTypedCause();
        }
    }

    @Override
    public long size() {
        return map.size();
    }

    @Override
    public void invalidateAll() {
        map.clear();
    }

}

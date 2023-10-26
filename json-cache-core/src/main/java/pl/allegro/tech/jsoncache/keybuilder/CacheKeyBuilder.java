package pl.allegro.tech.jsoncache.keybuilder;

/**
 * Define how should cache key be extracted from given value.
 *
 * @param <K> key type
 * @param <V> value type
 */
public interface CacheKeyBuilder<K, V> {

    /**
     * Extract key from given value.
     *
     * @param value key source
     * @return cache key
     * @throws KeyBuildingException in case cache key cannot be built
     */
    K buildKey(V value) throws KeyBuildingException;

}

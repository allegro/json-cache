package pl.allegro.tech.jsoncache;

/**
 * Storage for deserialized {@link CacheableEntity entities} associated by keys assembled with
 * {@link pl.allegro.tech.jsoncache.keybuilder.CacheKeyBuilder key builders}.
 *
 * @param <K> key type
 * @param <V> value type
 */
public interface EntityCache<K, V> {

    /**
     * Retrieve {@link CacheableEntity entity} associated to given key.
     *
     * @param key key
     * @return entity or {@code null} if there's no association
     */
    V get(K key);

    /**
     * Associate {@link CacheableEntity entity} with selected key, overriding any existing associations.
     *
     * @param key   key
     * @param value entity to associate
     * @return previously associated entity or {@code null} if there was no association
     */
    V put(K key, V value);

    /**
     * Attempt to retrieve {@link CacheableEntity entity} if associated to selected key, otherwise compute
     * {@link CacheLoader loading function} and if it completes successfully, associate loaded entity with the key.
     *
     * @param key    key
     * @param loader {@link CacheLoader loading function}
     * @param <X>    exception type that might be thrown by loading function
     * @return previously associated entity or new computed entity
     * @throws X in case loading function cannot provide an entity
     */
    <X extends Exception> V computeIfAbsent(K key, CacheLoader<? super K, ? extends V, ? extends X> loader) throws X;

    /**
     * Return amount of entries in the cache.
     *
     * @return entries count
     */
    long size();

    /**
     * Remove all currently stored associations.
     */
    void invalidateAll();

}

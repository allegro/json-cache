package pl.allegro.tech.jsoncache;

/**
 * Component implementing this interface is responsible for providing {@link EntityCache cache instances} for different
 * {@link CacheableEntity entities}, depending on the {@link CacheableEntity#cacheName() name parameter}.
 */
public interface CacheResolver {

    /**
     * Resolve cache instance for given name.
     *
     * @param cacheName {@link CacheableEntity#cacheName() cache reference}
     * @param <K>       key type
     * @param <V>       entity type
     * @return {@link EntityCache cache instance}
     * @apiNote There are no restrictions that each cache should be created per entity type, as long as keys could be
     * properly extracted and would remain unique
     */
    <K, V> EntityCache<K, V> resolveCache(String cacheName);

}

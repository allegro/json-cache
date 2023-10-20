package pl.allegro.tech.jsoncache.keybuilder.stategy;


import pl.allegro.tech.jsoncache.CacheableEntity;
import pl.allegro.tech.jsoncache.keybuilder.CacheKeyBuilder;

/**
 * Strategy that defines how should we analyze given value and select its properties to build the key.
 *
 * @param <K> key type
 * @param <V> value type
 */
public interface CacheKeyBuilderStrategy<K, V> {

    /**
     * Use {@link CacheableEntity entity cache metadata} to compile a {@link CacheKeyBuilder key builder}.
     *
     * @param entityDescriptor entity cache metadata
     * @return key builder
     * @throws Exception in case builder cannot be created for given {@link CacheableEntity entity descriptor}
     */
    CacheKeyBuilder<K, V> prepareBuilder(CacheableEntity entityDescriptor) throws Exception;

    /**
     * Verify if {@link CacheableEntity entity cache metadata} contains all necessary information for this strategy.
     *
     * @param entityDescriptor entity cache metadata
     * @return {@code true} if this strategy will be suitable for given entity, {@code false} otherwise
     */
    boolean supports(CacheableEntity entityDescriptor);

    boolean canExtractKeyFrom(Class<?> valueType);

}

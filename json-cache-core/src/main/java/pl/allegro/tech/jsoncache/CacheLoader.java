package pl.allegro.tech.jsoncache;

import java.util.function.Function;

/**
 * Strategy how to supply {@link EntityCache cache} with values.
 *
 * @param <K> key type
 * @param <V> value type
 * @param <X> exception type
 */
@FunctionalInterface
public interface CacheLoader<K, V, X extends Exception> {

    /**
     * Load value for given key.
     *
     * @param key key
     * @return value
     * @throws X in case value cannot be loaded
     */
    V loadValue(K key) throws X;

    /**
     * Translate loader into {@link Function function}, wrapping possible exception with {@link CacheLoadingException}
     *
     * @return loading function
     */
    default Function<K, V> asFunction() {
        return key -> {
            try {
                return loadValue(key);
            } catch (Exception ex) {
                throw new CacheLoadingException(ex);
            }
        };
    }

}

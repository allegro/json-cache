package pl.allegro.tech.jsoncache;

import java.util.function.Function;

@FunctionalInterface
public interface CacheLoader<K, V, X extends Exception> {

    V loadValue(K key) throws X;

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

package pl.allegro.tech.jsoncache;

public interface CacheResolver {

    <K, V> EntityCache<K, V> resolveCache(String cacheName);

}

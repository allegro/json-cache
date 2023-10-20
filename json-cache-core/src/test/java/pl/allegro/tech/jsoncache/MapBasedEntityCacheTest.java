package pl.allegro.tech.jsoncache;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MapBasedEntityCacheTest {

    @Test
    public void shouldInsertElement() {
        // given
        EntityCache<String, Object> cache = new MapBasedEntityCache<>();

        // and
        String key = "key";
        Object testObject = new Object();

        // when
        Object previous = cache.put(key, testObject);

        // then
        assertNull(previous);

        assertEquals(1L, cache.size());
        assertSame(testObject, cache.get(key));
    }

    @Test
    public void shouldReplaceElement() {
        // given
        String key = "key";
        Object testObject = new Object();

        // and
        Map<String, Object> precomputedCache = new HashMap<>();
        precomputedCache.put(key, testObject);
        EntityCache<String, Object> cache = new MapBasedEntityCache<>(() -> precomputedCache);

        // and
        Object newObject = new Object();

        // when
        Object previous = cache.put(key, newObject);

        // then
        assertSame(previous, testObject);

        assertEquals(1L, cache.size());
        assertSame(newObject, cache.get(key));
    }

    @Test
    public void shouldComputeElement() throws Exception {
        // given
        EntityCache<String, Object> cache = new MapBasedEntityCache<>();

        // and
        String key = "key";
        Object testObject = new Object();
        CacheLoader<String, Object, ?> loader = k -> testObject;

        // when
        Object computed = cache.computeIfAbsent(key, loader);

        // then
        assertEquals(1L, cache.size());
        assertSame(testObject, computed);
    }

    @Test
    public void shouldNotComputeIfEntryForGivenKeyAlreadyExists() throws Exception {
        // given
        String key = "key";
        Object testObject = new Object();

        // and
        Map<String, Object> precomputedCache = new HashMap<>();
        precomputedCache.put(key, testObject);
        EntityCache<String, Object> cache = new MapBasedEntityCache<>(() -> precomputedCache);

        // and
        CacheLoader<String, Object, ?> loader = k -> new Object();

        // when
        Object computed = cache.computeIfAbsent(key, loader);

        // then
        assertSame(computed, testObject);
        assertEquals(1L, cache.size());
    }

    @Test
    public void shouldInvalidateCache() {
        // given
        String key = "key";
        Object testObject = new Object();

        // and
        Map<String, Object> precomputedCache = new HashMap<>();
        precomputedCache.put(key, testObject);
        EntityCache<String, Object> cache = new MapBasedEntityCache<>(() -> precomputedCache);

        // when
        cache.invalidateAll();

        // then
        assertEquals(0L, cache.size());
    }

    @Test
    public void shouldRethrowAnExceptionIfCacheLoaderFails() {
        // given
        EntityCache<String, Object> cache = new MapBasedEntityCache<>();

        // and
        String key = "key";
        CacheLoader<String, Object, IOException> loader = k -> {
            throw new IOException();
        };

        // when
        Executable compute = () -> cache.computeIfAbsent(key, loader);

        // then
        assertThrows(IOException.class, compute);
        assertEquals(0L, cache.size());
    }

}

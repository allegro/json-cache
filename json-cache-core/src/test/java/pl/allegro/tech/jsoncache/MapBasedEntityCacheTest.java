package pl.allegro.tech.jsoncache;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MapBasedEntityCacheTest {

    @Test
    void shouldInsertElement() {
        // given
        var cache = new MapBasedEntityCache<>();

        // and
        var key = "key";
        var testObject = new Object();

        // when
        var previous = cache.put(key, testObject);

        // then
        assertNull(previous);

        assertEquals(1L, cache.size());
        assertSame(testObject, cache.get(key));
    }

    @Test
    void shouldReplaceElement() {
        // given
        var key = "key";
        var testObject = new Object();

        // and
        var precomputedCache = new ConcurrentHashMap<String, Object>();
        precomputedCache.put(key, testObject);
        var cache = new MapBasedEntityCache<>(() -> precomputedCache);

        // and
        var newObject = new Object();

        // when
        var previous = cache.put(key, newObject);

        // then
        assertSame(previous, testObject);

        assertEquals(1L, cache.size());
        assertSame(newObject, cache.get(key));
    }

    @Test
    void shouldComputeElement() {
        // given
        var cache = new MapBasedEntityCache<>();

        // and
        var key = "key";
        var testObject = new Object();

        // when
        var computed = cache.computeIfAbsent(key, k -> testObject);

        // then
        assertEquals(1L, cache.size());
        assertSame(testObject, computed);
    }

    @Test
    void shouldNotComputeIfEntryForGivenKeyAlreadyExists() {
        // given
        var key = "key";
        var testObject = new Object();

        // and
        var precomputedCache = new ConcurrentHashMap<String, Object>();
        precomputedCache.put(key, testObject);
        var cache = new MapBasedEntityCache<>(() -> precomputedCache);

        // when
        var computed = cache.computeIfAbsent(key, k -> new Object());

        // then
        assertSame(computed, testObject);
        assertEquals(1L, cache.size());
    }

    @Test
    void shouldInvalidateCache() {
        // given
        var key = "key";
        var testObject = new Object();

        // and
        var precomputedCache = new ConcurrentHashMap<String, Object>();
        precomputedCache.put(key, testObject);
        var cache = new MapBasedEntityCache<>(() -> precomputedCache);

        // when
        cache.invalidateAll();

        // then
        assertEquals(0L, cache.size());
    }

    @Test
    void shouldRethrowAnExceptionIfCacheLoaderFails() {
        // given
        var cache = new MapBasedEntityCache<>();

        // and
        var key = "key";

        // when
        var compute = (Executable) () -> cache.computeIfAbsent(key, k -> {
            throw new IOException();
        });

        // then
        assertThrows(IOException.class, compute);
        assertEquals(0L, cache.size());
    }

}

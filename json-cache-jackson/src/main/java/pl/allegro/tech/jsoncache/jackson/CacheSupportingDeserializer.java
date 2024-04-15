package pl.allegro.tech.jsoncache.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import pl.allegro.tech.jsoncache.EntityCache;
import pl.allegro.tech.jsoncache.keybuilder.CacheKeyBuilder;
import pl.allegro.tech.jsoncache.keybuilder.KeyBuildingException;

import java.io.IOException;

/**
 * Deserializer that wraps default deserializer and caches created instances in memory by key constructed using
 * {@link CacheKeyBuilder key builder}. Key is extracted directly from plain {@link JsonNode} before actual
 * deserialization occurs; new instance is only created if it's not currently present in the cache.
 *
 * @param <K> key type
 */
public class CacheSupportingDeserializer<K> extends DelegatingDeserializer {

    /**
     * Entity cache.
     */
    private final EntityCache<K, Object> cache;
    /**
     * Key builder for given entity.
     */
    private final CacheKeyBuilder<K, JsonNode> keyBuilder;

    /**
     * Default constructor.
     *
     * @param originalDeserializer original {@link JsonDeserializer deserializer}
     * @param cache                {@link EntityCache cache}
     * @param keyBuilder           {@link CacheKeyBuilder key builder}
     */
    public CacheSupportingDeserializer(JsonDeserializer<?> originalDeserializer,
                                       EntityCache<K, Object> cache,
                                       CacheKeyBuilder<K, JsonNode> keyBuilder) {
        super(originalDeserializer);
        this.cache = cache;
        this.keyBuilder = keyBuilder;
    }

    @Override
    protected JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> newDelegatee) {
        return new CacheSupportingDeserializer<>(newDelegatee, cache, keyBuilder);
    }

    /**
     * Extract key from {@link JsonNode parsed json} and locate deserialized instance in the cache. If not found,
     * recreate parser for target deserialization, use default deserializer to create an instance and store it in the cache.
     * In case key cannot be extracted (indicated by {@link KeyBuildingException}), skip caching and return deserialized
     * instance directly.
     *
     * @param p    Parser used for reading JSON content
     * @param ctxt Context that can be used to access information about
     *             this deserialization activity
     * @return deserialized instance
     * @throws IOException in case JSON cannot be properly parsed or deserialized
     */
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode parsedJson = ctxt.readTree(p);
        try {
            return cache.computeIfAbsent(
                    keyBuilder.buildKey(parsedJson),
                    key -> deserializeJson(parsedJson, p.getCodec(), ctxt)
            );
        } catch (KeyBuildingException ex) {
            return deserializeJson(parsedJson, p.getCodec(), ctxt);
        }
    }

    private Object deserializeJson(JsonNode parsedJson, ObjectCodec originalCodec, DeserializationContext ctxt)
            throws IOException {
        JsonParser p = parsedJson.traverse(originalCodec);
        if (p.currentToken() == null) {
            p.nextToken();
        }
        return super.deserialize(p, ctxt);
    }

}

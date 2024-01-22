package pl.allegro.tech.jsoncache.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import pl.allegro.tech.jsoncache.CacheResolver;
import pl.allegro.tech.jsoncache.CacheableEntity;
import pl.allegro.tech.jsoncache.keybuilder.CacheKeyBuilderFactory;

/**
 * Deserializer modifier used to wrap standard {@link com.fasterxml.jackson.databind.deser.BeanDeserializer POJO deserializer}
 * and apply caching mechanism based on presence of {@link CacheableEntity} annotation on deserialized type.
 */
public class CacheApplyingDeserializerModifier extends BeanDeserializerModifier {

    private final CacheKeyBuilderFactory cacheKeyBuilderFactory;
    private final CacheResolver cacheResolver;

    public CacheApplyingDeserializerModifier(CacheKeyBuilderFactory cacheKeyBuilderFactory, CacheResolver cacheResolver) {
        this.cacheKeyBuilderFactory = cacheKeyBuilderFactory;
        this.cacheResolver = cacheResolver;
    }

    /**
     * Conditionally apply caching mechanism to default {@link JsonDeserializer deserializer} depending on the presence
     * of {@link CacheableEntity} annotation in metadata of class which instance should be constructed from the JSON payload.
     *
     * @param config       deserialization config
     * @param beanDesc     class metadata
     * @param deserializer original deserializer
     * @return {@link CacheSupportingDeserializer caching deserializer} if cache should be applied, original deserializer otherwise
     * @apiNote we don't verify generic types at this point so any misconfiguration will be reported as {@link ClassCastException}
     * in runtime
     */
    @Override
    public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
        CacheableEntity cacheableEntity = beanDesc.getClassAnnotations().get(CacheableEntity.class);
        if (cacheableEntity == null) {
            return deserializer;
        }
        return cacheKeyBuilderFactory.findCacheKeyBuilderFor(cacheableEntity, JsonNode.class)
                .<JsonDeserializer<?>>map(cacheKeyBuilder -> new CacheSupportingDeserializer<>(
                        deserializer,
                        cacheResolver.resolveCache(cacheableEntity.cacheName()),
                        cacheKeyBuilder
                ))
                .orElse(deserializer);
    }

}

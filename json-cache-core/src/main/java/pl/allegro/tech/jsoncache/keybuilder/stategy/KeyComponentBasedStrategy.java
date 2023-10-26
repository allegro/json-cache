package pl.allegro.tech.jsoncache.keybuilder.stategy;


import pl.allegro.tech.jsoncache.CacheableEntity;
import pl.allegro.tech.jsoncache.keybuilder.CacheKeyBuilder;
import pl.allegro.tech.jsoncache.keybuilder.KeyPartMissingException;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * Helper strategy used as a base for component based builders.
 * Such strategy will analyze the presence of {@link CacheableEntity#keyComponents() key components} and use them to
 * extract proper key parts from given value in order they are listed.
 *
 * @param <V> value type
 * @apiNote since all values will be joined into one single value, key type is {@link String} for convenience
 */
public abstract class KeyComponentBasedStrategy<V> extends CacheKeyBuilderStrategyBase<String, V> {

    @Override
    public boolean supports(CacheableEntity entityDescriptor) {
        return entityDescriptor.keyComponents().length > 0;
    }

    @Override
    public CacheKeyBuilder<String, V> prepareBuilder(CacheableEntity entityDescriptor) {
        return value -> Stream.of(entityDescriptor.keyComponents())
                .map(component -> extractKeyPart(value, component))
                .collect(joining(entityDescriptor.keySeparator()));
    }

    /**
     * Define how to extract a {@link String} key part from given value.
     *
     * @param value     key source
     * @param component named property (e.g. field)
     * @return key part
     * @throws KeyPartMissingException if key part cannot be extracted from given value
     */
    protected abstract String extractKeyPart(V value, String component) throws KeyPartMissingException;

}

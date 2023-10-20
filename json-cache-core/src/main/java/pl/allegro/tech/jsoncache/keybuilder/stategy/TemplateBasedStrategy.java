package pl.allegro.tech.jsoncache.keybuilder.stategy;


import pl.allegro.tech.jsoncache.CacheableEntity;

/**
 * Helper strategy used as a base for template builders.
 * Such strategy will analyze the presence of {@link CacheableEntity#keyTemplate() key template} and use it to render
 * key as a whole using provided value as a context.
 *
 * @param <V> value type
 * @apiNote template is defined as {@link String} and so is the key type
 */
public abstract class TemplateBasedStrategy<V> extends CacheKeyBuilderStrategyBase<String, V> {

    @Override
    public boolean supports(CacheableEntity entityDescriptor) {
        return !entityDescriptor.keyTemplate().isBlank();
    }

}

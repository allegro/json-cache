package pl.allegro.tech.jsoncache.keybuilder.stategy;

/**
 * Common base for all {@link CacheKeyBuilderStrategy strategies}.
 *
 * @param <K> key type
 * @param <V> value type
 */
public abstract class CacheKeyBuilderStrategyBase<K, V> implements CacheKeyBuilderStrategy<K, V> {

    @Override
    public boolean canExtractKeyFrom(Class<?> valueType) {
        return valueType.isAssignableFrom(supportedValueType());
    }

    /**
     * Additional context about supported value type, next to {@link V type parameter}.
     *
     * @return supported {@link Class type}
     */
    protected abstract Class<? extends V> supportedValueType();

}

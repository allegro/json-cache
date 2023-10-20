package pl.allegro.tech.jsoncache.keybuilder.stategy;

public abstract class CacheKeyBuilderStrategyBase<K, V> implements CacheKeyBuilderStrategy<K, V> {

    @Override
    public boolean canExtractKeyFrom(Class<?> valueType) {
        return valueType.isAssignableFrom(supportedValueType());
    }

    protected abstract Class<? extends V> supportedValueType();

}

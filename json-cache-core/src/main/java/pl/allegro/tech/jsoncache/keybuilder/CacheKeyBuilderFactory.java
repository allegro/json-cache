package pl.allegro.tech.jsoncache.keybuilder;

import pl.allegro.tech.jsoncache.CacheableEntity;
import pl.allegro.tech.jsoncache.keybuilder.stategy.CacheKeyBuilderStrategy;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Simple factory that aggregates different {@link CacheKeyBuilderStrategy strategies} how to build a cache key
 * and selects one that supports provided {@link CacheableEntity entity metadata}.
 */
public class CacheKeyBuilderFactory {

    private final List<CacheKeyBuilderStrategy<?, ?>> cacheKeyBuilderStrategies;

    public CacheKeyBuilderFactory(Collection<? extends CacheKeyBuilderStrategy<?, ?>> cacheKeyBuilderStrategies) {
        this.cacheKeyBuilderStrategies = List.copyOf(cacheKeyBuilderStrategies);
    }

    /**
     * Select suitable strategy and prepare {@link CacheKeyBuilder key builder}.
     *
     * @param entityDescriptor entity cache metadata
     * @return {@link Optional optional} key builder or {@link Optional#empty() empty} otherwise
     * @apiNote we don't specify any generic parameters as they might get erased while dynamically autowiring key builder
     */
    public <V> Optional<CacheKeyBuilder<?, V>> findCacheKeyBuilderFor(CacheableEntity entityDescriptor,
                                                                      Class<? extends V> supportedValueType) {
        return cacheKeyBuilderStrategies.stream()
                .filter(strategy -> strategy.supports(entityDescriptor))
                .filter(strategy -> strategy.canExtractKeyFrom(supportedValueType))
                .flatMap(strategy -> this.<V>prepareBuilderIfPossible(strategy, entityDescriptor))
                .findFirst();
    }

    @SuppressWarnings("unchecked")
    private <V> Stream<CacheKeyBuilder<?, V>> prepareBuilderIfPossible(CacheKeyBuilderStrategy<?, ?> strategy,
                                                                       CacheableEntity entityDescriptor) {
        try {
            return Stream.of(
                    (CacheKeyBuilder<?, V>) strategy.prepareBuilder(entityDescriptor)
            );
        } catch (Exception ex) {
            return Stream.empty();
        }
    }

}

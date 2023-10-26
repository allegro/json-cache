package pl.allegro.tech.jsoncache.keybuilder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import pl.allegro.tech.jsoncache.CacheableEntity;
import pl.allegro.tech.jsoncache.keybuilder.stategy.CacheKeyBuilderStrategy;
import pl.allegro.tech.jsoncache.support.CacheableEntityBuilder;
import pl.allegro.tech.jsoncache.keybuilder.support.KeyBuilderStrategies;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CacheKeyBuilderFactoryTest {

    @Test
    public void givenKeyBuilderFactoryWithoutStrategiesAndEntityMetadataWhenRequestedForKeyBuilderNoneIsReturned() {
        // given
        CacheKeyBuilderFactory factory = new CacheKeyBuilderFactory(List.of());

        // and
        CacheableEntity entityMetadata = new CacheableEntityBuilder()
                .withKeyComponents("a", "b", "c")
                .build();

        // when
        Optional<? extends CacheKeyBuilder<?, ?>> keyBuilder = factory.findCacheKeyBuilderFor(entityMetadata, Object.class);

        // then
        assertTrue(keyBuilder::isEmpty);
    }

    @ParameterizedTest
    @ArgumentsSource(KeyBuilderStrategies.StrategyMatchingMetadata.class)
    public void givenKeyBuilderFactoryWithStrategyAndEntityMetadataWhenRequestedForKeyBuilderMatchingOneIsReturned(
            CacheKeyBuilderStrategy<?, ?> strategy,
            CacheableEntity entityMetadata
    ) {
        // given
        CacheKeyBuilderFactory factory = new CacheKeyBuilderFactory(List.of(strategy));

        // when
        Optional<? extends CacheKeyBuilder<?, ?>> keyBuilder = factory.findCacheKeyBuilderFor(entityMetadata, Object.class);

        // then
        assertTrue(keyBuilder::isPresent);
    }

    @Test
    public void givenKeyBuilderFactoryWithStrategiesAndInvalidEntityMetadataWhenRequestedForKeyBuilderNoneIsReturned() {
        // given
        CacheKeyBuilderFactory factory = new CacheKeyBuilderFactory(List.of(
                KeyBuilderStrategies.DUMMY_COMPONENT_STRATEGY,
                KeyBuilderStrategies.DUMMY_TEMPLATE_STRATEGY
        ));

        // and
        CacheableEntity entityMetadata = new CacheableEntityBuilder().build();

        // when
        Optional<? extends CacheKeyBuilder<?, ?>> keyBuilder = factory.findCacheKeyBuilderFor(entityMetadata, Object.class);

        // then
        assertTrue(keyBuilder::isEmpty);
    }

    @Test
    public void givenKeyBuilderFactoryWithStrategiesWhenBothSupportEntityMetadataThenKeyBuilderForFirstOneIsReturned()
            throws Exception {
        // given
        CacheKeyBuilderFactory factory = new CacheKeyBuilderFactory(List.of(
                KeyBuilderStrategies.DUMMY_COMPONENT_STRATEGY,
                KeyBuilderStrategies.DUMMY_TEMPLATE_STRATEGY
        ));

        // and
        CacheableEntity entityMetadata = new CacheableEntityBuilder()
                .withKeyComponents("a", "b", "c")
                .withKeyTemplate("{{a}}-{{b}}-{{c}}")
                .build();

        // when
        Optional<? extends CacheKeyBuilder<?, Object>> keyBuilder = factory.findCacheKeyBuilderFor(entityMetadata, Object.class);

        // then
        assertTrue(keyBuilder::isPresent);

        Object keySource = new Object();
        Object expectedKey = KeyBuilderStrategies.DUMMY_COMPONENT_STRATEGY
                .prepareBuilder(entityMetadata)
                .buildKey(keySource);
        Object actualKey = keyBuilder.orElseGet(Assertions::fail)
                .buildKey(new Object());

        assertEquals(expectedKey, actualKey);
    }

}

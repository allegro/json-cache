package pl.allegro.tech.jsoncache.keybuilder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import pl.allegro.tech.jsoncache.CacheableEntity;
import pl.allegro.tech.jsoncache.keybuilder.stategy.CacheKeyBuilderStrategy;
import pl.allegro.tech.jsoncache.keybuilder.support.KeyBuilderStrategies;
import pl.allegro.tech.jsoncache.support.CacheableEntityBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CacheKeyBuilderFactoryTest {

    @Test
    void givenKeyBuilderFactoryWithoutStrategiesAndEntityMetadataWhenRequestedForKeyBuilderNoneIsReturned() {
        // given
        var factory = new CacheKeyBuilderFactory(List.of());

        // and
        var entityMetadata = new CacheableEntityBuilder()
                .withKeyComponents("a", "b", "c")
                .build();

        // when
        var keyBuilder = factory.findCacheKeyBuilderFor(entityMetadata, Object.class);

        // then
        assertTrue(keyBuilder::isEmpty);
    }

    @ParameterizedTest
    @ArgumentsSource(KeyBuilderStrategies.StrategyMatchingMetadata.class)
    void givenKeyBuilderFactoryWithStrategyAndEntityMetadataWhenRequestedForKeyBuilderMatchingOneIsReturned(
            CacheKeyBuilderStrategy<?, ?> strategy,
            CacheableEntity entityMetadata
    ) {
        // given
        var factory = new CacheKeyBuilderFactory(List.of(strategy));

        // when
        var keyBuilder = factory.findCacheKeyBuilderFor(entityMetadata, Object.class);

        // then
        assertTrue(keyBuilder::isPresent);
    }

    @Test
    void givenKeyBuilderFactoryWithStrategiesAndInvalidEntityMetadataWhenRequestedForKeyBuilderNoneIsReturned() {
        // given
        var factory = new CacheKeyBuilderFactory(List.of(
                KeyBuilderStrategies.DUMMY_COMPONENT_STRATEGY,
                KeyBuilderStrategies.DUMMY_TEMPLATE_STRATEGY
        ));

        // and
        var entityMetadata = new CacheableEntityBuilder().build();

        // when
        var keyBuilder = factory.findCacheKeyBuilderFor(entityMetadata, Object.class);

        // then
        assertTrue(keyBuilder::isEmpty);
    }

    @Test
    void givenKeyBuilderFactoryWithStrategiesWhenBothSupportEntityMetadataThenKeyBuilderForFirstOneIsReturned() throws Exception {
        // given
        var factory = new CacheKeyBuilderFactory(List.of(
                KeyBuilderStrategies.DUMMY_COMPONENT_STRATEGY,
                KeyBuilderStrategies.DUMMY_TEMPLATE_STRATEGY
        ));

        // and
        var entityMetadata = new CacheableEntityBuilder()
                .withKeyComponents("a", "b", "c")
                .withKeyTemplate("{{a}}-{{b}}-{{c}}")
                .build();

        // when
        var keyBuilder = factory.findCacheKeyBuilderFor(entityMetadata, Object.class);

        // then
        assertTrue(keyBuilder::isPresent);

        var keySource = new Object();
        var expectedKey = KeyBuilderStrategies.DUMMY_COMPONENT_STRATEGY
                .prepareBuilder(entityMetadata)
                .buildKey(keySource);
        var actualKey = keyBuilder.orElseGet(Assertions::fail)
                .buildKey(new Object());

        assertEquals(expectedKey, actualKey);
    }

}

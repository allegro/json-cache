package pl.allegro.tech.jsoncache.keybuilder.support;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import pl.allegro.tech.jsoncache.CacheableEntity;
import pl.allegro.tech.jsoncache.keybuilder.CacheKeyBuilder;
import pl.allegro.tech.jsoncache.keybuilder.stategy.CacheKeyBuilderStrategy;
import pl.allegro.tech.jsoncache.keybuilder.stategy.KeyComponentBasedStrategy;
import pl.allegro.tech.jsoncache.keybuilder.stategy.TemplateBasedStrategy;
import pl.allegro.tech.jsoncache.support.CacheableEntityBuilder;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@SuppressWarnings("unused")
public class KeyBuilderStrategies {

    public static class StrategyMatchingMetadata implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    arguments(
                            DUMMY_COMPONENT_STRATEGY,
                            new CacheableEntityBuilder()
                                    .withKeyComponents("a", "b", "c")
                                    .build()
                    ),
                    arguments(
                            DUMMY_TEMPLATE_STRATEGY,
                            new CacheableEntityBuilder()
                                    .withKeyTemplate("{{a}}-{{b}}-{{c}}")
                                    .build()
                    )
            );
        }

    }

    public static final CacheKeyBuilderStrategy<String, Object> DUMMY_COMPONENT_STRATEGY = new KeyComponentBasedStrategy<>() {

        @Override
        protected Class<Object> supportedValueType() {
            return Object.class;
        }

        @Override
        protected String extractKeyPart(Object value, String component) {
            return component;
        }

        @Override
        public String toString() {
            return "DUMMY_COMPONENT_STRATEGY";
        }

    };

    public static final CacheKeyBuilderStrategy<String, Object> DUMMY_TEMPLATE_STRATEGY = new TemplateBasedStrategy<>() {

        @Override
        protected Class<Object> supportedValueType() {
            return Object.class;
        }

        @Override
        public CacheKeyBuilder<String, Object> prepareBuilder(CacheableEntity entityDescriptor) {
            return value -> entityDescriptor.keyTemplate();
        }

        @Override
        public String toString() {
            return "DUMMY_TEMPLATE_STRATEGY";
        }

    };

}

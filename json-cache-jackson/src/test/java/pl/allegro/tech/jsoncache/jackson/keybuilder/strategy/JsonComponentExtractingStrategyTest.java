package pl.allegro.tech.jsoncache.jackson.keybuilder.strategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import pl.allegro.tech.jsoncache.keybuilder.KeyBuildingException;
import pl.allegro.tech.jsoncache.keybuilder.stategy.CacheKeyBuilderStrategy;
import pl.allegro.tech.jsoncache.support.CacheableEntityBuilder;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.JsonNodeFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonComponentExtractingStrategyTest {

    private final CacheKeyBuilderStrategy<String, JsonNode> strategy = new JsonComponentExtractingStrategy();

    @Test
    void shouldExtractKeyFromValueIfDescriptorProvidesKeyComponents() throws Exception {
        // given
        var value = JsonNodeFactory.instance.objectNode()
                .put("a", 1)
                .put("b", 2);

        // and
        var entityDescriptor = new CacheableEntityBuilder()
                .withKeyComponents("a", "b")
                .withKeySeparator("-")
                .build();

        // when
        var keyBuilder = strategy.prepareBuilder(entityDescriptor);
        var key = keyBuilder.buildKey(value);

        // then
        assertEquals("1-2", key);
    }

    @Test
    void shouldExtractKeyFromNestedValueIfDescriptorUsesDotNotation() throws Exception {
        // given
        var value = JsonNodeFactory.instance.objectNode()
                .put("a", 1);
        value.putObject("b")
                .put("nested", 2);

        // and
        var entityDescriptor = new CacheableEntityBuilder()
                .withKeyComponents("a", "b.nested")
                .withKeySeparator("-")
                .build();

        // when
        var keyBuilder = strategy.prepareBuilder(entityDescriptor);
        var key = keyBuilder.buildKey(value);

        // then
        assertEquals("1-2", key);
    }

    @Test
    void shouldThrowExceptionIfValueHasMissingKeyParts() throws Exception {
        // given
        var value = JsonNodeFactory.instance.objectNode()
                .put("a", 1);

        // and
        var entityDescriptor = new CacheableEntityBuilder()
                .withKeyComponents("a", "b")
                .withKeySeparator("-")
                .build();

        // when
        var keyBuilder = strategy.prepareBuilder(entityDescriptor);
        var keySupplier = (Executable) () -> keyBuilder.buildKey(value);

        // then
        assertThrows(KeyBuildingException.class, keySupplier);
    }

    @Test
    void shouldExtractEmptyKeyFromValueIfDescriptorProvidesNoKeyComponents() throws Exception {
        // given
        var value = JsonNodeFactory.instance.objectNode()
                .put("a", 1)
                .put("b", 2);

        // and
        var entityDescriptor = new CacheableEntityBuilder()
                .withKeyTemplate("{{a}}-{{b}}")
                .build();

        // when
        var keyBuilder = strategy.prepareBuilder(entityDescriptor);
        var key = keyBuilder.buildKey(value);

        // then
        assertTrue(key.isEmpty());
    }

}

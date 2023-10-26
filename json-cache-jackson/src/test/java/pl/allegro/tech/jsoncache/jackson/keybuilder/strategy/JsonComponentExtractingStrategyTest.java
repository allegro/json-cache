package pl.allegro.tech.jsoncache.jackson.keybuilder.strategy;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import pl.allegro.tech.jsoncache.CacheableEntity;
import pl.allegro.tech.jsoncache.keybuilder.KeyBuildingException;
import pl.allegro.tech.jsoncache.keybuilder.stategy.CacheKeyBuilderStrategy;
import pl.allegro.tech.jsoncache.support.CacheableEntityBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonComponentExtractingStrategyTest {

    @Test
    public void shouldExtractKeyFromValueIfDescriptorProvidesKeyComponents() throws Exception {
        // given
        JsonNode value = JsonNodeFactory.instance.objectNode()
                .put("a", 1)
                .put("b", 2);

        // and
        CacheableEntity entityDescriptor = new CacheableEntityBuilder()
                .withKeyComponents("a", "b")
                .withKeySeparator("-")
                .build();

        // and
        CacheKeyBuilderStrategy<String, JsonNode> strategy = new JsonComponentExtractingStrategy();

        // when
        String key = strategy.prepareBuilder(entityDescriptor).buildKey(value);

        // then
        assertEquals("1-2", key);
    }

    @Test
    public void shouldThrowExceptionIfValueHasMissingKeyParts() {
        // given
        JsonNode value = JsonNodeFactory.instance.objectNode()
                .put("a", 1);

        // and
        CacheableEntity entityDescriptor = new CacheableEntityBuilder()
                .withKeyComponents("a", "b")
                .withKeySeparator("-")
                .build();

        // and
        CacheKeyBuilderStrategy<String, JsonNode> strategy = new JsonComponentExtractingStrategy();

        // when
        Executable keyBuilding = () -> strategy.prepareBuilder(entityDescriptor).buildKey(value);

        // then
        assertThrows(KeyBuildingException.class, keyBuilding);
    }

    @Test
    public void shouldExtractEmptyKeyFromValueIfDescriptorProvidesNoKeyComponents() throws Exception {
        // given
        JsonNode value = JsonNodeFactory.instance.objectNode()
                .put("a", 1)
                .put("b", 2);

        // and
        CacheableEntity entityDescriptor = new CacheableEntityBuilder()
                .withKeyTemplate("{{a}}-{{b}}")
                .build();

        // and
        CacheKeyBuilderStrategy<String, JsonNode> strategy = new JsonComponentExtractingStrategy();

        // when
        String key = strategy.prepareBuilder(entityDescriptor).buildKey(value);

        // then
        assertTrue(key::isEmpty);
    }

}

package pl.allegro.tech.jsoncache.jackson.keybuilder.strategy;

import pl.allegro.tech.jsoncache.keybuilder.KeyPartMissingException;
import pl.allegro.tech.jsoncache.keybuilder.stategy.KeyComponentBasedStrategy;
import tools.jackson.databind.JsonNode;

import java.util.Optional;

/**
 * Strategy for retrieving key parts directly from {@link JsonNode json nodes}.
 * Nested components can be extracted using dot notation, e.g. "user.name" will extract "name" from nested "user" object.
 */
public class JsonComponentExtractingStrategy extends KeyComponentBasedStrategy<JsonNode> {

    @Override
    protected String extractKeyPart(JsonNode value, String component) throws KeyPartMissingException {
        return findComponentNode(value, component)
                .filter(JsonNode::isValueNode)
                .map(JsonNode::asString)
                .orElseThrow(() -> new KeyPartMissingException(value, component));
    }

    @Override
    protected Class<? extends JsonNode> supportedValueType() {
        return JsonNode.class;
    }

    private Optional<JsonNode> findComponentNode(JsonNode value, String component) {
        if (component.indexOf('.') == -1) {
            // no nesting - just flat component
            return value.optional(component);
        }

        for (var fragment : component.split("\\.")) {
            value = value.path(fragment);
        }
        return value.asOptional();
    }

}

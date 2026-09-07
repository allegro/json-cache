package pl.allegro.tech.jsoncache.jackson.keybuilder.strategy;

import tools.jackson.databind.JsonNode;
import pl.allegro.tech.jsoncache.keybuilder.KeyPartMissingException;
import pl.allegro.tech.jsoncache.keybuilder.stategy.KeyComponentBasedStrategy;

/**
 * Strategy for retrieving key parts directly from {@link JsonNode json nodes}.
 */
public class JsonComponentExtractingStrategy extends KeyComponentBasedStrategy<JsonNode> {

    @Override
    protected String extractKeyPart(JsonNode value, String component) throws KeyPartMissingException {
        return value.optional(component)
                .filter(JsonNode::isValueNode)
                .map(JsonNode::asString)
                .orElseThrow(() -> new KeyPartMissingException(value, component));
    }

    @Override
    protected Class<? extends JsonNode> supportedValueType() {
        return JsonNode.class;
    }

}

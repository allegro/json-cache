package pl.allegro.tech.jsoncache.jackson.keybuilder.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import pl.allegro.tech.jsoncache.keybuilder.KeyPartMissingException;
import pl.allegro.tech.jsoncache.keybuilder.stategy.KeyComponentBasedStrategy;

import java.util.Optional;

public class JsonComponentExtractingStrategy extends KeyComponentBasedStrategy<JsonNode> {

    @Override
    protected String extractKeyPart(JsonNode value, String component) throws KeyPartMissingException {
        return Optional.ofNullable(value.get(component))
                .filter(JsonNode::isValueNode)
                .map(JsonNode::asText)
                .orElseThrow(() -> new KeyPartMissingException(value, component));
    }

    @Override
    protected Class<? extends JsonNode> supportedValueType() {
        return JsonNode.class;
    }

}

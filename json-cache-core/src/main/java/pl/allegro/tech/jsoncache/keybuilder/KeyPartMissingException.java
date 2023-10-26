package pl.allegro.tech.jsoncache.keybuilder;

/**
 * Specialized exception that should be used anytime cache key could not be built.
 */
public class KeyPartMissingException extends KeyBuildingException {

    public KeyPartMissingException(Object value, String component) {
        super(String.format("Cannot extract component: %s from value: %s", component, value));
    }

}

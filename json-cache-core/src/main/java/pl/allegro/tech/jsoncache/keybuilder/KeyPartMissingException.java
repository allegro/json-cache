package pl.allegro.tech.jsoncache.keybuilder;

/**
 * Specialized exception that should be used anytime cache key could not be built.
 */
public class KeyPartMissingException extends KeyBuildingException {

    /**
     * Default constructor.
     *
     * @param value     instance from which key was extracted
     * @param component fragment that could not be found
     */
    public KeyPartMissingException(Object value, String component) {
        super(String.format("Cannot extract component: %s from value: %s", component, value));
    }

}

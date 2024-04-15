package pl.allegro.tech.jsoncache.keybuilder;

/**
 * General exception used during key extraction related operations.
 */
public class KeyBuildingException extends RuntimeException {

    /**
     * Bare exception with specified message.
     *
     * @param message reason of exception
     */
    public KeyBuildingException(String message) {
        super(message);
    }

    /**
     * Exception wrapper.
     *
     * @param cause {@link Throwable exception to wrap}
     */
    public KeyBuildingException(Throwable cause) {
        super(cause);
    }

}

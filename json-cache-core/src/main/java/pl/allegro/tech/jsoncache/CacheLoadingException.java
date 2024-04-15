package pl.allegro.tech.jsoncache;

/**
 * Exception wrapper for any issues related to entity loading.
 */
public class CacheLoadingException extends RuntimeException {

    /**
     * Default constructor.
     *
     * @param cause {@link Throwable exception} to wrap
     */
    public CacheLoadingException(Throwable cause) {
        super(cause);
    }

    /**
     * Retrieve wrapped cause, casting it to given type. Should be used in conjunction with {@link CacheLoader} to ensure
     * type correctness.
     *
     * @param <X> original exception type
     * @return typed cause
     */
    @SuppressWarnings("unchecked")
    public <X extends Exception> X getTypedCause() {
        return (X) getCause();
    }

}

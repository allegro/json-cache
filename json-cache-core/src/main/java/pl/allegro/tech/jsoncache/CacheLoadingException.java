package pl.allegro.tech.jsoncache;

public class CacheLoadingException extends RuntimeException {

    public CacheLoadingException(Throwable cause) {
        super(cause);
    }

    @SuppressWarnings("unchecked")
    public <X extends Exception> X getTypedCause() {
        return (X) getCause();
    }

}

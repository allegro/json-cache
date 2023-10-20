package pl.allegro.tech.jsoncache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker indicating given entity should be cached during instance creation basing on key criteria.
 * It might be used e.g. for Jackson deserialization to avoid duplication of objects with the same data.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CacheableEntity {

    /**
     * Template that builds the key after application of the source object, e.g. Handlebars: {@code {{id}}-{{name}}}
     * @return key template
     */
    String keyTemplate() default "";

    /**
     * List of components that build the key basing on the properties of the source object, e.g. {@code { "id", "name" }}
     * Components will be joined using the {@link #keySeparator()}
     * @return components
     */
    String[] keyComponents() default {};

    /**
     * Separator used to join the {@link #keyComponents()}
     * @return key separator (default: {@code "-"})
     */
    String keySeparator() default "-";

    /**
     * Name pointing to valid cache reference, e.g. bean managed in Spring context
     * @return cache name
     */
    String cacheName();

}

package pl.allegro.tech.jsoncache.support;

import pl.allegro.tech.jsoncache.CacheableEntity;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.StringJoiner;

public class CacheableEntityBuilder {

    private static final String DEFAULT_TEMPLATE = "";
    private static final String DEFAULT_KEY_SEPARATOR = "-";

    private String keyTemplate;
    private String[] keyComponents;
    private String keySeparator;
    private String cacheName;

    public CacheableEntityBuilder withKeyTemplate(String keyTemplate) {
        this.keyTemplate = keyTemplate;
        return this;
    }

    public CacheableEntityBuilder withKeyComponent(String keyComponent) {
        this.keyComponents = new String[]{keyComponent};
        return this;
    }

    public CacheableEntityBuilder withKeyComponents(String... keyComponents) {
        this.keyComponents = Arrays.copyOf(keyComponents, keyComponents.length);
        return this;
    }

    public CacheableEntityBuilder withKeySeparator(String keySeparator) {
        this.keySeparator = keySeparator;
        return this;
    }

    public CacheableEntityBuilder withCacheName(String cacheName) {
        this.cacheName = cacheName;
        return this;
    }

    public CacheableEntity build() {
        return new CacheableEntity() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return CacheableEntity.class;
            }

            @Override
            public String keyTemplate() {
                return keyTemplate == null ? DEFAULT_TEMPLATE : keyTemplate;
            }

            @Override
            public String[] keyComponents() {
                return keyComponents == null ? new String[0] : keyComponents;
            }

            @Override
            public String keySeparator() {
                return keySeparator == null ? DEFAULT_KEY_SEPARATOR : keySeparator;
            }

            @Override
            public String cacheName() {
                return cacheName;
            }

            @Override
            public String toString() {
                return new StringJoiner(", ", "@" + annotationType().getName() + "{", "}")
                        .add("keyTemplate=\"" + keyTemplate() + "\"")
                        .add("keyComponents=" + Arrays.toString(keyComponents()))
                        .add("keySeparator=\"" + keySeparator() + "\"")
                        .add("cacheName=\"" + cacheName() + "\"")
                        .toString();
            }

        };
    }

}

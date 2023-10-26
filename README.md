# json-cache

A set of libraries focused on extracting abstraction layer for caching.

## json-cache-jackson

A library that allows for customization of POJO deserializers in `ObjectMapper` by enhancing them
with caching capabilities.
Consider the following example:

```java
public class CategoryParameter {

    private final int id;
    private final String value;

    public CategoryParameter(
            @JsonProperty("id") int id,
            @JsonProperty("value") String value
    ) {
        this.id = id;
        this.value = value;
    }

    // getters and setters
}
```

and the following JSON payload:

```json5
{
  categories: [
    {
      name: "cat 1",
      parameters: [
        {
          id: 10,
          value: "sanki"
        },
        {
          id: 111,
          value: "Nowy"
        },
        // ...
        {
          id: 123,
          value: "wartość"
        }
      ]
    },
    {
      name: "cat 2",
      parameters: [
        {
          id: 111,
          value: "Nowy"
        },
        {
          id: 555,
          value: "tak"
        },
        {
          id: 123,
          value: "wartość"
        }
      ]
    }
    // ...
  ]
}
```

Normally Jackson will spawn multiple instances of `CategoryParameter` even if such objects contain the same properties.

This library aims to reduce amount of consumed memory and computing time by caching and de-duplicating object instances
(object pooling).

### How to use

Follow those simple steps:

- add dependency for the module:

```groovy
dependencies {
    implementation group: 'pl.allegro.tech', name: 'json-cache-jackson', version: '...'
    // above module doesn't supply dependent libraries to avoid dependency pollution, so make sure you include them yourself
    implementation group: 'com.fasterxml.jackson.core', name: 'jackson-databind', version: '...'
}
```

- supply a proper _annotation_:

```java
@CacheableEntity(keyComponent = "id", cacheName = "categoryParametersCache")
public class CategoryParameter {
    //...
}
```

- implement cache resolver

```java
public class MyCacheResolver implements CacheResolver {

    private final Map<String, EntityCache<?, ?>> cacheMap = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> EntityCache<K, V> resolveCache(String cacheName) {
        return (EntityCache<K, V>) cacheMap.computeIfAbsent(cacheName, key -> new MapBasedEntityCache<>());
    }

}
```

- configure Jackson's `ObjectMapper`

```java
CacheResolver cacheResolver = new MyCacheResolver();
CacheKeyBuilderFactory keyBuilderFactory = new CacheKeyBuilderFactory(
        List.of(new JsonComponentExtractingStrategy())
);
Module cachedDeserializationModule = new CachedDeserializationModule(
        new CacheApplyingDeserializerModifier(keyBuilderFactory, cacheResolver)
);
ObjectMapper mapper = new ObjectMapper(); // or use existing
mapper.registerModule(cachedDeserializationModule);
```

and that's it - you're good to go :)

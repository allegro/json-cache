package pl.allegro.tech.jsoncache.jackson;

import tools.jackson.core.Version;
import tools.jackson.databind.JacksonModule;

/**
 * Jackson module that allows registration of {@link CacheApplyingDeserializerModifier deserializer modifier}.
 */
public class CachedDeserializationModule extends JacksonModule {

    private static final String MODULE_NAME = "cached-deserialization";
    private static final Version VERSION = new Version(1, 0, 0, null, "pl.allegro.tech", "json-cache-jackson");

    private final CacheApplyingDeserializerModifier cacheApplyingDeserializerModifier;

    /**
     * Default constructor.
     *
     * @param cacheApplyingDeserializerModifier {@link CacheApplyingDeserializerModifier deserializer modifier}
     */
    public CachedDeserializationModule(CacheApplyingDeserializerModifier cacheApplyingDeserializerModifier) {
        this.cacheApplyingDeserializerModifier = cacheApplyingDeserializerModifier;
    }

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public Version version() {
        return VERSION;
    }

    @Override
    public void setupModule(JacksonModule.SetupContext context) {
        context.addDeserializerModifier(cacheApplyingDeserializerModifier);
    }

}

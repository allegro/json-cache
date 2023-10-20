package pl.allegro.tech.jsoncache.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;

public class CachedDeserializationModule extends Module {

    private static final String MODULE_NAME = "cached-deserialization";
    private static final Version VERSION = new Version(1, 0, 0, null, "pl.allegro.tech", "json-cache-jackson");

    private final CacheApplyingDeserializerModifier cacheApplyingDeserializerModifier;

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
    public void setupModule(SetupContext context) {
        context.addBeanDeserializerModifier(cacheApplyingDeserializerModifier);
    }

}

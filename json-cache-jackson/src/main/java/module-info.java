/**
 * Jackson integration module.
 */
module json.cache.jackson {
    requires transitive json.cache.core;
    requires transitive com.fasterxml.jackson.databind;

    exports pl.allegro.tech.jsoncache.jackson;
    exports pl.allegro.tech.jsoncache.jackson.keybuilder.strategy;
}

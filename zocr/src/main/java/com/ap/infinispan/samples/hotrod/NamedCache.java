package com.ap.infinispan.samples.hotrod;

import static com.ap.common.util.IOUtils.findProperty;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

class NamedCache<K, V> {

    private static final String HOT_ROD_HOST = "hotrod.host";
    private static final String HOT_ROD_PORT = "hotrod.port";
    private final RemoteCacheManager cacheManager;
    final RemoteCache<K, V> cache;

    public NamedCache() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addServer()
                .host(findProperty(HOT_ROD_HOST))
                .port(Integer.parseInt(findProperty(HOT_ROD_PORT))
                );
        cacheManager = new RemoteCacheManager(builder.build());
        cache = cacheManager.<K, V>getCache("namedCache");
    }

    public void clearstop() {
        cache.clear();
        cacheManager.stop();
    }

    public void stop() {
        cacheManager.stop();
    }
}

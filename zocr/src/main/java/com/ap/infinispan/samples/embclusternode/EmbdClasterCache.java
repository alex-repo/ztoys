package com.ap.infinispan.samples.embclusternode;

import java.io.IOException;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.remoting.transport.Transport;

class EmbdClasterCache {
    
     boolean isMaster;
     String cfgFile;


    
        Cache<String, String> startCache() throws IOException {
        CacheBuilder cb = new CacheBuilder(cfgFile);
        EmbeddedCacheManager cacheManager = cb.getCacheManager();
        Configuration dcc = cacheManager.getDefaultCacheConfiguration();

        cacheManager.defineConfiguration("wordcount", new ConfigurationBuilder().read(dcc)
                .clustering().l1().disable().clustering().cacheMode(CacheMode.DIST_SYNC).hash()
                .numOwners(1).build());
        Cache<String, String> cache = cacheManager.getCache();

        Transport transport = cache.getAdvancedCache().getRpcManager().getTransport();
        if (isMaster) {
            System.out.printf("Node %s joined as master. View is %s.%n", transport.getAddress(), transport.getMembers());
        } else {
            System.out.printf("Node %s joined as slave. View is %s.%n", transport.getAddress(), transport.getMembers());
        }

        return cache;
    }


    
}

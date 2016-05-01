package com.ap.infinispan.samples.embclusternode;

import org.infinispan.commons.util.FileLookup;
import org.infinispan.commons.util.FileLookupFactory;
import org.infinispan.commons.util.Util;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import java.io.IOException;
import java.io.InputStream;

class CacheBuilder {

   private EmbeddedCacheManager cacheManager;

   public CacheBuilder(String ispnConfigFile) throws IOException {
      cacheManager = new DefaultCacheManager(findConfigFile(ispnConfigFile));
   }

   public EmbeddedCacheManager getCacheManager() {
      return this.cacheManager;
   }

   private String findConfigFile(String configFile) {
      FileLookup fl = FileLookupFactory.newInstance();
      if (configFile != null) {
         InputStream inputStream = fl.lookupFile(configFile, Thread.currentThread().getContextClassLoader());
         try {
            if (inputStream != null)
               return configFile;
         } finally {
            Util.close(inputStream);
         }
      }

      return null;
   }
}
package com.ap.infinispan.samples.embclusternode;

import com.ap.common.util.CommonUtils;
import com.ap.common.util.IOUtils;
import com.ap.infinispan.samples.LoggingListener;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class EmCNode {

//   private static final BasicLogger log = Logger.getLogger(EmCNode.class);
    private static final String CACHENAME = "emcnode.cachename";
    private static final String NODENAME = "emcnode.nodename";
    private static final String MASTERNODE = "emcnode.masternode";
    private static final String USEXMLCONFIG = "emcnode.xmlconfig";

    private final boolean useXmlConfig;
    private final String cacheName;
    private final String nodeName;
    private volatile boolean stop = false;

    private EmCNode(boolean useXmlConfig, String cacheName, String nodeName) {
        this.useXmlConfig = useXmlConfig;
        this.cacheName = cacheName;
        this.nodeName = nodeName;
    }

    public static void main(String[] args) throws Exception {
        boolean masterNode;
        boolean useXmlConfig;
        String cacheName;
        String nodeName;

        useXmlConfig = Boolean.valueOf(IOUtils.findProperty(USEXMLCONFIG));
        masterNode = Boolean.valueOf(IOUtils.findProperty(MASTERNODE));
        cacheName = IOUtils.findProperty(CACHENAME);
        nodeName = IOUtils.findProperty(NODENAME);
        new EmCNode(useXmlConfig, cacheName, nodeName).run();
    }

    private void run() throws IOException, InterruptedException {
        EmbeddedCacheManager cacheManager = createCacheManager();
        final Cache<String, String> cache = cacheManager.getCache(cacheName);
        System.out.printf("Cache %s started on %s, cache members are now %s\n", cacheName, cacheManager.getAddress(),
                cache.getAdvancedCache().getRpcManager().getMembers());

        cache.addListener(new LoggingListener());

        printCacheContents(cache);

        Thread putThread = new Thread() {
            @Override
            public void run() {
                int counter = 0;
                while (!stop) {
                    try {
                        cache.put("key-" + counter, "" + cache.getAdvancedCache().getRpcManager().getAddress() + "-" + counter);
                    } catch (Exception e) {
                        CommonUtils.getLogger(getClass()).warn(e);
                    }
                    counter++;

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        };
        putThread.start();

        System.out.println("Press Enter to print the cache contents, Ctrl+D/Ctrl+Z to stop.");
        while (System.in.read() > 0) {
            printCacheContents(cache);
        }

        stop = true;
        putThread.join();
        cacheManager.stop();
        System.exit(0);
    }

    private void printCacheContents(Cache<String, String> cache) {
        System.out.printf("Cache contents on node %s\n", cache.getAdvancedCache().getRpcManager().getAddress());
        ArrayList<Map.Entry<String, String>> entries = new ArrayList<>(cache.entrySet());
        Collections.sort(entries, (o1, o2) -> o1.getKey().compareTo(o2.getKey()));
        for (Map.Entry<String, String> e : entries) {
            System.out.printf("\t%s = %s\n", e.getKey(), e.getValue());
        }
        System.out.println();
    }

    private EmbeddedCacheManager createCacheManager() throws IOException {
        if (useXmlConfig) {
            return createCacheManagerFromXml();
        } else {
            return createCacheManagerProgrammatically();
        }
    }

    private EmbeddedCacheManager createCacheManagerProgrammatically() {
        System.out.println("Starting a cache manager with a programmatic configuration");
        DefaultCacheManager cacheManager = new DefaultCacheManager(
                GlobalConfigurationBuilder.defaultClusteredBuilder()
                .transport().nodeName(nodeName).addProperty("configurationFile", "jgroups.xml")
                .build(),
                new ConfigurationBuilder()
                .clustering()
                .cacheMode(CacheMode.REPL_SYNC)
                .build()
        );
        // The only way to get the "repl" cache to be exactly the same as the default cache is to not define it at all
        cacheManager.defineConfiguration("dist", new ConfigurationBuilder()
                .clustering()
                .cacheMode(CacheMode.DIST_SYNC)
                .hash().numOwners(2)
                .build()
        );
        return cacheManager;
    }

    private EmbeddedCacheManager createCacheManagerFromXml() throws IOException {
        System.out.println("Starting a cache manager with an XML configuration");
        System.setProperty("nodeName", nodeName);
        return new DefaultCacheManager("infinispan.xml");
    }

}

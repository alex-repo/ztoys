package com.ap.infinispan.samples.embclusternode;

import org.infinispan.Cache;
import org.infinispan.commons.util.Util;
import org.infinispan.distexec.DefaultExecutorService;
import org.infinispan.distexec.DistributedExecutorService;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.locks.LockSupport;

import static com.ap.common.util.IOUtils.findProperty;

public class PiApproximationDemo extends EmbdClasterCache {

    private static final String MASTERNODE = "emcnode.masternode";
    private static final String CONFIGFILE = "wcexample.configfile";
    private static final String NUMPOINTS = "desexample.numpoints";

    private static final int DEFAULT_NUM_POINTS = 50000000;

    private final int numPoints;

    // Monte Carlo
    private PiApproximationDemo(boolean isMaster, String cfgFile, int numPoints) {
        this.isMaster = isMaster;
        this.cfgFile = cfgFile;
        this.numPoints = numPoints;
    }

    public static void main(String... args) throws Exception {
        int num = DEFAULT_NUM_POINTS;
        try {
            num = Integer.parseInt(findProperty(NUMPOINTS));
        } catch (Exception e) {
            e.printStackTrace();
        }
        new PiApproximationDemo(Boolean.valueOf(findProperty(MASTERNODE)), findProperty(CONFIGFILE), num).run();
    }

    private void run() throws Exception {
        Cache<String, String> cache = startCache();
        try {
            if (isMaster) {
                int numServers = cache.getCacheManager().getMembers().size();
                int numberPerWorker = numPoints / numServers;
                DistributedExecutorService des = new DefaultExecutorService(cache);
                long start = System.currentTimeMillis();
                List<Future<Integer>> results = des.submitEverywhere(new CircleTest(numberPerWorker));
                int insideCircleCount = 0;
                for (Future<Integer> f : results) {
                    insideCircleCount += f.get();
                }
                double appxPi = 4.0 * insideCircleCount / numPoints;
                System.out.printf("Pi approximation is %s, computed in %s using %s nodes.%n"
                        , appxPi
                        , Util.prettyPrintTime(System.currentTimeMillis() - start)
                        , numServers);
            } else {
                System.out.println("Slave node waiting for Map/Reduce tasks.  Ctrl-C to exit.");
                LockSupport.park();
            }
        } finally {
            cache.getCacheManager().stop();
            System.exit(0);
        }
    }

    private static class CircleTest implements Callable<Integer>, Serializable {
        private static final long serialVersionUID = 3496135215525904755L;

        private final int loopCount;

        public CircleTest(int loopCount) {
            this.loopCount = loopCount;
        }

        @Override
        public Integer call() throws Exception {
            int insideCircleCount = 0;
            for (int i = 0; i < loopCount; i++) {
                double x = Math.random();
                double y = Math.random();
                if (insideCircle(x, y)) {
                    insideCircleCount++;
                }
            }
            return insideCircleCount;
        }

        private boolean insideCircle(double x, double y) {
            return (Math.pow(x - 0.5, 2) + Math.pow(y - 0.5, 2)) <= Math.pow(0.5, 2);
        }
    }
}

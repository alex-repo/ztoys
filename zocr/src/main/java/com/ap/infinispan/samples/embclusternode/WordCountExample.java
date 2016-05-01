package com.ap.infinispan.samples.embclusternode;

import com.ap.common.util.CommonUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.infinispan.Cache;
import org.infinispan.commons.util.Util;
import org.infinispan.distexec.mapreduce.Collector;
import org.infinispan.distexec.mapreduce.MapReduceTask;
import org.infinispan.distexec.mapreduce.Mapper;
import org.infinispan.distexec.mapreduce.Reducer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.locks.LockSupport;

import static com.ap.common.util.IOUtils.findProperty;
import static com.ap.common.util.IOUtils.listFilesForFolderAsFile;

public class WordCountExample extends EmbdClasterCache {

    private static final String MASTERNODE = "emcnode.masternode";
    private static final String CONFIGFILE = "wcexample.configfile";
    private static final String NUMWORD = "wcexample.numword";

    private static final int DEFAULT_NUMWORD = 1;

    private final Integer numPopularWords;

    private WordCountExample(boolean isMaster, String cfgFile, int numPopularWords) {
        this.isMaster = isMaster;
        this.cfgFile = cfgFile;
        this.numPopularWords = numPopularWords;
    }

    public static void main(String[] args) throws Exception {
        int num = DEFAULT_NUMWORD;
        try {
            num = Integer.parseInt(findProperty(NUMWORD));
        } catch (Exception e) {
            e.printStackTrace();
        }
        new WordCountExample(
                Boolean.valueOf(findProperty(MASTERNODE)),
                findProperty(CONFIGFILE),
                num)
                .run();
    }

    private static void loadData(Cache cache) throws IOException {
        List<File> files = new ArrayList<>();
        listFilesForFolderAsFile(files, ".pdf");
        if (!files.isEmpty()) {
            for (File file : files) {
                String name = file.getName();
                if (!cache.containsKey(name)) {
                    try (PDDocument inputPDF = PDDocument.load(file)) {
                        PDFTextStripper textStripper = new PDFTextStripper();
                        String s = textStripper.getText(inputPDF);
                        cache.put(name, s);
                    }
                }
            }
            CommonUtils.getLogger(WordCountExample.class).info("OK");
        }
    }

    private void run() throws IOException {
        Cache<String, String> cache = startCache();
        loadData(cache);
        try {
            if (isMaster) {
                long start = System.currentTimeMillis();
                MapReduceTask<String, String, String, Integer> mapReduceTask = new MapReduceTask<>(cache);
                List<Map.Entry<String, Integer>> topList
                        = mapReduceTask
                        .mappedWith(new WordCountMapper())
                        .reducedWith(new WordCountReducer())
                        .execute(new WordCountCollator(numPopularWords));
                System.out.printf(" ---- RESULTS: Top %s words ", numPopularWords);
                int z = 0;
                for (Map.Entry<String, Integer> e : topList) {
                    System.out.printf("  %s) %s [ %,d occurences ]%n", ++z, e.getKey(), e.getValue());
                }
                System.out.printf("%nCompleted in %s%n%n", Util.prettyPrintTime(System.currentTimeMillis() - start));
            } else {
                System.out.println("Slave node waiting for Map/Reduce tasks.  Ctrl-C to exit.");
                LockSupport.park();
            }
        } finally {
            cache.getCacheManager().stop();
        }
    }

    static class WordCountMapper implements Mapper<String, String, String, Integer> {

        private static final long serialVersionUID = -5943370243108735560L;

        @Override
        public void map(String key, String value, Collector<String, Integer> c) {
            StringTokenizer tokens = new StringTokenizer(value);
            while (tokens.hasMoreElements()) {
                String s = (String) tokens.nextElement();
                c.emit(s, 1);
            }
        }
    }

    static class WordCountReducer implements Reducer<String, Integer> {

        private static final long serialVersionUID = 1901016598354633256L;

        @Override
        public Integer reduce(String key, Iterator<Integer> iter) {
            int sum = 0;
            while (iter.hasNext()) {
                Integer i = iter.next();
                sum += i;
            }
            return sum;
        }
    }
}

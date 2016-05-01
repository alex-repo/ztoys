package com.ap.infinispan.samples.embclusternode;

import org.infinispan.distexec.mapreduce.Collator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

class WordCountCollator implements Collator<String, Integer, List<Map.Entry<String, Integer>>> {

   private final int kthFrequentWord;

   public WordCountCollator() {
      this.kthFrequentWord = 10;
   }

   public WordCountCollator(int kthFrequentWord) {
      if (kthFrequentWord < 0)
         throw new IllegalArgumentException("kth FrequentWord can not be less than 0");
      this.kthFrequentWord = kthFrequentWord;
   }

   @Override
   public List<Map.Entry<String, Integer>> collate(Map<String, Integer> reducedResults) {      
      // Set Thread Name
      Thread.currentThread().setName(String.format("CollatorThread-%d", Thread.currentThread().getId()));
      
      Set<Map.Entry<String, Integer>> entrySet = reducedResults.entrySet();
      ArrayList<Map.Entry<String, Integer>> l = new ArrayList<>(entrySet);
      Collections.sort(l, (o1, o2) -> o1.getValue() < o2.getValue() ? 1 : o1.getValue() > o2.getValue() ? -1 : 0);

      List<Map.Entry<String, Integer>> results = new LinkedList<>();
      for (int i=0; i < kthFrequentWord && i < l.size(); i++) results.add(l.get(i));

      return results;
   }
}
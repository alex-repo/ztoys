package com.ap.infinispan.samples.embclusternode;

import org.infinispan.distexec.mapreduce.Reducer;

import java.util.Iterator;

class WordCountReducer implements Reducer<String, Integer> {

   @Override
   public Integer reduce(String key, Iterator<Integer> iter) {
      // Set Thread Name
      Thread.currentThread().setName(String.format("ReducerThread-%d", Thread.currentThread().getId()));
      
      int sum = 0;
      while (iter.hasNext()) {
         Integer i = iter.next();
         sum += i;
      }
      return sum;
   }
}

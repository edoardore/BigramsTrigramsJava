package it.unifi;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Consumer implements Runnable {
    private BlockingQueue<Vector<String>> q;
    private ConcurrentHashMap<String, Integer> bigramConcurrentHashMap;
    private ConcurrentHashMap<String, Integer> trigramConcurrentHashMap;

    public Consumer(ConcurrentHashMap<String, Integer> bigramConcurrentHashMap, ConcurrentHashMap<String, Integer> trigramConcurrentHashMap,
                    BlockingQueue<Vector<String>> q) {
        this.bigramConcurrentHashMap = bigramConcurrentHashMap;
        this.trigramConcurrentHashMap = trigramConcurrentHashMap;
        this.q = q;
    }

    @Override
    public void run() {
        Vector<String> text = new Vector<String>();
        try {
            text = q.take();
        } catch (InterruptedException e) {
            e.getStackTrace();
        }
        String bigram = new String();
        String trigram = new String();
        String word = new String();
        for (int i = 0; i < text.size(); i++) {
            word = text.get(i);
            if (word.length() > 1) {
                for (int j = 0; j < word.length() - 1; j++) {
                    bigram = word.substring(j, j + 2);
                    if (!bigram.contains("_")) {
                        bigramConcurrentHashMap.merge(bigram, 1, Integer::sum);
                    }
                }
            }
            if (word.length() > 2) {
                for (int k = 0; k < word.length() - 2; k++) {
                    trigram = word.substring(k, k + 3);
                    if (!trigram.contains("_")) {
                        trigramConcurrentHashMap.merge(trigram, 1, Integer::sum);
                    }
                }
            }
        }
    }
}
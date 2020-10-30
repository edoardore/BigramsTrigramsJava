package it.unifi;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements Runnable {
    private BlockingQueue<Vector<String>> q;
    private ConcurrentHashMap<String, Integer> bigramConcurrentHashMap;
    private ConcurrentHashMap<String, Integer> trigramConcurrentHashMap;
    private AtomicInteger atomicIntegerProducer;
    private AtomicInteger atomicIntegerQueue;

    public Consumer(ConcurrentHashMap<String, Integer> bigramConcurrentHashMap, int nProducer, AtomicInteger atomicIntegerProducer,
                    AtomicInteger atomicIntegerQueue,
                    ConcurrentHashMap<String, Integer> trigramConcurrentHashMap,
                    BlockingQueue<Vector<String>> q) {
        this.bigramConcurrentHashMap = bigramConcurrentHashMap;
        this.atomicIntegerProducer = atomicIntegerProducer;
        this.atomicIntegerQueue = atomicIntegerQueue;
        this.trigramConcurrentHashMap = trigramConcurrentHashMap;
        this.q = q;
    }

    @Override
    public void run() {
        Vector<String> text = new Vector<String>();
        while (true) {
            if (!(atomicIntegerProducer.get() > 0) && atomicIntegerQueue.getAndDecrement() == 0)
                break;
            String bigram = new String();
            String trigram = new String();
            String word = new String();
            try {
                text = q.poll(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
            }
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
}
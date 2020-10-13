package it.unifi;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;


public class Consumer implements Runnable {
    private BlockingQueue<Vector<String>> q;
    private int nThread;
    private ConcurrentHashMap<String, Integer> concurrentHashMap;

    public Consumer(ConcurrentHashMap<String, Integer> concurrentHashMap, int nThread, BlockingQueue<Vector<String>> q) {
        this.concurrentHashMap = concurrentHashMap;
        this.nThread = nThread;
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
        String word = new String();
        for (int i = 0; i < text.size(); i++) {
            word = text.get(i);
            Character c = '"';
            String str = Character.toString(c);
            if (word.length() > 1) {
                for (int j = 0; j < word.length() - 1; j++) {
                    bigram = word.substring(j, j + 2);
                    if (!bigram.contains(str)) {
                        concurrentHashMap.merge(bigram, 1, Integer::sum);
                    }
                }
            }
        }
        System.out.println("id " + nThread + " " + concurrentHashMap);
    }
}
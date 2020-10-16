package it.unifi;

import org.jfree.ui.RefineryUtilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.*;


public class Main {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        BlockingQueue<Vector<String>> q = new LinkedBlockingQueue<>();
        BlockingQueue<Path> fileQueue = new LinkedBlockingQueue<>();
        ConcurrentHashMap<String, Integer> bigramConcurrentHashMap = new ConcurrentHashMap<String, Integer>();
        ConcurrentHashMap<String, Integer> trigramConcurrentHashMap = new ConcurrentHashMap<String, Integer>();
        String dirName = "/Users/edore/IdeaProjects/Bigrams/Gutenberg/txt";
        try {
            Files.list(new File(dirName).toPath())
                    .forEach(path -> {
                        try {
                            fileQueue.put(path);
                        } catch (InterruptedException e) {
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        Path end = Paths.get("endFileQueue");
        try {
            fileQueue.put(end);
        } catch (InterruptedException e) {
        }
        int nProducer = 300;
        int nConsumer = fileQueue.size() - 1;
        ExecutorService producer = Executors.newFixedThreadPool(nProducer);
        for (int i = 0; i < nProducer; i++) {
            producer.submit(new Producer(q, fileQueue));
        }
        ExecutorService consumer = Executors.newFixedThreadPool(nConsumer);
        for (int i = 0; i < nConsumer; i++) {
            consumer.submit(new Consumer(bigramConcurrentHashMap, trigramConcurrentHashMap, q));
        }
        producer.shutdown();
        consumer.shutdown();
        try {
            producer.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
            consumer.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
            long totalTime = System.currentTimeMillis() - startTime;
            TableGenerator.createHtml(bigramConcurrentHashMap, 2);
            TableGenerator.createHtml(trigramConcurrentHashMap, 3);
            int nBigram = bigramConcurrentHashMap.size();
            int nTrigram = trigramConcurrentHashMap.size();
            Map.Entry<String, Integer> maxBigramEntry = null;
            for (Map.Entry<String, Integer> entry : bigramConcurrentHashMap.entrySet()) {
                if (maxBigramEntry == null || entry.getValue().compareTo(maxBigramEntry.getValue()) > 0) {
                    maxBigramEntry = entry;
                }
            }
            Map.Entry<String, Integer> maxTrigramEntry = null;
            for (Map.Entry<String, Integer> entry : trigramConcurrentHashMap.entrySet()) {
                if (maxTrigramEntry == null || entry.getValue().compareTo(maxTrigramEntry.getValue()) > 0) {
                    maxTrigramEntry = entry;
                }
            }
            PlotGenerator chart = new PlotGenerator("Execution recap", "Some results of execution",
                    maxBigramEntry.getKey(), maxBigramEntry.getValue(), maxTrigramEntry.getKey(),
                    maxTrigramEntry.getValue(), nBigram, nTrigram);
            chart.pack();
            RefineryUtilities.centerFrameOnScreen(chart);
            chart.setVisible(true);
            System.out.println("Bigrammi=" + bigramConcurrentHashMap);
            System.out.println("Trigrammi=" + trigramConcurrentHashMap);
            System.out.println("Tempo Totale di esecuzione programma parallelo: " +
                    TimeUnit.MILLISECONDS.toMinutes(totalTime) + "min " +
                    (TimeUnit.MILLISECONDS.toSeconds(totalTime) - 60 * TimeUnit.MILLISECONDS.toMinutes(totalTime)) + "s");
        } catch (InterruptedException e) {
            e.getStackTrace();
        }
    }
}
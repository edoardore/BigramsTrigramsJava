package it.unifi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;
import java.util.concurrent.*;


public class Main {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        BlockingQueue<Vector<String>> q = new LinkedBlockingQueue<>();
        BlockingQueue<Path> fileQueue = new LinkedBlockingQueue<>();
        ConcurrentHashMap<String, Integer> concurrentHashMap = new ConcurrentHashMap<String, Integer>();
        String dirName = "/Users/edore/IdeaProjects/Bigrams/Italiano";
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
        dirName = "/Users/edore/IdeaProjects/Bigrams/English";
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
        int nProducer = 48; //Runtime.getRuntime().availableProcessors();
        int nConsumer = fileQueue.size() - 1;
        ExecutorService producer = Executors.newFixedThreadPool(nProducer);
        for (int i = 0; i < nProducer; i++) {
            producer.submit(new Producer(q, fileQueue));
        }
        ExecutorService consumer = Executors.newFixedThreadPool(nConsumer);
        for (int i = 0; i < nConsumer; i++) {
            consumer.submit(new Consumer(concurrentHashMap, i, q));
        }
        producer.shutdown();
        consumer.shutdown();
        try {
            producer.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            consumer.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            long totalTime = System.currentTimeMillis() - startTime;
            System.out.println("Tempo Totale di esecuzione programma parallelo: " +
                    TimeUnit.MILLISECONDS.toMinutes(totalTime) + "min " +
                    (TimeUnit.MILLISECONDS.toSeconds(totalTime) - 60 * TimeUnit.MILLISECONDS.toMinutes(totalTime)) + "s");
            System.out.println(concurrentHashMap);
            TableGenerator.createHtml(concurrentHashMap);
        } catch (InterruptedException e) {
            e.getStackTrace();
        }
    }
}
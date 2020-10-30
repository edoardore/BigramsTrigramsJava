package it.unifi;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable {
    private BlockingQueue<Vector<String>> q = null;
    private BlockingQueue<Path> fileQueue;
    private AtomicInteger atomicIntegerProducer;
    private AtomicInteger atomicIntegerQueue;

    public Producer(BlockingQueue<Vector<String>> q, AtomicInteger atomicIntegerProducer,AtomicInteger atomicIntegerQueue, BlockingQueue<Path> fileQueue) {
        this.q = q;
        this.atomicIntegerProducer = atomicIntegerProducer;
        this.atomicIntegerQueue=atomicIntegerQueue;
        this.fileQueue = fileQueue;
    }

    @Override
    public void run() {
        String path = new String();
        try {
            while (!(path = fileQueue.take().toString()).equals("endFileQueue")) {
                String readFile = new String();
                File file = new File(path);
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String str = new String();
                    try {
                        while ((str = br.readLine()) != null)
                            readFile += str + ' ';
                    } catch (IOException e) {
                        e.getStackTrace();
                    }
                } catch (FileNotFoundException e) {
                    e.getStackTrace();
                }
                Vector<String> producerUnit = new Vector<String>();
                String[] tokens = readFile.split("\\W");
                for (String token : tokens) {
                    if (!token.equals("") && !token.matches(".*\\d.*"))
                        producerUnit.add(token.toLowerCase());
                }
                try {
                    q.put(producerUnit);
                    atomicIntegerQueue.addAndGet(1);
                } catch (InterruptedException e) {
                    e.getStackTrace();
                }
            }
            Path end = Paths.get("endFileQueue");
            atomicIntegerProducer.decrementAndGet();
            try {
                fileQueue.put(end);
            } catch (InterruptedException e) {
                e.getStackTrace();
            }
        } catch (InterruptedException e) {
            e.getStackTrace();
        }
    }
}
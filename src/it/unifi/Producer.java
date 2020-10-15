package it.unifi;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {
    private BlockingQueue<Vector<String>> q = null;
    private BlockingQueue<Path> fileQueue;

    public Producer(BlockingQueue<Vector<String>> q, BlockingQueue<Path> fileQueue) {
        this.q = q;
        this.fileQueue = fileQueue;
    }

    @Override
    public void run() {
        String readFile = new String();
        String path = new String();
        try {
            while (!(path = fileQueue.take().toString()).equals("endFileQueue")) {
                File file = new File(path);
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String str;
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
                    if (!token.equals("")&&!token.matches(".*\\d.*"))
                        producerUnit.add(token.toLowerCase());
                }
                try {
                    q.put(producerUnit);
                } catch (InterruptedException e) {
                    e.getStackTrace();
                }
            }
        } catch (InterruptedException e) {
            e.getStackTrace();
        }
        Path end = Paths.get("endFileQueue");
        try {
            fileQueue.put(end);
        } catch (InterruptedException e) {
            e.getStackTrace();
        }
    }
}
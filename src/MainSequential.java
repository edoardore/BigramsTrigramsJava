import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MainSequential {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        ArrayList<Path> fileQueue = new ArrayList<>();
        ArrayList<Vector<String>> q = new ArrayList<>();
        HashMap<String, Integer> bigramHashMap = new HashMap<>();
        HashMap<String, Integer> trigramHashMap = new HashMap<>();
        String dirName = "/Users/edore/IdeaProjects/Bigrams/English";
        try {
            Files.list(new File(dirName).toPath())
                    .forEach(path -> {
                        fileQueue.add(path);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        long producerTime = 0;
        long consumerTime = 0;
        int size = fileQueue.size();
        for (int s = 0; s < size; s++) {
            long startProducerTime = System.currentTimeMillis();
            String readFile = new String();
            String path = fileQueue.remove(0).toString();
            File file = new File(path);
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String str;
                try {
                    while ((str = br.readLine()) != null)
                        readFile += str + ' ';
                } catch (IOException e) {
                }
            } catch (FileNotFoundException e) {
            }
            Vector<String> producerUnit = new Vector<String>();
            StringTokenizer tokenizer = new StringTokenizer(readFile, " $#%^&*+=~`,.:;<>_()[]{}!�-?@'/|1234567890");
            while (tokenizer.hasMoreTokens()) {
                producerUnit.add(tokenizer.nextToken().toLowerCase());
            }
            producerTime += System.currentTimeMillis() - startProducerTime;
            long startConsumerTime = System.currentTimeMillis();
            Vector<String> text = new Vector<String>();
            text = producerUnit;
            String bigram = new String();
            String trigram = new String();
            String word = new String();
            Character c = '"';
            String str = Character.toString(c);
            for (int i = 0; i < text.size(); i++) {
                word = text.get(i);
                if (word.length() > 1) {
                    for (int j = 0; j < word.length() - 1; j++) {
                        bigram = word.substring(j, j + 2);
                        if (!bigram.contains(str)) {
                            bigramHashMap.merge(bigram, 1, Integer::sum);
                        }
                    }
                }
                if (word.length() > 2) {
                    for (int k = 0; k < word.length() - 2; k++) {
                        trigram = word.substring(k, k + 3);
                        if (!trigram.contains(str)) {
                            trigramHashMap.merge(trigram, 1, Integer::sum);
                        }
                    }
                }
            }
            consumerTime += System.currentTimeMillis() - startConsumerTime;
        }
        long startConsumerTime = System.currentTimeMillis();
        System.out.println("Bigrammi: " + bigramHashMap);
        System.out.println("Trigrammi " + trigramHashMap);
        TableGenerator.createHtml(bigramHashMap, 2);
        TableGenerator.createHtml(trigramHashMap, 3);
        consumerTime += System.currentTimeMillis() - startConsumerTime;
        long totalTime = System.currentTimeMillis() - startTime;
        System.out.println("Tempo di esecuzione processo di lettura delle parole: " +
                TimeUnit.MILLISECONDS.toMinutes(producerTime) + "min " +
                (TimeUnit.MILLISECONDS.toSeconds(producerTime) - 60 * TimeUnit.MILLISECONDS.toMinutes(producerTime)) + "s");
        System.out.println("Tempo di conteggio dei bigrammi e trigrammi: " +
                TimeUnit.MILLISECONDS.toMinutes(consumerTime) + "min " +
                (TimeUnit.MILLISECONDS.toSeconds(consumerTime) - 60 * TimeUnit.MILLISECONDS.toMinutes(consumerTime)) + "s");
        System.out.println("Tempo Totale di esecuzione programma sequenziale: " +
                TimeUnit.MILLISECONDS.toMinutes(totalTime) + "min " +
                (TimeUnit.MILLISECONDS.toSeconds(totalTime) - 60 * TimeUnit.MILLISECONDS.toMinutes(totalTime)) + "s");
    }
}
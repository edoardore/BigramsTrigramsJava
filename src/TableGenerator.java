import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TableGenerator {
    public static void createHtml(HashMap<String, Integer> hashMap, int i) {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "    <title>Bigrams</title>\n" +
                "    <style>\n" +
                "        table {\n" +
                "            border-spacing: 0;\n" +
                "            width: 100%;\n" +
                "            border: 1px solid #ddd;\n" +
                "        }\n" +
                "\n" +
                "        th, td {\n" +
                "            text-align: left;\n" +
                "            padding: 16px;\n" +
                "        }\n" +
                "\n" +
                "        tr:nth-child(even) {\n" +
                "            background-color: #f2f2f2\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<table id=\"myTable\">");
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            htmlBuilder.append(String.format("<tr><td>%s</td><td>%d</td></tr>",
                    entry.getKey(), entry.getValue()));
        }
        htmlBuilder.append("</table>\n" +
                "</body>\n" +
                "</html>");
        String html = htmlBuilder.toString();
        if (i == 2) {
            File file = new File("Bigrams.html");
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(html);
                writer.close();
            } catch (IOException e) {
                e.getStackTrace();
            }
        }
        if (i == 3) {
            File file = new File("Trigrams.html");
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(html);
                writer.close();
            } catch (IOException e) {
                e.getStackTrace();
            }
        }
    }
}

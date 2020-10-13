package it.unifi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TableGenerator {
    public static void createHtml(ConcurrentHashMap<String, Integer> hashMap) {
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
                "\n" +
                "<p><button onclick=\"sortTable()\">Sort alphabetically</button></p>\n" +
                "\n" +
                "<p><button onclick=\"sortTabByInt()\">Sort by occurrence</button></p>\n" +
                "<table id=\"myTable\">");
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            htmlBuilder.append(String.format("<tr><td>%s</td><td>%d</td></tr>",
                    entry.getKey(), entry.getValue()));
        }
        htmlBuilder.append("</table>\n" +
                "\n" +
                "<script>\n" +
                "function sortTable() {\n" +
                "        var table, rows, switching, i, x, y, shouldSwitch;\n" +
                "        table = document.getElementById(\"myTable\");\n" +
                "        switching = true;\n" +
                "        while (switching) {\n" +
                "            switching = false;\n" +
                "            rows = table.rows;\n" +
                "            for (i = 0; i < (rows.length - 1); i++) {\n" +
                "                shouldSwitch = false;\n" +
                "                x = rows[i].getElementsByTagName(\"TD\")[0];\n" +
                "                y = rows[i + 1].getElementsByTagName(\"TD\")[0];\n" +
                "                if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {\n" +
                "                    shouldSwitch = true;\n" +
                "                    break;\n" +
                "                }\n" +
                "            }\n" +
                "            if (shouldSwitch) {\n" +
                "                rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);\n" +
                "                switching = true;\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    function sortTabByInt() {\n" +
                "        var table, rows, switching, i, x, y, shouldSwitch;\n" +
                "        table = document.getElementById(\"myTable\");\n" +
                "        switching = true;\n" +
                "        while (switching) {\n" +
                "            switching = false;\n" +
                "            rows = table.rows;\n" +
                "            for (i = 0; i < (rows.length - 1); i++) {\n" +
                "                shouldSwitch = false;\n" +
                "                x = rows[i].getElementsByTagName(\"TD\")[1];\n" +
                "                y = rows[i + 1].getElementsByTagName(\"TD\")[1];\n" +
                "                if (parseInt(x.innerHTML) < parseInt(y.innerHTML)) {\n" +
                "                    shouldSwitch = true;\n" +
                "                    break;\n" +
                "                }\n" +
                "            }\n" +
                "            if (shouldSwitch) {\n" +
                "                rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);\n" +
                "                switching = true;\n" +
                "            }\n" +
                "        }\n" +
                "    }" +
                "</script>\n" +
                "\n" +
                "</body>\n" +
                "</html>");
        String html = htmlBuilder.toString();
        File file = new File("Bigrams.html");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(html);
            writer.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }
}

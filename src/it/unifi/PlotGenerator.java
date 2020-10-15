package it.unifi;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

public class PlotGenerator extends ApplicationFrame {

    public PlotGenerator(String applicationTitle, String chartTitle, String maxBigram, int maxOccurrenceBigram,
                         String maxTrigram, int maxOccurenceTrigram, int nBi, int nTri) {
        super(applicationTitle);
        JFreeChart barChart = ChartFactory.createBarChart(
                chartTitle,
                "",
                "",
                createDataset(maxBigram, maxOccurrenceBigram, maxTrigram, maxOccurenceTrigram, nBi, nTri),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(860, 600));
        setContentPane(chartPanel);
    }

    private CategoryDataset createDataset(String maxBigram, int maxOccurrenceBigram, String maxTrigram,
                                          int maxOccurrenceTrigram, int nBigrams, int nTrigrams) {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(maxOccurrenceBigram, "Bigram piu frequente: " + maxBigram, "");
        dataset.addValue(maxOccurrenceTrigram, "Trigram piu frequente: " + maxTrigram, "");
        dataset.addValue(nBigrams, "Numero totale bigrammi distinti", "");
        dataset.addValue(nTrigrams, "Numero totale trigrammi distinti", "");
        return dataset;
    }

}
package isilimageprocessing.dialogues.Histogramme;

import cimage.CImageNG;
import cimage.exceptions.CImageNGException;
import cimage.observers.JLabelBeanCImage;
import imageprocessing.Histogramme.Histogramme;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.function.Function2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class JDialogAfficheRehaussement extends JDialog {
    protected Histogramme histogramme = new Histogramme();
    protected Map<String, JSpinner> parameters = new HashMap<>();

    protected CImageNG cImageBase;
    protected CImageNG cImageTransfo;

    private JLabelBeanCImage imageBase;
    private JLabelBeanCImage imageTransfo;
    private ChartPanel courbeTonalePanel;
    private ChartPanel histogrammeBase;
    private ChartPanel histogrammeTransfo;


    private final JScrollPane jScrollPaneBare = new JScrollPane();
    private final JScrollPane jScrollPaneTransfo = new JScrollPane();
    private final JScrollPane jScrollPaneCourbe = new JScrollPane();
    private final JScrollPane jScrollPaneHistoBase = new JScrollPane();
    private final JScrollPane jScrollPaneHistoTransfo = new JScrollPane();

    public JDialogAfficheRehaussement(Frame parent, boolean modal, int[][] matrice, String titre) {
        setTitle(titre);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        try {
            cImageBase = new CImageNG(matrice);
            cImageTransfo = new CImageNG(matrice.length, matrice[0].length, 255);
        } catch (CImageNGException ex) {
            System.err.println("Erreur CImageNG: " + ex.getMessage());
            return;
        }

        imageBase = new JLabelBeanCImage(cImageBase);
        imageTransfo = new JLabelBeanCImage(cImageTransfo);
        jScrollPaneBare.setViewportView(imageBase);
        jScrollPaneTransfo.setViewportView(imageTransfo);

        JPanel mainFrame = new JPanel();

        var button = new JButton("Afficher image");
        button.addActionListener(e -> {
            try {
                displayImage();
                jScrollPaneTransfo.setViewportView(imageTransfo);
                jScrollPaneCourbe.setViewportView(courbeTonalePanel);
                jScrollPaneHistoBase.setViewportView(histogrammeBase);
                jScrollPaneHistoTransfo.setViewportView(histogrammeTransfo);
            } catch (CImageNGException ex) {
                throw new RuntimeException(ex);
            }
        });

        setupParameters();

        JPanel parametersFrame = new JPanel();
        parametersFrame.setLayout(new BoxLayout(parametersFrame, BoxLayout.Y_AXIS));

        parameters.forEach((parameterName, spinner) -> {
            parametersFrame.add(new JLabel(String.format("%s: ", parameterName)));
            parametersFrame.add(spinner);
        });

        parametersFrame.add(button);

        mainFrame.setLayout(new GridLayout(2, 3, 8, 8));
        mainFrame.add(parametersFrame);
        mainFrame.add(jScrollPaneBare);
        mainFrame.add(jScrollPaneTransfo);
        mainFrame.add(jScrollPaneCourbe);
        mainFrame.add(jScrollPaneHistoBase);
        mainFrame.add(jScrollPaneHistoTransfo);

        getContentPane().add(mainFrame);
        pack();
        setVisible(true);
    }

    protected abstract void setupParameters();
    protected void displayImage() throws CImageNGException {
        var courbeTonale = getTonalCurve();

        // Affichage de la courbe tonale
        displayTonalCurve(courbeTonale);

        // Affichage de l'histogramme de base
        displayBaseHistogram();

        // Affichage de l'image modifiée
        var transfo = histogramme.rehaussement(cImageBase.getMatrice(), courbeTonale);
        cImageTransfo.setMatrice(transfo);

        // Affichage de l'histogramme de l'image modifiée
        displayTransformedHistogram();
    }

    protected abstract Function<Integer, Integer> getTonalCurve() throws CImageNGException;

    protected void displayTonalCurve(Function<Integer, Integer> courbeTonale) {
        Function2D courbeTonale2d = value -> courbeTonale.apply((int) value);
        XYDataset courbeTonaleDataset = DatasetUtilities.sampleFunction2D(courbeTonale2d, 0, 255.0, 255, "f(x)");
        JFreeChart courbeTonaleChart = ChartFactory.createXYLineChart(
                "Courbe Tonale",
                "Intensite d'entree",
                "Intensite de sortie",
                courbeTonaleDataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );

        courbeTonalePanel = new ChartPanel(courbeTonaleChart);
    }

    protected void displayBaseHistogram() throws CImageNGException {
        var baseHisto = displayHistogram(cImageBase.getMatrice(), "Histogramme de l'image de base");
        histogrammeBase = new ChartPanel(baseHisto);
    }

    protected void displayTransformedHistogram() throws CImageNGException {
        var transfoHisto = displayHistogram(cImageTransfo.getMatrice(), "Histogramme de l'image transformee");
        histogrammeTransfo = new ChartPanel(transfoHisto);
    }

    private JFreeChart displayHistogram(int[][] image, String title) {
        var integerData = Histogramme.Histogramme256(image);
        var serie = new XYSeries("Histogram");
        for (int i = 0; i < integerData.length; i++) {
            serie.add(i, integerData[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(serie);

        var chart = ChartFactory.createHistogram(
                "Histogramme",
                "Niveaux de gris",
                "Nombre de pixels",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );

        var plot = chart.getXYPlot();
        var axeX = plot.getDomainAxis();
        axeX.setRange(0, 255);
        plot.setDomainAxis(axeX);

        return chart;
    }
}
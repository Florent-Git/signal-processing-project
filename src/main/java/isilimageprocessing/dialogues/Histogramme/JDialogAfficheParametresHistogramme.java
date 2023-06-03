package isilimageprocessing.dialogues.Histogramme;

import cimage.CImageNG;
import cimage.exceptions.CImageNGException;
import cimage.observers.JLabelBeanCImage;
import imageprocessing.Histogramme.Histogramme;

import javax.swing.*;
import java.awt.*;

public class JDialogAfficheParametresHistogramme extends JDialog {
    private final Histogramme histogramme = new Histogramme();
    private final JLabel minimumText;
    private final JLabel maximumText;
    private final JLabel luminanceText;
    private final JLabel contraste1Text;
    private final JLabel contraste2Text;
    private final int M, N;
    private CImageNG imageBare;
    private final JLabelBeanCImage observerBare;
    private final JScrollPane jScrollPaneBare = new JScrollPane();

    public JDialogAfficheParametresHistogramme(Frame parent, boolean modal, int[][] matrice, String titre) {
        //super(parent, modal);
        setTitle(titre);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        M = matrice.length;
        N = matrice[0].length;
        try
        {
            imageBare = new CImageNG(M,N,0);
            imageBare.setMatrice(matrice);
        }
        catch (CImageNGException ex)
        { System.out.println("Erreur CImageNG : " + ex.getMessage()); }

        observerBare = new JLabelBeanCImage(imageBare);
        jScrollPaneBare.setViewportView(observerBare);

        // Créer le sélecteur de nombres entiers
        minimumText = new JLabel();
        maximumText = new JLabel();
        luminanceText = new JLabel();
        contraste1Text = new JLabel();
        contraste2Text = new JLabel();

        // Créer un bouton pour afficher l'image sélectionnée
        JButton showImageButton = new JButton("Afficher les paramètres");
        showImageButton.addActionListener(e -> {
            try {
                displayParameters();
            } catch (CImageNGException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Créer un conteneur principal et ajouter les composants
        JPanel mainFrame = new JPanel();
        mainFrame.setLayout(new BoxLayout(mainFrame,BoxLayout.Y_AXIS));
        mainFrame.add(jScrollPaneBare);
        mainFrame.add(showImageButton);
        mainFrame.add(minimumText);
        mainFrame.add(maximumText);
        mainFrame.add(luminanceText);
        mainFrame.add(contraste1Text);
        mainFrame.add(contraste2Text);

        // Ajouter le conteneur principal à la fenêtre
        getContentPane().add(mainFrame);
        pack();
        setVisible(true);
    }

    private void displayParameters() throws CImageNGException {
        int minimum = histogramme.minimum(imageBare.getMatrice());
        minimumText.setText(String.format("Minimum: %d", minimum));
        int maximum = histogramme.maximum(imageBare.getMatrice());
        maximumText.setText(String.format("Maximum: %d", maximum));
        int luminance = histogramme.luminance(imageBare.getMatrice());
        luminanceText.setText(String.format("Luminance: %d", luminance));
        double contraste1 = histogramme.contraste1(imageBare.getMatrice());
        contraste1Text.setText(String.format("Contraste 1: %.2f", contraste1));
        double contraste2 = histogramme.contraste2(imageBare.getMatrice());
        contraste2Text.setText(String.format("Contraste 2: %.2f", contraste2));
    }
}
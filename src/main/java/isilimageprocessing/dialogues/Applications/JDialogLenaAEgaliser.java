package isilimageprocessing.dialogues.Applications;

import cimage.CImageNG;
import cimage.CImageRGB;
import cimage.exceptions.CImageNGException;
import cimage.exceptions.CImageRGBException;
import cimage.observers.JLabelBeanCImage;
import imageprocessing.Histogramme.Histogramme;
import imageprocessing.Lineaire.FiltrageLineaireGlobal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class JDialogLenaAEgaliser extends JDialog {
    private Histogramme histogramme = new Histogramme();
    private JLabel imageLabel;
    private int M, N;
    private CImageRGB imageBare, imageTransf;
    private JLabelBeanCImage observerBare, observerTransf;
    private JScrollPane jScrollPaneBare = new JScrollPane(), jScrollPaneTransf = new JScrollPane();

    public JDialogLenaAEgaliser(Frame parent, boolean modal, String titre) {
        //super(parent, modal);
        setTitle(titre);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        M = 256;
        N = 256;
        try
        {
            imageBare = new CImageRGB(new File(getClass().getClassLoader().getResource("images_step_5/lenaAEgaliser.jpg").toURI()));
            imageTransf = new CImageRGB(M,N, Color.WHITE);
        }
        catch (CImageRGBException | URISyntaxException ex)
        { System.out.println("Erreur CImageNG : " + ex.getMessage()); } catch (IOException e) {
            throw new RuntimeException(e);
        }

        observerBare = new JLabelBeanCImage(imageBare);
        observerTransf = new JLabelBeanCImage(imageTransf);
        jScrollPaneBare.setViewportView(observerBare);
        jScrollPaneTransf.setViewportView(observerTransf);

        // Créer un bouton pour afficher l'image sélectionnée
        JButton showImageIndependent = new JButton("Traiter l'image (RGB Separement)");
        JButton showImageDependent = new JButton("Traiter l'image (RGB sur base des niveaux de gris");
        showImageIndependent.addActionListener(e -> {
            try {
                displayImageFromRGB();
            } catch (CImageNGException | CImageRGBException ex) {
                throw new RuntimeException(ex);
            }
        });

        showImageDependent.addActionListener(e -> {
            try {
                displayImageFromNG();
            } catch (CImageNGException ex) {
                throw new RuntimeException(ex);
            } catch (CImageRGBException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Créer un conteneur principal et ajouter les composants
        JPanel mainFrame = new JPanel();
        mainFrame.setLayout(new BoxLayout(mainFrame,BoxLayout.Y_AXIS));
        mainFrame.add(jScrollPaneBare);
        mainFrame.add(showImageDependent);
        mainFrame.add(showImageIndependent);
        mainFrame.add(jScrollPaneTransf);

        // Ajouter le conteneur principal à la fenêtre
        getContentPane().add(mainFrame);
        pack();
        setVisible(true);
    }

    private void displayImageFromNG() throws CImageRGBException, CImageNGException {
        var ngMatrix = imageBare.getCImageNG().getMatrice();
        var equalizeNG = histogramme.creerCourbeTonaleEgalisation(imageBare.getCImageNG().getMatrice());
        int[][] red = new int[ngMatrix.length][ngMatrix[0].length],
            green = new int[ngMatrix.length][ngMatrix[0].length],
            blue = new int[ngMatrix.length][ngMatrix[0].length];
        imageBare.getMatricesRGB(red, green, blue);
        red = histogramme.rehaussement(red, equalizeNG);
        green = histogramme.rehaussement(green, equalizeNG);
        blue = histogramme.rehaussement(blue, equalizeNG);

        imageTransf.setMatricesRGB(red, green, blue);
    }

    private void displayImageFromRGB() throws CImageNGException, CImageRGBException {
        var ngMatrix = imageBare.getCImageNG().getMatrice();


        int[][] red = new int[ngMatrix.length][ngMatrix[0].length],
                green = new int[ngMatrix.length][ngMatrix[0].length],
                blue = new int[ngMatrix.length][ngMatrix[0].length];

        imageBare.getMatricesRGB(red, green, blue);

        var courbe_red = histogramme.creerCourbeTonaleEgalisation(red);
        var courbe_green = histogramme.creerCourbeTonaleEgalisation(blue);
        var courbe_blue = histogramme.creerCourbeTonaleEgalisation(green);

        red = histogramme.rehaussement(red, courbe_red);
        green = histogramme.rehaussement(green, courbe_green);
        blue = histogramme.rehaussement(blue, courbe_blue);

        imageTransf.setMatricesRGB(red, green, blue);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JDialogLenaAEgaliser(new JFrame(), true,null).setVisible(true);
            }
        });
    }
}

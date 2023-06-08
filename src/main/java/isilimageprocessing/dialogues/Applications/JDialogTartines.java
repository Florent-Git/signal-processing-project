package isilimageprocessing.dialogues.Applications;

import cimage.CImageNG;
import cimage.CImageRGB;
import cimage.exceptions.CImageNGException;
import cimage.exceptions.CImageRGBException;
import cimage.observers.JLabelBeanCImage;
import imageprocessing.Contours.ContoursLineaire;
import imageprocessing.Contours.ContoursNonLineaire;
import imageprocessing.Histogramme.Histogramme;
import imageprocessing.Lineaire.FiltrageLineaireGlobal;
import imageprocessing.NonLineaire.MorphoComplexe;
import imageprocessing.NonLineaire.MorphoElementaire;
import imageprocessing.Seuillage.Seuillage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.spec.ECField;

public class JDialogTartines extends JDialog {
    private JLabel imageLabel;
    private int M, N;
    private CImageRGB imageBare, imageTransf;
    private JLabelBeanCImage observerBare, observerTransf;
    private JScrollPane jScrollPaneBare = new JScrollPane(), jScrollPaneTransf = new JScrollPane();

    public JDialogTartines(Frame parent, boolean modal, String titre) {
        //super(parent, modal);
        setTitle(titre);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        M = 800;
        N = 600;
        try
        {
            imageBare = new CImageRGB(new File(getClass().getClassLoader().getResource("images_step_5/Tartines.jpg").toURI()));
            imageTransf = new CImageRGB(M,N, 255,255,255);
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
        JButton showImageButton = new JButton("Traiter l'image");
        showImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    displayImage();
                } catch (CImageNGException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Créer un conteneur principal et ajouter les composants
        JPanel mainFrame = new JPanel();
        mainFrame.setLayout(new BoxLayout(mainFrame,BoxLayout.Y_AXIS));
        mainFrame.add(jScrollPaneBare);
        mainFrame.add(showImageButton);
        mainFrame.add(jScrollPaneTransf);

        // Ajouter le conteneur principal à la fenêtre
        getContentPane().add(mainFrame);
        pack();
        setVisible(true);
    }

    private void displayImage() throws CImageNGException{
        try {
            int[][] matriceR = new int[M][N];
            int[][] matriceG = new int[M][N];
            int[][] matriceB = new int[M][N];
            imageBare.getMatricesRGB(matriceR, matriceG, matriceB);

            int[][] contours = new int[M][N];
            Histogramme histogramme = new Histogramme();
            var courbe = histogramme.creerCourbeTonaleEgalisation(matriceG);
            contours = histogramme.rehaussement(matriceG, courbe);
            contours = MorphoComplexe.filtreMedian(contours, 5);
            contours = ContoursNonLineaire.gradientBeucher(contours);
            contours = Seuillage.seuillageSimple(contours, 60);
            contours = MorphoElementaire.dilatation(contours, 3);

            matriceG = colleMatrix(matriceG, contours);

            imageTransf.setMatricesRGB(matriceR, matriceG, matriceB);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void displayImage() throws CImageNGException {
//
//        try {
//            int[][] matriceR = new int[M][N];
//            int[][] matriceG = new int[M][N];
//            int[][] matriceB = new int[M][N];
//            imageBare.getMatricesRGB(matriceR, matriceG, matriceB);
//
////            matriceG = Seuillage.seuillageSimple(matriceG, 200);
//
//            Histogramme histogramme = new Histogramme();
//
//            var courbe = histogramme.creerCourbeTonaleEgalisation(matriceR);
//            int[][] contours = histogramme.rehaussement(matriceR, courbe);
//
//            courbe = histogramme.creerCourbeTonaleEgalisation(contours);
//            contours = histogramme.rehaussement(contours, courbe);
//
//            contours = Seuillage.seuillageSimple(contours, 200);
//            contours = MorphoElementaire.dilatation(contours, 5);
//            contours = MorphoElementaire.erosion(contours, 7);
//
//            contours = ContoursLineaire.laplacien8(contours);
//
//            contours = Seuillage.seuillageSimple(contours, 1);
//            contours = MorphoElementaire.dilatation(contours, 3);
//
//            matriceR = colleMatrix(contours, new int[M][N]);
//            matriceG = colleMatrix(matriceG, contours);
//
//            imageTransf.setMatricesRGB(matriceR, matriceG, matriceB);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JDialogTartines(new JFrame(), true,null).setVisible(true);
            }
        });
    }

    public static int[][] colleMatrix(int[][] matrice, int[][] masque){
        int[][] result = new int[matrice.length][matrice[0].length];
        for (int i = 0; i < matrice.length; i++) {
            for (int j = 0; j < matrice[0].length; j++) {
                if(masque[i][j] > 15)
                    result[i][j] = masque[i][j];
                else
                    result[i][j] = matrice[i][j];
            }
        }
        return  result;
    }

    public static int[][] andMatrix(int[][] matrice, int[][] masque){
        int[][] result = new int[matrice.length][matrice[0].length];
        for (int i = 0; i < matrice.length; i++) {
            for (int j = 0; j < matrice[0].length; j++) {
                result[i][j] = matrice[i][j] & masque[i][j];
            }
        }
        return  result;
    }
}

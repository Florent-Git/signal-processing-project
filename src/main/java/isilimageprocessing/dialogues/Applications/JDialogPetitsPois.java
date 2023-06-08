package isilimageprocessing.dialogues.Applications;

import cimage.CImageNG;
import cimage.CImageRGB;
import cimage.exceptions.CImageNGException;
import cimage.exceptions.CImageRGBException;
import cimage.observers.JLabelBeanCImage;
import imageprocessing.Contours.ContoursNonLineaire;
import imageprocessing.Histogramme.Histogramme;
import imageprocessing.Lineaire.FiltrageLineaireGlobal;
import imageprocessing.NonLineaire.MorphoElementaire;
import imageprocessing.Seuillage.Seuillage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class JDialogPetitsPois extends JDialog {
    private JLabel imageLabel;
    private int M, N;
    private CImageRGB imageBare, imageBleu, imageRouge, imageTraitement;
    private JLabelBeanCImage observerBare, observerBleu, observerRouge;
    private JScrollPane jScrollPaneBare = new JScrollPane(), jScrollPaneBleu = new JScrollPane(), jScrollPaneRouge = new JScrollPane();

    public JDialogPetitsPois(Frame parent, boolean modal, String titre) {
        //super(parent, modal);
        setTitle(titre);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        M = 649;
        N = 485;
        try
        {
            imageBare = new CImageRGB(new File(getClass().getClassLoader().getResource("images_step_5/petitsPois.png").toURI()));
            imageTraitement = new CImageRGB(new File(getClass().getClassLoader().getResource("images_step_5/petitsPois.png").toURI()));
            imageBleu = new CImageRGB(M,N, 255, 255, 255);
            imageRouge = new CImageRGB(M,N, 255, 255, 255);
        }
        catch (CImageRGBException | URISyntaxException ex)
        { System.out.println("Erreur CImageNG : " + ex.getMessage()); } catch (IOException e) {
            throw new RuntimeException(e);
        }

        observerBare = new JLabelBeanCImage(imageBare);
        observerBleu = new JLabelBeanCImage(imageBleu);
        observerRouge = new JLabelBeanCImage(imageRouge);
        jScrollPaneBare.setViewportView(observerBare);
        jScrollPaneBleu.setViewportView(observerBleu);
        jScrollPaneRouge.setViewportView(observerRouge);

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
        JPanel imagePanel = new JPanel();
        JPanel panelHolder = new JPanel();
        mainFrame.setLayout(new BoxLayout(mainFrame,BoxLayout.Y_AXIS));
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.X_AXIS));
        imagePanel.add(jScrollPaneBare);
        imagePanel.add(jScrollPaneBleu);
        imagePanel.add(jScrollPaneRouge);
        mainFrame.add(showImageButton);
        panelHolder.setLayout(new BoxLayout(panelHolder, BoxLayout.X_AXIS));
        panelHolder.add(mainFrame);
        panelHolder.add(imagePanel);

        // Ajouter le conteneur principal à la fenêtre
        getContentPane().add(panelHolder);
        pack();
        setVisible(true);
    }

    private void displayImage() throws CImageNGException {
        try {
            int[][] matriceBareR = new int[M][N], matriceBareG = new int[M][N], matriceBareB = new int[M][N];
            imageBare.getMatricesRGB(matriceBareR, matriceBareG, matriceBareB);
            int[][] matriceR = matriceBareR, matriceG = matriceBareG, matriceB = matriceBareB;
            int[][] matriceRB = matriceBareR, matriceGB = matriceBareG, matriceBB = matriceBareB;

            // Avoir un calque blanc et noir sans les points rouges (fond blanc)
            matriceR = Seuillage.seuillageSimple(matriceR, 255);
            matriceG = Seuillage.seuillageSimple(matriceR, 255);
            matriceB = Seuillage.seuillageSimple(matriceR, 255);

            // Avoir un calque blanc et noir sans les points bleus (fond blanc)
            matriceRB = Seuillage.seuillageSimple(matriceBB, 255);
            matriceGB = Seuillage.seuillageSimple(matriceBB, 255);
            matriceBB = Seuillage.seuillageSimple(matriceBB, 255);

            // Avoir le calque négatif (rouge)
            for (int i = 0; i < matriceR.length; i++) {
                for (int j = 0; j < matriceR[0].length; j++) {
                    if(matriceR[i][j]==0){
                        matriceR[i][j]=255;
                        matriceG[i][j]=255;
                        matriceB[i][j]=255;
                    }else{
                        matriceR[i][j]=0;
                        matriceG[i][j]=0;
                        matriceB[i][j]=0;
                    }
                }
            }

            // Avoir le calque négatif (bleu)
            for (int i = 0; i < matriceBB.length; i++) {
                for (int j = 0; j < matriceBB[0].length; j++) {
                    if(matriceBB[i][j]==0){
                        matriceRB[i][j]=255;
                        matriceGB[i][j]=255;
                        matriceBB[i][j]=255;
                    }else{
                        matriceRB[i][j]=0;
                        matriceGB[i][j]=0;
                        matriceBB[i][j]=0;
                    }
                }
            }

            // Enlever les points verts
            matriceG = ContoursNonLineaire.subtractMatrices(matriceBareG, matriceG);
            matriceGB = ContoursNonLineaire.subtractMatrices(matriceBareG, matriceGB);

            matriceGB = Seuillage.seuillageSimple(matriceGB, 1);
            matriceBB = Seuillage.seuillageSimple(matriceBB, 0);
            matriceRB = matriceGB;
            imageBleu.setMatricesRGB(matriceRB, matriceGB, matriceBB);

            matriceR = Seuillage.seuillageSimple(matriceR, 0);
            matriceG = Seuillage.seuillageSimple(matriceG, 1);
            matriceB = matriceG;
            imageRouge.setMatricesRGB(matriceR, matriceG, matriceB);

        } catch (CImageRGBException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JDialogPetitsPois(new JFrame(), true,null).setVisible(true);
            }
        });
    }
}

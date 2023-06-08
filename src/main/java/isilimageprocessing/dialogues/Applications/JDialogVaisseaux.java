package isilimageprocessing.dialogues.Applications;

import cimage.CImageRGB;
import cimage.exceptions.CImageNGException;
import cimage.exceptions.CImageRGBException;
import cimage.observers.JLabelBeanCImage;
import imageprocessing.Contours.ContoursLineaire;
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

public class JDialogVaisseaux extends JDialog {
    private int M, N;
    private CImageRGB imageBare, imageSynthese, imageSynthese2, imageMasque, imagePlanete;
    private JLabelBeanCImage observerBare, observerSynthese, observerSynthese2;
    private JScrollPane jScrollPaneBare = new JScrollPane(), jScrollPaneSynthese = new JScrollPane(), jScrollPaneSynthese2 = new JScrollPane();

    public JDialogVaisseaux(Frame parent, boolean modal, String titre) {
        //super(parent, modal);
        setTitle(titre);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        M = 800;
        N = 600;
        try
        {
            imageBare = new CImageRGB(new File(getClass().getClassLoader().getResource("images_step_5/vaisseaux.jpg").toURI()));
            imageSynthese = new CImageRGB(M,N, 255, 255, 255);
            imageSynthese2 = new CImageRGB(M,N, 255, 255, 255);
            imageMasque = new CImageRGB(new File(getClass().getClassLoader().getResource("vaisseauxMasque.jpg").toURI()));
            imagePlanete = new CImageRGB(new File(getClass().getClassLoader().getResource("images_step_5/planete.jpg").toURI()));
        }
        catch (CImageRGBException | URISyntaxException ex)
        { System.out.println("Erreur CImageNG : " + ex.getMessage()); } catch (IOException e) {
            throw new RuntimeException(e);
        }

        observerBare = new JLabelBeanCImage(imageBare);
        observerSynthese = new JLabelBeanCImage(imageSynthese);
        observerSynthese2 = new JLabelBeanCImage(imageSynthese2);
        jScrollPaneBare.setViewportView(observerBare);
        jScrollPaneSynthese.setViewportView(observerSynthese);
        jScrollPaneSynthese2.setViewportView(observerSynthese2);

        // Créer un bouton pour afficher l'image sélectionnée
        JButton showImageButton = new JButton("Traiter l'image");
        JButton saveImageButton1 = new JButton("Sauver l'image 1");
        JButton saveImageButton2 = new JButton("Sauver l'image 2");

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

        saveImageButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enregistrerSous(imageSynthese);
            }
        });

        saveImageButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enregistrerSous(imageSynthese2);
            }
        });

        // Créer un conteneur principal et ajouter les composants
        JPanel mainFrame = new JPanel();
        JPanel imagePanel = new JPanel();
        JPanel panelHolder = new JPanel();
        mainFrame.setLayout(new BoxLayout(mainFrame,BoxLayout.Y_AXIS));
        mainFrame.add(showImageButton);
        mainFrame.add(saveImageButton1);
        mainFrame.add(saveImageButton2);
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.X_AXIS));
        imagePanel.add(jScrollPaneBare);
        imagePanel.add(jScrollPaneSynthese);
        imagePanel.add(jScrollPaneSynthese2);
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
            int[][] matriceR = new int[M][N], matriceG = new int[M][N], matriceB = new int[M][N];
            imageBare.getMatricesRGB(matriceR, matriceG, matriceB);
            int[][] bareGris = imageBare.getCImageNG().getMatrice();
            int[][] masqueGris = imageMasque.getCImageNG().getMatrice();

            // Avoir un masque du vaisseau correct
            bareGris = Seuillage.seuillageSimple(bareGris, 20);
            bareGris = MorphoElementaire.erosion(bareGris, 3);
            int[][] vaisseau = MorphoComplexe.reconstructionGeodesique(masqueGris, bareGris);
            vaisseau = MorphoComplexe.reconstructionGeodesique(vaisseau, bareGris);
            vaisseau = MorphoElementaire.dilatation(vaisseau, 30);
            vaisseau = MorphoComplexe.reconstructionGeodesique(vaisseau, bareGris);
            vaisseau = Seuillage.seuillageSimple(vaisseau, 20);
            vaisseau = MorphoElementaire.dilatation(vaisseau, 9);

            // Récupérer le vaisseau sur l'image
            matriceR = andMatrix(matriceR, vaisseau);
            matriceG = andMatrix(matriceG, vaisseau);
            matriceB = andMatrix(matriceB, vaisseau);

            // Coller le vaisseau sur l'image Planete
            int[][] matriceRP = new int[M][N], matriceGP = new int[M][N], matriceBP = new int[M][N];
            imagePlanete.getMatricesRGB(matriceRP, matriceGP, matriceBP);

            // Faire le contour rouge
            matriceRP = colleMatrix(matriceRP, matriceR);
            matriceGP = colleMatrix(matriceGP, matriceG);
            matriceBP = colleMatrix(matriceBP, matriceB);

            imageSynthese.setMatricesRGB(matriceRP, matriceGP, matriceBP);

            vaisseau = ContoursLineaire.laplacien4(vaisseau);
            matriceRP = colleMatrix(matriceRP, vaisseau);

            imageSynthese2.setMatricesRGB(matriceRP, matriceGP, matriceBP);
        } catch (CImageRGBException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JDialogVaisseaux(new JFrame(), true,null).setVisible(true);
            }
        });
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

    public static int[][] orMatrix(int[][] matrice, int[][] masque){
        int[][] result = new int[matrice.length][matrice[0].length];
        for (int i = 0; i < matrice.length; i++) {
            for (int j = 0; j < matrice[0].length; j++) {
                result[i][j] = matrice[i][j] | masque[i][j];
            }
        }
        return  result;
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

    public static void enregistrerSous(CImageRGB imageRGB){
        JFileChooser choix = new JFileChooser();
        File fichier;

        choix.setCurrentDirectory(new File ("./src/main/resources"));
        if (choix.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            fichier = choix.getSelectedFile();
            if (fichier != null)
            {
                try
                {
                    if (imageRGB != null) imageRGB.enregistreFormatPNG(fichier);
                }
                catch (IOException ex)
                {
                    System.err.println("Erreur I/O : " + ex.getMessage());
                }
            }
        }
    }
}

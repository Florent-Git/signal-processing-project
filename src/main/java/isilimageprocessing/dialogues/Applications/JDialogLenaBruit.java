package isilimageprocessing.dialogues.Applications;

import cimage.CImageNG;
import cimage.CImageRGB;
import cimage.exceptions.CImageNGException;
import cimage.exceptions.CImageRGBException;
import cimage.observers.JLabelBeanCImage;
import imageprocessing.Histogramme.Histogramme;
import imageprocessing.Lineaire.FiltrageLineaireGlobal;
import imageprocessing.NonLineaire.MorphoComplexe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class JDialogLenaBruit extends JDialog {
    private JLabel imageLabel;
    private int M, N;
    private CImageRGB imageBare, imageTransf;
    private JLabelBeanCImage observerBare, observerTransf;
    private JScrollPane jScrollPaneBare = new JScrollPane(), jScrollPaneTransf = new JScrollPane();
    private JRadioButton jRadioButtonBruit, jRadioButtonNet;
    private ButtonGroup radioGroup;

    public JDialogLenaBruit(Frame parent, boolean modal, String titre) {
        //super(parent, modal);
        setTitle(titre);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        M = 256;
        N = 256;
        try
        {
            imageBare = new CImageRGB(new File(getClass().getClassLoader().getResource("images_step_5/lenaBruit.png").toURI()));
            imageTransf = new CImageRGB(M,N, 255, 255, 255);
        }
        catch (CImageRGBException | URISyntaxException ex)
        { System.out.println("Erreur CImageNG : " + ex.getMessage()); } catch (IOException e) {
            throw new RuntimeException(e);
        }

        jRadioButtonBruit = new JRadioButton("Le moins de bruit mais flou");
        jRadioButtonNet = new JRadioButton("Le moins flou avec le moins de bruit");
        jRadioButtonBruit.setSelected(true);
        radioGroup = new ButtonGroup();
        radioGroup.add(jRadioButtonNet);
        radioGroup.add(jRadioButtonBruit);

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
        mainFrame.add(jRadioButtonBruit);
        mainFrame.add(jRadioButtonNet);
        mainFrame.add(jScrollPaneBare);
        mainFrame.add(showImageButton);
        mainFrame.add(jScrollPaneTransf);

        // Ajouter le conteneur principal à la fenêtre
        getContentPane().add(mainFrame);
        pack();
        setVisible(true);
    }

    private void displayImage() throws CImageNGException {
        try {
            int fc = 70, tailleMasque = jRadioButtonNet.isSelected() ? 3 : 5;
            int[][] matriceR = new int[M][N], matriceG = new int[M][N], matriceB = new int[M][N];
            imageBare.getMatricesRGB(matriceR, matriceG, matriceB);
            matriceR = FiltrageLineaireGlobal.filtrePasseBasIdeal(matriceR, fc);
            matriceG = FiltrageLineaireGlobal.filtrePasseBasIdeal(matriceG, fc);
            matriceB = FiltrageLineaireGlobal.filtrePasseBasIdeal(matriceB, fc);
            matriceR = MorphoComplexe.filtreMedian(matriceR, tailleMasque);
            matriceG = MorphoComplexe.filtreMedian(matriceG, tailleMasque);
            matriceB = MorphoComplexe.filtreMedian(matriceB, tailleMasque);
            imageTransf.setMatricesRGB(matriceR, matriceG, matriceB);
        } catch (CImageRGBException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JDialogLenaBruit(new JFrame(), true,null).setVisible(true);
            }
        });
    }
}

package isilimageprocessing.dialogues.Applications;

import cimage.CImageNG;
import cimage.exceptions.CImageNGException;
import cimage.observers.JLabelBeanCImage;
import imageprocessing.Contours.ContoursLineaire;
import imageprocessing.Contours.ContoursNonLineaire;
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

public class JDialogTools extends JDialog {
    private JLabel imageLabel;
    private int M, N;
    private CImageNG imageBare, imageGros, imagePetits;
    private JLabelBeanCImage observerBare, observerGros;
    private JScrollPane jScrollPaneBare = new JScrollPane(), jScrollPaneGros = new JScrollPane();

    public JDialogTools(Frame parent, boolean modal, String titre) {
        //super(parent, modal);
        setTitle(titre);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        M = 256;
        N = 256;
        try
        {
            imageBare = new CImageNG(new File(getClass().getClassLoader().getResource("images_step_5/tools.png").toURI()));
            imageGros = new CImageNG(M,N,255);
            imagePetits = new CImageNG(M,N,255);
        }
        catch (CImageNGException | URISyntaxException ex)
        { System.out.println("Erreur CImageNG : " + ex.getMessage()); } catch (IOException e) {
            throw new RuntimeException(e);
        }

        observerBare = new JLabelBeanCImage(imageBare);
        observerGros = new JLabelBeanCImage(imageGros);
        jScrollPaneBare.setViewportView(observerBare);
        jScrollPaneGros.setViewportView(observerGros);

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
        imagePanel.add(jScrollPaneBare);
        imagePanel.add(jScrollPaneGros);
        mainFrame.add(showImageButton);
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.X_AXIS));
        panelHolder.add(mainFrame);
        panelHolder.add(imagePanel);

        // Ajouter le conteneur principal à la fenêtre
        getContentPane().add(panelHolder);
        pack();
        setVisible(true);
    }

    private void displayImage() throws CImageNGException {
        int[][] imageTraitee = imageBare.getMatrice();
        int[][] fond = MorphoElementaire.erosion(imageTraitee, 21);

        imageTraitee = ContoursNonLineaire.subtractMatrices(imageBare.getMatrice(), fond);
        imageTraitee = Seuillage.seuillageSimple(imageTraitee,52);

        imageGros.setMatrice(imageTraitee);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JDialogTools(new JFrame(), true,null).setVisible(true);
            }
        });
    }
}

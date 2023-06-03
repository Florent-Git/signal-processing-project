package isilimageprocessing.dialogues.Seuillage;

import cimage.CImageNG;
import cimage.exceptions.CImageNGException;
import cimage.observers.JLabelBeanCImage;
import imageprocessing.Seuillage.Seuillage;

import javax.swing.*;
import java.awt.*;

public class JDialogAfficheSeuilDouble extends JDialog {
    private final JSpinner numberSpinner1, numberSpinner2;
    private final int M, N;
    private CImageNG imageBare, imageTransf;
    private final JLabelBeanCImage observerBare, observerTransf;
    private final JScrollPane jScrollPaneBare = new JScrollPane(), jScrollPaneTransf = new JScrollPane();

    public JDialogAfficheSeuilDouble(Frame parent, boolean modal, int[][] matrice, String titre) {
        //super(parent, modal);
        setTitle(titre);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        M = matrice.length;
        N = matrice[0].length;
        try
        {
            imageBare = new CImageNG(M,N,0);
            imageTransf = new CImageNG(M,N, 255);
            imageBare.setMatrice(matrice);
        }
        catch (CImageNGException ex)
        { System.out.println("Erreur CImageNG : " + ex.getMessage()); }

        observerBare = new JLabelBeanCImage(imageBare);
        observerTransf = new JLabelBeanCImage(imageTransf);
        jScrollPaneBare.setViewportView(observerBare);
        jScrollPaneTransf.setViewportView(observerTransf);

        // Créer le sélecteur de nombres entiers
        numberSpinner1 = new JSpinner(new SpinnerNumberModel(1, 1, 255, 1));
        numberSpinner2 = new JSpinner(new SpinnerNumberModel(127, 1, 255, 1));

        // Créer un bouton pour afficher l'image sélectionnée
        JButton showImageButton = new JButton("Traiter l'image");
        showImageButton.addActionListener(e -> {
            try {
                displayImage();
            } catch (CImageNGException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Créer un conteneur principal et ajouter les composants
        JPanel mainFrame = new JPanel();
        mainFrame.setLayout(new BoxLayout(mainFrame,BoxLayout.Y_AXIS));
        mainFrame.add(new JLabel("Seuil 1 :"));
        mainFrame.add(numberSpinner1);
        mainFrame.add(new JLabel("Seuil 2 :"));
        mainFrame.add(numberSpinner2);
        mainFrame.add(jScrollPaneBare);
        mainFrame.add(showImageButton);
        mainFrame.add(jScrollPaneTransf);

        // Ajouter le conteneur principal à la fenêtre
        getContentPane().add(mainFrame);
        pack();
        setVisible(true);
    }

    private void displayImage() throws CImageNGException {
        // Obtenir la valeur sélectionnée dans le sélecteur de nombres
        int seuil1 = (int) numberSpinner1.getValue();
        int seuil2 = (int) numberSpinner2.getValue();

        // Obtenir l'image traitée
        int[][] imageTraitee = Seuillage.seuillageDouble(imageBare.getMatrice(), seuil1, seuil2);

        // Afficher l'image dans l'étiquette
        imageTransf.setMatrice(imageTraitee);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JDialogAfficheSeuilDouble(new JFrame(), true,null,null).setVisible(true));
    }
}

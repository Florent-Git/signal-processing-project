package isilimageprocessing.dialogues.Lineaire.Global;

import cimage.CImageNG;
import cimage.exceptions.CImageNGException;
import cimage.observers.JLabelBeanCImage;
import imageprocessing.Lineaire.FiltrageLineaireGlobal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JDialogAffichePHIGlobal extends JDialog {
    private JLabel imageLabel;
    private JSpinner numberSpinner;
    private int M, N;
    private CImageNG imageBare, imageTransf;
    private JLabelBeanCImage observerBare, observerTransf;
    private JScrollPane jScrollPaneBare = new JScrollPane(), jScrollPaneTransf = new JScrollPane();

    public JDialogAffichePHIGlobal(Frame parent, boolean modal, int matrice[][], String titre) {
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
        numberSpinner = new JSpinner(new SpinnerNumberModel(15, 15, 255, 1));

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
        mainFrame.add(new JLabel("Frequence de coupure :"));
        mainFrame.add(numberSpinner);
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
        int freqCoup = (int) numberSpinner.getValue();

        // Obtenir l'image traitée
        int[][] imageTraitee = FiltrageLineaireGlobal.filtrePasseHautIdeal(imageBare.getMatrice(), freqCoup);

        // Afficher l'image dans l'étiquette
        imageTransf.setMatrice(imageTraitee);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JDialogAffichePHIGlobal(new javax.swing.JFrame(), true,null,null).setVisible(true);
            }
        });
    }
}

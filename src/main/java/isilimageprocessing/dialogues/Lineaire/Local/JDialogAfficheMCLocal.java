package isilimageprocessing.dialogues.Lineaire.Local;

import cimage.CImageNG;
import cimage.exceptions.CImageNGException;
import cimage.observers.JLabelBeanCImage;
import imageprocessing.Lineaire.FiltrageLineaireLocal;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JDialogAfficheMCLocal extends JDialog {
    private JLabel imageLabel;
    private JSpinner numberSpinnerTaille;
    private JSpinner[][] convolutionSpinners = new JSpinner[9][9];
    private int M, N;
    private CImageNG imageBare, imageTransf;
    private JLabelBeanCImage observerBare, observerTransf;
    private JScrollPane jScrollPaneBare = new JScrollPane(), jScrollPaneTransf = new JScrollPane();

    public JDialogAfficheMCLocal(Frame parent, boolean modal, int matrice[][], String titre) {
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
        numberSpinnerTaille = new JSpinner(new SpinnerNumberModel(1, 1, 9, 2));
        numberSpinnerTaille.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        convolutionSpinners[i][j].setVisible(false);
                    }
                }
                int offsetSpinner = (int)numberSpinnerTaille.getValue()/2;
                for (int i = -offsetSpinner + 4; i <= offsetSpinner + 4; i++) {
                    for (int j = -offsetSpinner + 4; j <= offsetSpinner + 4; j++) {
                        convolutionSpinners[i][j].setVisible(true);
                    }
                }
            }
        });

        // Créer les sélecteurs de nombres doubles
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                convolutionSpinners[i][j] = new JSpinner((new SpinnerNumberModel(0, 0, 1, 0.01)));
                convolutionSpinners[i][j].setVisible(false);
            }
        }
        convolutionSpinners[4][4].setVisible(true);


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
        JPanel topPanel = new JPanel();
        JPanel convPanel = new JPanel();
        JPanel mainPanel = new JPanel();
        JPanel panelHolder = new JPanel();

        topPanel.setLayout(new FlowLayout());
        convPanel.setLayout(new GridLayout(9,9));
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        topPanel.add(new JLabel("Taille du masque :"));
        topPanel.add(numberSpinnerTaille);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                convPanel.add(convolutionSpinners[i][j]);
            }
        }
        mainPanel.add(jScrollPaneBare);
        mainPanel.add(showImageButton);
        mainPanel.add(jScrollPaneTransf);

        // Ajouter le conteneur principal à la fenêtre
        panelHolder.setLayout(new BoxLayout(panelHolder,BoxLayout.Y_AXIS));
        panelHolder.add(topPanel);
        panelHolder.add(convPanel);
        panelHolder.add(mainPanel);
        getContentPane().add(panelHolder);
        pack();
        setVisible(true);
    }

    private void displayImage() throws CImageNGException {
        // Obtenir la valeur sélectionnée dans le sélecteur de nombres
        int tailleMasque = (int) numberSpinnerTaille.getValue();

        // Obtenir le masque de convolution (par défaut, l'addition des éléments donne 1)
        double[][] masque = new double[tailleMasque][tailleMasque];

        int offsetSpinner = (int)numberSpinnerTaille.getValue()/2;
        for (int i = -offsetSpinner + 4; i <= offsetSpinner + 4; i++) {
            for (int j = -offsetSpinner + 4; j <= offsetSpinner + 4; j++) {
                masque[i-4+offsetSpinner][j-4+offsetSpinner] = (double) convolutionSpinners[i][j].getValue();
            }
        }

        // Obtenir l'image traitée
        int[][] imageTraitee = FiltrageLineaireLocal.filtreMasqueConvolution(imageBare.getMatrice(), masque);

        // Afficher l'image dans l'étiquette
        imageTransf.setMatrice(imageTraitee);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JDialogAfficheMCLocal(new JFrame(), true,null,null).setVisible(true);
            }
        });
    }
}

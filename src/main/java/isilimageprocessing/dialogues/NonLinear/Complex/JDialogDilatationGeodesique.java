package isilimageprocessing.dialogues.NonLinear.Complex;

import cimage.CImageNG;
import cimage.exceptions.CImageNGException;
import cimage.observers.JLabelBeanCImage;
import imageprocessing.NonLineaire.MorphoComplexe;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class JDialogDilatationGeodesique extends JDialog {
    private final JSpinner numberSpinner;
    private final int M, N;
    private CImageNG imageBare, imageTransf, geodesicMask;
    private final JLabelBeanCImage observerBare, observerTransf, observerMask;
    private final JScrollPane jScrollPaneBare = new JScrollPane(), jScrollPaneTransf = new JScrollPane(), jScrollPaneMask = new JScrollPane();

    public JDialogDilatationGeodesique(Frame parent, boolean modal, int[][] matrice, String titre) {
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

            geodesicMask = new CImageNG(M,N,255);
        }
        catch (CImageNGException ex)
        { System.out.println("Erreur CImageNG : " + ex.getMessage()); }

        observerBare = new JLabelBeanCImage(imageBare);
        observerTransf = new JLabelBeanCImage(imageTransf);
        observerMask = new JLabelBeanCImage(geodesicMask);
        jScrollPaneBare.setViewportView(observerBare);
        jScrollPaneTransf.setViewportView(observerTransf);
        jScrollPaneMask.setViewportView(observerMask);

        // Créer le sélecteur de nombres entiers
        numberSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

        // Créer un bouton pour afficher l'image sélectionnée
        JButton showImageButton = new JButton("Traiter l'image");
        showImageButton.addActionListener(e -> {
            try {
                displayImage();
            } catch (CImageNGException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Créer un bouton pour charger un masque
        JButton loadMaskButton = new JButton("Selectionner un masque");
        loadMaskButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (selectedFile != null)
                {
                    try
                    {
                        CImageNG matrix = new CImageNG(selectedFile);
                        geodesicMask.setMatrice(matrix.getMatrice());
                    }
                    catch (IOException ex)
                    {
                        System.err.println("Erreur I/O : " + ex.getMessage());
                    } catch (CImageNGException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        // Créer un conteneur principal et ajouter les composants
        JPanel topPanel = new JPanel();
        JPanel convPanel = new JPanel();
        JPanel mainPanel = new JPanel();
        JPanel secondPanel = new JPanel();
        JPanel panelHolder = new JPanel();

        topPanel.setLayout(new FlowLayout());
        convPanel.setLayout(new GridLayout(9,9));
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        secondPanel.setLayout(new BoxLayout(secondPanel,BoxLayout.Y_AXIS));
        topPanel.add(new JLabel("Nombre d'iterations :"));

        mainPanel.add(numberSpinner);
        mainPanel.add(jScrollPaneBare);
        mainPanel.add(showImageButton);
        mainPanel.add(jScrollPaneTransf);

        secondPanel.add(loadMaskButton);
        secondPanel.add(jScrollPaneMask);

        // Ajouter le conteneur principal à la fenêtre
        panelHolder.setLayout(new BoxLayout(panelHolder,BoxLayout.Y_AXIS));
        panelHolder.add(topPanel);
        panelHolder.add(convPanel);
        panelHolder.add(mainPanel);
        panelHolder.add(secondPanel);
        getContentPane().add(panelHolder);
        pack();
        setVisible(true);
    }

    private void displayImage() throws CImageNGException {
        // Obtenir la valeur sélectionnée dans le sélecteur de nombres
        int nbrIter = (int) numberSpinner.getValue();

        int[][] imageTraitee = MorphoComplexe.dilatationGeodesique(imageBare.getMatrice(), geodesicMask.getMatrice(), nbrIter);

        // Afficher l'image dans l'étiquette
        imageTransf.setMatrice(imageTraitee);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JDialogDilatationGeodesique(new JFrame(), true,null,null).setVisible(true));
    }
}

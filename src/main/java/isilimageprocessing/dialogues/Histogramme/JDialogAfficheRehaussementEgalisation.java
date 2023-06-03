package isilimageprocessing.dialogues.Histogramme;

import cimage.exceptions.CImageNGException;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

public class JDialogAfficheRehaussementEgalisation extends JDialogAfficheRehaussement {
    public JDialogAfficheRehaussementEgalisation(Frame parent, boolean modal, int[][] matrice, String titre) {
        super(parent, modal, matrice, titre);
    }

    @Override
    protected void setupParameters() { }

    @Override
    protected Function<Integer, Integer> getTonalCurve() throws CImageNGException {
        return histogramme.creerCourbeTonaleEgalisation(cImageBase.getMatrice());
    }
}
package isilimageprocessing.dialogues.Histogramme;

import imageprocessing.Histogramme.Histogramme;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

public class JDialogAfficheRehaussementLineaireSaturation extends JDialogAfficheRehaussement {
    public JDialogAfficheRehaussementLineaireSaturation(Frame parent, boolean modal, int[][] matrice, String titre) {
        super(parent, modal, matrice, titre);
    }

    @Override
    protected void setupParameters() {
        parameters.put("smin", new JSpinner(new SpinnerNumberModel(0, 0, 255, 1)));
        parameters.put("smax", new JSpinner(new SpinnerNumberModel(0, 0, 255, 1)));
    }

    @Override
    protected Function<Integer, Integer> getTonalCurve() {
        return histogramme.creerCourbeTonaleLineaireSaturation(
                (Integer) parameters.get("smin").getValue(),
                (Integer) parameters.get("smax").getValue()
        );
    }
}
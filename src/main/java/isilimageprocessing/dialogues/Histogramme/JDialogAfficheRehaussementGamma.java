package isilimageprocessing.dialogues.Histogramme;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

public class JDialogAfficheRehaussementGamma extends JDialogAfficheRehaussement {
    public JDialogAfficheRehaussementGamma(Frame parent, boolean modal, int[][] matrice, String titre) {
        super(parent, modal, matrice, titre);
    }

    @Override
    protected void setupParameters() {
        parameters.put("gamma", new JSpinner(new SpinnerNumberModel(1.3, 1.3, 3.0, .1)));
    }

    @Override
    protected Function<Integer, Integer> getTonalCurve() {
        return histogramme.creerCourbeTonaleGamma(
                (Double) parameters.get("gamma").getValue()
        );
    }
}
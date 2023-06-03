package imageprocessing.Histogramme;

import java.util.function.Function;

public interface IHistogrammeOperations {
    int[][] rehaussement(int[][] image, Function<Integer, Integer> courbeTotale);
    Function<Integer, Integer> creerCourbeTonaleLineaireSaturation(int smin, int smax);
    Function<Integer, Integer> creerCourbeTonaleGamma(double gamma);
    Function<Integer, Integer> creerCourbeTonaleNegatif();
    Function<Integer, Integer> creerCourbeTonaleEgalisation(int[][] image);
}

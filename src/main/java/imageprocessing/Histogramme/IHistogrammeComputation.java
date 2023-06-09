package imageprocessing.Histogramme;

public interface IHistogrammeComputation {
    int minimum(int[][] image);
    int maximum(int[][] image);
    int luminance(int[][] image);
    double contraste1(int[][] image);
    double contraste2(int[][] image);
}

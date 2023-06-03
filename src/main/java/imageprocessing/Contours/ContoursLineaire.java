package imageprocessing.Contours;

import imageprocessing.Lineaire.FiltrageLineaireLocal;

public class ContoursLineaire {

    public static int[][] gradientPrewitt(int[][] image,int dir) {
        // Définition des masques de Prewitt pour le calcul du gradient horizontal et vertical
        double[][] maskHorizontal = {{1, 0, -1}, {1, 0, -1}, {1, 0, -1}};
        double[][] maskVertical = {{1, 1, 1}, {0, 0, 0}, {-1, -1, -1}};

        double[][] selectedMask = (dir == 1) ? maskHorizontal : maskVertical;

        return FiltrageLineaireLocal.filtreMasqueConvolution(image, selectedMask);
    }

    public static int[][] gradientSobel(int[][] image,int dir) {
        // Définition des masques de Prewitt pour le calcul du gradient horizontal et vertical
        double[][] maskHorizontal = {{1, 0, -1}, {2, 0, -2}, {1, 0, -1}};
        double[][] maskVertical = {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};

        double[][] selectedMask = (dir == 1) ? maskHorizontal : maskVertical;

        return FiltrageLineaireLocal.filtreMasqueConvolution(image, selectedMask);
    }

    public static int[][] laplacien4(int[][] image) {
        double[][] maskLaplace4 = {{0, 1, 0}, {1, -4, 1}, {0, 1, 0}};

        return FiltrageLineaireLocal.filtreMasqueConvolution(image, maskLaplace4);
    }

    public static int[][] laplacien8(int[][] image) {
        double[][] maskLaplace8 = {{1, 1, 1}, {1, -8, 1}, {1, 1, 1}};

        return FiltrageLineaireLocal.filtreMasqueConvolution(image, maskLaplace8);
    }
}

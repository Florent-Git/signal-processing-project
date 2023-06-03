package imageprocessing.Contours;

import imageprocessing.Lineaire.FiltrageLineaireLocal;

public class ContoursLineaire {

    /**
     * Calcule le gradient de Prewitt de l'image dans la direction spécifiée.
     *
     * @param image l'image d'entrée en niveaux de gris
     * @param dir   la direction du gradient (1 pour horizontal, 2 pour vertical)
     * @return l'image des contours par le gradient de Prewitt
     */
    public static int[][] gradientPrewitt(int[][] image,int dir) {
        // Définition des masques de Prewitt pour le calcul du gradient horizontal et vertical
        double[][] maskHorizontal = {{1, 0, -1}, {1, 0, -1}, {1, 0, -1}};
        double[][] maskVertical = {{1, 1, 1}, {0, 0, 0}, {-1, -1, -1}};

        double[][] selectedMask = (dir == 1) ? maskHorizontal : maskVertical;

        return FiltrageLineaireLocal.filtreMasqueConvolution(image, selectedMask);
    }

    /**
     * Calcule le gradient de Sobel de l'image dans la direction spécifiée.
     *
     * @param image l'image d'entrée en niveaux de gris
     * @param dir   la direction du gradient (1 pour horizontal, 2 pour vertical)
     * @return l'image des contours par le gradient de Sobel
     */
    public static int[][] gradientSobel(int[][] image,int dir) {
        // Définition des masques de Prewitt pour le calcul du gradient horizontal et vertical
        double[][] maskHorizontal = {{1, 0, -1}, {2, 0, -2}, {1, 0, -1}};
        double[][] maskVertical = {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};

        double[][] selectedMask = (dir == 1) ? maskHorizontal : maskVertical;

        return FiltrageLineaireLocal.filtreMasqueConvolution(image, selectedMask);
    }

    /**
     * Calcule le laplacien 4-voisinage de l'image.
     *
     * @param image l'image d'entrée en niveaux de gris
     * @return l'image des contours par le laplacien 4-voisinage
     */
    public static int[][] laplacien4(int[][] image) {
        double[][] maskLaplace4 = {{0, 1, 0}, {1, -4, 1}, {0, 1, 0}};

        return FiltrageLineaireLocal.filtreMasqueConvolution(image, maskLaplace4);
    }

    /**
     * Calcule le laplacien 8-voisinage de l'image.
     *
     * @param image l'image d'entrée en niveaux de gris
     * @return l'image des contours par le laplacien 8-voisinage
     */
    public static int[][] laplacien8(int[][] image) {
        double[][] maskLaplace8 = {{1, 1, 1}, {1, -8, 1}, {1, 1, 1}};

        return FiltrageLineaireLocal.filtreMasqueConvolution(image, maskLaplace8);
    }
}

package imageprocessing.Contours;

import imageprocessing.NonLineaire.MorphoElementaire;

public class ContoursNonLineaire {

    /**
     * Calcule le gradient d'érosion de l'image.
     *
     * @param image l'image d'entrée en niveaux de gris
     * @return l'image des contours par le gradient d'érosion
     */
    public static int[][] gradientErosion(int[][] image) {
        int[][] erodedImage = MorphoElementaire.erosion(image, 3);

        return subtractMatrices(image, erodedImage);
    }

    /**
     * Calcule le gradient de dilatation de l'image.
     *
     * @param image l'image d'entrée en niveaux de gris
     * @return l'image des contours par le gradient de dilatation
     */
    public static int[][] gradientDilatation(int[][] image) {
        int[][] dilatedImage = MorphoElementaire.dilatation(image, 3);

        return subtractMatrices(dilatedImage, image);
    }

    /**
     * Calcule le gradient de Beucher de l'image.
     *
     * @param image l'image d'entrée en niveaux de gris
     * @return l'image des contours par le gradient de Beucher
     */
    public static int[][] gradientBeucher(int[][] image) {
        int[][] dilatedImage = MorphoElementaire.dilatation(image, 3);
        int[][] erodedImage = MorphoElementaire.erosion(image, 3);

        return subtractMatrices(dilatedImage, erodedImage);
    }

    /**
     * Calcule le laplacien non linéaire de l'image.
     *
     * @param image l'image d'entrée en niveaux de gris
     * @return l'image des contours par le laplacien non linéaire
     */
    public static int[][] laplacienNonLineaire(int[][] image) {
        int[][] gradDilated = gradientDilatation(image);
        int[][] gradEroded = gradientErosion(image);

        return subtractMatrices(gradDilated,gradEroded);
    }

    /**
     * Méthode utilitaire qui soustrait deux matrices pixel par pixel.
     *
     * @param firstMatrix  la première matrice
     * @param secondMatrix la deuxième matrice
     * @return la matrice résultante de la soustraction
     */
    public static int[][] subtractMatrices(int[][] firstMatrix, int[][] secondMatrix) {
        int height = firstMatrix.length;
        int width = firstMatrix[0].length;
        int[][] result = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                result[i][j] = Math.abs(firstMatrix[i][j] - secondMatrix[i][j]);
            }
        }

        return result;
    }
}

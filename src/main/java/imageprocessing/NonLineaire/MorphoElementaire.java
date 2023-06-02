package imageprocessing.NonLineaire;

public class MorphoElementaire {

    /**
     * Réalise l'érosion de l'image.
     *
     * @param image        L'image d'origine.
     * @param tailleMasque La taille du masque utilisé pour l'érosion.
     * @return L'image érodée.
     */
    public static int[][] erosion(int [][] image,int tailleMasque){
        int height = image.length;
        int width = image[0].length;
        int[][] erosionResult = new int[height][width];

        int offset = tailleMasque / 2;

        for (int i = offset; i < height - offset; i++) {
            for (int j = offset; j < width - offset; j++) {
                int minPixelValue = 255;

                // Vérifier si tous les pixels de la fenêtre sont à 1
                for (int k = i - offset; k <= i + offset; k++) {
                    for (int l = j - offset; l <= j + offset; l++) {
                        minPixelValue = Math.min(minPixelValue, image[k][l]);
                    }
                }
                erosionResult[i][j] = minPixelValue;
            }
        }

        return erosionResult;
    }

    /**
     * Réalise la dilatation de l'image.
     *
     * @param image        L'image d'origine.
     * @param tailleMasque La taille du masque utilisé pour la dilatation.
     * @return L'image dilatée.
     */
    public static int[][] dilatation(int [][] image,int tailleMasque) {
        int height = image.length;
        int width = image[0].length;
        int[][] dilatationResult = new int[height][width];

        int offset = tailleMasque / 2;

        for (int i = offset; i < height - offset; i++) {
            for (int j = offset; j < width - offset; j++) {
                int maxPixelValue = 0;

                // Trouver la valeur maximale parmi les pixels du masque
                for (int k = i - offset; k <= i + offset; k++) {
                    for (int l = j - offset; l <= j + offset; l++) {
                        maxPixelValue = Math.max(maxPixelValue, image[k][l]);
                    }
                }
                dilatationResult[i][j] = maxPixelValue;
            }
        }

        return dilatationResult;
    }

    /**
     * Réalise l'ouverture de l'image.
     * Utilise une érosion suivie d'une dilatation
     *
     * @param image        L'image d'origine.
     * @param tailleMasque La taille du masque utilisé pour l'ouverture.
     * @return L'image ouverte.
     */
    public static int[][] ouverture(int [][] image,int tailleMasque) {
        int[][] erosionResult = erosion(image, tailleMasque);
        return dilatation(erosionResult, tailleMasque);
    }

    /**
     * Réalise la fermeture de l'image.
     * Utilise une dilatation suivie d'une érosion
     *
     * @param image        L'image d'origine.
     * @param tailleMasque La taille du masque utilisé pour la fermeture.
     * @return L'image fermée.
     */
    public static int[][] fermeture(int [][] image,int tailleMasque) {
        int[][] dilatationResult = dilatation(image, tailleMasque);
        return erosion(dilatationResult, tailleMasque);
    }
}

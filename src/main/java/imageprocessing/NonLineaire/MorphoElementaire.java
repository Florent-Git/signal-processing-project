package imageprocessing.NonLineaire;

public class MorphoElementaire {

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

    public static int[][] ouverture(int [][] image,int tailleMasque) {
        int[][] erosionResult = erosion(image, tailleMasque);
        return dilatation(erosionResult, tailleMasque);
    }

    public static int[][] fermeture(int [][] image,int tailleMasque) {
        int[][] dilatationResult = dilatation(image, tailleMasque);
        return erosion(dilatationResult, tailleMasque);
    }
}

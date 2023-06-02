package imageprocessing.NonLineaire;

import java.util.Arrays;

import static imageprocessing.NonLineaire.MorphoElementaire.dilatation;

public class MorphoComplexe {

    public static int[][] dilatationGeodesique(int[][] image,int[][] masqueGeodesique, int nbIter) {
        int[][] dilatedImage = Arrays.copyOf(image, image.length);

        for (int i = 0; i < nbIter; i++) {
            dilatedImage = dilatation(dilatedImage, 3);

            // Application conditionnelle du masque géodésique
            for (int j = 0; j < image.length; j++) {
                for (int k = 0; k < image[0].length; k++) {
                    dilatedImage[j][k] = dilatedImage[j][k] & masqueGeodesique[j][k];
                }
            }
        }

        return dilatedImage;
    }

    public static int[][] reconstructionGeodesique(int[][] image, int[][] masqueGeodesique) {
        int[][] reconstructedImage = Arrays.copyOf(image, image.length);
        boolean hasChanged = true;
        int nbrIter = 0;

        while (hasChanged) {
            int[][] lastReconstructedImage = dilatationGeodesique(reconstructedImage, masqueGeodesique, 1);

            if (Arrays.deepEquals(lastReconstructedImage, reconstructedImage)) {
                hasChanged = false;
                System.out.println("hasnt changed");
            }
            reconstructedImage = lastReconstructedImage;
            nbrIter++;
        }
        System.out.println("Required iterations to fully reconstruct : " + nbrIter);

        return reconstructedImage;
    }

    public static int[][] filtreMedian(int[][] image, int tailleMasque) {
        int height = image.length;
        int width = image[0].length;
        int[][] filteredImage = new int[height][width];
        int offset = tailleMasque / 2;

        for (int i = offset; i < height - offset; i++) {
            for (int j = offset; j < width - offset; j++) {
                int[] values = new int[tailleMasque * tailleMasque];
                int index = 0;

                for (int k = i - offset; k <= i + offset; k++) {
                    for (int l = j - offset; l <= j + offset; l++) {
                        values[index++] = image[k][l];
                    }
                }
                filteredImage[i][j] = getMedianValue(values);
            }
        }
        return filteredImage;
    }

    private static int getMedianValue(int[] values) {
        Arrays.sort(values);
        int middle = values.length / 2;
        if (values.length % 2 == 1) {
            return values[middle];
        } else {
            return (values[middle - 1] + values[middle]) / 2;
        }
    }

}

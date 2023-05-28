package imageprocessing.Lineaire;

import imageprocessing.Complexe.MatriceComplexe;
import imageprocessing.Fourier.Fourier;

public class FiltrageLineaireGlobal {
    /**
     * Réalise le filtrage passe-bas de l’image en passant par sa transformée de Fourier
     * en utilisant une fenêtre de forme circulaire de rayon égal à la fréquence de coupure
     * @param image L'image à transformer
     * @param fc La fréquence de coupure
     * @return L'image transformée par le filtre passe-bas
     */
    public static int[][] filtrePasseBasIdeal(int[][] image, int fc){
        double[][] dImage = getDoubleArray(image);
        int hauteur = image.length;
        int largeur = image[0].length;
        /*
        ----------> X/u (largeur)
        |
        |
        |
        v
        Y/v (hauteur)
         */

        //D'abord faire Fourier 2D avec DFT
        MatriceComplexe fourier = Fourier.Fourier2D(dImage);

        //Ensuite, décroiser
        fourier = Fourier.decroise(fourier);

        //Appliquer le filtre passe-bas
        int centreH = hauteur / 2;
        int centreL = largeur / 2;
        for (int v = 0; v < hauteur; v++) {
            for (int u = 0; u < largeur; u++) {
                int distCentre = (int) Math.sqrt(Math.pow(centreL - u, 2) + Math.pow(centreH - v, 2));
                if (distCentre > fc)
                    fourier.set(v, u, 0, 0);
            }
        }

        //Recroiser
        fourier = Fourier.decroise(fourier);

        //Finir par Fourier 2D inverse avec DFT inverse
        fourier = Fourier.InverseFourier2D(fourier);

        //Renvoyer la partie réelle du Fourier inverse
        return getIntArray(fourier.getPartieReelle());
    }

    /**
     * Réalise le filtrage passe-haut de l’image en passant par sa transformée de Fourier
     * en utilisant une fenêtre de forme circulaire de rayon égal à la fréquence de coupure
     * @param image L'image à transformer
     * @param fc La fréquence de coupure
     * @return L'image transformée par le filtre passe-haut
     */
    public static int[][] filtrePasseHautIdeal(int[][] image, int fc){
        double[][] dImage = getDoubleArray(image);
        int hauteur = image.length;
        int largeur = image[0].length;
        /*
        ----------> X/u (largeur)
        |
        |
        |
        v
        Y/v (hauteur)
         */

        //D'abord faire Fourier 2D avec DFT
        MatriceComplexe fourier = Fourier.Fourier2D(dImage);

        //Ensuite, décroiser
        fourier = Fourier.decroise(fourier);

        //Appliquer le filtre passe-haut
        int centreH = hauteur / 2;
        int centreL = largeur / 2;
        for (int v = 0; v < hauteur; v++) {
            for (int u = 0; u < largeur; u++) {
                int distCentre = (int) Math.sqrt(Math.pow(centreL - u, 2) + Math.pow(centreH - v, 2));
                if (distCentre < fc)
                    fourier.set(v, u, 0, 0);
            }
        }

        //Recroiser
        fourier = Fourier.decroise(fourier);

        //Finir par Fourier 2D inverse avec DFT inverse
        fourier = Fourier.InverseFourier2D(fourier);

        //Renvoyer la partie réelle du Fourier inverse
        int[][] partReelle = getIntArray(fourier.getPartieReelle());

        return add128(partReelle);
    }

    /**
     * Réalise le filtrage passe-bas de l’image en passant par sa transformée de Fourier
     * en utilisant la fonction de transfert de Butterworth correspondante
     * @param image L'image à transformer
     * @param fc La fréquence de coupure
     * @param ordre L'ordre
     * @return L'image transformée par le filtre passe-bas de Butterworth
     */
    public static int[][] filtrePasseBasButterworth(int[][] image, int fc, int ordre){
        return null;
    }

    /**
     * Réalise le filtrage passe-haut de l’image en passant par sa transformée de Fourier
     * en utilisant la fonction de transfert de Butterworth correspondante
     * @param image L'image à transformer
     * @param fc La fréquence de coupure
     * @param ordre L'ordre
     * @return L'image transformée par le filtre passe-haut de Butterworth
     */
    public static int[][] filtrePasseHautButterworth(int[][] image, int fc, int ordre){
        return null;
    }

    public static double[][] getDoubleArray(int[][] intArray){
        double[][] doubleArray = new double[intArray.length][intArray[0].length];
        for (int row = 0; row < intArray.length; row++) {
            for (int column = 0; column < intArray[0].length; column++) {
                doubleArray[row][column] = (double) intArray[row][column];
            }
        }
        return doubleArray;
    }

    public static int[][] getIntArray(double[][] doubleArray){
        int[][] intArray = new int[doubleArray.length][doubleArray[0].length];
        for (int row = 0; row < doubleArray.length; row++) {
            for (int column = 0; column < doubleArray[0].length; column++) {
                intArray[row][column] = (int) doubleArray[row][column];
            }
        }
        return intArray;
    }

    public static int[][] add128(int[][] array){
        for (int row = 0; row < array.length; row++) {
            for (int column = 0; column < array[0].length; column++) {
                array[row][column] += 128;
            }
        }
        return array;
    }
}

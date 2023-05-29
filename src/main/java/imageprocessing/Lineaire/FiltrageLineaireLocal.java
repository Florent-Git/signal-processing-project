package imageprocessing.Lineaire;

public class FiltrageLineaireLocal {
    /**
     * Réalise le filtrage local de l’image en utilisant le masque de convolution, celui-ci
     * étant une matrice carrée quelconque de dimension nxn, avec n impair
     * @param image L'image à transformer
     * @param masque Le masque de convolution
     * @return L'image transformée
     */
    public static int[][] filtreMasqueConvolution(int[][] image, double[][] masque){
        int hauteur = image.length;
        int largeur = image[0].length;
        /*
        ----------> X (largeur)
        |
        |
        |
        v
        Y(hauteur)
        */

        //Appliquer le masque de convolution
        int centreMasque = masque.length / 2;
        int[][] imageConv = new int[hauteur][largeur];

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                int pixel = 0;
                for (int i = 0; i < masque.length; i++) {
                    for (int j = 0; j < masque.length; j++) {
                        try {
                            pixel += masque[i][j]*image[-centreMasque + j + y][-centreMasque + i + x];
                        }catch (ArrayIndexOutOfBoundsException e){
                            pixel += masque[i][j]*0;
                        }
                    }
                }
                if (pixel > 255)
                    imageConv[y][x] = 255;
                else if (pixel < 0)
                    imageConv[y][x] = 0;
                else
                    imageConv[y][x] = pixel;
            }
        }
        return imageConv;
    }

    /**
     * Réalise un filtrage moyenneur de l’image avec un masque de convolution
     * de dimension tailleMasque x tailleMasque
     * @param image L'image à transformer
     * @param tailleMasque La taille du masque de convolution
     * @return L'image transformée
     */
    public static int[][] filtreMoyenneur(int[][] image, int tailleMasque){
        // Obtenir le filtre moyenneur
        double[][] masque = new double[tailleMasque][tailleMasque];
        for (int i = 0; i < tailleMasque; i++) {
            for (int j = 0; j < tailleMasque; j++) {
                masque[i][j] = 1.0/(tailleMasque*tailleMasque);
            }
        }
        return filtreMasqueConvolution(image, masque);
    }
}

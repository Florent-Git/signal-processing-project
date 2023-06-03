package imageprocessing.Seuillage;

public class Seuillage {

    /**
     * Réalise le seuillage simple d'une image en niveaux de gris.
     *
     * @param image l'image d'entrée en niveaux de gris
     * @param seuil le seuil à utiliser pour le seuillage
     * @return l'image seuillée en niveaux de gris (0 ou 255)
     */
    public static int[][] seuillageSimple(int[][] image, int seuil) {
        int height = image.length;
        int width = image[0].length;
        int[][] binaryImage = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (image[i][j] >= seuil) {
                    binaryImage[i][j] = 255; // Niveau de gris 1 pour le seuillage
                } else {
                    binaryImage[i][j] = 0; // Niveau de gris 0 pour le seuillage
                }
            }
        }

        return binaryImage;
    }

    /**
     * Réalise le seuillage multiple à deux seuils d'une image en niveaux de gris.
     *
     * @param image l'image d'entrée en niveaux de gris
     * @param seuil1 le premier seuil à utiliser pour le seuillage
     * @param seuil2 le deuxième seuil à utiliser pour le seuillage
     * @return l'image seuillée en niveaux de gris (0, 127 ou 255)
     */
    public static int[][] seuillageDouble(int[][] image,int seuil1, int seuil2) {
        int height = image.length;
        int width = image[0].length;
        int[][] grayImage = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (image[i][j] < seuil1) {
                    grayImage[i][j] = 0; // Niveau de gris 0 pour les pixels inférieurs au premier seuil
                } else if (image[i][j] >= seuil1 && image[i][j] < seuil2) {
                    grayImage[i][j] = 127; // Niveau de gris 127 pour les pixels entre les deux seuils
                } else {
                    grayImage[i][j] = 255; // Niveau de gris 255 pour les pixels supérieurs au deuxième seuil
                }
            }
        }

        return grayImage;
    }

    /**
     * Réalise le seuillage automatique d'une image en niveaux de gris selon les étapes suivantes:
     *
     * 1. Choisir un seuil T initial (moyenne, médiane, ...)
     * 2. On obtient alors 2 groupes de pixels : le groupe G1 contenant les pixels d'intensité
     *    supérieure à T et le groupe G2 contenant les pixels d'intensité inférieure à T.
     * 3. Calculer les moyennes des niveaux de gris sur les deux groupes de pixels G1 et G2 ;
     *    ceci fournit les deux moyennes µ1 et µ2.
     * 4. Mettre à jour la valeur du seuil: T = (µ1 + µ2)/2
     *
     * @param image l'image d'entrée en niveaux de gris
     * @return l'image seuillée en niveaux de gris (0 ou 255)
     */
    public static int[][] seuillageAutomatique(int[][] image) {
        int height = image.length;
        int width = image[0].length;

        int seuil = calculerSeuilInitial(image);
        int seuilPrecedent;

        do {
            seuilPrecedent = seuil;

            // Groupes de pixels : G1 (intensité supérieure à T) et G2 (intensité inférieure à T)
            int sommeG1 = 0;
            int nbPixelsG1 = 0;
            int sommeG2 = 0;
            int nbPixelsG2 = 0;

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int pixel = image[i][j];
                    if (pixel > seuil) {
                        sommeG1 += pixel;
                        nbPixelsG1++;
                    } else {
                        sommeG2 += pixel;
                        nbPixelsG2++;
                    }
                }
            }

            int moyenneG1 = sommeG1 / nbPixelsG1;
            int moyenneG2 = sommeG2 / nbPixelsG2;

            seuil = (moyenneG1 + moyenneG2) / 2;

        } while (seuil != seuilPrecedent);

        System.out.println("Seuil calculé: " + seuil);

        return seuillageSimple(image, seuil);
    }

    private static int calculerSeuilInitial(int[][] image) {
        int height = image.length;
        int width = image[0].length;
        int somme = 0;
        int nbPixels = height * width;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                somme += image[i][j];
            }
        }

        return somme / nbPixels;
    }
}

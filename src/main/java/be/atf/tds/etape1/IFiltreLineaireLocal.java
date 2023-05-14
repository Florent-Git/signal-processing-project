package be.atf.tds.etape1;

public interface IFiltreLineaireLocal {
    int[][] filtreMasqueConvolution(int[][] image, int[][] masque);
    int[][] filtreMoyenneur(int[][] image, int tailleMasque);
}

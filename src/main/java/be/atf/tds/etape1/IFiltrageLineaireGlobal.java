package be.atf.tds.etape1;

public interface IFiltrageLineaireGlobal {
    int[][] filtrePasseBasIdeal(int[][] image, int frequenceCoupure);
    int[][] filtrePasseHautIdeal(int[][] image, int frequenceCoupure);
    int[][] filtrePasseBasButterworth(int[][] image, int frequenceCoupure);
    int[][] filtrePasseHautButterworth(int[][] image, int frequenceCoupure);
}

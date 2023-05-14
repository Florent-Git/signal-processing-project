/*
 * MatriceComplexe.java
 *
 * Created on 6 ao�t 2007, 10:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package imageprocessing.Complexe;

import java.util.Arrays;

/**
 *
 * @author HP_Propri�taire
 */
public class MatriceComplexe 
{
    private Complexe  m[][];
    private int       lignes;
    private int       colonnes;
    
    /** Creates a new instance of MatriceComplexe */
    public MatriceComplexe(int l,int c) 
    {
        lignes = l;
        colonnes = c;
        m = new Complexe[l][c];
        for(int i=0 ; i<lignes ; i++)
            for(int j=0 ; j<colonnes ; j++)
                m[i][j] = new Complexe(0.0,0.0);
    }
    
    public void set(int ligne,int colonne,Complexe complexe)
    {
        m[ligne][colonne] = complexe;
    }
    
    public void set(int ligne,int colonne,double partieReelle,double partieImaginaire)
    {
        Complexe c = new Complexe(partieReelle,partieImaginaire);
        set(ligne,colonne,c);
    }
    
    public Complexe get(int ligne,int colonne)
    {
        return m[ligne][colonne];
    }

    public double[][] getPartieReelle()
    {
        double d[][] = new double[lignes][colonnes];
        for(int i=0 ; i<lignes ; i++)
            for(int j=0 ; j<colonnes ; j++)
                d[i][j] = m[i][j].getPartieReelle();
        return d;
    }

    public double[][] getPartieImaginaire()
    {
        double d[][] = new double[lignes][colonnes];
        for(int i=0 ; i<lignes ; i++)
            for(int j=0 ; j<colonnes ; j++)
                d[i][j] = m[i][j].getPartieImaginaire();
        return d;
    }

    public double[][] getModule()
    {
        double d[][] = new double[lignes][colonnes];
        for(int i=0 ; i<lignes ; i++)
            for(int j=0 ; j<colonnes ; j++)
                d[i][j] = m[i][j].getModule();
        return d;
    }
     
    public double[][] getPhase()
    {
        double d[][] = new double[lignes][colonnes];
        for(int i=0 ; i<lignes ; i++)
            for(int j=0 ; j<colonnes ; j++)
                d[i][j] = m[i][j].getPhase();
        return d;
    }
        
    public int getLignes() {
        return lignes;
    }

    public int getColonnes() {
        return colonnes;
    }

    /**
     * Redéfinition des méthodes HASH et EQUALS
     * Généré avec IntelliJ
     * @author Florent
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MatriceComplexe that = (MatriceComplexe) o;

        if (lignes != that.lignes) return false;
        if (colonnes != that.colonnes) return false;
        return Arrays.deepEquals(m, that.m);
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(m);
        result = 31 * result + lignes;
        result = 31 * result + colonnes;
        return result;
    }
}

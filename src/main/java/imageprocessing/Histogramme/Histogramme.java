/*
 * Histogramme.java
 *
 * Created on 23 septembre 2007, 20:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package imageprocessing.Histogramme;

/**
 *
 * @author Jean-Marc
 */
public class Histogramme implements IHistogramme
{
    public static int[] Histogramme256(int mat[][]) {
        int M = mat.length;
        int N = mat[0].length;
        int histo[] = new int[256];
        
        for(int i=0 ; i<256 ; i++) histo[i] = 0;
        
        for(int i=0 ; i<M ; i++)
            for(int j=0 ; j<N ; j++)
                if ((mat[i][j] >= 0) && (mat[i][j]<=255)) histo[mat[i][j]]++;
        
        return histo;
    }

    @Override
    public int minimum(int[][] image) {
        int minimum = 255;
        for (int i = 0; i < image.length; ++i) {
            for (int j = 0; j < image[i].length; ++j) {
                if (image[j][i] < minimum) {
                    minimum = image[j][i];
                }
            }
        }

        return minimum;
    }

    @Override
    public int maximum(int[][] image) {
        int maximum = 0;
        for (int i = 0; i < image.length; ++i) {
            for (int j = 0; j < image[i].length; ++j) {
                if (image[j][i] > maximum) {
                    maximum = image[j][i];
                }
            }
        }

        return maximum;
    }

    @Override
    public int luminance(int[][] image) {
        int sum = 0;

        for (int i = 0; i < image.length; ++i) {
            for (int j = 0; j < image[i].length; ++j) {
                sum += image[j][i];
            }
        }

        return (int) ((1.0 / (image.length * image[0].length)) * ((double) sum));
    }

    @Override
    public double contraste1(int[][] image) {
        double fraction = 1.0 / (image.length * image[0].length);

        double sum = 0;
        int luminance = luminance(image);

        for (int i = 0; i < image.length; ++i) {
            for (int j = 0; j < image[i].length; ++j) {
                sum += Math.pow(image[j][i] - luminance, 2);
            }
        }

        return Math.sqrt(fraction * sum);
    }

    @Override
    public double contraste2(int[][] image) {
        int min = minimum(image);
        int max = maximum(image);
        return (double) (max - min) / (max + min);
    }
}

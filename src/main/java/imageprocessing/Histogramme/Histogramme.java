/*
 * Histogramme.java
 *
 * Created on 23 septembre 2007, 20:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package imageprocessing.Histogramme;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 *
 * @author Jean-Marc
 */
public class Histogramme implements IHistogrammeComputation, IHistogrammeOperations
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

    // Operations

    @Override
    public int[][] rehaussement(int[][] image, Function<Integer, Integer> courbeTonale) {
        var result = new int[image.length][image[0].length];

        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                result[i][j] = courbeTonale.apply(image[i][j]);
            }
        }

        return result;
    }

    @Override
    public Function<Integer, Integer> creerCourbeTonaleLineaireSaturation(int smin, int smax) {
        AtomicReference<Double> m = new AtomicReference<>((double) 255 / (smax - smin)); // get the slope
        AtomicReference<Double> p = new AtomicReference<>(-(m.get()) * smin); // get the offset

        return intensity -> {
            if (intensity < smin)
                return 0;
            if (intensity > smax)
                return 255;
            else
                return (int) Math.round(m.get() * intensity + p.get()); // mx + p
        };
    }

    @Override
    public Function<Integer, Integer> creerCourbeTonaleGamma(double gamma) {
        return intensity -> (int) Math.round(
            Math.pow(
                    (double) intensity / 255,
                    1.0 / gamma
            ) * 255
        );
    }

    @Override
    public Function<Integer, Integer> creerCourbeTonaleNegatif() {
        return intensity -> 255 - intensity;
    }

    @Override
    public Function<Integer, Integer> creerCourbeTonaleEgalisation(int[][] image) {
        var histogrammeImage = Histogramme256(image);
        var histoNormalImage = new double[histogrammeImage.length];

        for (int i = 0; i < histogrammeImage.length; i++) {
            histoNormalImage[i] = (double) histogrammeImage[i] / (image.length * image[0].length);
        }

        var cumulativeHisto = new double[histogrammeImage.length];

        for (int i = 0; i < histoNormalImage.length; i++) {
            cumulativeHisto[i] = histoNormalImage[i];
            for (int j = 0; j < i; j++) {
                cumulativeHisto[i] += histoNormalImage[j];
            }
        }

        return intensity -> (int) (255 * cumulativeHisto[intensity]);
    }
}

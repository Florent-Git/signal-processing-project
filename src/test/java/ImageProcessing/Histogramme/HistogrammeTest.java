package ImageProcessing.Histogramme;

import imageprocessing.Histogramme.Histogramme;
import imageprocessing.Histogramme.IHistogrammeComputation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistogrammeTest {
    private IHistogrammeComputation histogramme;

    @BeforeEach
    public void setup() {
        histogramme = new Histogramme();
    }

    @Test
    public void GivenAnImage_WhenGettingMinimum_ReturnsMinimum() {
        int expectedValue = 4;

        int returnedValue = histogramme.minimum(ImageTest.VALUE);

        assertEquals(expectedValue, returnedValue);
    }

    @Test
    public void GivenAnImage_WhenGettingMaximum_ReturnsMaximum() {
        int expectedValue = 244;
        int returnedValue = histogramme.maximum(ImageTest.VALUE);
        assertEquals(expectedValue, returnedValue);
    }

    @Test
    public void GivenAnImage_WhenGettingLuminance_ReturnsLuminance() {
        int expectedValue = 129;
        int returnedValue = histogramme.luminance(ImageTest.VALUE);
        assertEquals(expectedValue, returnedValue);
    }

    @Test
    public void GivenAnImage_WhenGettingContrast1_ReturnsContrast1() {
        double expectedValue = 71.64882078141224;
        double returnedValue = histogramme.contraste1(ImageTest.VALUE);
        assertEquals(expectedValue, returnedValue, .1);
    }

    @Test
    public void GivenAnImage_WhenGettingContrast2_ReturnsContrast2() {
        double expectedValue = 0.967741935483871;
        double returnedValue = histogramme.contraste2(ImageTest.VALUE);
        assertEquals(expectedValue, returnedValue, .1);
    }
}

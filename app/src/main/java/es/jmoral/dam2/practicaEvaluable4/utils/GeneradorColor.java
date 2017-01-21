package es.jmoral.dam2.practicaEvaluable4.utils;

import com.manolovn.colorbrewer.ColorBrewer;
import com.manolovn.trianglify.generator.color.ColorGenerator;

/**
 * Clase de soporte para trianglify que genera la paleta de colores
 */

public class GeneradorColor implements ColorGenerator {
    private ColorBrewer palette;
    private int index = 0;
    private int[] colors;

    public GeneradorColor(ColorBrewer palette) {
        this.palette = palette;
    }

    public void setCount(int count) {
        colors = palette.getColorPalette(count);
        index = 0;
    }

    @Override
    public int nextColor() {
        int color = colors[index];
        index++;

        return color;
    }
}

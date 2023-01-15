package org.basmat.mapgen;

import org.basmat.cell.data.ECellType;
import org.basmat.cell.util.CubicInterpolation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

class TextureFilterTest {
    @BeforeAll
    public void setupClasses() {
        CubicInterpolation ci = new CubicInterpolation(20);
        BufferedImage noiseGraph = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
    }
    @Test
    public void determineGrassFromColor_AverageRGB_True() {
        assertEquals(ECellType.GRASS, TextureFilter.setupCell(new Color(250, 0, 0)));
    }

    @Test
    public void determineGradientFromColor_AverageRGB_True() {
        assertEquals(ECellType.GRADIENT, TextureFilter.setupCell((new Color(220, 200, 200))));
    }
}
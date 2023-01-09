package org.basmat.mapgen;

import org.basmat.cell.ECellType;

import java.awt.Color;
import java.awt.image.BufferedImage;


public class TextureFilter {
	public static ECellType setupCell(Color colour) {
		if (colour.getRed() > 230 && colour.getGreen() < 10 && colour.getBlue() < 10) { 
			return ECellType.GRASS;
		} else if (colour.getRed() > 230 && colour.getGreen() > 230 && colour.getBlue() < 230) { 
			return ECellType.SAND;
		} else if(colour.getRed() < 8 && colour.getGreen() < 8 && colour.getBlue() < 8) { 
			return ECellType.UNINHABITABLE;
		} else if (colour.getRed() > 200 && colour.getGreen() > 200 && colour.getBlue() > 200) { 
			return ECellType.SEA;
		} else {
			return ECellType.GRADIENT;
		}
	}

	public static void setupColour(BufferedImage noiseGraph, CubicInterpolation ci, int frameSizeX, int frameSizeY) {
		for (int i = 0; i < frameSizeX; i++) {
			for (int j = 0; j < frameSizeY; j++) {
				double noise = ci.noiseGenerator(i, j, 100);
				int alphaValue = (int) Math.floor(noise * 200);

				if (alphaValue < 1) {
					alphaValue = alphaValue * -1; //Flipping negative number
				}

				if (alphaValue < 1) {
					alphaValue = alphaValue * -1;
				}

				if (alphaValue < 30) {
					noiseGraph.setRGB(i, j, new Color(255, 255, 255).getRGB());
				}

				if (alphaValue >= 30 && alphaValue < 60) {
					noiseGraph.setRGB(i, j, new Color(255, 255, 0).getRGB());
				}

				if ((alphaValue >= 60 && alphaValue < 180)) {
					noiseGraph.setRGB(i, j, new Color(255, 0, 0).getRGB());
				}

				if (alphaValue > 180 && alphaValue < 250) {
					noiseGraph.setRGB(i, j, new Color(0, 0, 0).getRGB());
				}
			}
		}
	}
}

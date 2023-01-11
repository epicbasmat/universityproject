package org.basmat.cell;

import org.basmat.mapgen.CubicInterpolation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class CellController {
    private HashMap<Point, NutrientCell> globalNutrientCells;
    private HashMap<Point, SocietyCell> globalSocietyCells;
    private HashMap<Point, WorldCell> globalWorldCells;
    private BufferedImage[] imageCache;
    private CellMatrixPanel cellMatrixPanel;
    private int cellSize;
    private int sizeX;
    private int sizeY;
    public CellController(int sizeX, int sizeY, int cellSize) throws IOException {
        globalNutrientCells = new HashMap<>();
        globalSocietyCells = new HashMap<>();
        globalWorldCells = new HashMap<>();
        this.cellSize = cellSize;
        imageCache = new BufferedImage[ECellType.values().length];
        this.cellMatrixPanel = new CellMatrixPanel(sizeX, sizeY);
        this.cacheCellTextures();


    }

    public void setupGraph() throws IOException {
        /**
         * Phase 1.
         *
         * This phase of the graph setup initializes a bufferedimage of size X and Y, and then applies a range of RGB values
         * to noise calculations called to the bufferedimage
         */
        BufferedImage noiseGraph = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        CubicInterpolation ci = new CubicInterpolation();
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
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
        /**
         * Phase 2.
         *
         * This phase then gets the average of a designated 5x5 square and matches that value to a cell type
         */
        for (int y = 0; y <= 750 - 5; y += 5) {
            for (int z = 0; z <= 750 - 5; z += 5) {
                int average = 0;
                for (int i = y; i < y + cellSize; i++) {
                    for (int j = z; j < z + cellSize; j++) {
                        average += noiseGraph.getRGB(i, j);

                    }
                }
                Color averagedColour = new Color(average /= cellSize * cellSize);
                Point point = new Point(y/cellSize, z/cellSize);
                if (averagedColour.getRed() > 230 &&(averagedColour.getGreen() < 10 &&(averagedColour.getBlue() < 10))) {
                    WorldCell value = new WorldCell(ECellType.GRASS, imageCache[4]);
                    globalWorldCells.put(point, value);
                    cellMatrixPanel.addCellPanel(new CellPanel(5,5, imageCache[4]), point);
                } else if (averagedColour.getRed() > 230 &&(averagedColour.getGreen() > 230 &&(averagedColour.getBlue() < 230))) {
                    WorldCell value = new WorldCell(ECellType.SAND, imageCache[2]);
                    globalWorldCells.put(point, value);
                    cellMatrixPanel.addCellPanel(new CellPanel(5,5, imageCache[2]), point);
                } else if (averagedColour.getRed() < 8 &&(averagedColour.getGreen() < 8 &&(averagedColour.getBlue() < 8))) {
                    WorldCell value = new WorldCell(ECellType.UNINHABITABLE, imageCache[3]);
                    globalWorldCells.put(point, value);
                    cellMatrixPanel.addCellPanel(new CellPanel(5,5, imageCache[3]), point);
                } else if (averagedColour.getRed() > 200 &&(averagedColour.getGreen() > 200 &&(averagedColour.getBlue() > 200))) {
                    WorldCell value = new WorldCell(ECellType.GRASS, imageCache[1]);
                    globalWorldCells.put(point, value);
                    cellMatrixPanel.addCellPanel(new CellPanel(5,5, imageCache[1]), point);
                } else {
                    WorldCell value = new WorldCell(ECellType.GRADIENT, imageCache[5]);
                    globalWorldCells.put(point, value);
                    cellMatrixPanel.addCellPanel(new CellPanel(5,5, imageCache[5]), point);
                }
            }
        }
        /**
         * Phase 3.
         *
         * Add society cells
         */
        /*for (int i = 0; i < 5; i++) {

            ArrayList<Point> tempKeyArr = globalWorldCells.entrySet()
                    .stream()
                    .filter(c ->
                            c.getValue().getCellType() == ECellType.GRASS)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toCollection(ArrayList::new));

            Random r = new Random();
            int randomInt = r.nextInt(tempKeyArr.size());
            Point key = (Point) tempKeyArr.get(randomInt);
            WorldCell worldCell = globalWorldCells.get(key);

            //updateCell(globalWorldCells, globalSocietyCells, new SocietyCell(imageCache[6]), imageCache[6], new Point(1,1), 3);


            int radius = 12;
            int tempRadius = radius;

            for (int y = 0; y <= 180; y = y + 5) {
                int circumferenceX = (int) (radius * Math.cos(y * 3.141519 / 180));
                int circumferenceY = (int) (radius * Math.sin(y * 3.141519 / 180));
                try {
                    for (int b = -circumferenceY; b <= circumferenceY; b++) {
                        for (int a = -circumferenceX; a <= circumferenceX; a++) {
                            Cell tempCell = cellMatrix[b + j][a + k];
                            if (tempCell.getOwner() == "" && tempCell.getCellType().isHabitable()) {
                                tempCell.setOwner("test");
                                tempCell.setTint(0, 1);
                            }
                        }
                    }
                } catch (Exception e) {}
            }
        }*/
    }

    /*5public void removeFromCell(HashMap<Point, ? extends CellData> delete, HashMap<Point, ? extends CellData> replace, Object cell, BufferedImage image, Point point, int cachedTexture) {
        delete.remove(point);
        replace.put(point, cell);
        cellMatrixPanel.getCellPanel((int) point.getX(), (int) point.getY()).updateTexture(imageCache[cachedTexture]);
    }

    public void updateCell(HashMap delete){};*/

    public void cacheCellTextures() throws IOException {
        int index = 0;
        for (ECellType cellType : ECellType.values()) {
            index++;
            imageCache[index] = ImageIO.read(new File(cellType.getPath()));
        }
    }

    public void releaseCellTextureCache() {
        Arrays.stream(imageCache).forEach(Image::flush);
    }

    public void addSocietyCell(Point point) throws IOException {
        globalSocietyCells.put(point, new SocietyCell(imageCache[6]));
    }

    public void addNutrientCell(Point point) throws IOException {
        globalNutrientCells.put(point, new NutrientCell(imageCache[8]));
    }
}

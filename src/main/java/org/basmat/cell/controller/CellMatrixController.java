package org.basmat.cell.controller;

import org.basmat.cell.data.*;
import org.basmat.cell.view.CellMatrixPanel;
import org.basmat.cell.view.CellPanel;
import org.basmat.cell.view.PanelContainer;
import org.basmat.mapgen.CubicInterpolation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class CellMatrixController {

    public static final String output = "output/";
    private CellController[][] cellControllerMatrix;
    private BufferedImage[] imageCache;
    private CellMatrixPanel cellMatrixPanel;
    private CellSubscriber cellSubscriber;
    private UUID uuid;

    public CellMatrixController(int cellMatrixWidth, int cellMatrixHeight) throws IOException {
        uuid = UUID.randomUUID();
        cellMatrixPanel = new CellMatrixPanel(cellMatrixWidth, cellMatrixHeight);
        cellControllerMatrix = new CellController[cellMatrixWidth][cellMatrixHeight];
        cellSubscriber = new CellSubscriber();
        imageCache = new BufferedImage[ECellType.values().length];
        PanelContainer pc = new PanelContainer(cellMatrixPanel);
        initializer();
    }

    public void initializer() {
        BufferedImage graph = new BufferedImage(750, 750, BufferedImage.TYPE_INT_ARGB);
        cacheCellTextures();
        System.out.println("Generating gradient graph");
        setupGradientGraph(-1, graph);
        System.out.println("Applying texture filter");
        applyTextureFilter(graph, 5);
        //System.out.println("Applying foundations");
        //setupSocietyFoundations();
    }

    public void subscribePanelToJPanelMatrix(CellPanel cellView, int x, int y) {
        cellMatrixPanel.addCellPanel(cellView, x, y);
    }

    public void cacheCellTextures() {
        int index = 0;
        System.out.println("Grabbing textures");
        for (ECellType cellType : ECellType.values()) {
            /**
             * - 0 SEA
             * - 1 SAND
             * - 2 UNINHABITABLE
             * - 3 GRASS
             * - 4 GRADIENT
             * - 5 SOCIETYBLOCK
             * - 6 SOCIETYRADIUS
             * - 7 NUTRIENTS
             */
            try {
                if (cellType.getLocalizedName() != null) {
                    imageCache[index] = ImageIO.read(new File(cellType.getPath()));
                    System.out.println(cellType.getPath() + " at index: " + index );
                    index++;
                }
            } catch (Exception e) {
                System.out.println("An error has occurred fetching textures: ");
                System.out.println(e.getMessage());
                System.out.println("Accessing file path: " + cellType.getPath());
            }
        }
        System.out.println("Finished");

    }

    public SocietyCell generateSocietyCell(String name) {
        return new SocietyCell(name);
    }
    public NutrientCell generateNutrientCell() {
        return new NutrientCell(null);
    }
    public NutrientCell generateNutrientCell(SocietyCell owner) {
        return new NutrientCell(owner);
    }
    public WorldCell generateWorldCell(ECellType cellType, SocietyCell owner) {
        return new WorldCell(cellType, owner);
    }
    public WorldCell generateWorldCell(ECellType cellType) {
        return new WorldCell(cellType, null);
    }

    public void setupGradientGraph(int seed, BufferedImage noiseGraph) {
        CubicInterpolation ci = new CubicInterpolation((int)(Math.random()*(10000 - 1+1)+1));
        for (int i = 0; i < noiseGraph.getWidth(); i++) {
            for (int j = 0; j < noiseGraph.getHeight(); j++) {
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
        boolean save = false;
        if (save) {
            try {
                File file = new File(output + uuid.toString() + "-noise_graph" + ".png");
                System.out.println("Saving to: " + file.getAbsolutePath());
                ImageIO.write(noiseGraph, "png", file);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void applyTextureFilter(BufferedImage noiseGraph, int cellSize) {
        for (int x = 0; x <= 150 - 5; x++) {
            for (int y = 0; y <= 150 - 5; y++) {
                int average = 0;
                for (int i = x ; i < x + 5; i++) {
                    for (int j = y; j < y + 5; j++) {
                        average += noiseGraph.getRGB(i * cellSize, j* cellSize);
                    }
                }
                Color averagedColour = new Color(average / (cellSize * cellSize));
                if (averagedColour.getRed() > 230 &&(averagedColour.getGreen() < 10 &&(averagedColour.getBlue() < 10)))
                { //Grass
                    cellControllerMatrix[x][y] = new CellController<>(generateWorldCell(ECellType.GRASS), imageCache[3], this.cellMatrixPanel, x, y);
                } else if (averagedColour.getRed() > 230 &&(averagedColour.getGreen() > 230 &&(averagedColour.getBlue() < 230)))
                { //Sand
                    cellControllerMatrix[x][y] = new CellController<>(generateWorldCell(ECellType.SAND), imageCache[1], this.cellMatrixPanel, x, y);
                } else if (averagedColour.getRed() < 8 &&(averagedColour.getGreen() < 8 &&(averagedColour.getBlue() < 8)))
                { //Uninhabitable
                    cellControllerMatrix[x][y] = new CellController<>(generateWorldCell(ECellType.UNINHABITABLE), imageCache[2], this.cellMatrixPanel, x, y);
                } else if (averagedColour.getRed() > 200 &&(averagedColour.getGreen() > 200 &&(averagedColour.getBlue() > 200)))
                { //Sea
                    cellControllerMatrix[x][y] = new CellController<>(generateWorldCell(ECellType.SEA), imageCache[0], this.cellMatrixPanel, x, y);
                 } else {
                    cellControllerMatrix[x][y] = new CellController<>(generateWorldCell(ECellType.GRADIENT), imageCache[4], this.cellMatrixPanel, x, y);
                }
            }
        }
    }

    public void setupSocietyFoundations() {
        for (int i = 0; i < 20; i++) {
            int j = (int) (Math.random() * (150 - 1 - 1 + 1) + 1);
            int k = (int) (Math.random() * (150 - 1 - 1 + 1) + 1);

            if (cellControllerMatrix[j][k].getCellTypeFromModel() == ECellType.GRASS) {
                SocietyCell societyCell = generateSocietyCell("Test");
                cellSubscriber.addToGlobalSocietyCells(societyCell);
                cellControllerMatrix[j][k] = new CellController<>(societyCell, imageCache[5], this.cellMatrixPanel, j, k);
                int radius = 12;
                int tempRadius = radius;

                for (int y = 0; y <= 180; y = y + 5) {
                    int circumferenceX = (int) (radius * Math.cos(y * 3.141519 / 180));
                    int circumferenceY = (int) (radius * Math.sin(y * 3.141519 / 180));
                    try {
                        for (int b = -circumferenceY; b <= circumferenceY; b++) {
                            for (int a = -circumferenceX; a <= circumferenceX; a++) {
                                /*Cell tempCell = cellMatrix[b + j][a + k];
                                if (tempCell.getOwner() == "" && tempCell.getCellType().isHabitable()) {
                                    tempCell.setOwner("test");
                                    tempCell.setTint(0, 1);
                                }*/
                                WorldCell wc = (WorldCell) cellControllerMatrix[b + j][a + j].getChildCell();
                                if (wc.getOwner() == null && wc.getCellType() == ECellType.GRASS) {
                                    cellControllerMatrix[b + j][a + j].tellViewToTint();
                                    wc.setOwner(societyCell);
                                }
                            }
                        }
                    } catch (Exception e) {}
                }
            } else {
            }
        }
    }
}

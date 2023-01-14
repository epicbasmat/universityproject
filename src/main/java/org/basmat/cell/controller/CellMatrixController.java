package org.basmat.cell.controller;

import org.basmat.cell.data.*;
import org.basmat.cell.view.CellMatrixPanel;
import org.basmat.cell.view.CellPanel;
import org.basmat.cell.view.PanelContainer;
import org.basmat.cell.util.CubicInterpolation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Semaphore;

public class CellMatrixController {

    public static final String output = "output/";
    private CellController[][] cellControllerMatrix;
    //private BufferedImage[] imageCache;
    private CellMatrixPanel cellMatrixPanel;
    private Semaphore s;
    private CellSubscriber cellSubscriber;
    private HashMap<ECellType, BufferedImage> imageCache;
    private UUID uuid;

    public CellMatrixController(int cellMatrixWidth, int cellMatrixHeight) throws IOException, InterruptedException {
        uuid = UUID.randomUUID();
        cellMatrixPanel = new CellMatrixPanel(cellMatrixWidth, cellMatrixHeight);
        cellControllerMatrix = new CellController[cellMatrixWidth][cellMatrixHeight];
        cellSubscriber = new CellSubscriber();
        imageCache = new HashMap<>();
        PanelContainer pc = new PanelContainer(cellMatrixPanel);
        initializer();
    }

    public void initializer() throws InterruptedException {
        BufferedImage graph = new BufferedImage(750, 750, BufferedImage.TYPE_INT_ARGB);
        cacheCellTextures();
        System.out.println("Generating gradient graph");
        setupGradientGraph(-1, graph);
        System.out.println("Applying texture filter");
        applyTextureFilter(graph, 5);
        System.out.println("Applying foundations");
        setupSocietyFoundations();

    }

    public void subscribePanelToJPanelMatrix(CellPanel cellView, int x, int y) {
        cellMatrixPanel.addCellPanel(cellView, x, y);
    }

    public void cacheCellTextures() {
        System.out.println("Grabbing textures");
        for (ECellType cellType : ECellType.values()) {
            try {
                if (cellType.getLocalizedName() != null) {
                    BufferedImage temp = ImageIO.read(new File(cellType.getPath()));
                    BufferedImage texture = new BufferedImage(temp.getWidth(), temp.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    for (int i = 0; i < temp.getWidth(); i++) {
                        for (int j = 0; j < temp.getHeight(); j++) {
                            texture.setRGB(i, j, temp.getRGB(i, j));
                        }
                    }
                    imageCache.put(cellType, texture);
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
        CubicInterpolation ci = new CubicInterpolation((int) (Math.random() * (10000 - 1 + 1) + 1));
        for (int i = 0; i < noiseGraph.getWidth(); i++) {
            for (int j = 0; j < noiseGraph.getHeight(); j++) {
                double noise = ci.noiseGenerator(i, j, 110);
                int alphaValue = (int) Math.floor(noise * 200);
                if (alphaValue <= -220) { //Deep water
                    noiseGraph.setRGB(i, j, new Color(0, 0, 100).getRGB());
                }
                if (alphaValue > -220 && alphaValue < -130) { //Water
                    noiseGraph.setRGB(i, j, new Color(0, 0, 190).getRGB());
                }
                if (alphaValue >= -130 && alphaValue < -60) {//Light water
                    noiseGraph.setRGB(i, j, new Color(0, 0, 255).getRGB());
                }
                if (alphaValue > -60 && alphaValue < -35) { //Beach
                    noiseGraph.setRGB(i, j, new Color(255, 0, 0).getRGB());
                }
                if (alphaValue >= -35 & alphaValue < 130) { //Land
                    noiseGraph.setRGB(i, j, new Color(0, 250, 0).getRGB());
                }
                if (alphaValue >= 130 && alphaValue < 160) { //Base mountain
                    noiseGraph.setRGB(i, j, new Color(0, 200, 0).getRGB());
                }
                if (alphaValue >= 160 && alphaValue < 220) { //Mountain body
                    noiseGraph.setRGB(i, j, new Color(0, 150, 0).getRGB());
                }
                if (alphaValue >= 220) { //Mountain peak
                    noiseGraph.setRGB(i, j, new Color(0, 100, 0).getRGB());
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
                //Calculate average RGB value of a 5x5 grid
                for (int i = x * cellSize; i < x * cellSize + 5; i++) {
                    for (int j = y * cellSize; j < y * cellSize + 5; j++) {
                        average += noiseGraph.getRGB(i, j);
                    }
                }
                //Sets the texture according to the averaged colour's RGB bands
                Color averagedColour = new Color(average / (cellSize * cellSize));
                if ((averagedColour.getBlue() > 0 && averagedColour.getBlue() < 160) && averagedColour.getGreen() < 10 && averagedColour.getRed() < 10) {
                    cellControllerMatrix[x][y] = new CellController<>(generateWorldCell(ECellType.DEEP_WATER), imageCache.get(ECellType.DEEP_WATER), this.cellMatrixPanel, x, y);
                } if ((averagedColour.getBlue() >= 160 && averagedColour.getBlue() < 220)  && averagedColour.getGreen() < 10 && averagedColour.getRed() < 10) {
                    cellControllerMatrix[x][y] = new CellController<>(generateWorldCell(ECellType.WATER), imageCache.get(ECellType.WATER), this.cellMatrixPanel, x, y);
                } if ((averagedColour.getBlue() >= 220)  && averagedColour.getGreen() < 10 && averagedColour.getRed() < 10) {
                    cellControllerMatrix[x][y] = new CellController<>(generateWorldCell(ECellType.LIGHT_WATER), imageCache.get(ECellType.LIGHT_WATER), this.cellMatrixPanel, x, y);
                } if (averagedColour.getRed() > 0) {
                    cellControllerMatrix[x][y] = new CellController<>(generateWorldCell(ECellType.SAND), imageCache.get(ECellType.SAND), this.cellMatrixPanel, x, y);
                } if (averagedColour.getGreen() <= 255 && averagedColour.getGreen() > 210) {
                    cellControllerMatrix[x][y] = new CellController<>(generateWorldCell(ECellType.GRASS), imageCache.get(ECellType.GRASS), this.cellMatrixPanel, x, y);
                } if (averagedColour.getGreen()  <= 210 && averagedColour.getGreen() > 160) {
                    cellControllerMatrix[x][y] = new CellController<>(generateWorldCell(ECellType.MOUNTAIN_BASE), imageCache.get(ECellType.MOUNTAIN_BASE), this.cellMatrixPanel, x, y);
                } if (averagedColour.getGreen()  <= 160 && averagedColour.getGreen() > 120) {
                    cellControllerMatrix[x][y] = new CellController<>(generateWorldCell(ECellType.MOUNTAIN_BODY), imageCache.get(ECellType.MOUNTAIN_BODY), this.cellMatrixPanel, x, y);
                } if (averagedColour.getGreen()  <= 120 && averagedColour.getGreen() > 10) {
                    cellControllerMatrix[x][y] = new CellController<>(generateWorldCell(ECellType.MOUNTAIN_PEAK), imageCache.get(ECellType.MOUNTAIN_PEAK), this.cellMatrixPanel, x, y);
                }
            }
        }
    }

    public void setupSocietyFoundations() {
        for (int i = 0; i < 20; i++) {
            int j = (int) (Math.random() * (145 - 1 - 1 + 1) + 1);
            int k = (int) (Math.random() * (145 - 1 - 1 + 1) + 1);
            if (cellControllerMatrix[j][k].getCellTypeFromModel() == ECellType.GRASS) {
                SocietyCell societyCell = generateSocietyCell("Test");
                cellSubscriber.addToGlobalSocietyCells(societyCell);
                cellMatrixPanel.removeCell(cellControllerMatrix[j][k].getView());
                cellControllerMatrix[j][k] = new CellController<>(societyCell, imageCache.get(ECellType.SOCIETYBLOCK), this.cellMatrixPanel, j, k);
                int radius = 12;
                for (int r = 0; r <= 180; r = r + 5) {
                    int circumferenceX = (int) (radius * Math.cos(r * 3.141519 / 180));
                    int circumferenceY = (int) (radius * Math.sin(r * 3.141519 / 180));
                    try{
                        //Get the coordinate of the circumference of the circle and the diametric coordinate of the circle
                        for (int x = -circumferenceX + j; x < circumferenceX + j; x++) {
                            for (int y = -circumferenceY + k; y < circumferenceY + k; y++) {
                                if (cellControllerMatrix[x][y].getChildCell().getCellType().isHabitable()) {
                                    WorldCell wc = (WorldCell) cellControllerMatrix[x][y].getChildCell();
                                    if (wc.getOwner() == null) {
                                        cellControllerMatrix[x][y].tellViewToTint();
                                    }
                                }
                            }
                        }


                    } catch (Exception e) { }
                }
            }
        }
    }
}

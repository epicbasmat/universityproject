package org.basmat.map.setup;

import org.basmat.map.cellfactory.NutrientCell;
import org.basmat.map.cellfactory.SocietyCell;
import org.basmat.map.cellfactory.WorldCell;
import org.basmat.map.controller.MVBinder;
import org.basmat.map.data.CellDataHelper;
import org.basmat.map.util.CubicInterpolation;
import org.basmat.map.util.ECellType;
import org.basmat.map.util.path.CCL;
import org.basmat.map.view.CellMatrixPanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.UUID;

public class MapSetup {

    private BufferedImage noiseGraph;
    private final HashMap<ECellType, BufferedImage> imageCache;
    private CellDataHelper cellDataHelper;
    private HashMap<Integer, MVBinder<?>> bindingAgent;
    private CellMatrixPanel cellMatrixPanel;

    /**
     *
     * @param cellDataHelper The cellDataHelper that enables cell generation
     * @param bindingAgent The HashMap for binding id's to new MVBinders
     * @param cellMatrixPanel The cellMatrixPanel for rendering new cells.
     */
    public MapSetup(CellDataHelper cellDataHelper, HashMap<Integer, MVBinder<?>> bindingAgent, CellMatrixPanel cellMatrixPanel) {
        this.bindingAgent = bindingAgent;
        this.cellMatrixPanel = cellMatrixPanel;
        imageCache = new HashMap<>();
        noiseGraph = new BufferedImage(750, 750, BufferedImage.TYPE_INT_ARGB);
        this.cellDataHelper = cellDataHelper;
    }

    /**
     * Provides the setup to cache cell textures, must be called for the main class to function.
     */
    private void cacheCellTextures() {
        System.out.println("Grabbing textures");
        for (ECellType cellType : ECellType.values()) {
            try {
                if (cellType.getCellDescription() != null) {
                    BufferedImage temp = ImageIO.read(new File(cellType.getPath()));
                    //A new texture has to be loaded from the original Image.IO read to determine the imagetype to enable alpha transparency.
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

    /**
     * Calls methods appropriate for rendering and setup for the matrix
     * @throws InterruptedException
     */
    public void setupMap() throws InterruptedException {
        BufferedImage graph = new BufferedImage(750, 750, BufferedImage.TYPE_INT_ARGB);
        cacheCellTextures();
        System.out.println("Generating gradient graph");
        setupGradientGraph(-1);
        System.out.println("Applying texture filter");
        setupWorldCells(graph, 5);
        System.out.println("Applying foundations");
        setupSocietyCells(7);
        System.out.println("Applying nutrient cells");
        setupNutrientCells();
    }

    /**
     * This method provides the setup for the noise graph by applying an arbitrary algorithm to a supplied buffered image.
     * @param seed the seed to provide the noise generator
     */
    private void setupGradientGraph(int seed) {
        CubicInterpolation ci = new CubicInterpolation((int) (Math.random() * (10000 - 1 + 1) + 1));
        //For X and Y coordinate in the bufferedimage, apply a noise gradient filter to the x,y coords and set an RGB value applied to the coordinates
        for (int i = 0; i < noiseGraph.getWidth(); i++) {
            for (int j = 0; j < noiseGraph.getHeight(); j++) {
                double noise = ci.noiseGenerator(i, j, 110);
                // times by 200 to easily separate values when determining RGB values
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
    }

    /**
     * Sets up the world cell generation and maps an RGB range to a cell type and texture.
     * @param noiseGraph The noise graph to provide
     * @param cellSize The cell size to provide
     */
    private void setupWorldCells(BufferedImage noiseGraph, int cellSize) {
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
                int id = (int) (Math.random() * (100000000 - 1 + 1) + 1);
                if ((averagedColour.getBlue() > 0 && averagedColour.getBlue() < 160) && averagedColour.getGreen() < 10 && averagedColour.getRed() < 10) { //DEEP_WATER
                    bindingAgent.put(id, cellDataHelper.setCellData(cellDataHelper.generateWorldBinder(ECellType.DEEP_WATER, id, new Point(x, y))));
                } else if ((averagedColour.getBlue() >= 160 && averagedColour.getBlue() < 220)  && averagedColour.getGreen() < 10 && averagedColour.getRed() < 10) { //WATER
                    bindingAgent.put(id, cellDataHelper.setCellData(cellDataHelper.generateWorldBinder(ECellType.WATER, id, new Point(x, y))));
                } else if ((averagedColour.getBlue() >= 220)  && averagedColour.getGreen() < 10 && averagedColour.getRed() < 10) {// LIGHT WATER
                    bindingAgent.put(id, cellDataHelper.setCellData(cellDataHelper.generateWorldBinder(ECellType.LIGHT_WATER, id, new Point(x, y))));
                } else if (averagedColour.getRed() > 0) { //SAND
                    bindingAgent.put(id, cellDataHelper.setCellData(cellDataHelper.generateWorldBinder(ECellType.SAND, id, new Point(x, y))));
                } else if (averagedColour.getGreen() <= 255 && averagedColour.getGreen() > 210) { //GRASS
                    bindingAgent.put(id, cellDataHelper.setCellData(cellDataHelper.generateWorldBinder(ECellType.GRASS, id, new Point(x, y))));
                } else if (averagedColour.getGreen()  <= 210 && averagedColour.getGreen() > 160) { //MOUNTAIN BASE
                    bindingAgent.put(id, cellDataHelper.setCellData(cellDataHelper.generateWorldBinder(ECellType.MOUNTAIN_BASE, id, new Point(x, y))));
                } else if (averagedColour.getGreen()  <= 160 && averagedColour.getGreen() > 120) { //MOUNTAIN BODY
                    bindingAgent.put(id, cellDataHelper.setCellData(cellDataHelper.generateWorldBinder(ECellType.MOUNTAIN_BODY, id, new Point(x, y))));
                } else if (averagedColour.getGreen()  <= 120 && averagedColour.getGreen() > 10) { //MOUNTAIN PEAK
                    bindingAgent.put(id, cellDataHelper.setCellData(cellDataHelper.generateWorldBinder(ECellType.MOUNTAIN_PEAK, id, new Point(x, y))));
                } else {
                    System.out.println("Error: no RGB band could be assigned with value " + averagedColour.getRGB() + "!");
                    System.out.println("RGB Values: " + "[R: " + averagedColour.getRed() + ", B: " + averagedColour.getBlue() + ", G: " + averagedColour.getGreen() + "]"); //Missing texture
                    bindingAgent.put(id, cellDataHelper.setCellData(cellDataHelper.generateWorldBinder(ECellType.MISSING_TEXTURE, id, new Point(x, y))));
                }
            }
        }
    }

    /**
     * This method sets up the society cell generation by spawning society cells at random coordinates and generating an area of effect visualized through tinting all affected cells
     * @param target The amount of society cells to generate
     */
    private void setupSocietyCells(int target) {
        //The counter provides an incrementing integer for each time a successful society creation occurs
        int counter = 0;
        while (counter < target) {
            int j = (int) (Math.random() * (145 - 1 - 1 + 1) + 1);
            int k = (int) (Math.random() * (145 - 1 - 1 + 1) + 1);
            //check if the coordinate that was gotten was an instance of worldcell, has no owner and is of type grass
            if (bindingAgent.get(cellMatrixPanel.getPanel(j, k).getId()).model() instanceof WorldCell newSocietyCell && newSocietyCell.getOwner() == null && newSocietyCell.getECellType() == ECellType.GRASS) {
                int id = (int) (Math.random() * (100000000 - 1 + 1) + 1);
                counter++;
                //Create a new cell that overwrites the cell that currently inhabits the coordinates
                bindingAgent.put(id, cellDataHelper.overwriteCellData(cellDataHelper.generateSocietyBinder(UUID.randomUUID().toString(), id, new Point(j, k))));
                //TODO: ADD MVBINDER TO GLOBAL ARRAY
                //Then get the reference of the society cell just instantiated because i cannot think of a better way to do it right now
                //bindingAgent.get(cellMatrixPanel.getPanel(j, k))
                //cellSubscriber.addToGlobalSocietyCells(societyCell);

                //Sets a tint in the style that is in standard with Java's ColorModel.
                int tint = 0x00009000;
                tint = tint | (int) (Math.random() * 255 - 150) + 150 << 16; //Set red and bit shift 16 places to align it with the red bytes
                tint = tint | (int) (Math.random() * 255 - 150) + 150; //Set blue to align it to blue bytes

                //1 unit of radius is 1 cell
                int radius = 12;
                for (int r = 0; r <= 180; r = r + 5) {
                    int circumferenceX = (int) (radius * Math.cos(r * 3.141519 / 180));
                    int circumferenceY = (int) (radius * Math.sin(r * 3.141519 / 180));
                    try{
                        //Get the coordinate of the circumference of the circle and the diametric coordinate of the circle, and fills in the cells between the two calculated coordinates
                        //TODO: Reject any attempts to generate a society cell if it is within an AOE of another society cell
                        for (int x = -circumferenceX + j; x < circumferenceX + j; x++) {
                            for (int y = -circumferenceY + k; y < circumferenceY + k; y++) {
                                //Checks if retrieved value mapped to panelobject is instance of worldcell, owner is not null and is habitable
                                if (bindingAgent.get(cellMatrixPanel.getPanel(j, k).getId()).model() instanceof WorldCell worldCell && worldCell.getOwner() == null && worldCell.getECellType().isHabitable()) {
                                    //Set the owner of the cell, provided the cell has no owner and is habitable
                                    cellMatrixPanel.getPanel(x, y).setTint(tint);
                                    worldCell.setOwner((SocietyCell) bindingAgent.get(id).model());
                                }
                            }
                        }
                    } catch (Exception e) {/* System.out.println(e.getMessage());*/ }
                }
            }
        }
    }

    /**
     * This method sets up nutrient cell generation, using a similar way of society cell generation.
     */
    private void setupNutrientCells() {
        for (int i = 0; i < 100; i++) {
            int j = (int) (Math.random() * (145 - 1 - 1 + 1) + 1);
            int k = (int) (Math.random() * (145 - 1 - 1 + 1) + 1);
            int id = (int) (Math.random() * (100000000 - 1 + 1) + 1);
            if (bindingAgent.get(cellMatrixPanel.getPanel(j, k).getId()).model() instanceof WorldCell worldCell && worldCell.getECellType() == ECellType.GRASS) {
                //Necessary administration handling, adding to the global pool, removing the current cell and setting the new cell
                cellDataHelper.overwriteCellData(cellDataHelper.generateNutrientBinder(worldCell.getOwner(), id, new Point(j, k)));
                //TODO: ADD MVBINDER TO GLOBAL ARRAY
                //cellSubscriber.addToGlobalNutrientCells(nutrientCell);
            }
        }
    }
}

package org.basmat.cell.controller;

import org.basmat.cell.data.*;
import org.basmat.cell.view.CellMatrixPanel;
import org.basmat.cell.view.CellPanel;
import org.basmat.cell.util.CubicInterpolation;
import org.basmat.cell.view.PanelContainer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class CellMatrixController {

    public static final String output = "output/";
    //private BufferedImage[] imageCache;
    private CellMatrixPanel cellMatrixPanel;
    private CellSubscriber cellSubscriber;
    private HashMap<ECellType, BufferedImage> imageCache;
    private HashMap<CellPanel, ? super CellData> mapViewToModel;
    private UUID uuid;

    /**
     * CellMatrixController provides the Controller aspect of the implemented M-C-V setup. This will also help control individual cell controllers.
     * @param cellMatrixWidth the width of the cellMatrix to be generated
     * @param cellMatrixHeight the height of the cellMatrix to be generated
     * @throws IOException
     * @throws InterruptedException
     */
    public CellMatrixController(int cellMatrixWidth, int cellMatrixHeight) throws IOException, InterruptedException {
        //Initializing class-wide variables. 2
        uuid = UUID.randomUUID();
        cellMatrixPanel = new CellMatrixPanel(cellMatrixWidth, cellMatrixHeight, this);
        cellSubscriber = new CellSubscriber();
        imageCache = new HashMap<>();
        //cellDataMap maps the view of cellpanel to the model that it renders
        mapViewToModel = new HashMap<>();
        PanelContainer pc = new PanelContainer(cellMatrixPanel);
        initializer();
    }


    /**
     * Calls methods appropriate for rendering and setup for the matrix
     * @throws InterruptedException
     */
    public void initializer() throws InterruptedException {
        BufferedImage graph = new BufferedImage(750, 750, BufferedImage.TYPE_INT_ARGB);
        cacheCellTextures();
        System.out.println("Generating gradient graph");
        setupGradientGraph(-1, graph);
        System.out.println("Applying texture filter");
        setupWorldCells(graph, 5);
        System.out.println("Applying foundations");
        setupSocietyCells();
        System.out.println("Applying nutrient cells");
        //setupNutrientCells();

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

    /**
     * This method provides the setup for the noise graph by applying an arbitrary algorithm to a supplied buffered image.
     * @param seed the seed to provide the noise generator
     * @param noiseGraph the BufferedImage to apply the noise to
     */
    private void setupGradientGraph(int seed, BufferedImage noiseGraph) {
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

    /**
     * This methods sets up the world cell generation. A generated noise graph is provided alongside the X or Y size of the cell.
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
                if ((averagedColour.getBlue() > 0 && averagedColour.getBlue() < 160) && averagedColour.getGreen() < 10 && averagedColour.getRed() < 10) { //DEEP_WATER
                    setCellData(x, y, ECellType.DEEP_WATER);
                } else if ((averagedColour.getBlue() >= 160 && averagedColour.getBlue() < 220)  && averagedColour.getGreen() < 10 && averagedColour.getRed() < 10) { //WATER
                    setCellData(x, y, ECellType.WATER);
                } else if ((averagedColour.getBlue() >= 220)  && averagedColour.getGreen() < 10 && averagedColour.getRed() < 10) {// LIGHT WATER
                    setCellData(x, y, ECellType.LIGHT_WATER);
                } else if (averagedColour.getRed() > 0) { //SAND
                    setCellData(x, y, ECellType.SAND);
                } else if (averagedColour.getGreen() <= 255 && averagedColour.getGreen() > 210) { //GRASS
                    setCellData(x, y, ECellType.GRASS);
                } else if (averagedColour.getGreen()  <= 210 && averagedColour.getGreen() > 160) { //MOUNTAIN BASE
                    setCellData(x, y, ECellType.MOUNTAIN_BASE);
                } else if (averagedColour.getGreen()  <= 160 && averagedColour.getGreen() > 120) { //MOUNTAIN BODY
                    setCellData(x, y, ECellType.MOUNTAIN_BODY);
                } else if (averagedColour.getGreen()  <= 120 && averagedColour.getGreen() > 10) { //MOUNTAIN PEAK
                    setCellData(x, y, ECellType.MOUNTAIN_PEAK);
                } else {
                    System.out.println("Error: no RGB band could be assigned with value " + averagedColour.getRGB() + "!");
                    System.out.println("RGB Values: " + "[R: " + averagedColour.getRed() + ", B: " + averagedColour.getBlue() + ", G: " + averagedColour.getGreen() + "]"); //Missing texture
                    setCellData(x, y, ECellType.MISSING_TEXTURE);
                }
            }
        }
    }

    /**
     * Deletes the current rendered panel in the class's instantiated cellMatrixPanel. Calls setCellData() after to set the new data
     * @param x the x coordinate to delete and replace
     * @param y the y coordinate to delete and replace
     * @param cellType the cellType to replace
     */
    private void overwriteCellData(int x, int y, ECellType cellType) {
        cellMatrixPanel.removeCell(cellMatrixPanel.getPanel(x, y));
        setCellData(x, y, cellType);
    }

    /**
     * Sets up a new JPanel for a cell and instantiates a new model, linking the two with an internal HashMap
     * @param x the x coordinate to set in the matrix
     * @param y the y coordinate to set in the matrix
     * @param cellType the celltype to instantiate and set in the matrix.
     */
    private void setCellData(int x, int y, ECellType cellType) {
        CellPanel cellPanel = new CellPanel(imageCache.get(cellType));
        cellMatrixPanel.addCellPanel(cellPanel, x, y);
        mapViewToModel.put(cellPanel, new WorldCell(cellType));
    }

    /**
     * This method sets up the society cell generation.
     */
    private void setupSocietyCells() {
        //i = each iteration has a chance to spawn, so ideal spawn rate * 2 for i
        //TODO: change to while loop for minimum amount to spawn
        for (int i = 0; i < 20; i++) {
            int j = (int) (Math.random() * (145 - 1 - 1 + 1) + 1);
            int k = (int) (Math.random() * (145 - 1 - 1 + 1) + 1);
            //get the panel from the matrix panel coordinates and then apply that to the map
            if (mapViewToModel.get(cellMatrixPanel.getPanel(j, k)) == ECellType.GRASS) {
                SocietyCell societyCell = generateSocietyCell(UUID.randomUUID().toString());
                //Add to cell subscriber and delete the cell that it is going to overwrite
                //Necessary administration handling, adding to the global pool, removing the current cell and setting the new cell
                cellSubscriber.addToGlobalSocietyCells(societyCell);
                overwriteCellData(j, k, ECellType.SOCIETYBLOCK);
                int radius = 12;
                for (int r = 0; r <= 180; r = r + 5) {
                    int circumferenceX = (int) (radius * Math.cos(r * 3.141519 / 180));
                    int circumferenceY = (int) (radius * Math.sin(r * 3.141519 / 180));
                    try{
                        //Get the coordinate of the circumference of the circle and the diametric coordinate of the circle, and fills in the cells between the two calculated coordinates
                        //TODO: Reject any attempts to generate a society cell if it is within an AOE of another society cell
                        for (int x = -circumferenceX + j; x < circumferenceX + j; x++) {
                            for (int y = -circumferenceY + k; y < circumferenceY + k; y++) {
                                //checking if the cell is habitable
                                WorldCell worldCell = (WorldCell) mapViewToModel.get(cellMatrixPanel.getPanel(x, y));
                                if (worldCell.getCellType().isHabitable()) {
                                    //Set the owner of the cell, provided the cell has no owner
                                    if (worldCell.getOwner() == null) {
                                        cellMatrixPanel.getPanel(x,y).setTint(0x0090cc90);
                                        worldCell.setOwner(societyCell);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) { }
                }
            }
        }
    }

    /**
     * This method sets up nutrient cell generation, using a similar way of society cell generation.
     */
    /*private void setupNutrientCells() {
        for (int i = 0; i < 100; i++) {
            int j = (int) (Math.random() * (145 - 1 - 1 + 1) + 1);
            int k = (int) (Math.random() * (145 - 1 - 1 + 1) + 1);
            if (cellControllerMatrix[j][k].getCellTypeFromModel() == ECellType.GRASS) {
                //Initial cast is necessary to get owner.
                WorldCell wc = (WorldCell) cellControllerMatrix[j][k].getChildCell();
                SocietyCell sc = wc.getOwner();
                NutrientCell nutrientCell;
                //Checks to make sure nutrient cell generation will have owner, and if so provides the owner as an argument
                if (sc != null) {
                    nutrientCell = new NutrientCell(sc);
                } else {
                    nutrientCell = new NutrientCell();
                }
                //Necessary administration handling, adding to the global pool, removing the current cell and setting the new cell
                cellSubscriber.addToGlobalNutrientCells(nutrientCell);
                cellMatrixPanel.removeCell(cellControllerMatrix[j][k].getView());
                cellControllerMatrix[j][k] = new CellController<>(nutrientCell, imageCache.get(ECellType.NUTRIENTS), this.cellMatrixPanel, j, k);
            }
        }
    }*/

    /**
     * Provides a temporary method for viewing data requested by the cell matrix view.
     * @param e the MouseEvent that the cell captures
     */
    public void displayData(MouseEvent e) {
        //Weird subtractions are necessary to align click co-ordinate with cell matrix coordinate
        int x = (int) e.getPoint().getX() / 5 - 27;
        int y = (int) e.getPoint().getY() / 5 - 29;
        System.out.println("==");
        System.out.println(x + ", " + y);
        System.out.println(mapViewToModel.get(cellMatrixPanel.getPanel(x, y)).toString());
    }
}

package org.basmat.map.controller.rules;


import org.basmat.map.controller.Controller;
import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.LifeCell;
import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.util.ECellType;
import org.basmat.map.util.PointUtilities;
import org.basmat.map.util.path.Node;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * The winnower class provides methods for death. Whereas the gardener provides methods for birth, survival, the Winnower provides how the cells will die, such as lack of food or fighting.
 */
/*
 "The winnower reaped the day's crop, and separated what would flourish from what had failed."
 */
public class Winnower {
    private final Controller controller;
    private final ModelStructure modelStructure;
    private final LinkedList<Point> globalSocietyCellList;
    private final ArrayList<Point> globalLifeCellList;
    private final ArrayList<LinkedList<Node>> listOfPaths;

    public Winnower(Controller controller, ModelStructure modelStructure, LinkedList<Point> globalSocietyCellList, ArrayList<Point> globalLifeCellList, ArrayList<LinkedList<Node>> listOfPaths) {
        this.controller = controller;
        this.modelStructure = modelStructure;
        this.globalSocietyCellList = globalSocietyCellList;
        this.globalLifeCellList = globalLifeCellList;
        this.listOfPaths = listOfPaths;
    }

    /**
     * This method goes through all life cells and assesses the area around the life cell. If the amount of life cells near it is greater
     * than the LifeCell threshold then the life cell is suffocated or overcrowded.
     */
    public void overcrowded() {
        List<Point> copyOfList = new LinkedList<>(globalLifeCellList);
        for (Point lifeCell : copyOfList) {
            int overcrowd = 0;
            for (Point coords : PointUtilities.getAllValidatedNeighbours(lifeCell)) {
                if (modelStructure.getCoordinate(coords).getECellType() == ECellType.LIFE_CELL) {
                    overcrowd++;
                }
            } if (overcrowd > controller.getSimulationProperties().overcrowdThreshold()) {
                controller.pushText("A life cell has been killed from overcrowding!");
                kill(lifeCell);
            }
        }
    }

    /**
     * Famine goes through each society cell and evaluates the nutrient capacity to population ratio against the food threshold.
     * If the food threshold is exceeded then a LifeCell is killed as there is not enough food to go around to sustain all LifeCells.
     */
    public void famine() {
        for (Point point : globalSocietyCellList) {
            SocietyCell coordinate = modelStructure.getCoordinate(point);
            int population = coordinate.getPopulationCount();
            int nutrientCapacity = coordinate.getNutrientCapacity();
            //If the ratio of nutrient to population goes below 0.6, i.e. food cannot be split much more than 33% below then a random cell cannot be fed and will die
            if (nutrientCapacity / population < controller.getSimulationProperties().foodThreshold()) {
                //get all cells allocated to a society cell and put them in a list, then randomly kill one
                List<Point> points = globalLifeCellList.parallelStream().filter(p -> modelStructure.getCoordinate(p) instanceof LifeCell lifeCell && lifeCell.getSocietyCell() == point).toList();
                kill(points.get((int) (Math.random() * points.size())));
                controller.pushText("A death has occurred due to starvation!");
            }
        }
    }


    /**
     * Stagnation goes through each life cell and looks at each life cell and determines if it dies to attrition. If the cell hits
     * the attrition threshold then it is killed as it has not been visited by an amount of steps determined by the user.
     */
    public void stagnation() {
        List<Point> copyOfList = new LinkedList<>(globalLifeCellList);
        for (Point point : copyOfList) {
            LifeCell lifeCell = modelStructure.getCoordinate(point);
            boolean hasPartner = false;
            for (Point partner : PointUtilities.getAllValidatedNeighbours(point)) {
                if (modelStructure.getCoordinate(partner) instanceof LifeCell ) {
                    lifeCell.resetAttrition();
                    hasPartner = true;
                    break;
                }
            }
            if (!hasPartner) {
                lifeCell.incrementAttrition();
                if (lifeCell.getAttrition() > controller.getSimulationProperties().attritionThreshold()) {
                    kill(point);
                    controller.pushText("A death cells has occurred because it has not had any other cells visit it");
                }
            }
        }
    }

    /**
     * This method goes through each society cell and evaluates the land mass / life cell ratio against the threshold to collapse the society.
     * If the society exceeds the threshold designated by the user, the society collapses with each Life Cell being killed in the society and the area being de-tinted
     */
    public void collapse() {
        List<Point> copyOfList = new LinkedList<>(globalSocietyCellList);
        for (Point point : copyOfList) {
            SocietyCell coordinate = modelStructure.getCoordinate(point);
            if (coordinate.getLandPerLifeCell() > controller.getSimulationProperties().ratioThreshold() || coordinate.getPopulationCount() == 0) {
                controller.pushText("Society collapsed, the population was overstretched too much over the land it had at:  " + point);
                globalLifeCellList.parallelStream().filter(p -> modelStructure.getCoordinate(p) instanceof LifeCell lifeCell && lifeCell.getSocietyCell() == point).toList().forEach(this::kill);
                PointUtilities.resetArea(coordinate.getRadius(), point, modelStructure);
                modelStructure.deleteFrontLayer(point);
                globalSocietyCellList.remove(point);
            }
        }
    }


    /**
     * Removes all instances of the lifecell from the simulation.
     * @param lifeCell The life cell to remove
     */
    public void kill(Point lifeCell) {
        //To remove every instance that the LifeCell has, we need to remove the coordinate references from
        // - the ModelStructure
        // - The global list of life cells
        // - the List of Paths
        ((SocietyCell) modelStructure.getCoordinate(((LifeCell) modelStructure.getCoordinate(lifeCell)).getSocietyCell())).killCell();
        modelStructure.deleteFrontLayer(lifeCell);
        globalLifeCellList.remove(lifeCell);
        List<LinkedList<Node>> elementsToRemove = listOfPaths.parallelStream().filter(Objects::nonNull).filter(e -> {
            assert e.peek() != null;
            return e.peek().point().equals(lifeCell);
        }).toList();
        listOfPaths.removeAll(elementsToRemove);
    }
}

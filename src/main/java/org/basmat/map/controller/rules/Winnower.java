package org.basmat.map.controller.rules;


import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.LifeCell;
import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.util.ECellType;
import org.basmat.map.util.PointUtilities;
import org.basmat.map.util.path.Node;
import org.basmat.map.view.UI;
import org.basmat.map.view.ViewStructure;

import java.awt.*;
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

    private final UI ui;
    private ModelStructure modelStructure;
    private LinkedList<Point> globalSocietyCellList;
    private LinkedList<Point> globalLifeCellList;
    private LinkedList<LinkedList<Node>> listOfPaths;

    public Winnower(UI ui, ModelStructure modelStructure, LinkedList<Point> globalSocietyCellList, LinkedList<Point> globalLifeCellList, LinkedList<LinkedList<Node>> listOfPaths) {
        this.ui = ui;
        this.modelStructure = modelStructure;
        this.globalSocietyCellList = globalSocietyCellList;
        this.globalLifeCellList = globalLifeCellList;
        this.listOfPaths = listOfPaths;
    }

    public void overcrowded() {
        List<Point> copyOfList = new LinkedList<>(globalLifeCellList);
        for (Point lifeCell : copyOfList) {
            int overcrowd = 0;
            for (Point coords : PointUtilities.getAllValidatedNeighbours(lifeCell)) {
                if (modelStructure.getCoordinate(coords).getECellType() == ECellType.LIFE_CELL) {
                    overcrowd++;
                }
            } if (overcrowd > 4) {
                ui.appendText("A life cell has been killed from overcrowding!");
                kill(lifeCell);
            }
        }
    }

    public void famine() {
        for (Point point : globalSocietyCellList) {
            SocietyCell coordinate = modelStructure.getCoordinate(point);
            int population = coordinate.getPopulationCount();
            int nutrientCapacity = coordinate.getNutrientCapacity();
            //If the ratio of nutrient to population goes below 0.6, i.e food cannot be split much more than 33% below then a random cell cannot be fed and will die
            if (nutrientCapacity / population < 0.6) {
                List<Point> points = globalLifeCellList.parallelStream().filter(p -> modelStructure.getCoordinate(p) instanceof LifeCell lifeCell && lifeCell.getSocietyCell() == point).toList();
                kill(points.get((int) (Math.random() * points.size())));
                ui.appendText("A death has occurred due to starvation!");
            }
        }
    }


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
                if (lifeCell.getAttrition() > 20) {
                    kill(point);
                    ui.appendText("A death cells has occurred because it has not had any other cells visit it");
                }
            }
        }
    }

    public void collapse() {
        List<Point> copyOfList = new LinkedList<>(globalSocietyCellList);
        for (Point point : copyOfList) {
            SocietyCell coordinate = modelStructure.getCoordinate(point);
            if (coordinate.getLandPerLifeCell() > 75) {
                ui.appendText("Society collapsed, the population was overstretched too much over the land it had at:  " + point);
                globalLifeCellList.parallelStream().filter(p -> modelStructure.getCoordinate(p) instanceof LifeCell lifeCell && lifeCell.getSocietyCell() == point).toList().forEach(this::kill);
                PointUtilities.resetArea(coordinate.getRadius(), point, modelStructure);
                modelStructure.deleteCoordinate(point);
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
        // - the List of Paths
        ((SocietyCell) modelStructure.getCoordinate(((LifeCell) modelStructure.getCoordinate(lifeCell)).getSocietyCell())).killCell();
        modelStructure.deleteCoordinate(lifeCell);
        globalLifeCellList.remove(lifeCell);
        List<LinkedList<Node>> elementsToRemove = listOfPaths.parallelStream().filter(Objects::nonNull).filter(e -> {
            assert e.peek() != null;
            return e.peek().point().equals(lifeCell);
        }).toList();
        listOfPaths.removeAll(elementsToRemove);
    }
}

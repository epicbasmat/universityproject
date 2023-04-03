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
    private LinkedList<Point> globalNutrientCellList;
    private LinkedList<Point> globalSocietyCellList;
    private LinkedList<Point> globalLifeCellList;
    private LinkedList<LinkedList<Node>> listOfPaths;

    public Winnower(UI ui, ModelStructure modelStructure, LinkedList<Point> globalNutrientCellList, LinkedList<Point> globalSocietyCellList, LinkedList<Point> globalLifeCellList, LinkedList<LinkedList<Node>> listOfPaths) {
        this.ui = ui;
        this.modelStructure = modelStructure;
        this.globalNutrientCellList = globalNutrientCellList;
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

    public void collapse() {
        List<Point> copyOfList = new LinkedList<>(globalSocietyCellList);
        for (Point point : copyOfList) {
            SocietyCell coordinate = modelStructure.getCoordinate(point);
            if (coordinate.getLandPerLifeCell() > 75) {
                ui.appendText("Society collapsed, too much land for people at " + point);
                globalLifeCellList.parallelStream().filter(p -> modelStructure.getCoordinate(p) instanceof LifeCell lifeCell && lifeCell.getSocietyCell() == point).toList().forEach(this::kill);
                PointUtilities.resetArea(coordinate.getRadius(), point, modelStructure);
                modelStructure.deleteCoordinate(point);
                globalSocietyCellList.remove(point);
            }
        }
    }


    /**
     * Removes all instances of the lifecell from the simulation.
     * @param lifeCell
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

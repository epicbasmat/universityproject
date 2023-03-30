package org.basmat.map.controller.rules;


import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.LifeCell;
import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.util.ECellType;
import org.basmat.map.util.PointUtilities;
import org.basmat.map.util.path.Node;
import org.basmat.map.view.ViewStructure;
import org.checkerframework.checker.units.qual.C;

import java.awt.*;
import java.util.Iterator;
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

    private ViewStructure viewStructure;
    private ModelStructure modelStructure;
    private LinkedList<Point> globalNutrientCellList;
    private LinkedList<Point> globalSocietyCellList;
    private LinkedList<Point> globalLifeCellList;
    private LinkedList<LinkedList<Node>> listOfPaths;

    public Winnower(ViewStructure viewStructure, ModelStructure modelStructure, LinkedList<Point> globalNutrientCellList, LinkedList<Point> globalSocietyCellList, LinkedList<Point> globalLifeCellList, LinkedList<LinkedList<Node>> listOfPaths) {
        this.viewStructure = viewStructure;
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
                System.out.println("A life cell has been killed from overcrowding!");
                kill(lifeCell);
            }
        }
    }

    public void collapse() {
        for (Iterator<Point> iterator = globalSocietyCellList.iterator(); iterator.hasNext();) {
            Point next = iterator.next();
            SocietyCell coordinate = modelStructure.getCoordinate(next);
            if (coordinate.getLandPerLifeCell() > 75) {
                System.out.println("Society collapsed, too much land for people!");
                PointUtilities.untintArea(coordinate.getRadius(), next, modelStructure);
                coordinate.getLifeCells().stream().toList().forEach(this::kill);
                modelStructure.deleteCoordinate(next);
                globalSocietyCellList.remove(next);
            }
        }
    }


    /**
     * Removes all instances of the lifecell from the simulation.
     * @param lifeCell
     */
    public void kill(Point lifeCell) {
        //To remove every instance that the LifeCell has, you need to remove the coordinate references from
        // - the ModelStructure
        // - the List of Paths
        ((SocietyCell) modelStructure.getCoordinate(((LifeCell) modelStructure.getCoordinate(lifeCell)).getSocietyCell())).killCell(lifeCell);
        modelStructure.deleteCoordinate(lifeCell);
        globalLifeCellList.remove(lifeCell);
        List<LinkedList<Node>> elementsToRemove = listOfPaths.parallelStream().filter(Objects::nonNull).filter(e -> {
            assert e.peek() != null;
            return e.peek().point().equals(lifeCell);
        }).toList();
        listOfPaths.removeAll(elementsToRemove);
    }
}

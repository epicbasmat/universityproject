package org.basmat.map.controller.rules;

import org.basmat.map.controller.Controller;
import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.LifeCell;
import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.model.cells.factory.CellFactory;
import org.basmat.map.util.ECellType;
import org.basmat.map.util.PointUtilities;
import org.basmat.map.util.path.Node;
import org.basmat.map.util.path.Pathfind;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * The gardener rule set provides methods into life. The rules govern how the life cells will recreate, as well as attempts to survive, such as scavenge if food resources are low.
 */
/*
 In the morning, the gardener pushed the seeds down into the wet loam of the garden to see what they would become
 */
public class
Gardener {
    private final LinkedList<Point> globalSocietyCellList;
    private final ArrayList<Point> globalLifeCellList;

    //Update
    private final ArrayList<LinkedList<Node>> listOfPaths;

    private final Controller controller;
    private final ModelStructure modelStructure;
    private CellFactory cellFactory;

    public Gardener(Controller controller, ModelStructure modelStructure, LinkedList<Point> globalSocietyCellList, ArrayList<Point> globalLifeCellList, ArrayList<LinkedList<Node>> listOfPaths) {
        this.controller = controller;
        this.modelStructure = modelStructure;
        this.globalSocietyCellList = globalSocietyCellList;
        this.globalLifeCellList = globalLifeCellList;
        this.listOfPaths = listOfPaths;
        cellFactory = new CellFactory();
    }

    /**
     * Expands the area of influence for the society cell if the amount of life cells in the society cell has gone up by a certain amount.
     */
    public void expand() {
        for (Point societyCellPoint : globalSocietyCellList) {
            SocietyCell societyCell = modelStructure.getCoordinate(societyCellPoint);
            //If the life cell count is divisable by six and the previous capacity is not the same as the current capacity, expand the borders
            if (societyCell.getPreviousExpansionQuotient() < societyCell.getPopulationCount()/6) {
                societyCell.setRadius(societyCell.getRadius() + 2);
                societyCell.setPreviousExpansionQuotient(societyCell.getPopulationCount() / 6);
                PointUtilities.tintArea(societyCell.getRadius(), societyCellPoint, societyCell.getTint(), modelStructure);
                controller.pushText(societyCell.getName() + " just expanded!");
            }
        }
    }

    /**
     * Tells any cells with a certain reproduction cool down to go to a randomized coordinate with it's SocietyCell area of influence to prevent overcrowding.
     */
    public void scatter() {
        //System.out.println("Scattering");
        for (Point lifeCellPoint : globalLifeCellList) {
            LifeCell lifeCell = modelStructure.getCoordinate(lifeCellPoint);
            SocietyCell societyCell = modelStructure.getCoordinate(lifeCell.getSocietyCell());
            if (lifeCell.getReproductionCooldown() == 9) {
                //If the lifecell has recently reproduced, we want it to scatter to avoid overcrowding. To assert this, we want to check if it currently has any paths to follow, if so remove it and replace it with the scatter function
                Point destination = PointUtilities.calculateRandomValidCoordinates(lifeCell.getSocietyCell(), societyCell.getRadius(), modelStructure, List.of(new ECellType[]{ECellType.GRASS, ECellType.SAND}));
                LinkedList<Node> value = Pathfind.aStar(250, modelStructure, lifeCellPoint, destination);
                if (!value.isEmpty()) {
                    //Remove any current paths if the life cell has one as to avoid conflicts.
                    List<LinkedList<Node>> elementsToRemove = listOfPaths.parallelStream().filter(Objects::nonNull).filter(e -> {
                        assert e.peek() != null;
                        return e.peek().equals(value.peek());
                    }).toList();
                    listOfPaths.removeAll(elementsToRemove);
                    listOfPaths.add(value);
                }
            }
        }
    }

    /**
     * Scans all life cells and their area to see if they have a partner, if they do then try to generate a new life cell.
     */
    public void checkForValidReproduction() {
        //Copy list to prevent concurrency exceptions
        List<Point> copyOfList = new LinkedList<>(globalLifeCellList);
        for (Point lifeCellPoint : copyOfList) {
            LifeCell parent1 = modelStructure.getCoordinate(lifeCellPoint);
            //Get a 3x3 area from the origin of the cell and see if there are any life cell to reproduce with
            List<Point> allValidatedNeighbours = PointUtilities.getImmediateValidatedNeighbours(lifeCellPoint);
            for (Point parent2 : allValidatedNeighbours) {
                if (modelStructure.getCoordinate(parent2) instanceof LifeCell lifeCell && lifeCell.getReproductionCooldown() == 0 && parent1.getReproductionCooldown() == 0) {
                    Point newLifeCell;
                    //for the little list surrounding the cell, try to generate a new life cell, however if the break condition is reached (to prevent hard lock) then we cannot create one
                    int breakcnd = 0;
                    do {
                        newLifeCell = allValidatedNeighbours.get(((int) (Math.random() * allValidatedNeighbours.size())));
                        breakcnd++;
                        //Repeat if there is no valid spawn place -- try to find one within 4 attempts or failure
                    } while (!(modelStructure.getCoordinate(newLifeCell).getECellType().isPathable()) && breakcnd < 4);/* && !(worldCell.getECellType().isHabitable())*/;
                    if (breakcnd == 4) {
                        controller.pushText("A life cell failed to be created! Too many cells in the surrounding area.");
                        continue;
                    }
                    //If horrible things haven't happened:
                    //Create a new life cell and add it to the global array, and add it to the model
                    modelStructure.setFrontLayer(newLifeCell, cellFactory.createLifeCell(lifeCell.getSocietyCell()));
                    globalLifeCellList.add(newLifeCell);
                    parent1.resetReproductionCooldown();
                    ((LifeCell) modelStructure.getCoordinate(parent2)).resetReproductionCooldown();
                    ((LifeCell) modelStructure.getCoordinate(newLifeCell)).resetReproductionCooldown();
                    controller.pushText("A new life cell has been created!");
                    ((SocietyCell) modelStructure.getCoordinate(parent1.getSocietyCell())).addLifeCells();
                    break;
                }
            }
            parent1.decrementReproductionCooldown();
        }
    }

    /**
     * This method manages any active life cells currently moving around. For each life cell path in the List containing all current paths, the head is removed from the path and the model and view structures are updated to reflect the LifeCell's
     * current position.
     */
    public void unison() {
        //Concurrency exception dodging
        LinkedList<LinkedList<Node>> copyOfList = new LinkedList<>(listOfPaths);
        //For each society cell that has an active path
        for (LinkedList<Node> value : copyOfList){
            //Get the LinkedList containing all nodes from point A to point B
            //All passed paths should have a minimum of a head, if only a head is present then it will be removed and the loop continued to prevent overwriting
            Node current = value.remove();

            if (value.isEmpty()) {
                listOfPaths.remove(value);
                continue;
            }

            Node toMoveTo = value.peek();

            //For each movement, we need to evaluate if there has been a model change since the initial pathfind. If there has been a change, we need to regenerate the path
            //We can guarantee that the life cell traversing will never overwrite a cell that it's moving to, as any invalid cells would have been avoided at the time of path generation,
            //as long as there has been no change to the state of the co-ordinate at path generation, the path is valid.
            if (modelStructure.getCoordinate(toMoveTo.point()).getECellType() != toMoveTo.cellType()) {
                listOfPaths.remove(value);
                LinkedList<Node> newPath = Pathfind.aStar(250, modelStructure, current.point(), value.getLast().point());
                if (!(newPath.isEmpty())) {
                    listOfPaths.add(newPath);
                }
            } else {
                //Get the current node which should be the node the cell is on, and then the cell to move to.
                Point cPoint = current.point();
                modelStructure.replaceFrontLayerAt(cPoint, toMoveTo.point());
                globalLifeCellList.remove(cPoint);
                globalLifeCellList.add(toMoveTo.point());
            }
        }
    }

    /**
     * This method determines if any life cells within a society cell will generate a path to seek another life cell to create. The chance of this happening is determined by the current
     * population by the amount of food distributed by the society cell.
     */
    public void reproduce() {
        //System.out.println("Reproduce time");
        for (Point point : globalSocietyCellList) {
            SocietyCell societyCell = modelStructure.getCoordinate(point);
            int percentageCapacity = 0;
            if (societyCell.getPopulationCount() == 0) {
                continue;
            }
            if (!(societyCell.getNutrientCapacity() == 0)) {
                percentageCapacity = societyCell.getPopulationCount() / societyCell.getNutrientCapacity() * 100;
            }
            //The population and it's resources determine the probability of a societycell determining if it needs to reproduce.
            // A high population and not a lot of food left means there is a lower chance of reproduction. Vice versa.
            int reproduceProbability;
            if (percentageCapacity < 20) {
                reproduceProbability = 100;
            } else if (percentageCapacity < 50) {
                reproduceProbability =  50;
            } else if (percentageCapacity < 80) {
                reproduceProbability =  20;
            } else {
                reproduceProbability =  10;
            }

            if (reproduceProbability > Math.random() * 100) {
                try {
                    LinkedList<Node> pathBetweenCouple = getPathBetweenCouple(point);
                    //We want to make sure no current paths for the life cell exists and the path generated isnt empty
                    if (listOfPaths.parallelStream().map(LinkedList::peek).filter(Objects::nonNull).noneMatch(e -> e.equals(pathBetweenCouple.peek())) && !pathBetweenCouple.isEmpty()){
                        listOfPaths.add(pathBetweenCouple);
                        controller.pushText("A society has decided to reproduce Life Cells");
                    }
                } catch (Exception e) {
                    controller.pushText("A Life Cell tried to find a mate, but only could find itself");
                };
            }
        }
    }

    private LinkedList<Node> getPathBetweenCouple(Point societyCell) throws ArrayIndexOutOfBoundsException {
        Point[] couple = selectCoupleLifeCell(societyCell);
        return Pathfind.aStar(250, modelStructure, couple[0], couple[1]);
    }

    private Point selectRandomLifeCell(Point societyCell) throws ArrayIndexOutOfBoundsException {
        List<Point> points = globalLifeCellList.parallelStream().filter(p -> modelStructure.getCoordinate(p) instanceof LifeCell lifeCell && lifeCell.getSocietyCell() == societyCell).toList();
        return points.get((int) (Math.random() * points.size()));
    }

    private Point[] selectCoupleLifeCell(Point societyCell) {
        Point[] points = new Point[2];
        points[0] = selectRandomLifeCell(societyCell);
        int breakcnd = 0;
        do {
            points[1] = selectRandomLifeCell(societyCell);
            breakcnd++;
        } while (points[1].equals(points[0]) && breakcnd < 5);
        if (breakcnd == 5) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return points;
    }
}

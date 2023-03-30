package org.basmat.map.controller.rules;

import org.basmat.map.model.ModelStructure;
import org.basmat.map.model.cells.LifeCell;
import org.basmat.map.model.cells.SocietyCell;
import org.basmat.map.model.cells.factory.CellFactory;
import org.basmat.map.util.PointUtilities;
import org.basmat.map.util.ECellType;
import org.basmat.map.util.TextureHelper;
import org.basmat.map.util.path.Node;
import org.basmat.map.util.path.Pathfind;
import org.basmat.map.view.ViewStructure;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The gardener rule set provides methods into life. The rules govern how the life cells will recreate, as well as attempts to survive, such as scavenge if food resources are low.
 */
/*
 In the morning, the gardener pushed the seeds down into the wet loam of the garden to see what they would become
 */
public class Gardener {

    private LinkedList<Point> globalNutrientCellList;
    private LinkedList<Point> globalSocietyCellList;
    private LinkedList<Point> globalLifeCellList;

    //Update
    private LinkedList<LinkedList<Node>> listOfPaths;
    private ViewStructure viewStructure;
    private final ModelStructure modelStructure;

    /**
     *
     *
     * @param viewStructure
     * @param modelStructure
     * @param globalNutrientCellList
     * @param globalSocietyCellList
     * @param globalLifeCellList
     */
    public Gardener(ViewStructure viewStructure, ModelStructure modelStructure, LinkedList<Point> globalNutrientCellList, LinkedList<Point> globalSocietyCellList, LinkedList<Point> globalLifeCellList, LinkedList<LinkedList<Node>> listOfPaths) {
        this.viewStructure = viewStructure;
        this.modelStructure = modelStructure;
        this.globalNutrientCellList = globalNutrientCellList;
        this.globalSocietyCellList = globalSocietyCellList;
        this.globalLifeCellList = globalLifeCellList;
        this.listOfPaths = listOfPaths;
    }

    /**
     *
     * @param societyCell The society cell to determine if anything needs to happen
     * @param percentageCapacity The population / nutrient capacity * 100
     */
    public void scavenge(SocietyCell societyCell, double percentageCapacity) {
        //If the nutrient capacity of the society exceeds 85%, then tell someone to scavenge
        if (percentageCapacity > 85) {
            //Look for food, or risk dying
        }
    }

    public void expand() {
        for (Point societyCellPoint : globalSocietyCellList) {
            SocietyCell societyCell = modelStructure.getCoordinate(societyCellPoint);
            //If the life cell count is divisable by six and the previous capacity is not the same as the current capacity, expand the borders
            if (societyCell.getPreviousExpansionQuotient() != societyCell.getPopulationCount()/6) {
                societyCell.setRadius(societyCell.getRadius() + 2);
                societyCell.setPreviousExpansionQuotient(societyCell.getPopulationCount() / 6);
                PointUtilities.tintArea(societyCell.getRadius(), societyCellPoint, societyCell.getTint(), modelStructure);
                System.out.println(societyCell.getName() + " just expanded!");
            }
        }
    }

    /**
     * Tells any cells with a certain reproduction cool down to go to a randomized coordinate with it's SocietyCell to prevent overcrowding.
     */
    public void scatter() {
        //System.out.println("Scattering");
        for (Point lifeCellPoint : globalLifeCellList) {
            LifeCell lifeCell = modelStructure.getCoordinate(lifeCellPoint);
            SocietyCell societyCell = modelStructure.getCoordinate(lifeCell.getSocietyCell());
            if (lifeCell.getReproductionCooldown() == 9) {
                //If the lifecell has recently reproduced, we want it to scatter to avoid overcrowding. To assert this, we want to check if it currently has any paths to follow, if so remove it and replace it with the scatter function
                Point destination = PointUtilities.calculateValidCoordinates(lifeCell.getSocietyCell(), societyCell.getRadius(), modelStructure, List.of(new ECellType[]{ECellType.GRASS, ECellType.SAND}));
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
        //System.out.println("Checking reproduction rules");
        //Copy list to prevent concurrency exceptions
        List<Point> copyOfList = new LinkedList<>(globalLifeCellList);
        for (Point lifeCellPoint : copyOfList) {
            LifeCell parent1 = modelStructure.getCoordinate(lifeCellPoint);

            //Get a 3x3 area from the origin of the cell and see if there are any life cell to reproduce with
            List<Point> allValidatedNeighbours = PointUtilities.getAllValidatedNeighbours(lifeCellPoint);
            for (Point parent2 : allValidatedNeighbours) {
                if (modelStructure.getCoordinate(parent2) instanceof LifeCell lifeCell && lifeCell.getReproductionCooldown() == 0 && parent1.getReproductionCooldown() == 0) {
                    Point newLifeCell;
                    //for the little list surrounding the cell, try to generate a new life cell, however if the break condition is reached (to prevent hard lock) then we cannot create one
                    int breakcnd = 0;
                    do {
                        newLifeCell = allValidatedNeighbours.get(((int) (Math.random() * allValidatedNeighbours.size())));
                        breakcnd++;
                        //Repeat if there is no valid spawn place -- try to find one within 4 attempts or failure
                    } while (Pathfind.isInvalid(modelStructure.getCoordinate(newLifeCell).getECellType()) && breakcnd < 4);/* && !(worldCell.getECellType().isHabitable())*/;
                    if (breakcnd == 4) {
                        System.out.println("A life cell cannot be created!");
                        continue;
                    }

                    //If horrible things havent happened:
                    //Create a new life cell and add it to the global array, and add it to the model
                    modelStructure.setFrontLayer(newLifeCell, new CellFactory().createLifeCell(lifeCell.getSocietyCell(), TextureHelper.cacheCellTextures(new HashMap<>()).get(ECellType.LIFE_CELL)));
                    globalLifeCellList.add(newLifeCell);
                    parent1.setReproductionCooldown();
                    ((LifeCell) modelStructure.getCoordinate(parent2)).setReproductionCooldown();
                    ((LifeCell) modelStructure.getCoordinate(newLifeCell)).setReproductionCooldown();
                    System.out.println("A new life cell has been created!");
                    ((SocietyCell) modelStructure.getCoordinate(parent1.getSocietyCell())).addLifeCells(newLifeCell);
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
                ((SocietyCell) modelStructure.getCoordinate(((LifeCell) modelStructure.getCoordinate(toMoveTo.point())).getSocietyCell())).changeLifeCellLoc(current.point(), toMoveTo.point());
            }
        }
    }

    /**
     * Logic provider for reproduction. Determines if a society cell will cause reproduction in it's owned life cells.
     */
    public void reproduce() {
        //System.out.println("Reproduce time");
        for (Point point : globalSocietyCellList) {
            SocietyCell societyCell = modelStructure.getCoordinate(point);
            int percentageCapacity = societyCell.getPopulationCount() / societyCell.getNutrientCapacity() * 100;
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
                LinkedList<Node> pathBetweenCouple = getPathBetweenCouple(societyCell);
                //We want to make sure no current paths for the life cell exists and the path generated isnt useless
                if (listOfPaths.stream().map(LinkedList::peek).filter(Objects::nonNull).noneMatch(e -> e.equals(pathBetweenCouple.peek())) && !pathBetweenCouple.isEmpty()){
                    listOfPaths.add(pathBetweenCouple);
                }
            }
        }
    }

    private LinkedList<Node> getPathBetweenCouple(SocietyCell societyCell) {
        Point[] couple = selectCoupleLifeCell(societyCell);
        LifeCell coordinate = modelStructure.getCoordinate(couple[0]);
        LifeCell coordinate2 = modelStructure.getCoordinate(couple[1]);

        return Pathfind.aStar(250, modelStructure, couple[0], couple[1]);
    }

    private Point selectRandomLifeCell(SocietyCell societyCell) {
        return societyCell.getLifeCells().get((int) (Math.random() * societyCell.getPopulationCount()));
    }

    private Point[] selectCoupleLifeCell(SocietyCell societyCell) {
        Point[] points = new Point[2];
        points[0] = selectRandomLifeCell(societyCell);
        do {
            points[1] = selectRandomLifeCell(societyCell);
        } while (points[1].equals(points[0]));
        return points;
    }
}

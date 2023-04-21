package org.basmat.map.model;

import org.basmat.map.model.cells.WorldCell;
import org.basmat.map.model.cells.factory.IMapCell;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;


/**
 * ModelStructure provides 2 primary matrices, a WorldCell matrix and a IOwnedCell matrix. The WorldCell matrix acts as a back layer where any unfilled space by the IOwnedLayer will be filled in through the view, and IOwnedLayer acts as the front layer,
 * where any objects in that matrix get priority rendering.
 */
public class ModelStructure implements Serializable {

    private final HashMap<Coordinates, WorldCell> backLayer;
    private final HashMap<Coordinates, ? super IMapCell> frontLayer;

    public ModelStructure() {
        this.backLayer = new HashMap<>();
        this.frontLayer = new HashMap<>();
    }

    public WorldCell getBackLayer(Point point) {
        return backLayer.get(new Coordinates(point));
    }

    /**
     * Sets an object at the specified point in the internal back layer
     * @param point The point to set the object at
     * @param toSet The object to set
     */
    public void setBackLayer(Point point, WorldCell toSet) {
        backLayer.put(new Coordinates(point), toSet);
    }

    /**
     * Returns the object at the point provided in the front layer
     * @param point The point to get an object from
     * @return An object at the specified point
     */
    @SuppressWarnings("unchecked")
    public <T extends IMapCell> T getFrontLayer(Point point) {
        return (T) frontLayer.get(new Coordinates(point));
    }

    /**
     * Sets an object at the specified point in the internal front layer
     * @param point The Point to set at
     * @param toSet The object to set at
     */
    public <T extends IMapCell> void setFrontLayer(Point point, T toSet) {
        frontLayer.put(new Coordinates(point), toSet);
    }

    /**
     * This method returns an object that implements <code> IMapClass </code>. If the top layer contains an object, then that overrides returning the bottom object.
     * @return The object that is in the coordinates, with the front layer taking priority
     */
    @SuppressWarnings("unchecked")
    public <T extends IMapCell> T getCoordinate(Point point) {
        return getFrontLayer(point) == null ? (T) getBackLayer(point) : getFrontLayer(point);
    }


    /**
     * Deletes a Coordinate within the system. Can delete the back layer.
     * @param point The coordinate to delete, uses getCoordinate() to get the priority object
     */
    public void deleteCoordinate(Point point) {
        if (getCoordinate(point) instanceof WorldCell) {
            backLayer.remove(new Coordinates(point));
        } else {
            frontLayer.remove(new Coordinates(point));
        }
    }

    /**
     * Deletes a Coordinate in the back layer of the system.
     * @param point The coordinate to delete
     */
    public void deleteBackLayer(Point point) {
        backLayer.remove(new Coordinates(point));
    }

    /**
     * Deletes a Coordinate in the back layer of the system.
     * @param point The coordinate to delete
     */
    public void deleteFrontLayer(Point point) {
        frontLayer.remove(new Coordinates(point));
    }

    /** Replaces the current Coordinate with a new Coordinate.
     * @param toDelete The coordinate to delete
     * @param toReplaceAt The coordinate to replace at
     */
    public <T extends IMapCell> void replaceFrontLayerAt(Point toDelete, Point toReplaceAt) {
        T model = getFrontLayer(toDelete);
        deleteFrontLayer(toDelete);
        setFrontLayer(toReplaceAt, model);
    }
}

/**
 * Represents a Point in the system.
 * This class overrides @hashCode to collide differing Objects of the same (x,y).
 */
class Coordinates extends Point{
    /**
     * The Pont to represent in the system
     * @param point The Point to represent
     */
    public Coordinates(Point point){
        super(point);
    }

    //hashCode is overridden to ensure that even though an object could be different, if the coordinates match then the bucket will be the same.
    @Override
    public int hashCode() {
        return (y * 150) + x;
    }
}

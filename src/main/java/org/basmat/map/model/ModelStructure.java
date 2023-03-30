package org.basmat.map.model;

import org.basmat.map.model.cells.WorldCell;
import org.basmat.map.model.cells.factory.IMapCell;

import java.awt.*;
import java.util.HashMap;


/**
 * ModelStructure provides 2 primary matrices, a WorldCell matrix and a IOwnedCell matrix. The WorldCell matrix acts as a back layer where any unfilled space by the IOwnedLayer will be filled in through the view, and IOwnedLayer acts as the front layer,
 * where any objects in that matrix get priority rendering.
 */
public class ModelStructure {
    private final HashMap<Coordinates, WorldCell> backLayer;
    private final HashMap<Coordinates, ? super IMapCell> frontLayer;

    public ModelStructure() {
        this.backLayer = new HashMap<>();
        this.frontLayer = new HashMap<>();
    }

    public WorldCell getBackLayer(Point point) {
        return backLayer.get(new Coordinates(point));
    }

    public void setBackLayer(Point point, WorldCell toSet) {
        backLayer.put(new Coordinates(point), toSet);
    }

    public <T extends IMapCell> T getFrontLayer(Point point) {
        return (T) frontLayer.get(new Coordinates(point));
    }

    public <T extends IMapCell> void setFrontLayer(Point point, T toSet) {
        frontLayer.put(new Coordinates(point.x, point.y), toSet);
    }

    /**
     * This method returns the object that is current on either matrix's coordinate. If the top layer contains an object, then that overrides returning the bottom object.
     * @return The object that is in the coordinates, with the front layer taking priority
     */
    public <T extends IMapCell> T getCoordinate(Point point) {
        return getFrontLayer(point) == null ? (T) getBackLayer(point) : getFrontLayer(point);
    }

    /**
     * Be careful
     * @param point The coordinate to delete, uses getCoordinate() to get the priority object
     */
    public <T extends IMapCell> void deleteCoordinate(Point point) {
        if (getCoordinate(point) instanceof WorldCell) {
            backLayer.remove(new Coordinates(point));
        } else {
            frontLayer.remove(new Coordinates(point));
        }
    }

    /**
     *
     * @param toDelete The coordinate to delete
     * @param toReplaceAt The coordinate to replace at
     */
    public <T extends IMapCell> void replaceFrontLayerAt(Point toDelete, Point toReplaceAt) {
        T model = getFrontLayer(toDelete);
        deleteCoordinate(toDelete);
        setFrontLayer(toReplaceAt, model);
    }
}

class Coordinates {
    public int x;
    public int y;

    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Coordinates(Point point){
        this.x = point.x;
        this.y = point.y;
    }
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Coordinates coords && this.x == coords.x && this.y == coords.y;
    }

    //hashCode is overridden to ensure that even though an object could be different, if the coordinates match then the bucket will be the same.
    @Override
    public int hashCode() {
        return (y * 150) + x;
    }
}

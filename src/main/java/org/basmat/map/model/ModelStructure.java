package org.basmat.map.model;

import org.basmat.map.model.cells.WorldCell;
import org.basmat.map.model.cells.factory.IMapCell;
import org.basmat.map.model.cells.factory.IOwnedCell;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * ModelStructure provides 2 primary matrices, a WorldCell matrix and a IOwnedCell matrix. The WorldCell matrix acts as a back layer where any unfilled space by the IOwnedLayer will be filled in through the view, and IOwnedLayer acts as the front layer,
 * where any objects in that matrix get priority rendering.
 */
public class ModelStructure {
    //TODO: When bucketing is fixed, convert 2d array of WorldCell[][] to HashMap<Point, WorldCell>
    private final WorldCell[][] backLayer;
    private final HashMap<Coords, ? super IMapCell> frontLayer;

    public ModelStructure(int backLayerWidth, int backLayerHeight) {
        this.backLayer = new WorldCell[backLayerWidth][backLayerHeight];
        this.frontLayer = new HashMap<>();
    }

    public WorldCell getBackLayer(int x, int y) {
        return backLayer[x][y];
    }

    public void setBackLayer(int x, int y, WorldCell toSet) {
        backLayer[x][y] = toSet;
    }

    public <T extends IMapCell> T getFrontLayer(Point point) {
        return (T) frontLayer.get(new Coords(point.x, point.y));
    }

    public <T extends IMapCell> void setFrontLayer(Point point, T toSet) {
        frontLayer.put(new Coords(point.x, point.y), toSet);
    }

    /**
     * This method returns the object that is current on either matrix's coordinate. If the top layer contains an object, then that overrides returning the bottom object. i.e
     * @return The object that is in the coordinates, with the front layer taking priority
     */
    public <T extends IMapCell> T getCoordinate(Point point) {
        return getFrontLayer(point) == null ? (T) getBackLayer(point.x, point.y) : getFrontLayer(point);
    }
}

class Coords{
    public int x;
    public int y;

    public Coords(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Coords coords && this.x == coords.x && this.y == coords.y;
    }

    @Override
    public int hashCode() {
        //TODO: Make it so that coordinates are unique as (5, 2) (2,5) would be put under the same bucket. Look at Elliptic Curve Point Compression.
        return x ^ y;
    }
}

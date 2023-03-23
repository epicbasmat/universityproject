package org.basmat.map.util.path;

import org.basmat.map.util.ECellType;

import java.awt.*;


/**
 * A representation of a point in a graph, recording additional data for analysis.
 * @param point The point that the node represents
 * @param f f(n) in A* traversal, a combination of g(n) + h(n)
 * @param g g(n) in A* traversal, the culmination of every prior search's g(n) function
 * @param cellType The CellType that the modelStructures `getCoordinate()` returns
 */
public record Node(Point point, int f, int g, ECellType cellType, Node parent) implements Comparable<Node> {
    @Override
    public int compareTo(Node toBeCompared) {
        return Integer.compare(this.f(), toBeCompared.f());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Node node && node.point().equals(this.point());
    }
};
package org.basmat.cell.util.path;

import java.awt.*;

public class Node {
    private Point point;
    private int f;

    public Point getPoint() {
        return point;
    }

    public int getF() {
        return f;
    }

    /**
     *
     * @param point The X & Y coordinates of the node
     * @param f the calculated heuristic
     */
    public Node(Point point, int f) {
        this.point = point;
        this.f = f;
    }
}

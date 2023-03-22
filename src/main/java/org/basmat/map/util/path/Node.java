package org.basmat.map.util.path;

import org.basmat.map.util.ECellType;

import java.awt.*;

public record Node(Point point, int heuristic, ECellType cellType) {
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Node node && node.point().equals(this.point());
    }
};
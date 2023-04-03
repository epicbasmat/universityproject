package org.basmat.map.util;

@FunctionalInterface
public interface Action <Point> {
    void action(Point point);
}

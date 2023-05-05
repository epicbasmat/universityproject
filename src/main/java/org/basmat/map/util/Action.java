package org.basmat.map.util;

/**
 * This interface is a functional interface and applies an action to a Point object, returning no value.
 * @param <Point>
 */
@FunctionalInterface
public interface Action <Point> {
    /**
     * The action to apply to the Point object.
     * @param point The point to apply an action to
     */
    void action(Point point);
}

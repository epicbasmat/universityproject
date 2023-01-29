package org.basmat.map.controller;

import org.basmat.map.cellfactory.IMapCell;

import org.basmat.map.view.CellPanel;
import org.junit.jupiter.api.Test;

import java.awt.*;

/**
 * MVBinder contains a simple data model to contain a model, view and points that are all associated.
 * @param <T>
 */
public record MVBinder<T extends IMapCell>(T model, CellPanel view, Point point) {

    /**
     * @param model The model to bind
     * @param view  The view to bind
     * @param point The point to bind
     */
    public MVBinder {
    }
}

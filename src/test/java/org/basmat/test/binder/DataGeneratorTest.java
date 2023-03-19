package org.basmat.test.binder;

import org.basmat.map.model.cells.factory.CellFactory;
import org.basmat.map.model.cells.WorldCell;
import org.basmat.map.setup.ModelSetup;
import org.basmat.map.util.ECellType;
import org.basmat.map.view.ViewStructure;
import org.basmat.map.view.CellPanel;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class DataGeneratorTest {
    private final ViewStructure viewStructure;
    private final CellDataHelper cellDataHelper;
    private final CellFactory cellFactory;
    private final HashMap<ECellType, BufferedImage> cache;
    private final CellPanel view;
    private final Point point;
    private final WorldCell model;
    private MVBinder<?> binder;

    DataGeneratorTest() {
        this.cellFactory = new CellFactory();
        cache = new ModelSetup(null, null, null, null, null, null, globalLifeCellList).getImageCache();
        viewStructure = new ViewStructure(750, 750, null);
        cellDataHelper = new CellDataHelper(viewStructure, cache);
        view = new CellPanel(cache.get(ECellType.GRASS), 400);
        model = cellFactory.createWorldCell(ECellType.GRASS, null, 400);
        point = new Point(40, 40);
        binder = new MVBinder<>(model, view, point);
    }

    @Test
    void GetNewModelViewPoint_MVBinderModel_True() {
        assertEquals(binder.model(), model);
        assertEquals(binder.view(), view);
        assertEquals(binder.point(), point);
    }

    @Test
    void GetIdFromSetViewInBinder_MVBinder_True() {
        assertEquals(400, binder.view().getId());
    }

    @Test
    void IdIsSharedBetweenViewAndModel_MVBinder_True() {
        assertEquals(binder.view().getId(), binder.model().getId());
    }

    @Test
    void CreateSocietyBinder_CellDataHelperGenerators_True() {
        MVBinder<?> name = cellDataHelper.generateSocietyBinder("name", 600, 12, new Point(45, 45));
        assertInstanceOf(MVBinder.class, name);
        assertEquals(ECellType.SOCIETY_CELL, name.model().getECellType());
    }

    @Test
    void GetIdFromModel_CellPanelMatrix_True() {
        cellDataHelper.setCellData(binder);
        assertEquals(400, viewStructure.getPanel(40, 40).getId());
    }
}

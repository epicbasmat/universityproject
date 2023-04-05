package org.basmat.map.controller.rules;

import org.basmat.map.model.ModelStructure;
import org.basmat.map.util.ECellType;
import org.basmat.map.util.TestUtilities;
import org.junit.jupiter.api.Test;

class GardenerTest {

    private final ModelStructure modelStructure;

    GardenerTest() {
        modelStructure = new ModelStructure();
        TestUtilities.fillModelWithWorldCell(modelStructure, ECellType.GRASS);
    }

    @Test
    void expansion_checkExpansionOccurs_expansionOccurs() {

    }

    @Test
    void scatter() {
    }

    @Test
    void checkForValidReproduction() {
    }

    @Test
    void unison() {
    }

    @Test
    void reproduce() {
    }
}
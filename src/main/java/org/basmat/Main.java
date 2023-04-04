package org.basmat;

import org.basmat.map.controller.CellMatrixController;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        CellMatrixController cellMatrixController = new CellMatrixController(150, 150);
    }
}
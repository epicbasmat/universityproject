package org.basmat.map.view;

import org.basmat.map.controller.CellMatrixController;
import org.basmat.map.controller.rules.Gardener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UI extends JPanel {

    private final GridBagConstraints c;
    private CellMatrixController cellMatrixController;

    public UI(CellMatrixController cellMatrixController) {
        this.cellMatrixController = cellMatrixController;
        setSize(100,100);
        setVisible(true);
        c = new GridBagConstraints();
        setLayout(new GridBagLayout());
        button();
    }

    public void button() {
        Button scrumpMonkeys = new Button("Generate paths");
        scrumpMonkeys.addActionListener(new Temp(cellMatrixController));
        scrumpMonkeys.setSize(30, 30);
        this.add(scrumpMonkeys);
    }
}

class Temp implements ActionListener {

    private CellMatrixController cellMatrixController;

    public Temp(CellMatrixController cellMatrixController){
        this.cellMatrixController = cellMatrixController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        cellMatrixController.doThing();
    }
}


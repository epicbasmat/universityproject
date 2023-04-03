package org.basmat.map.view;

import org.basmat.map.controller.CellMatrixController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UI extends JPanel {

    private JScrollPane txtArea;
    private CellMatrixController cellMatrixController;
    private JTextArea textArea;
    private JScrollPane textScrollArea;

    public UI(CellMatrixController cellMatrixController) {
        this.cellMatrixController = cellMatrixController;
        setSize(100,100);
        setVisible(true);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        button();
        textArea();
    }

    private void button() {
        Button paths = new Button("Generate paths");
        paths.addActionListener(new PathButton(cellMatrixController));
        paths.setSize(30, 30);
        this.add(paths);
    }

    private void textArea() {
        textArea = new JTextArea(null, null, 10, 10);
        textScrollArea = new JScrollPane(textArea);
        textScrollArea.setPreferredSize(new Dimension(400, 300));
        textArea.setEditable(false);
        this.add(textScrollArea);
    }

    public void appendText(String string) {
        textArea.append(string + "\n");
    }
}

class PathButton implements ActionListener {

    private CellMatrixController cellMatrixController;

    public PathButton(CellMatrixController cellMatrixController){
        this.cellMatrixController = cellMatrixController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        cellMatrixController.doThing();
    }
}


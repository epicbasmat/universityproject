package org.basmat.map.view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * CellPanel represents a specific cell within the overall SimulationUI.
 */
public class CellPanel extends JPanel {
    private BufferedImage texture;
    private Graphics2D g2d;

    /**
     * Instantiate CellPanel with the provided texture to render
     * @param texture the texture to render for the cell
     */
    public CellPanel(BufferedImage texture) {
        setSize(texture.getWidth(), texture.getHeight());
        setVisible(true);
        this.texture = texture;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (texture != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            int x = (getWidth() - texture.getWidth(this)) / 2;
            int y = (getHeight() - texture.getHeight(this)) / 2;
            g2d.drawImage(texture, x, y, this);
            g2d.dispose();
        }
    }

    public boolean isEqualTexture(BufferedImage texture) {
        return texture == this.texture;
    }
}

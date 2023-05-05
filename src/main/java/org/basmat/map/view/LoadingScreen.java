package org.basmat.map.view;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


/*
Thanks to https://icons8.com/preloaders/ for providing the icon used for the loading screen
 */

/**
 * Thic class is the main implementation of the Loading Screen. It loads the loading.gif on intiialization and will continue to render it so long as it's in focus
 */
public class LoadingScreen extends JPanel {
    public LoadingScreen() {
        this.setLayout(new BorderLayout());
        JFrame layout = new JFrame();
        layout.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        try {
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("textures/loading.gif");
            byte[] imageData = new byte[Objects.requireNonNull(resourceAsStream).available()];
            resourceAsStream.read(imageData);
            Icon image = new ImageIcon(imageData);
            JLabel comp = new JLabel(image);
            this.add(comp, BorderLayout.CENTER);
            this.setVisible(true);
            this.validate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

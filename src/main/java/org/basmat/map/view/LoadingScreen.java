package org.basmat.map.view;

import org.basmat.map.util.ECellType;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;


/*
Thanks to https://icons8.com/preloaders/ for providing the icon used for the loading screen
 */
public class LoadingScreen extends JPanel {

    public LoadingScreen() {
        this.validate();
        this.setLayout(new BorderLayout());
        JFrame layout = new JFrame();
        layout.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        Icon image = new ImageIcon("./assets/loading.gif");
        JLabel comp = new JLabel(image);
        this.add(comp, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void setInfoForUser() {

    }
}

package org.basmat;

import org.basmat.map.controller.Controller;

import java.io.IOException;

/**
 * The starting class.
 * @author George Brilus
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Controller controller = new Controller(150, 150);
        controller.displayUI();
    }
}
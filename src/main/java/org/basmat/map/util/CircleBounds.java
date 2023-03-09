package org.basmat.map.util;

import org.basmat.map.cellfactory.cells.SocietyCell;
import org.basmat.map.controller.MVBinder;

import java.util.HashMap;

public class CircleBounds {
    public static int[] calculateAndReturnRandomCoords(HashMap<Integer, MVBinder<?>> bindingAgent, int id) {
        //Calculates a random point within a circle, in this case the area of effect of a societycell
        double random = Math.random();
        int r = (int) (((SocietyCell) bindingAgent.get(id).model()).getRadius() * Math.sqrt(random));
        int x = (int) (bindingAgent.get(id).point().getX() + (r * Math.cos(random * 2 * 3.1415)));
        int y = (int) (bindingAgent.get(id).point().getY() + (r * Math.sin(random * 2 * 3.1415)));
        return new int[] {x, y};
    }
}

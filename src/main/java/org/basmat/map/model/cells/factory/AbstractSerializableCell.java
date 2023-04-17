package org.basmat.map.model.cells.factory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is an abstract class which details how a Cell can be serializable. It primarily deals with the texture that all cells need to be rendered. As BufferedImages cannot be Serialized, this Abstract Class exists to enable Serialization easily for Cell Objects.
 * It is recommended for all Cell classes to extend this Class.
 */
public class AbstractSerializableCell implements Serializable {

    private transient BufferedImage texture;

    protected AbstractSerializableCell(BufferedImage texture) {
        this.texture = texture;
    }

    public BufferedImage getTexture() {
        return this.texture;
    }

    @Serial
    private void writeObject(java.io.ObjectOutputStream out) throws IOException, ClassNotFoundException{
        out.defaultWriteObject();
        ImageIO.write(this.getTexture(), "png", out);
    }

    @Serial
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.texture = (ImageIO.read(in));
    }
}

package org.basmat.map.model.cells.factory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

/**
 * This class is an abstract class which details how a Cell can be serializable.
 * All Cells need a texture to be rendered to the user, however BufferedImages cannot be Serialized.
 * For this reason, this class exists to enable Serialization easily for Cell Objects.
 * It is recommended for all Cell classes to extend this Class.
 */
public abstract class AbstractSerializableCell implements Serializable {

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

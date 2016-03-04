package org.jzy3d.javafx.drawables;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;

public class Images {
    public ImageView imageFX(BufferedImage bf, File f) {
        try {
            bf = ImageIO.read(f);
        } catch (IOException ex) {
            System.out.println("Image failed to load.");
        }

        WritableImage wr = null;
        if (bf != null) {
            wr = new WritableImage(bf.getWidth(), bf.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            copyBufferedImage(bf, pw);
        }

        return new ImageView(wr);

    }

    public void copyBufferedImage(BufferedImage bf, PixelWriter pw) {
        for (int x = 0; x < bf.getWidth(); x++) {
            for (int y = 0; y < bf.getHeight(); y++) {
                pw.setArgb(x, y, bf.getRGB(x, y));
            }
        }
    }
}

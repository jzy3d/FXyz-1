package org.jzy3d.javafx.drawables;

import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import org.jzy3d.maths.Coord3d;

public class Labels {
    public void addAxisLabels(Group parent, int cubeWidth) {
        int fontSize = 30;

        for (int i = 0; i < 10; i++) {
            double y = -400; // y est croissant de bas en haut -> dans notre cas c'est un Z
            double z = 0; // Z NE SEMBLE PAS TOLERE?! METTRE DANS UN AUTRE OBJET
            BorderPane la = addLabel("X" + i, fontSize, new Coord3d(i * 100 - cubeWidth / 2, y, z));
            // BorderPane la = addLabel("X"+i, fontSize, new
            // Coord3d(f*Math.random(), f*Math.random(), f*Math.random()));
            parent.getChildren().add(la);
        }


        BorderPane label_xmax = createLabel("X max=" + cubeWidth / 2, fontSize);
        positionXmax(cubeWidth, label_xmax);
        parent.getChildren().add(label_xmax);

        BorderPane label_ymax = createLabel("y max=" + cubeWidth / 2, fontSize);
        positionYmax(cubeWidth, label_ymax);
        parent.getChildren().add(label_ymax);

        BorderPane label_zmax = createLabel("z max=" + cubeWidth / 2, fontSize);
        Group z = new Group();
        z.getChildren().add(label_zmax);
        positionZmax(cubeWidth, z);
        parent.getChildren().add(z);
    }
    

    private void positionZmax(int cubeWidth, Node label_zmax) {
        label_zmax.translateXProperty().set(-cubeWidth / 2);
        label_zmax.translateYProperty().set(-cubeWidth / 2);
        label_zmax.translateZProperty().set(+cubeWidth / 2);
    }

    private void positionYmax(int cubeWidth, Node label_ymax) {
        label_ymax.translateXProperty().set(-cubeWidth / 2);
        label_ymax.translateYProperty().set(+cubeWidth / 2);
        //label_ymax.translateZProperty().set(-cubeWidth / 2);
    }

    private void positionXmax(int cubeWidth, Node label_xmax) {
        label_xmax.translateXProperty().set(+cubeWidth / 2);
        label_xmax.translateYProperty().set(-cubeWidth / 2);
       // label_xmax.translateZProperty().set(-cubeWidth / 2);
    }

    private void positionOrigin(int cubeWidth, Node label_0) {
        label_0.translateXProperty().set(-cubeWidth / 2);
        label_0.translateYProperty().set(-cubeWidth / 2);
      //  label_0.translateZProperty().set(-cubeWidth / 2);
    }

    public BorderPane createLabel(String txt, int fontSize) {
        boolean cache  =true;
        
        Text text = new Text();
        text.setText(txt);
        text.setStyle("-fx-font-size: " + fontSize + ";");
        text.setCache(cache);

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-border-color: black;-fx-background-color: #66CCFF;");
        borderPane.setTop(text);
        borderPane.setCache(cache);
        borderPane.setCacheHint(CacheHint.SCALE_AND_ROTATE);

        return borderPane;
    }

    public BorderPane addLabel(String txt, int fontSize, Coord3d position) {
        BorderPane pane = createLabel(txt, fontSize);
        pane.translateXProperty().set(position.x);
        pane.translateYProperty().set(position.y);
        pane.translateZProperty().set(position.z);
        return pane;
    }
    
    
    
    public void Text3d(){
        // Text3DMesh label_0 = new Text3DMesh("" + (-cubeWidth/2),
        // "Gadugi Bold", 40, true, 120, 0, 1);

        
        // Text3DMesh label_xmax = new Text3DMesh("x max="+cubeWidth/2,
        // "Gadugi Bold", 40, true, 120, 0, 1);

        // label_xmax.rotateProperty().set(Math.PI);
        // label_xmax.setTextureModeVertices3D(1530, p -> p.magnitude());

        
        // Text3DMesh label_zmax = new Text3DMesh("z max="+cubeWidth/2,
        // "Gadugi Bold", 40, true, 120, 0, 1);

        
        // letters.setDrawMode(DrawMode.LINE);
        // NONE
        // letters.setTextureModeNone(Color.ROYALBLUE);
        // IMAGE
        // letters.setTextureModeImage(getClass().getResource("res/steel-background1.jpg").toExternalForm());
        // letters.setTextureModeImage(getClass().getResource("res/marvel1.jpg").toExternalForm());
        // DENSITY
        // letters.setTextureModeVertices3D(1530,p->Math.sin(p.y/50)*Math.cos(p.x/40)*p.z);
        // FACES
        // letters.setTextureModeFaces(Palette.ColorPalette.HSB,16);
    }

}

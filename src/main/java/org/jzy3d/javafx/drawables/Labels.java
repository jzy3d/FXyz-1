package org.jzy3d.javafx.drawables;

import javafx.geometry.Point3D;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import org.jzy3d.javafx.chart.Settings;
import org.jzy3d.maths.Coord3d;

public class Labels {
    Settings settings = new Settings();
    
    
    public void addAxisLabels(Group parent, int cubeWidth) {

        double offset = settings.textSize;
        
        for (int i = 0; i < 10; i++) {
            double x = i * 100 - cubeWidth / 2;
            double y = - cubeWidth / 2 - offset; // y est croissant de bas en haut -> dans notre cas c'est un Z
            double z = -cubeWidth / 2 - offset; // Z NE SEMBLE PAS TOLERE?! METTRE DANS UN AUTRE OBJET
            //BorderPane la = addLabel("X" + i, fontSize, new Coord3d(i * 100 - cubeWidth / 2, y, z));
            Text la = createText("X="+x, settings.textSize, settings.textColor, settings.font);
            positionLabel(new Coord3d(x, y, z), la);

            parent.getChildren().add(la);
        }
        
        for (int i = 0; i < 10; i++) {
            double x = -cubeWidth / 2 - offset; // y est croissant de bas en haut -> dans notre cas c'est un Z
            double y = i * 100 - cubeWidth / 2;
            double z = -cubeWidth / 2 - offset; // Z NE SEMBLE PAS TOLERE?! METTRE DANS UN AUTRE OBJET
            //BorderPane la = addLabel("Y" + i, fontSize, new Coord3d(x, i * 100 - cubeWidth / 2, z));
            Text la = createText("Y="+y, settings.textSize, settings.textColor, settings.font);
            positionLabel(new Coord3d(x, y, z), la);
            parent.getChildren().add(la);
        }

        for (int i = 0; i < 10; i++) {
            double x = -cubeWidth / 2 - offset; // y est croissant de bas en haut -> dans notre cas c'est un Z
            double y = -cubeWidth / 2 - offset; // Z NE SEMBLE PAS TOLERE?! METTRE DANS UN AUTRE OBJET
            double z = i * 100 - cubeWidth / 2;
            //BorderPane la = addLabel("Y" + i, fontSize, new Coord3d(x, y, i * 100 - cubeWidth / 2));
            Text la = createText("Z="+z, settings.textSize, settings.textColor, settings.font);
            positionLabel(new Coord3d(x, y, z), la);
            parent.getChildren().add(la);
        }
        

        Text label_xmax = createText("X max", settings.textSize, settings.textColor, settings.font);
        positionXmax(cubeWidth, label_xmax);
        parent.getChildren().add(label_xmax);

        Text label_ymax = createText("Y max", settings.textSize, settings.textColor, settings.font);
        positionYmax(cubeWidth, label_ymax);
        parent.getChildren().add(label_ymax);

        Text label_zmax = createText("Z max", settings.textSize, settings.textColor, settings.font);
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

        BorderPane borderPane = wrapLabel(cache, text);

        return borderPane;
    }


    public BorderPane wrapLabel(boolean cache, Text text) {
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-border-color: black;-fx-background-color: #66CCFF;");
        borderPane.setTop(text);
        borderPane.setCache(cache);
        borderPane.setCacheHint(CacheHint.SCALE_AND_ROTATE);
        return borderPane;
    }

    public BorderPane addLabel(String txt, int fontSize, Coord3d position) {
        BorderPane pane = createLabel(txt, fontSize);
        positionLabel(position, pane);
        return pane;
    }


    public void positionLabel(Coord3d position, Node pane) {
        pane.translateXProperty().set(position.x);
        pane.translateYProperty().set(position.y);
        pane.translateZProperty().set(position.z);
        if(settings!=null){
            if(settings.normalProjection){
                //pane.rotationAxisProperty().set(new Point3D(90, 90, 270));
                pane.rotationAxisProperty().set(new Point3D(270, 90, 90));
                pane.rotateProperty().set(270);
            }
        }
    }
    
    public Text createText(String text, double mistery, Color color, Font font){
        final Text text1 = new Text(mistery, mistery, text);
        text1.setFill(color);
        text1.setFont(font);
        return text1;
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

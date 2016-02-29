package org.jzy3d.demos.javafx3d;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;

import org.fxyz.geometry.Point3D;
import org.fxyz.shapes.primitives.ScatterMesh;
import org.fxyz.shapes.primitives.SurfacePlotMesh;
import org.fxyz.utils.Patterns.CarbonPatterns;

public class DrawableFactory {
    private static final double epsilon = 0.00000000000000001;
    private Function<Point3D, Number> dens = p->p.x;
    
    Function<Point2D,Number> sample = p -> Math.sin(p.magnitude() + 0.00000000000000001) / (p.magnitude() + 0.00000000000000001);

    public void addWireframe(Group sceneRoot, int size, int spacing, double sceneWidth, double sceneHeight) {
        float [][] arrayY = new float[2*size][2*size];
        //The Sombrero
        for(int i=-size;i<size;i++) {
            for(int j=-size;j<size;j++) {
                double R = Math.sqrt((i * i)  + (j * j)) + epsilon;
                arrayY[i+size][j+size] = ((float) -(Math.sin(R)/R)) * 100;
            }
        }
        
        float xOffset = (arrayY.length * spacing)/2;
        
        TriangleMesh mesh = new TriangleMesh();

        // Fill Points
        for (int x = 0; x < arrayY.length; x++) {
            for (int y = 0; y < arrayY[0].length; y++) {
                mesh.getPoints().addAll((x * spacing)-xOffset, arrayY[x][y], (y * spacing)-xOffset);
            }
        }
        
        
        Surface surfacePlot = new Surface(mesh, arrayY.length, Color.BLACK, false, true);

        sceneRoot.getChildren().addAll(surfacePlot);

        PointLight light = new PointLight(Color.WHITE);
        sceneRoot.getChildren().add(light);
        light.setTranslateZ(sceneWidth / 2);
        light.setTranslateY(-sceneHeight + 10);

        PointLight light2 = new PointLight(Color.WHITE);
        sceneRoot.getChildren().add(light2);
        light2.setTranslateZ(-sceneWidth + 10);
        light2.setTranslateY(-sceneHeight + 10);
    }

    public void addScatterDemo(Group sceneRoot) {
        // Create and add some data to the Cube
        ArrayList<Double> dataX = new ArrayList<>();
        ArrayList<Double> dataY = new ArrayList<>();
        ArrayList<Double> dataZ = new ArrayList<>();
        generateData(dataX, dataY, dataZ);

        // The cube viewer will add data nodes as cubes here but you can add
        // your own scatter plot to the same space as the cube if you want.
        /*cubeViewer.setxAxisData(dataX);
        cubeViewer.setyAxisData(dataY);
        cubeViewer.setzAxisData(dataZ);*/

        List<Point3D> data = generateScatter();
        addScatter(sceneRoot, data);
    }

    public void generateData(ArrayList<Double> dataX, ArrayList<Double> dataY, ArrayList<Double> dataZ) {
        for (int i = -250; i < 250; i++) {
            dataX.add(new Double(i));
            dataY.add(new Double(Math.sin(i) * 50) + i);
            dataZ.add(new Double(Math.cos(i) * 50) + i);
        }
    }


    public List<Point3D> generateScatter() {
        List<Point3D> data = new ArrayList<>();
        IntStream.range(0, 100000).forEach(i -> data.add(new Point3D((float) (30 * Math.sin(50 * i)), (float) (Math.sin(i) * (100 + 30 * Math.cos(100 * i))), (float) (Math.cos(i) * (100 + 30 * Math.cos(200 * i))), i))); // <--
        return data;
    }

    public void addScatter(Group sceneRoot, List<Point3D> data) {
        ScatterMesh scatter = new ScatterMesh(data, true, 1, 0);
        // NONE
        // scatter.setTextureModeNone(Color.ROYALBLUE);
        // scatter.setDrawMode(DrawMode.LINE);
        // IMAGE
        // scatter.setTextureModeImage(getClass().getResource("res/steel-background1.jpg").toExternalForm());
        // scatter.setTextureModeImage(getClass().getResource("res/share-carousel2.jpg").toExternalForm());
        // DENSITY
        scatter.setTextureModeVertices3D(1530, p -> p.magnitude());
        sceneRoot.getChildren().add(scatter);
        // sceneRoot.getChildren().addAll(group);
    }

    public void addSurface(Group sceneRoot, Function<Point2D,Number> function, double rangeX, double rangeY, int divisionsX, int divisionsY, double functionScale) {
        SurfacePlotMesh surface = new SurfacePlotMesh(function, rangeX, rangeY, divisionsX, divisionsY, functionScale);
        // PhongMaterial matTorus = new PhongMaterial(Color.FIREBRICK);
        // torus.setMaterial(matTorus);
        // surface.setDrawMode(DrawMode.LINE);
        surface.setCullFace(CullFace.NONE);
        // NONE
        // surface.setTextureModeNone(Color.FORESTGREEN);
        // IMAGE
        // torus.setTextureModeImage(getClass().getResource("res/grid1.png").toExternalForm());
        // banner.setTextureModeImage(getClass().getResource("res/Duke3DprogressionSmall.jpg").toExternalForm());
        // PATTERN
        surface.setTextureModePattern(CarbonPatterns.CARBON_KEVLAR, 1.0d);
        // DENSITY
        surface.setTextureModeVertices3D(1530, dens);
        // FACES
        surface.setTextureModeFaces(256 * 256);

        Rotate rotateY = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);

        surface.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS), rotateY);

        sceneRoot.getChildren().addAll(surface);
    }
}

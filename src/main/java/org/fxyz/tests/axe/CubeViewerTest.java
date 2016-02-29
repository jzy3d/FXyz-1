/*
 * Copyright (C) 2013-2015 F(X)yz, 
 * Sean Phillips, Jason Pollastrini and Jose Pereda
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fxyz.tests.axe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.CullFace;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import org.fxyz.cameras.CameraTransformer;
import org.fxyz.geometry.Point3D;
import org.fxyz.shapes.composites.SurfacePlot;
import org.fxyz.shapes.primitives.ScatterMesh;
import org.fxyz.shapes.primitives.SurfacePlotMesh;
import org.fxyz.tools.CubeViewer;
import org.fxyz.utils.Patterns.CarbonPatterns;

import javafx.scene.shape.Polygon;
/**
 *
 * @author Sean
 */
@SuppressWarnings("restriction")

public class CubeViewerTest extends Application {
    private PerspectiveCamera camera;
    private final double sceneWidth = 600;
    private final double sceneHeight = 600;
    private double cameraDistance = 5000;
    private CubeViewer cubeViewer;
    private CameraTransformer cameraTransform = new CameraTransformer();

    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;

    private Function<Point3D, Number> dens = p->p.x;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group sceneRoot = new Group();
        Scene scene = new Scene(sceneRoot, sceneWidth, sceneHeight, true, SceneAntialiasing.BALANCED);
        //scene.setFill(Color.BLACK);
        scene.setFill(Color.WHITESMOKE);
        // Setup camera and scatterplot cubeviewer
        
        setupCamera(sceneRoot, scene, -3300);
        int cubeWidth = 1000;
        int cubeLineSpace = 50;
        
        addAxe(sceneRoot, cubeWidth, cubeLineSpace);
        
        addScatterDemo(sceneRoot);
        
        //Polygon polygon = new Polygon();
        //polygon.getPoints();
        
        int range = 1000;
        double ratio = 5000d;
        int step = 100;
        //addSurface(sceneRoot, p -> Math.sin(p.magnitude() + 0.00000000000000001) / (p.magnitude() + 0.00000000000000001), range, range, step, step, ratio);

        
        int size = 10;
        addWireframe(sceneRoot, size);
        
        // First person shooter keyboard movement
        setupKeyboardCameraController(scene);
        setupMouseCameraController(scene);

        show(primaryStage, scene, "F(X)yz CubeViewerTest");
    }

    private void addWireframe(Group sceneRoot, int size) {
        float [][] arrayY = new float[2*size][2*size];
        //The Sombrero
        for(int i=-size;i<size;i++) {
            for(int j=-size;j<size;j++) {
                double R = Math.sqrt((i * i)  + (j * j)) + 0.00000000000000001;
                arrayY[i+size][j+size] = ((float) -(Math.sin(R)/R)) * 100;
            }
        }
        SurfacePlot surfacePlot = new SurfacePlot(arrayY, 10, Color.AQUA, false, false);

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

    private void addScatterDemo(Group sceneRoot) {
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

    private void addAxe(Group sceneRoot, int cubeWidth, int cubeLineSpace) {
        cubeViewer = new CubeViewer(cubeWidth, cubeLineSpace, true);
        sceneRoot.getChildren().addAll(cubeViewer);
        // setup camera transform for rotational support
        cubeViewer.getChildren().add(cameraTransform);
    }

    private List<Point3D> generateScatter() {
        List<Point3D> data = new ArrayList<>();
        IntStream.range(0, 100000).forEach(i -> data.add(new Point3D((float) (30 * Math.sin(50 * i)), (float) (Math.sin(i) * (100 + 30 * Math.cos(100 * i))), (float) (Math.cos(i) * (100 + 30 * Math.cos(200 * i))), i))); // <--
        return data;
    }

    private void addScatter(Group sceneRoot, List<Point3D> data) {
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

    private void addSurface(Group sceneRoot, Function<Point2D,Number> function, double rangeX, double rangeY, int divisionsX, int divisionsY, double functionScale) {
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

    private void show(Stage primaryStage, Scene scene, String title) {
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void generateData(ArrayList<Double> dataX, ArrayList<Double> dataY, ArrayList<Double> dataZ) {
        for (int i = -250; i < 250; i++) {
            dataX.add(new Double(i));
            dataY.add(new Double(Math.sin(i) * 50) + i);
            dataZ.add(new Double(Math.cos(i) * 50) + i);
        }
    }

    private void setupMouseCameraController(Scene scene) {
        scene.setOnMousePressed((MouseEvent me) -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged((MouseEvent me) -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);

            double modifier = 10.0;
            double modifierFactor = 0.1;

            if (me.isControlDown()) {
                modifier = 0.1;
            }
            if (me.isShiftDown()) {
                modifier = 50.0;
            }
            if (me.isPrimaryButtonDown()) {
                cameraTransform.ry.setAngle(((cameraTransform.ry.getAngle() + mouseDeltaX * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180); // +
                cameraTransform.rx.setAngle(((cameraTransform.rx.getAngle() - mouseDeltaY * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180); // -
                cubeViewer.adjustPanelsByPos(cameraTransform.rx.getAngle(), cameraTransform.ry.getAngle(), cameraTransform.rz.getAngle());
            } else if (me.isSecondaryButtonDown()) {
                double z = camera.getTranslateZ();
                double newZ = z + mouseDeltaX * modifierFactor * modifier;
                camera.setTranslateZ(newZ);
            } else if (me.isMiddleButtonDown()) {
                cameraTransform.t.setX(cameraTransform.t.getX() + mouseDeltaX * modifierFactor * modifier * 0.3); // -
                cameraTransform.t.setY(cameraTransform.t.getY() + mouseDeltaY * modifierFactor * modifier * 0.3); // -
            }
        });
    }

    private void setupKeyboardCameraController(Scene scene) {
        scene.setOnKeyPressed(event -> {
            double change = 10.0;
            // Add shift modifier to simulate "Running Speed"
            if (event.isShiftDown()) {
                change = 50.0;
            }
            // What key did the user press?
            KeyCode keycode = event.getCode();
            // Step 2c: Add Zoom controls
            if (keycode == KeyCode.W) {
                camera.setTranslateZ(camera.getTranslateZ() + change);
            }
            if (keycode == KeyCode.S) {
                camera.setTranslateZ(camera.getTranslateZ() - change);
            }
            // Step 2d: Add Strafe controls
            if (keycode == KeyCode.A) {
                camera.setTranslateX(camera.getTranslateX() - change);
            }
            if (keycode == KeyCode.D) {
                camera.setTranslateX(camera.getTranslateX() + change);
            }
        });
    }

    private void setupCamera(Group sceneRoot, Scene scene, double cameraTranslateZ) {
        camera = new PerspectiveCamera(true);
        
        
        cameraTransform.setTranslate(0, 0, 0);
        cameraTransform.getChildren().add(camera);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        //camera.setTranslateZ(-5000);
        camera.setTranslateZ(cameraTranslateZ);
        //camera.setTranslateY(1000);
        cameraTransform.ry.setAngle(-45.0);
        cameraTransform.rx.setAngle(-10.0);
        // add a Point Light for better viewing of the grid coordinate system
        PointLight light = new PointLight(Color.WHITE);
        cameraTransform.getChildren().add(light);
        light.setTranslateX(camera.getTranslateX());
        light.setTranslateY(camera.getTranslateY());
        light.setTranslateZ(camera.getTranslateZ());
        scene.setCamera(camera);
    }

    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

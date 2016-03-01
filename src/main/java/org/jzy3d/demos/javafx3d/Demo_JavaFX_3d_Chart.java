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
package org.jzy3d.demos.javafx3d;

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
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import org.fxyz.cameras.CameraTransformer;
import org.fxyz.geometry.Point3D;
import org.fxyz.shapes.primitives.ScatterMesh;
import org.fxyz.shapes.primitives.SurfacePlotMesh;
import org.fxyz.shapes.primitives.Text3DMesh;
import org.fxyz.tools.CubeViewer;
import org.fxyz.utils.Patterns.CarbonPatterns;

/**
 *
 * @author Sean
 */
@SuppressWarnings("restriction")
public class Demo_JavaFX_3d_Chart extends Application {
    public static void main(String[] args) {
        //System.setProperty("prism.lcdtext", "false");
        //System.setProperty("prism.text", "t2k");
        launch(args);
    }

    Settings settings;
    Chart chart;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Settings settings = new Settings();
        settings.sceneWidth = 600;
        settings.sceneHeight = 600;
        settings.cubeWidth = 1000;
        settings.cubeLineSpace = 50;
        settings.cameraTranslateZ = -3500;
        settings.sceneColor = Color.WHITESMOKE;
        settings.axeColor = Color.WHITE;
        
        Chart chart = new Chart(settings);
        chart.show(primaryStage, Demo_JavaFX_3d_Chart.class.getSimpleName());

        initSceneGraph(chart);
    }

    public void initSceneGraph(Chart chart) {
        DrawableFactory f = new DrawableFactory();

        // Scatter
        //f.addScatterDemo(chart.getGraph());

        // Surface
        int SurfaceRange = 1000;
        double ZRatio = 5000d;
        int SurfaceStep = 100;
        //f.addSurface(chart.getGraph(), f.sample, SurfaceRange, SurfaceRange, SurfaceStep, SurfaceStep, ZRatio);

        // Wireframe
        int WireFrameSteps = 10;
        int WireFrameSpacing = 100;
        f.addWireframe(chart.getGraph(), WireFrameSteps / 2, WireFrameSpacing, chart.getSettings().sceneWidth, chart.getSettings().sceneHeight);
    }

}

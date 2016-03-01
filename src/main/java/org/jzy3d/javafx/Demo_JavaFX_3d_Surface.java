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
package org.jzy3d.javafx;

import java.util.function.Function;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.fxyz.geometry.Point3D;
import org.jzy3d.javafx.chart.Chart;
import org.jzy3d.javafx.chart.Settings;
import org.jzy3d.javafx.drawables.DrawableFactory;

/**
 *
 * @author Sean
 */
@SuppressWarnings("restriction")
public class Demo_JavaFX_3d_Surface extends Application {
    public static void main(String[] args) {
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.text", "t2k");
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
        chart.show(primaryStage, Demo_JavaFX_3d_Surface.class.getSimpleName());

        initSceneGraph(chart);
    }

    public void initSceneGraph(Chart chart) {
        DrawableFactory f = new DrawableFactory();

        // Surface
        // coloring surface :
        // http://stackoverflow.com/questions/26831871/coloring-individual-triangles-in-a-triangle-mesh-on-javafx
        // http://stackoverflow.com/questions/31073007/how-to-create-a-3d-surface-chart-with-javafx/31125736#31125736
        int SurfaceRange = 800;
        double ZRatio = 0.25d;
        int SurfaceStep = 100;
        
        Function<Point2D,Number> function = f.jzy;
        Function<Point3D,Number> colormap = f.colormap;
        
        f.addSurface(chart.getGraph(), function, colormap, SurfaceRange, SurfaceRange, SurfaceStep, SurfaceStep, ZRatio);
    }

}

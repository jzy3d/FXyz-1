package org.jzy3d.javafx.chart;

import org.jzy3d.javafx.view.View;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Chart {
    Settings settings;
    View view;
    Scene scene;
    Group graph;

    public Chart() {
    }

    public Chart(Settings settings) {
        init(settings);
    }

    public void show(Stage primaryStage, String title) {
        getView().show(primaryStage, getScene(), title);
    }

    protected void init(Settings settings) {
        this.settings = settings;
        Group graph = getGraph();
        Scene scene = getScene(graph, settings.sceneWidth, settings.sceneHeight, settings.sceneColor);
        View view = getView();
        
        view.init(settings, graph, scene);
    }

    

    public View getView() {
        if (view == null) {
            view = new View();
        }
        return view;
    }

    public Scene getScene() {
        return scene;
    }

    /** init method.  */
    protected Scene getScene(Group sceneRoot, double sceneWidth, double sceneHeight, Color fillColor) {
        if (scene == null) {
            scene = new Scene(sceneRoot, sceneWidth, sceneHeight, true, SceneAntialiasing.BALANCED);
            scene.setFill(fillColor);
        }
        return scene;
    }

    public Group getGraph() {
        if (graph == null) {
            graph = new Group();
        }
        return graph;
    }

    public Settings getSettings() {
        return settings;
    }
}

package org.jzy3d.javafx.view;

import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.ParallelCamera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.fxyz.cameras.CameraTransformer;
import org.jzy3d.javafx.chart.Settings;

public class View {
    Settings settings = new Settings();
    
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;

    private Camera camera;
    private CameraTransformer cameraTransform = new CameraTransformer();

    private AxeBox axe;

    public void show(Stage primaryStage, Scene scene, String title) {
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void init(Settings settings, Group graph, Scene scene) {
        initCamera(graph, scene, settings);
        initAxe(graph, settings.cubeWidth, settings.cubeLineSpace, settings.axeColor);
        initKeyboardCamera(scene);
        initMouseCamera(scene);
    }

    public void initCamera(Group sceneRoot, Scene scene, Settings settings) {
        camera = new PerspectiveCamera(true);
        //camera = new ParallelCamera();
        transformDefault(settings.cameraTranslateZ);
        clippingPlanes();
        if (settings.isFlipped)
            rotate3dDefault();
        addLightOnCamera(scene);
    }

    private void transformDefault(double cameraTranslateZ) {
        cameraTransform.setTranslate(0, 0, 0);
        cameraTransform.getChildren().add(camera);
        // camera.setTranslateZ(-5000);
        camera.setTranslateZ(cameraTranslateZ);
    }

    private void clippingPlanes() {
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
    }

    private void addLightOnCamera(Scene scene) {
        PointLight light = new PointLight(Color.WHITE);
        cameraTransform.getChildren().add(light);
        light.setTranslateX(camera.getTranslateX());
        light.setTranslateY(camera.getTranslateY());
        light.setTranslateZ(camera.getTranslateZ());
        scene.setCamera(camera);
    }

    private void rotate3dDefault() {
        // camera.setTranslateY(1000);
        // cameraTransform.ry.setAngle(90);//-45.0);
        cameraTransform.rx.setAngle(180 + 90);// -10.0);
        // cameraTransform.rz.setAngle(90);//-45.0);
        // add a Point Light for better viewing of the grid coordinate system
    }

    public void initAxe(Group sceneRoot, int cubeWidth, int cubeLineSpace, Color axeColor) {
        axe = new AxeBox(cubeWidth, cubeLineSpace, true);
        sceneRoot.getChildren().addAll(axe);
        // setup camera transform for rotational support
        axe.getChildren().add(getCameraTransform());
        axe.setColor(axeColor);
    }

    public void initMouseCamera(Scene scene) {
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
                if(!settings.isFlipped){
                    cameraTransform.ry.setAngle(((cameraTransform.ry.getAngle() + mouseDeltaX * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180); // +
                    cameraTransform.rx.setAngle(((cameraTransform.rx.getAngle() - mouseDeltaY * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180); // -
                    axe.adjustPanelsByPos(cameraTransform.rx.getAngle(), cameraTransform.ry.getAngle(), cameraTransform.rz.getAngle());
                }
                else{
                    cameraTransform.rz.setAngle(((cameraTransform.rz.getAngle() + mouseDeltaX * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180); // +
                    cameraTransform.rx.setAngle(((cameraTransform.rx.getAngle() - mouseDeltaY * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180); // -
                    axe.adjustPanelsByPos(cameraTransform.rx.getAngle(), cameraTransform.ry.getAngle(), cameraTransform.rz.getAngle());
                    
                }
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

    public void initKeyboardCamera(Scene scene) {
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

    public Camera getCamera() {
        return camera;
    }

    public CameraTransformer getCameraTransform() {
        return cameraTransform;
    }

    public AxeBox getAxe() {
        return axe;
    }

}

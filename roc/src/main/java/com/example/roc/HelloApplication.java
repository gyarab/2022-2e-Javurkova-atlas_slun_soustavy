package com.example.roc;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    Sphere sun = new Sphere(400);
    Sphere mercury = new Sphere(20);
    Sphere venus = new Sphere(25);
    Sphere earth = new Sphere(35);
    Sphere mars = new Sphere(32);
    Sphere jupiter = new Sphere(70);
    Sphere saturn = new Sphere(50);
    Sphere uranus = new Sphere(40);
    Sphere neptune = new Sphere(35);


    @Override
    public void start(Stage stage) throws IOException {
        /*ArrayList<Sphere> planety = new ArrayList<>();
        planety.add(new Sphere(400));*/

        sun.translateXProperty().set(1);
        sun.translateYProperty().set(400);
        PhongMaterial m1 = new PhongMaterial();
        m1.setDiffuseMap(new Image("file:///C:/Users/sabča/Desktop/prog/roc/src/main/resources/sun.jpg"));
        sun.setMaterial(m1);

        mercury.translateXProperty().set(500);
        mercury.translateYProperty().set(400);
        PhongMaterial m2 = new PhongMaterial();
        m2.setDiffuseMap(new Image("file:///C:/Users/sabča/Desktop/prog/roc/src/main/resources/mercury.jpg"));
        mercury.setMaterial(m2);

        venus.translateXProperty().set(580);
        venus.translateYProperty().set(400);
        PhongMaterial m3 = new PhongMaterial();
        m3.setDiffuseMap(new Image("file:///C:/Users/sabča/Desktop/prog/roc/src/main/resources/venus.jpg"));
        venus.setMaterial(m3);

        earth.translateXProperty().set(670);
        earth.translateYProperty().set(400);
        PhongMaterial m4 = new PhongMaterial();
        m4.setDiffuseMap(new Image("file:///C:/Users/sabča/Desktop/prog/roc/src/main/resources/earth.jpg"));
        earth.setMaterial(m4);

        mars.translateXProperty().set(760);
        mars.translateYProperty().set(400);
        PhongMaterial m5 = new PhongMaterial();
        m5.setDiffuseMap(new Image("file:///C:/Users/sabča/Desktop/prog/roc/src/main/resources/mars.jpg"));
        mars.setMaterial(m5);

        jupiter.translateXProperty().set(890);
        jupiter.translateYProperty().set(400);
        PhongMaterial m6 = new PhongMaterial();
        m6.setDiffuseMap(new Image("file:///C:/Users/sabča/Desktop/prog/roc/src/main/resources/jupiter.jpg"));
        jupiter.setMaterial(m6);

        saturn.translateXProperty().set(1040);
        saturn.translateYProperty().set(400);
        PhongMaterial m7 = new PhongMaterial();
        m7.setDiffuseMap(new Image("file:///C:/Users/sabča/Desktop/prog/roc/src/main/resources/saturn.jpg"));
        saturn.setMaterial(m7);

        uranus.translateXProperty().set(1160);
        uranus.translateYProperty().set(400);
        PhongMaterial m8 = new PhongMaterial();
        m8.setDiffuseMap(new Image("file:///C:/Users/sabča/Desktop/prog/roc/src/main/resources/uranus.jpg"));
        uranus.setMaterial(m8);

        neptune.translateXProperty().set(1260);
        neptune.translateYProperty().set(400);
        PhongMaterial m9 = new PhongMaterial();
        m9.setDiffuseMap(new Image("file:///C:/Users/sabča/Desktop/prog/roc/src/main/resources/neptune.jpg"));
        neptune.setMaterial(m9);


        animation();
        Group root =new Group();
        root.getChildren().add(sun);
        root.getChildren().add(mercury);
        root.getChildren().add(venus);
        root.getChildren().add(earth);
        root.getChildren().add(mars);
        root.getChildren().add(jupiter);
        root.getChildren().add(saturn);
        root.getChildren().add(uranus);
        root.getChildren().add(neptune);
        Scene scene = new Scene(root,1500,800);
        stage.setTitle("Solar system");
        stage.setScene(scene);
        stage.show();
    }

    private void animation() {//tahle metoda se volá stále dokola, tím pádem se úhel mění konstantně
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                sun.setRotationAxis(new Point3D(0,1,0));//točí se podle y osy
                sun.rotateProperty().set(sun.getRotate()+0.03);//getRotate nám dá současný úhel, +x nám přídá rychlost

                mercury.setRotationAxis(new Point3D(0,1,0));
                mercury.rotateProperty().set(mercury.getRotate()+0.3);

                venus.setRotationAxis(new Point3D(0,1,0));
                venus.rotateProperty().set(venus.getRotate()+0.1);

                earth.setRotationAxis(new Point3D(0,1,0));
                earth.rotateProperty().set(earth.getRotate()+0.35);

                mars.setRotationAxis(new Point3D(0,1,0));
                mars.rotateProperty().set(mars.getRotate()+0.35);

                jupiter.setRotationAxis(new Point3D(0,1,0));
                jupiter.rotateProperty().set(jupiter.getRotate()+1);

                saturn.setRotationAxis(new Point3D(0,1,0));
                saturn.rotateProperty().set(saturn.getRotate()+0.9);

                uranus.setRotationAxis(new Point3D(0,1,0));
                uranus.rotateProperty().set(uranus.getRotate()+0.5);

                neptune.setRotationAxis(new Point3D(0,1,0));
                neptune.rotateProperty().set(neptune.getRotate()+0.6);
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch();
    }
}
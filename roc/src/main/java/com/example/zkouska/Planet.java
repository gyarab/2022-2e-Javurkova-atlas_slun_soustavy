package com.example.zkouska;

import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

// ještě musím zakomponovat do main

public class Planet extends Sphere {
    String name;
    int size;
    int xProperty;
    double rotationSpeed;
    String coatLocation;

    public Planet(String name, int size, int xProperty, double rotationSpeed, String coatLocation) {
        super(size);
        this.name = name;
        this.size = size;
        this.xProperty = xProperty;
        this.rotationSpeed = rotationSpeed;
        this.coatLocation = coatLocation;

        PhongMaterial coat = new PhongMaterial();
        if (name.equals("Sun")) {
            coat.setSelfIlluminationMap(new Image(coatLocation));
        }else{
            coat.setDiffuseMap(new Image(coatLocation));
        }
        if (name.equals("Earth")) {
            coat.setSelfIlluminationMap(new Image("C:/Users/sabča/Desktop/programovani/zkouska/src/main/resources/selfillumination.jpg"));
            coat.setBumpMap(new Image("C:/Users/sabča/Desktop/programovani/zkouska/src/main/resources/bumps.png"));
        }

        this.setMaterial(coat);
        this.translateXProperty().set(xProperty);
        this.translateYProperty().set(400);
    }

    public void rotate() {
        this.setRotationAxis(new Point3D(0,1,0));
        this.rotateProperty().set(this.getRotate() + rotationSpeed);
    }
}

package com.example.rocnikova_prace;

import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

import java.io.IOException;
import java.net.URL;

public class Planet extends Sphere {

    String name;
    int size;
    int xProperty;
    double rotationSpeed;
    URL coatURL;
    String path;

    public Planet(String name, int size, int xProperty, double rotationSpeed, URL coatURL) throws IOException {

        super(size);
        this.name = name;
        this.size = size;
        this.xProperty = xProperty;
        this.rotationSpeed = rotationSpeed;
        this.coatURL = coatURL;

        PhongMaterial coat = new PhongMaterial();

        if (!name.equals("Sun")) {
            coat.setDiffuseMap(new Image(coatURL.toExternalForm()));
        } else{
            coat.setSelfIlluminationMap(new Image(coatURL.toExternalForm()));
        }

        if (name.equals("Earth")) {
            coat.setSelfIlluminationMap(new Image(getClass().getResource("/Coats/Earth_SelfIllumination.jpg").openStream()));
            coat.setBumpMap(new Image(getClass().getResource("/Coats/Earth_bumps.png").openStream()));
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

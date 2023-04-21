package com.example.zkouska;

import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;

public class Planet extends Sphere {
    String name;
    double rotationSpeed;

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(Sphere planet, int size) {
        planet.setRadius(size);
    }

    public void setProperties(Sphere planet, int xProperty) {
        planet.getTransforms().add(new Translate(xProperty,400,0));
    }

    /*public void setRotation(Sphere planet, double rotationSpeed) {
        planet.setRotationAxis(new Point3D(0,1,0));
        this.rotationSpeed = rotationSpeed;
    }*/

    public void setCoat(Sphere planet, String location) {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(location));

        planet.setMaterial(material);
    }
}

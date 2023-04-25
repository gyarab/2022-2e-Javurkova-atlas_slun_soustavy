package com.example.rocnikova_prace;

import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;


    /**
     * Třída Planet je potomek třídy Sphere.
     *
     * Prvně vytvořím proměnné potřebné pro vytvoření planety.
     * Poté generuji konstruktor s atributy name, size, xProperty, rotationSpeed a coatUrl.
     * Získanou proměnou size uložím jako poloměr objektu Planet využitím funkce super, což zavolá nadtřídu, tedy Sphere.
     *
     * Vytvořím proměnnou PhongMaterial m do které podle aktuálního objektu Planet ukládám její "kabát".
     * Všem objektům Planet definuji jejich X souřadnici pomocí proměnné xProperty a Y souřadnici jako 400 - tu mají všechny stejné.
     *
     * Metoda rotate() se stará o to aby se všechny objekty Planet rotovali svojí vlastní rychlostí.
     * Všechny mají stejný rotační bod s hodnotou 0 v X,Z a 1 v Y.
     * Rotaci objektu zajistím metodami rotateProperty() a getRotate().
    */

public class Planet extends Sphere {

    String name;
    int size;
    int xProperty;
    double rotationSpeed;
    URL coatURL;

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

            coat.setSelfIlluminationMap(new Image(Objects.requireNonNull(getClass().getResource("/Coats/Earth_SelfIllumination.jpg")).openStream()));
            coat.setBumpMap(new Image(Objects.requireNonNull(getClass().getResource("/Coats/Earth_bumps.png")).openStream()));

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

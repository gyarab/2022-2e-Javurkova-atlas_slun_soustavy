package com.example.zkouska;

import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class HelloApplication extends Application {
    
    public final int HEIGHT = 1400;
    public final int WIDTH = 700;

    Sphere[] planets = new Sphere[9];
    private final String[] names = {"Sun","Mercury","Venus","Earth","Mars", "Jupiter","Saturn","Uranus","Neptune"};
    private final int[] sizes = {900,20,27,30,25,120,100,40,38};
    private final int[] xProperties = {-650,325,385,455,530,745,1080,1340,1480};
    private final double[] rotationSpeed = {0.03, 0.3, -0.1, 0.35, 0.35, 1, 0.9, -0.5, 0.6};
    //private final PhongMaterial[] coats = new PhongMaterial[9];
    private final String[] coatsLocation = {
            "C:/Users/sabča/Desktop/programovani/zkouska/src/main/resources/sun2.jpg",
            "file:///C:/Users/sabča/Desktop/prog/roc/src/main/resources/mercury.jpg",
            "file:///C:/Users/sabča/Desktop/prog/roc/src/main/resources/venus.jpg",
            "file:///C:/Users/sabča/Desktop/prog/roc/src/main/resources/earth.jpg",
            "file:///C:/Users/sabča/Desktop/prog/roc/src/main/resources/mars.jpg",
            "file:///C:/Users/sabča/Desktop/prog/roc/src/main/resources/jupiter.jpg",
            "file:///C:/Users/sabča/Desktop/prog/roc/src/main/resources/saturn.jpg",
            "file:///C:/Users/sabča/Desktop/prog/roc/src/main/resources/uranus.jpg",
            "file:///C:/Users/sabča/Desktop/prog/roc/src/main/resources/neptune.jpg"};
    private Node[] bigBang() {

        for (int i = 0; i < 9; i++) {
            planets[i] = new Sphere(sizes[i]);
            PhongMaterial coat = new PhongMaterial();
            if (i == 0) {
                coat.setSelfIlluminationMap(new Image(coatsLocation[0]));
            } else {
                coat.setDiffuseMap(new Image(coatsLocation[i]));
            }
            if (i == 3) {
                coat.setSelfIlluminationMap(new Image("C:/Users/sabča/Desktop/programovani/zkouska/src/main/resources/selfillumination.jpg"));
                //coat.setSpecularMap(new Image("C:/Users/sabča/Desktop/programovani/zkouska/src/main/resources/reflection.jpg"));
                coat.setBumpMap(new Image("C:/Users/sabča/Desktop/programovani/zkouska/src/main/resources/bumps.png"));
            }
            planets[i].setMaterial(coat);
            planets[i].translateXProperty().set(xProperties[i]);
            planets[i].translateYProperty().set(400);
        }

        /*for (int i = 0; i < 9; i++) {
            planets[i] = new Sphere(sizes[i]);
            PhongMaterial coat = new PhongMaterial();
            if (i==0){
                coat.setSelfIlluminationMap(new Image(coatsLocation[0]));
            }else {coat.setDiffuseMap(new Image(coatsLocation[i]));}
            if (i==3) {
                coat.setSelfIlluminationMap(new Image("C:/Users/sabča/Desktop/programovani/zkouska/src/main/resources/selfillumination.jpg"));
                //coat.setSpecularMap(new Image("C:/Users/sabča/Desktop/programovani/zkouska/src/main/resources/reflection.jpg"));
               coat.setBumpMap(new Image("C:/Users/sabča/Desktop/programovani/zkouska/src/main/resources/bumps.png"));
            }
            planets[i].setMaterial(coat);
            planets[i].translateXProperty().set(xProperties[i]);
            planets[i].translateYProperty().set(400);


            /*if (i==6){
                Sphere saturnRing = new Sphere(300);
                saturnRing.translateXProperty().set(xProperties[6]);
                saturnRing.translateYProperty().set(400);
                coat.setSpecularMap(new Image("C:/Users/sabča/Desktop/programovani/zkouska/src/main/resources/saturnRing.png"));
                saturnRing.setMaterial(coat);
            }*/
        /*planet.setRadius(size);
        PhongMaterial coat = new PhongMaterial();
        coat.setDiffuseMap(new Image(coatsLocation));
        planet.setMaterial(coat);
        planet.translateXProperty().set(xProperties);
        planet.translateYProperty().set(400);*/
        // return planet;
        return planets;
    }

    private void animation() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                for (int i = 0; i < 9; i++) {
                    planets[i].setRotationAxis(new Point3D(0,1,0));
                    planets[i].rotateProperty().set(planets[i].getRotate()+rotationSpeed[i]);
                }
                moon.setRotate(moon.getRotate()+0.9);
            }
        };timer.start();
    }

    Group universe = new Group();
    Camera camera = new PerspectiveCamera();
    @Override
    public void start(Stage stage) throws IOException {
        universe.getChildren().addAll(bigBang());
        universe.getChildren().add(moon());
        universe.getChildren().add(sunLight());
        universe.getChildren().add(background());
        universe.getChildren().addAll(circle(planets));
        universe.getChildren().addAll(titles(planets));
        universe.getChildren().addAll(saturnRings());

        Group root = new Group();
        root.getChildren().add(universe);
        root.getChildren().addAll(buttons());

        camera.setNearClip(0.01);
        camera.setFarClip(5000);

        Scene scene = new Scene(root, HEIGHT, WIDTH, true);
        scene.setCamera(camera);

        Image im = new Image("C:/Users/sabča/Desktop/programovani/zkouska/src/main/resources/cursor.png");
        scene.setCursor(new ImageCursor(im));

        animation();
        planetInteractions(planets);
        initMouseControl(universe, scene, stage);
        stage.setTitle("Solar system");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    private Node[] buttons() throws IOException {
        //Path filePath = Path.of("C:/Users/sabča/Desktop/programovani/zkouska/src/main/resources/info.txt");
        //String info = Files.readString(filePath);
        String info =
        "\tThe solar system is a collection of 8 planets (Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, Neptune), their moons and other smaller objects (such as comets or asteroids) bound by gravity and orbiting around our star Sun.\n" +
        "\tThe so-called inner system planets are Mercury, Venus, Earth and Mars. These 4 planets are terrestrial planets meaning they are solid and composed primarily of rock and metal. None of them have ring systems and they either have no moons or very few.\n" +
        "\tThe outer solar system is home to the giant planets and their large moons.The 4 furthest planets from the Sun are Jupiter, Saturn, Uranus and Neptun. They all have ring systems, although only Saturn´s is easily observed from Earth. The 2 largest ones - Jupiter and Saturn - are called gas giants, being composed mainly of hydrogen and helium. Uranus and Neptune are composed mostly of ice so they belong in their own category of ice giants.\n" +
        "\tAside from the planets the solar system contains a variety of celestial objects, including over 200 moons, asteroids located mainly between Mars and Jupiter, comets, Dwarf planets like Pluto and most importantly in the centre of the solar system is the Sun.\n" +
        "\n" +
        "Source: Solar System. (2023, April 8). In Wikipedia. https://en.wikipedia.org/wiki/Solar_System";

        Label text = new Label(info);

        text.setTextFill(Color.DARKBLUE);
        text.setPrefSize(1000,190);
        text.setWrapText(true);
        text.setFont(Font.font("Lucida Bright",12));
        //text.getTransforms().add(new Translate(400, 30));


        Rectangle box = new Rectangle(1020,210);
        //box.getTransforms().add(new Translate(400, 30));
        box.setArcWidth(10);
        box.setArcHeight(10);
        box.setFill(Color.BLACK);
        box.setStroke(Color.DARKBLUE);

        StackPane popUp = new StackPane(box, text);
        popUp.getTransforms().add(new Translate(775-box.getWidth()/2, 420-box.getHeight()/2,-400));
        popUp.setVisible(false);

        Button[] button = new Button[]{new Button("Info"),new Button("Exit")};
        for (int i = 0; i < button.length; i++) {
            button[i].setPrefSize(80,30);
            if(i==0){button[i].getTransforms().add(new Translate(1440,10));}
            else{button[i].getTransforms().add(new Translate(1440,820));}
            button[i].setFont(Font.font("Lucida Bright",9));
            button[i].setStyle("-fx-background-color: #191970; -fx-text-fill: white;");

            int finalI = i;
            button[i].setOnMouseClicked(MouseEvent -> {
                if(finalI==0){
                    if(!popUp.isVisible()){
                        popUp.setVisible(true);
                        button[finalI].setText("Back");
                    }
                    else{
                        popUp.setVisible(false);
                        button[finalI].setText("Info");
                    }
                }else{Platform.exit();}
            });
        }

        return new Node[]{button[0], button[1], popUp};
    }

    Sphere moon = new Sphere(6);
    private Node moon() {
        PhongMaterial m = new PhongMaterial();
        m.setDiffuseMap(new Image("C:/Users/sabča/Desktop/programovani/zkouska/src/main/resources/moon.jpg"));
        moon.setMaterial(m);
        moon.getTransforms().add(new Translate(planets[3].getTranslateX()+30,370));
        //moon.setRotationAxis(new Point3D(435,350,1));
        moon.setRotationAxis(new Point3D(planets[3].getTranslateX(),planets[3].getTranslateY(),planets[3].getTranslateZ()));

        return moon;
    }

    PointLight pl = new PointLight(Color.KHAKI.brighter());
    private Node sunLight() {
        //PointLight pl = new PointLight(Color.WHITESMOKE);
        pl.getTransforms().add(new Translate(0,400,-300));
        //pl.setMaxRange(1500);
        return pl;
    }
    private Node[] saturnRings() {

        Circle[] rings = new Circle[]{new Circle(200),new Circle(170),new Circle(140), new Circle(120)};
        rings[0].setStrokeWidth(10);
        rings[1].setStrokeWidth(30);
        rings[2].setStrokeWidth(20);
        rings[3].setStrokeWidth(5);
        Shadow g = new Shadow(5,Color.DARKGRAY);

        for (Circle c: rings) {
            c.getTransforms().add(new Translate(planets[6].getTranslateX(),planets[6].getTranslateY(),0));
            c.setRotationAxis(new Point3D(planets[6].getTranslateX(),planets[6].getTranslateY(),0));
            c.setRotate(110);
            c.setFill(Color.TRANSPARENT);
            c.setStroke(Color.DARKGRAY);
            c.setEffect(g);
        }

        return rings;
    }

    private Node background() {
        Image image = new Image("C:/Users/sabča/Desktop/programovani/zkouska/src/main/resources/background.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        // with cam: imageView.getTransforms().add(new Translate(-3000, -2000,3500));
        imageView.getTransforms().add(new Translate(-3300, -1600,1500));
        return imageView;
    }

    private double startingX, startingY;
    private double currentX, currentY;
    private final DoubleProperty newX = new SimpleDoubleProperty(0);
    private final DoubleProperty newY = new SimpleDoubleProperty(0);
    private void initMouseControl(Group group, Scene scene, Stage stage){//detects every mouse movement

        Translate tX;
        Translate tY;
        group.getTransforms().addAll(
                tX = new Translate(0,0,0),
                tY = new Translate(0,0,0)
        );
        tX.xProperty().bind(newX);
        tY.yProperty().bind(newY);

        scene.setOnMousePressed(mouseEvent -> {
            startingX = mouseEvent.getSceneX();
            startingY = mouseEvent.getSceneY();

            currentX = newX.get();
            currentY = newY.get();
        });

        scene.setOnMouseDragged(mouseEvent -> {
            newX.set(currentX + (mouseEvent.getSceneX()-startingX));
            newY.set(currentY + (mouseEvent.getSceneY()- startingY));
        });

        stage.addEventHandler(ScrollEvent.SCROLL, scrollEvent -> {
            double movement = scrollEvent.getDeltaY();
            group.translateZProperty().set(group.getTranslateZ() - 1.5*movement);
        });
    }

    private void planetInteractions(Sphere[] spheres) {
        for (int i = 0; i < 9; i++) {

            int finalI = i;
            spheres[i].setOnMouseEntered(mouseEvent -> {
                if(!zoomedIn){
                    c[finalI].setVisible(true);
                    titles[finalI].setVisible(true);
                }
            });
            //}
            spheres[i].setOnMouseExited(mouseEvent -> {
                c[finalI].setVisible(false );
                titles[finalI].setVisible(false);
            });

            spheres[i].setOnMouseClicked(mouseEvent -> {
                action(spheres, finalI);
            });
        }

    }

    boolean zoomedIn = false;
    private final Timeline zoomIn = new Timeline();
    private final Timeline zoomInPl = new Timeline();
    private final Timeline zoomInPlSun = new Timeline();
    private final Timeline zoomOut = new Timeline();
    private final KeyValue zoomOut1 = new KeyValue(pl.translateZProperty(), -300);
    private final KeyValue zoomOut2 = new KeyValue(pl.translateXProperty(), 0);
    private final KeyValue zoomIn1 = new KeyValue(pl.translateXProperty(), 750);
    private final KeyValue zoomIn2 = new KeyValue(pl.translateZProperty(), -800);
    private void action(Sphere[] sphere, int index) {

        zoomIn.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(2),
                        new KeyValue(sphere[index].translateXProperty (), WIDTH/2),
                        new KeyValue(sphere[index].radiusProperty(), 220)
        ));
        zoomInPl.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(2),
                        zoomIn1,zoomIn2
        ));
        zoomInPlSun.getKeyFrames().add(
                new KeyFrame(Duration.seconds(2),
                        new KeyValue(pl.translateZProperty(),1000)
        ));


        zoomOut.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(2),
                        new KeyValue(sphere[index].translateXProperty(), xProperties[index]),
                        new KeyValue(sphere[index].radiusProperty(), sizes[index]),
                        zoomOut1

        ));

        if(index != 0){
            zoomOut.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(2),
                            zoomOut2
            ));
        }

        if(!zoomedIn) {
            zoomedIn = true;

            zoomIn.play();
            if(index==0){zoomInPlSun.play();}
            else{zoomInPl.play();}

            for (int i = 0; i < sphere.length; i++) {
                if(i != index){
                    Timeline tl2 = new Timeline(
                    new KeyFrame(Duration.seconds(2),
                            //new KeyValue(sphere[i].radiusProperty(),0))
                            new KeyValue(sphere[i].radiusProperty(), 0)
                    ));tl2.play();
                }
            }
        }
        else{
            zoomOut.play();

            for (int i = 0; i < sphere.length; i++) {
                if(i != index){
                    Timeline tl2 = new Timeline(
                            new KeyFrame(Duration.seconds(2),
                                    new KeyValue(sphere[i].radiusProperty(), sizes[i])
                            ));tl2.play();
                }
            }

            zoomedIn=false;
        }

    }

    Circle[] c = new Circle[9];
    private Node[] circle(Sphere[] planets) {
        Glow glow = new Glow(0.4);

        for (int i = 0; i < 9; i++) {
            if(i==0){c[i]=new Circle(950);
            }else {c[i]=new Circle(planets[i].getRadius()+planets[i].getRadius()/6);}

            c[i].setEffect(glow);
            c[i].setFill(Color.TRANSPARENT);
            c[i].setStroke(Color.ROYALBLUE);
            c[i].getTransforms().add(new Translate(planets[i].getTranslateX(),planets[i].getTranslateY(),0));
            c[i].setVisible(false);
        }
        return c;
    }
    Text[] titles = new Text[9];
    private Node[] titles(Sphere[] planets) {
        Glow glow = new Glow(0.4);

        for (int i = 0; i < 9; i++) {
            titles[i] = new Text(names[i]);
            titles[i].setEffect(glow);
            titles[i].setFill(Color.ROYALBLUE);
            titles[i].setTextAlignment(TextAlignment.CENTER);
            titles[i].setFont(Font.font("Lucida Bright",9));

            if(i == 0){titles[i].getTransforms().add(new Translate(310,460,0));
            } else{
                titles[i].getTransforms().add(new Translate(planets[i].getTranslateX()-13, 400+c[i].getRadius()+16, 0));
            }

            titles[i].setVisible(false);
        }
        return titles;
    }

    public static void main(String[] args) {
        launch();
    }
}
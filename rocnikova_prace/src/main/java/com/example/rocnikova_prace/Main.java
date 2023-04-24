package com.example.rocnikova_prace;

import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Main extends Application {


    private final int WIDTH = 700;

    private final Planet[] planets = new Planet[9];

    private final String[] names = {"Sun","Mercury","Venus","Earth","Mars", "Jupiter","Saturn","Uranus","Neptune"};

    private final int[] sizes = {900,20,27,30,25,120,100,40,38};

    private final int[] xProperties = {-650,325,385,455,530,745,1080,1340,1480};

    private final double[] rotationSpeeds = {0.03, 0.3, -0.1, 0.35, 0.35, 1, 0.9, -0.5, 0.6};

    private final URL[] coatsUrls = {
            getClass().getResource("/Coats/Sun.jpg"),
            getClass().getResource("/Coats/Mercury.jpg"),
            getClass().getResource("/Coats/Venus.jpg"),
            getClass().getResource("/Coats/Earth.jpg"),
            getClass().getResource("/Coats/Mars.jpg"),
            getClass().getResource("/Coats/Jupiter.jpg"),
            getClass().getResource("/Coats/Saturn.jpg"),
            getClass().getResource("/Coats/Uranus.jpg"),
            getClass().getResource("/Coats/Neptune.jpg")
    };

    private final URL[] infoUrls = {
            getClass().getResource("/TxtFiles/Sun.txt"),
            getClass().getResource("/TxtFiles/Mercury.txt"),
            getClass().getResource("/TxtFiles/Venus.txt"),
            getClass().getResource("/TxtFiles/Earth.txt"),
            getClass().getResource("/TxtFiles/Mars.txt"),
            getClass().getResource("/TxtFiles/Jupiter.txt"),
            getClass().getResource("/TxtFiles/Saturn.txt"),
            getClass().getResource("/TxtFiles/Uranus.txt"),
            getClass().getResource("/TxtFiles/Neptune.txt")
    };

    private final Group universe = new Group();

    private final Camera camera = new PerspectiveCamera();

    private final URL systemInfoPath = getClass().getResource("/TxtFiles/info.txt");

    private final Sphere moon = new Sphere(6);

    private final PointLight pl = new PointLight(Color.KHAKI.brighter());

    private final Circle[] saturnRings = new Circle[]{
            new Circle(200),new Circle(170),
            new Circle(140), new Circle(120)};

    private final double[] ringRadiuses = new double[4];

    private double startingX, startingY;
    private double currentX, currentY;
    private final DoubleProperty newX = new SimpleDoubleProperty(0);
    private final DoubleProperty newY = new SimpleDoubleProperty(0);

    private boolean zoomedIn = false;

    private final Timeline zoomInSize = new Timeline();
    private final TranslateTransition zoomInX = new TranslateTransition(Duration.seconds(1.5));

    private final KeyFrame zoomInPlKF = new KeyFrame(Duration.seconds(1.5),
            new KeyValue(pl.translateXProperty(),  750), new KeyValue(pl.translateZProperty(), -800));
    private final Timeline zoomInPl = new Timeline(zoomInPlKF);

    private final KeyFrame zoomInPlSunKF = new KeyFrame(Duration.seconds(1.5),
            new KeyValue(pl.translateZProperty(),1000));
    private final Timeline zoomInPlSun = new Timeline(zoomInPlSunKF);


    private final Timeline zoomOutSize = new Timeline(
            new KeyFrame(Duration.seconds(1.5),
                    new KeyValue(moon.radiusProperty(), 6),
                    new KeyValue(pl.translateZProperty(), -300)));
    private final TranslateTransition zoomOutX = new TranslateTransition(Duration.seconds(1.5));

    private final KeyFrame zoomOutPlKF =  new KeyFrame(Duration.seconds(1.5),
            new KeyValue(pl.translateXProperty(), 0));
    private final Timeline zoomOutPl = new Timeline(zoomOutPlKF);

    private StackPane currentInfoBox;

    private final Circle[] planetsHighlights = new Circle[9];

    private final Text[] planetsTitles = new Text[9];


    @Override
    public void start(Stage stage) throws IOException {

        universe.getChildren().addAll(bigBang());
        universe.getChildren().add(moon());
        universe.getChildren().add(sunLight());
        universe.getChildren().add(background());
        universe.getChildren().addAll(highlights());
        universe.getChildren().addAll(titles());
        universe.getChildren().addAll(saturnRings());

        Group root = new Group();
        root.getChildren().add(universe);
        root.getChildren().addAll(buttons());

        camera.setNearClip(0.01);

        Scene scene = new Scene(root, 1400, WIDTH, true);
        scene.setCamera(camera);

        Image im = new Image(Objects.requireNonNull(getClass().getResource("/Cursor.png")).openStream());
        scene.setCursor(new ImageCursor(im));

        animation();
        planetInteractions();
        initMouseControl(scene, stage);
        stage.setTitle("Solar system");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }


    private Node[] bigBang() throws IOException {

        for (int i = 0; i < 9; i++) {
            planets[i] = new Planet(names[i], sizes[i], xProperties[i], rotationSpeeds[i], coatsUrls[i]);
        }

        return planets;
    }

    private Node moon() throws IOException {

        PhongMaterial m = new PhongMaterial();
        m.setDiffuseMap(new Image(getClass().getResource("/Coats/Moon.jpg").openStream()));
        moon.setMaterial(m);
        moon.getTransforms().add(new Translate(planets[3].getTranslateX()+30,370));
        moon.setRotationAxis(new Point3D(planets[3].getTranslateX(),planets[3].getTranslateY(),planets[3].getTranslateZ()));

        return moon;
    }

    private Node sunLight() {

        pl.getTransforms().add(new Translate(0,400,-300));

        return pl;
    }

    private Node background() throws IOException {

        Image image = new Image(getClass().getResource("/Background.jpg").openStream());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.getTransforms().add(new Translate(-3300, -1600,1500));

        return imageView;
    }

    private Node[] highlights() {

        for (int i = 0; i < planets.length; i++) {

            if(i==0){
                planetsHighlights[i]=new Circle(950);
            }else {
                planetsHighlights[i]=new Circle(planets[i].getRadius()+planets[i].getRadius()/6);}

            planetsHighlights[i].setEffect(new Glow(0.4));
            planetsHighlights[i].setFill(Color.TRANSPARENT);
            planetsHighlights[i].setStroke(Color.ROYALBLUE);
            planetsHighlights[i].getTransforms().add(new Translate(planets[i].getTranslateX(),planets[i].getTranslateY(),0));
            planetsHighlights[i].setVisible(false);
        }

        return planetsHighlights;
    }

    private Node[] titles() {

        for (int i = 0; i < 9; i++) {

            planetsTitles[i] = new Text(names[i]);
            planetsTitles[i].setEffect(new Glow(0.4));
            planetsTitles[i].setFill(Color.ROYALBLUE);
            planetsTitles[i].setTextAlignment(TextAlignment.CENTER);
            planetsTitles[i].setFont(Font.font("Lucida Bright",9));

            if(i == 0){
                planetsTitles[i].getTransforms().add(new Translate(310,460,0));
            }else{
                planetsTitles[i].getTransforms().add(new Translate(planets[i].getTranslateX()-13, 400+ planetsHighlights[i].getRadius()+16, 0));
            }

            planetsTitles[i].setVisible(false);
        }

        return planetsTitles;
    }

    private Node[] saturnRings() {

        saturnRings[0].setStrokeWidth(10);
        saturnRings[1].setStrokeWidth(30);
        saturnRings[2].setStrokeWidth(20);
        saturnRings[3].setStrokeWidth(5);

        for (int i = 0; i < saturnRings.length; i++) {
            saturnRings[i].getTransforms().add(new Translate(planets[6].getTranslateX(), planets[6].getTranslateY(), 0));
            saturnRings[i].setRotationAxis(new Point3D(planets[6].getTranslateX(), planets[6].getTranslateY(), 0));
            saturnRings[i].setRotate(110);
            saturnRings[i].setFill(Color.TRANSPARENT);
            saturnRings[i].setStroke(Color.DARKGRAY);
            saturnRings[i].setEffect(new Shadow(5, Color.DARKGRAY));
            ringRadiuses[i] = saturnRings[i].getRadius();
        }

        return saturnRings;
    }

    private Node[] buttons() {

        StringBuilder info = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(systemInfoPath.getPath()))){
            String line;
            while ((line = reader.readLine()) != null){
                info.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Label systemInfo = new Label(info.toString());

        systemInfo.setTextFill(Color.DARKBLUE);
        systemInfo.setPrefSize(980,190);
        systemInfo.setWrapText(true);
        systemInfo.setFont(Font.font("Lucida Bright",12));


        Rectangle systemInfoBox = new Rectangle(1020, 210);
        systemInfoBox.setArcWidth(10);
        systemInfoBox.setArcHeight(10);
        systemInfoBox.setFill(Color.BLACK);
        systemInfoBox.setStroke(Color.DARKBLUE);

        StackPane infoPopUp = new StackPane(systemInfoBox, systemInfo);
        infoPopUp.getTransforms().add(new Translate(775- systemInfoBox.getWidth()/2, 420- systemInfoBox.getHeight()/2,-400));
        infoPopUp.setVisible(false);

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
                    if(!infoPopUp.isVisible()){
                        infoPopUp.setVisible(true);
                        button[finalI].setText("Back");
                    }
                    else{
                        infoPopUp.setVisible(false);
                        button[finalI].setText("Info");
                    }
                }else{
                    Platform.exit();}
            });
        }

        return new Node[]{button[0], button[1], infoPopUp};
    }

    private void animation() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {

                for (int i = 0; i < 9; i++) {
                    planets[i].rotate();
                }

                moon.setRotate(moon.getRotate()+0.9);
            }
        };timer.start();
    }

    private void planetInteractions() {

        for (int i = 0; i < 9; i++) {

            int finalI = i;
            planets[i].setOnMouseEntered(mouseEvent -> {
                if(!zoomedIn){
                    planetsHighlights[finalI].setVisible(true);
                    planetsTitles[finalI].setVisible(true);
                }
            });

            planets[i].setOnMouseExited(mouseEvent -> {
                planetsHighlights[finalI].setVisible(false);
                planetsTitles[finalI].setVisible(false);
            });

            planets[i].setOnMouseClicked(mouseEvent -> {
                try {
                    action(planets, finalI);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }

    private void action(Sphere[] sphere, int index) throws IOException {

        zoomInX.setNode(sphere[index]);
        zoomInX.setToX(WIDTH/2);
        zoomOutX.setNode(sphere[index]);
        zoomOutX.setToX(xProperties[index]);

        zoomInSize.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1.5),
                        new KeyValue(sphere[index].radiusProperty(), 220)
                ));

        zoomOutSize.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1.5),
                        new KeyValue(sphere[index].radiusProperty(), sizes[index])
                ));


        if(!zoomedIn) {
            currentInfoBox = prepareInfoBox(index);
            planetsInfoBox(zoomedIn, currentInfoBox);

            zoomedIn = true;

            zoomInSize.play();
            zoomInX.play();

            if(index==0){zoomInPlSun.play();}
            else{zoomInPl.play();}

            Timeline tlIn = new Timeline();
            for (int i = 0; i < sphere.length; i++) {
                if(i != index){
                    tlIn.getKeyFrames().add(
                            new KeyFrame(Duration.seconds(1.5),
                                    new KeyValue(sphere[i].radiusProperty(), 0)
                            ));
                }
            }

            for (Circle r : saturnRings) {
                tlIn.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(1.5),
                                new KeyValue(r.radiusProperty(), -1)
                        ));
            }
            tlIn.play();
        }
        else{

            zoomOutSize.play();
            zoomOutX.play();

            if(index != 0){zoomOutPl.play();}

            planetsInfoBox(zoomedIn, currentInfoBox);
            zoomedIn=false;

            Timeline tlOut = new Timeline();
            for (int i = 0; i < sphere.length; i++) {
                if(i != index){
                    tlOut.getKeyFrames().add(
                            new KeyFrame(Duration.seconds(1.5),
                                    new KeyValue(sphere[i].radiusProperty(), sizes[i])
                            ));
                }
            }

            for (int i = 0; i < saturnRings.length; i++) {
                tlOut.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(1.5),
                                new KeyValue(saturnRings[i].radiusProperty(), ringRadiuses[i])
                        ));
            }tlOut.play();
        }

    }

    private StackPane prepareInfoBox(int index) throws IOException {

        StringBuilder planetInfo = new StringBuilder();

        try(BufferedReader reader = new BufferedReader(new FileReader(infoUrls[index].getPath()))){
            String line;
            while ((line = reader.readLine()) != null){
                planetInfo.append(line).append("\n");
            }
        }

        Label text = new Label(planetInfo.toString());

        text.setText(planetInfo.toString());
        text.setTextFill(Color.DARKBLUE);
        text.setPrefSize(470,500);
        text.setWrapText(true);
        text.setFont(Font.font("Lucida Bright",11));

        Rectangle box = new Rectangle(500,400);
        box.setArcWidth(10);
        box.setArcHeight(10);
        box.setFill(Color.BLACK);
        box.setStroke(Color.DARKBLUE);

        StackPane infoBox = new StackPane(box,text);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setLayoutX(790);
        infoBox.setLayoutY(160);
        infoBox.setTranslateZ(-200);
        universe.getChildren().add(infoBox);
        return infoBox;
    }

    private void planetsInfoBox(boolean zoom, StackPane infoBox) {
        ScaleTransition scaleIn = new ScaleTransition(Duration.seconds(1.5),infoBox);
        scaleIn.setFromX(0);
        scaleIn.setFromY(0);
        scaleIn.setToX(1);
        scaleIn.setToY(1);

        ScaleTransition scaleOut = new ScaleTransition(Duration.seconds(1.5),infoBox);
        scaleOut.setFromX(1);
        scaleOut.setFromY(1);
        scaleOut.setToX(0);
        scaleOut.setToY(0);

        if(!zoom){
            scaleIn.play();
        }
        else{
            scaleOut.play();
        }
    }

    private void initMouseControl(Scene scene, Stage stage){//detects every mouse movement

        Translate tX;
        Translate tY;
        universe.getTransforms().addAll(
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
            universe.translateZProperty().set(universe.getTranslateZ() - 1.5*movement);
        });
    }


    public static void main(String[] args) {
        launch();
    }
}
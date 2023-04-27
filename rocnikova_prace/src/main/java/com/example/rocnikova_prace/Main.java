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


    /**
     * Na začátku kódu sepíšu proměnné, jejichž deklarace by se zakomponováním do metody opakovala,
     * nebo ty ke kterým potřebuju přístup z několika různých metod.
     */

    private final int HEIGHT = 700;

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

    private final URL systemInfoPath = getClass().getResource("/TxtFiles/Info.txt");

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


    /**
     * Metoda start() je první metoda která se při spuštění kódu volá.
     *
     * Do již deklarované proměnné universe typu Group přidám veškeré potřebné objekty pro zobrazení toho, co chci v programu vidět.
     *
     * Poté deklaruji proměnnou root typu do které přidám proměnnou universe a objekty vrácené metodou buttons(),
     * toto udělám protože proměnná universe je kontrolována myší - upřesněno u metody planetInteractions().
     *
     * Upřesním si atributy u již deklarované proměnné camera a přidám ji ke scéně.
     *
     * Vytvořím a deklaruju proměnou im typu Image, kterou využiji jako kurzor aplikace.
     *
     * Zavolám metody animation(), planetInteractions() a metodu initMouseControl() s atributy scene a stage.
     *
     * Proměnné stage, jenž představuje okno aplikace, přiřadím titulek, maximální velikosti, scénu a poté zavolám metodu show(), která okno zobrazí.
     */

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
        camera.setTranslateZ(-500);

        Scene scene = new Scene(root, 1400, HEIGHT, true);
        scene.setCamera(camera);

        Image im = new Image(Objects.requireNonNull(getClass().getResource("/Cursor.png")).openStream());
        scene.setCursor(new ImageCursor(im));

        animation();
        planetInteractions();
        initMouseControl(scene, stage);

        stage.setTitle("Solar system");
        stage.setScene(scene);
        stage.setMaxWidth(scene.getWidth());
        stage.setMaxHeight(scene.getHeight());
        stage.show();
    }


    /**
     * Metoda bigBang() je první metoda, která přidává objekty do proměnné universe.
     *
     * Metoda obsahuje pouze jeden for cyklus ve kterém se po jednom vytvoří 9 potřebných objektů Planet a uloží do pole planets.
     *
     * Vrací pole planets, což je pole 9- ti vytvořených Planet.
     */

    private Node[] bigBang() throws IOException {

        for (int i = 0; i < 9; i++) {
            planets[i] = new Planet(names[i], sizes[i], xProperties[i], rotationSpeeds[i], coatsUrls[i]);
        }

        return planets;
    }


    /**
     * Metoda moon() vrací objekt moon typu Sphere.
     *
     * Jako první vytvořím proměnnou m typu PhongMaterial, které přiřadím DiffuseMap a poté "obleču" sféru moon.
     *
     * U proměnné moon určím souřadnice a rotační bod RotationAxis, což bude objekt z pole planets umístěn na indexu s číslem 3, tedy Země.
     */

    private Node moon() throws IOException {

        PhongMaterial m = new PhongMaterial();
        m.setDiffuseMap(new Image(Objects.requireNonNull(getClass().getResource("/Coats/Moon.jpg")).openStream()));
        moon.setMaterial(m);

        moon.getTransforms().add(new Translate(planets[3].getTranslateX()+30,370));
        moon.setRotationAxis(new Point3D(planets[3].getTranslateX(),planets[3].getTranslateY(),planets[3].getTranslateZ()));

        return moon;
    }


    /**
     * Metoda sunlight() vrací proměnnou pl typu Pointlight, což představuje "svit Slunce".
     *
     * Jediné co tato metoda obsahuje je upřesnění souřadnic, ze které pl svítí.
     */

    private Node sunLight() {

        pl.getTransforms().add(new Translate(0,400,-300));

        return pl;
    }


    /**
     * Metoda background() je zodpovědna za vytvoření a vrácení proměnné imageView, která je poté zobrazena jako pozadí aplikace.
     *
     * Metoda obsahuje pouze upřesnění cesty k obrázku a jeho zobrazení.
     */

    private Node background() throws IOException {

        Image image = new Image(Objects.requireNonNull(getClass().getResource("/Background.jpg")).openStream());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.getTransforms().add(new Translate(-3300, -1600,1500));

        return imageView;
    }


    /**
     *  Metoda higlights() vrací pole planetsHighlights[] typu Circle.
     *
     *  Cyklus for zařídí aby všechny proměnné v tomto poli měly správné atributy.
     *
     *  Podmínka if(i == 0), je zde protože na místě v poli planetsHighlights s indexem 0 je barevné označení Slunce, které potřebuje speciální deklaraci kvůli obrovskému poloměru.
     *  Pro ostatní proměnné z pole planetsHiglights je jednotné určení.
     *
     *  Každé proměnné z pole přidám svítící efekt a ostatní detaily.
     *
     *  Poté jeho viditelnost nastavím na false - tedy není viditelný. Bude viděno pouze při přejetí myší na určitou planetu což řeším v metodě planetInteractions().
     */

    private Node[] highlights() {

        for (int i = 0; i < planets.length; i++) {

            if(i==0){
                planetsHighlights[i]=new Circle(950);
            }
            else {
                planetsHighlights[i]=new Circle(planets[i].getRadius()+planets[i].getRadius()/6);}

            planetsHighlights[i].setEffect(new Glow(0.4));
            planetsHighlights[i].setFill(Color.TRANSPARENT);
            planetsHighlights[i].setStroke(Color.LIGHTSEAGREEN);
            planetsHighlights[i].getTransforms().add(new Translate(planets[i].getTranslateX(),planets[i].getTranslateY(),0));

            planetsHighlights[i].setVisible(false);
        }

        return planetsHighlights;
    }


    /**
     * Metoda titles() je velmi podobná metodě highlights().
     *
     * Vrací pole planetsTitles[] typu Text, které slouží jako názvy zobrazené pod planetou.
     */

    private Node[] titles() {

        for (int i = 0; i < planets.length; i++) {

            planetsTitles[i] = new Text(names[i]);
            planetsTitles[i].setEffect(new Glow(0.4));
            planetsTitles[i].setFill(Color.LIGHTSEAGREEN);
            planetsTitles[i].setTextAlignment(TextAlignment.CENTER);
            planetsTitles[i].setFont(Font.font("Lucida Bright",10));

            if(i == 0){
                planetsTitles[i].getTransforms().add(new Translate(310,460,0));
            }
            else{
                planetsTitles[i].getTransforms().add(new Translate(planets[i].getTranslateX()-13, 400+ planetsHighlights[i].getRadius()+16, 0));
            }

            planetsTitles[i].setVisible(false);
        }

        return planetsTitles;
    }


    /**
     * Metoda saturnRings() vrací pole saturnRings[] typu Circle, které zobrazuje prstence saturnu.
     *
     * Definuju 4 proměnné v poli saturnRings, každý s jiným poloměrem.
     * Ve for cyklu všem z nich přiřadím stejné vlastnosti a atributy jako souřadnice, barvu, natočení a další.
     *
     * Poté do již deklarovaného pole ringRadiuses na místo s indexem i přiřadím poloměr prstence s indexem i.
     * Pole ringRadiuses využiji později v metodě action().
     */

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


    /**
     * Metoda buttons() vrací pole typu Node s proměnými pole button[] a StackPane popUp.
     *
     * Prvně chci naskenovat text z textového souboru, který se zobrazí po kliknutí na tlačítko info.
     *
     * To udělám vytvořením proměnné info typu StringBuilder do kterého pomocí cyklu while a try-catch vložím text z textového souboru.
     * Vytvořím proměnnou systemInfo typu Label ve které zobrazím uložený text a upřesním u něj detaily.
     *
     *  Poté vytvořím a definuji proměnnou systemInfoBox typu Rectangle, což je box, ve kterém se text bude zobrazovat.
     *
     *  Do objektu popUp typu StackPane přídám obě proměnné text i systemInfoBox, abych s nimi mohla manipulovat najednou.
     *
     *  Vytvořím pole buttons[] typu Button do kterého uložím dvě proměnné typu Button, jedna s textem "Info", druhá s "Exit".
     *
     *  Ve for cyklu jim přiřadím souřadnice a vzhled.
     *
     *  Poté se pomocí metody setOnMouseClicked() postarám o to, aby tlačítka reagovala na kliknutí.
     *  Když je kliknuto na tlačítko s indexem 0 v poli buttons[] - což je info - zobrazí se nebo zmizí informační okno.
     *  Při kliknutí na tlačítko exit se aplikace zavře.
     */

    private Node[] buttons() {

        StringBuilder info = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(systemInfoPath.getPath()))){

            String line;
            while ((line = reader.readLine()) != null){
                info.append(line).append("\n");
            }

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }


        Label systemInfo = new Label(info.toString());

        systemInfo.setTextFill(Color.LIGHTSEAGREEN);
        systemInfo.setPrefSize(980,190);
        systemInfo.setWrapText(true);
        systemInfo.setFont(Font.font("Lucida Bright",12));


        Rectangle systemInfoBox = new Rectangle(1020, 210);
        systemInfoBox.setFill(Color.BLACK);
        systemInfoBox.setStroke(Color.LIGHTSEAGREEN);


        StackPane infoPopUp = new StackPane(systemInfoBox, systemInfo);
        infoPopUp.getTransforms().add(new Translate(775- systemInfoBox.getWidth()/2, 400- systemInfoBox.getHeight()/2,-400));
        infoPopUp.setVisible(false);


        Button[] buttons = new Button[]{new Button("Info"),new Button("Exit")};

        for (int i = 0; i < buttons.length; i++) {

            buttons[i].setPrefSize(80,40);

            if(buttons[i].getText().equals("Info")){
                buttons[i].getTransforms().add(new Translate(1570,-120));
            }
            else{
                buttons[i].getTransforms().add(new Translate(1570,770));
            }

            buttons[i].setFont(Font.font("Lucida Bright",15));
            buttons[i].setStyle("-fx-background-color:  #00000000; -fx-text-fill: #20B2AA; -fx-border-width: 2px; -fx-border-color: #20B2AA");

            int finalI = i;

            buttons[i].setOnMouseClicked(MouseEvent -> {

                if(finalI==0){

                    if(!infoPopUp.isVisible()){
                        infoPopUp.setVisible(true);
                        buttons[finalI].setText("Back");
                    }
                    else{
                        infoPopUp.setVisible(false);
                        buttons[finalI].setText("Info");
                    }

                }
                else{
                    Platform.exit();
                }

            });
        }

        return new Node[]{buttons[0], buttons[1], infoPopUp};
    }


    /**
     *  Metoda animation() je nevracící metoda a stará se o rotaci všech proměnných typu Planet z pole planets[] a rotaci měsíce.
     *  Na všechny proměnné z tohoto pole zavolám metodu rotate() definovanou ve třídě Planet.
     *  U proměnné moon se rotace Měsíce kolem Země zajistí za pomocí metod setRotate() a getRotate().
     */

    private void animation() {

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {

                for (Planet planet : planets) {
                    planet.rotate();
                }

                moon.setRotate(moon.getRotate()+0.9);
            }
        };

        timer.start();

    }


    /**
     *  Metoda planetInteractions() se stará o animaci zmíněných proměnných z pole highlights[] a titles[].
     *
     *  Vše co metoda obsahuje je obklopeno for cyklem, aby vše platilo pro každý komponent ze všech každý metodou využitých polí.
     *
     *  Pomocí metody setOnMouseEntered() zajistím aby se zobrazilo označení a název určité planety při přejetí na tutéž planetu.
     *  Pomocí metody setOnMouseExited() zajistím přesný opak - tedy označení a název planety zmizí po přemístění kurzoru.
     *
     *  Metoda setOnMouseClicked() volá metodu action() s atributem finalI, což je index planety v poli planets[] na kterou bylo kliknuto.
     */

    private void planetInteractions() {

        for (int i = 0; i < planets.length; i++) {

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
                    action(finalI);
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
        }
    }


    /**
     *  První metoda která není volaná počateční metodou start() je metoda action() s atributem index typu int.
     *  Tato metoda animuje planety při kliknutí, tím že volá různé metody.
     *  To jaké metody zavolá závisí na tom, jestli už na planetu bylo poprvé kliknuto (tedy je přiblížena), nebo ne (je oddálena).
     *
     *  Prvně přidám již definované proměnné zoomInX typu TranslateTransition(zaslouží se o posunutí planety po souřadnici X při přibližování) objekt který bude animovat a X souřadnici do které objekt půjde.
     *  To samé udělám pro proměnou zoomOutX typu TranslateTransition, akorát ten bude objekt přesouvat na jeho výchozí polohu, tedy na již definovanou proměnnou z pole xProperties[].
     *
     *  K již definované proměnné zoomInSize typu TimeLine přidám nový KeyFrame, který bude mít na starosti zvětšení poloměru uživatelem zvolené planety na 220.
     *  To samé udělám u definované proměnné zoomOutSize typu TimeLine, akorát zde se poloměr planety bude vracet na svoji původní velikost.
     *
     *  U podmínky if(!zoomedIn) zjistím zda je planeta již příblížena nebo ne.
     *  Zda není přiblížena zavolám metodu prepareInfoBox() s atributem indextypu int, která připraví informační okno o planetě.
     *  Poté zavolám metodu planetsInfoBox() s atributy zoomIn typu boolean - dá metodě vědět zda je planeta přiblížena nebo ne - a s atributem currentInfoBox typu StackPane.
     *
     *  Proměnnou zoomedIn definuju jako true. Pouštím animace zoomInX a zoomInSize.
     *  Jestli je zvoleným objektem Slunce - tedy proměnná z pole planets[] s indexem 0- pouštím již definovanou animaci zoomInPlSun, jestli je to jakákoli jiná planeta pouštím animaci zoomInPl.
     *  Tyto animace se starají o pohyb proměnné pl typu PointLight - tedy "svit Slunce".
     *
     *  Poté pomocí for cyklu a animaci tlIn typu TimeLine zmenším poloměry všech planet na které nebylo kliknuto.
     *  Pomocí cyklu foreach udělám to samé i pro proměnné z pole saturnRings[].
     *
     *
     *  V případě že je již na planetu přiblíženo spouštím animaci zoomOutSize a zoomOutX. Jestli je zvoleným objektem Slunce pouštím animaci zoomOutPl.
     *  Poté volám metodu planetsInfo s atributy zoomedIn a currentInfobox, a proměnné zoomedIn přiřazuji hodnotu false.
     *
     *  Poté opět pomocí cyklu for a foreach obejdu všechny ostatní nevybrané planety a prstence Saturnu, ale v tomto případě je vracím na jejich původní místo.
     */

    private void action(int index) throws IOException {

        zoomInX.setNode(planets[index]);
        zoomInX.setToX(HEIGHT /2);
        zoomOutX.setNode(planets[index]);
        zoomOutX.setToX(xProperties[index]);

        zoomInSize.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1.5),
                        new KeyValue(planets[index].radiusProperty(), 220)
                ));

        zoomOutSize.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1.5),
                        new KeyValue(planets[index].radiusProperty(), sizes[index])
                ));


        if(!zoomedIn) {

            currentInfoBox = prepareInfoBox(index);
            planetsInfoBox(zoomedIn, currentInfoBox);

            zoomedIn = true;

            zoomInSize.play();
            zoomInX.play();

            if(index==0){
                zoomInPlSun.play();
            }
            else{
                zoomInPl.play();
            }

            Timeline tlIn = new Timeline();
            for (int i = 0; i < planets.length; i++) {

                if(i != index){
                    tlIn.getKeyFrames().add(
                            new KeyFrame(Duration.seconds(1.5),
                                    new KeyValue(planets[i].radiusProperty(), 0)
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

            if(index != 0){
                zoomOutPl.play();
            }

            planetsInfoBox(zoomedIn, currentInfoBox);
            zoomedIn=false;

            Timeline tlOut = new Timeline();
            for (int i = 0; i < planets.length; i++) {

                if(i != index){
                    tlOut.getKeyFrames().add(
                            new KeyFrame(Duration.seconds(1.5),
                                    new KeyValue(planets[i].radiusProperty(), sizes[i])
                            ));
                }

            }

            for (int i = 0; i < saturnRings.length; i++) {

                tlOut.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(1.5),
                                new KeyValue(saturnRings[i].radiusProperty(), ringRadiuses[i])
                        ));

            }

            tlOut.play();
        }

    }


    /**
     *  Metoda prepareInfoBox() vrací proměnnou infoBox typu StackPane.
     *
     *  Za pomocí proměnné planetInfo typu StringBuilder a reader typu BufferedReader načtu informace o zvolené planetě z jejího textového souboru - ten určím za pomocí atributu index typu int, který získá metoda při jejím zavolání.
     *
     *  Vytvořím si proměnnou text typu Label do které vložím text z proměnné planetInfo.
     *  Poté proměnné text dodám detaily. Vytvořím si proměnnou box typu Rectangle a infoBox typu StackPane do které vložím již definované proměnné text a box.
     */

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
        text.setTextFill(Color.LIGHTSEAGREEN);
        text.setPrefSize(470,500);
        text.setWrapText(true);
        text.setFont(Font.font("Lucida Bright",12));

        Rectangle box = new Rectangle(500,400);
        box.setArcWidth(10);
        box.setArcHeight(10);
        box.setFill(Color.BLACK);
        box.setStroke(Color.LIGHTSEAGREEN);

        StackPane infoBox = new StackPane(box,text);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setLayoutX(790);
        infoBox.setLayoutY(160);
        infoBox.setTranslateZ(-200);
        universe.getChildren().add(infoBox);


        return infoBox;
    }


    /**
     *  Metoda planetsInfoBox() se stará o to, aby se již připravený/vytvořený objekt infoBox typu StackPane přibližoval, nebo zmenšoval - to závisí na známém atributu zoom typu boolean.
     *
     *  Nejprve si vytvořím proměnnou scaleIn typu ScaleTransition, které přiřadím souřadnice, časovou hodnotu a objekt.
     *  Poté udělám to samé pro proměnnou scaleOut typu ScaleTransition.
     *
     *  Podle atributu zoomIn volám animaci scaleIn a scaleOut.
     */

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


    /**
     *  Metoda initMouseControl() s atributy scene typu Scene a stage typu Stage se stará o interakci "vesmíru" s kurzorem.
     *
     *  Prvně vytvořím proměnné tX, tY typu Translate, které následně přiřadím k proměnné universe.
     *  Souřadnici X proměnné tX spojím s již definovanou proměnnou newX typu DoubleProperty.
     *  To samé udělám pro souřadnici Y proměnné tY a newY typu DoubleProperty.
     *
     *  Metoda setOnMousePressed() reaguje na interakci myší se scénou scene.
     *  Do proměnné startingX se uloží X souřadnice kurzoru. To samé pro startingY a souřadnici Y.
     *  Do již definované proměnné currentX se uloží proměnná typu double získana metodou get() zavolanou na proměnnou newX. To samé pro currentY a newY.
     *
     *  Metoda setOnMouseDragged() je volaná při posunu kurzoru po scéne zároveň když je stisknut.
     *  Hodnotu newX určím jako součet a rozdíl proměnných závisejících na pohybu kurzoru po souřadnici X. To samé pro pro newY a souřadnici Y.
     *
     *  Pomocí metody addEventHandler() s atributem ScrollEvent.SCROLL přibližuji a oddaluji proměnnou universe.
     *  Do proměnné movement typu double uložím pohyb kolečka na myši, jenž pak odečtu od souřadnice Z proměnné universe.
     */

    private void initMouseControl(Scene scene, Stage stage){

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
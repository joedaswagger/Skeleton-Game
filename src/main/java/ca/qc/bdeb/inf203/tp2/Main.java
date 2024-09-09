package ca.qc.bdeb.inf203.tp2;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


/**
 * Débute le programme de Squelette Espiègle.
 */
public class Main extends Application {

    /**
     * Classe Main ou le programme débute, initialisant avec la scène d'accueil.
     */
    public static void main(String[] args) {
        launch(args);
    }

    private Stage stage;

    /**
     * Constantes pour la largeur et la hauteur de la fenêtre du programme.
     */
    private static final int WIDTH = 640, HEIGHT = 480;

    @Override
    public void start(Stage primaryStage) {

        this.stage = primaryStage;

        stage.setResizable(false);

        stage.getIcons().add(new Image("squelette.png"));
        primaryStage.setTitle("Squelette Espiègle");
        primaryStage.setScene(sceneAccueil());
        primaryStage.show();
    }

    /**
     * Génère la scène d'accueil.
     * @return un Scene qui est la scène d'accueil.
     */
    private Scene sceneAccueil() {
        var root = new VBox();
        var scene = new Scene(root, WIDTH, HEIGHT);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);

        var image = new Image("logo.png");
        var imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(450);

        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        var buttonJouer = new Button("Jouer!");
        buttonJouer.setOnAction(event -> {
            stage.setScene(sceneJeu());
        });

        var buttonInfos = new Button("Infos");
        buttonInfos.setOnAction(event -> {
            stage.setScene(sceneInfos());
        });

        root.getChildren().addAll(imageView, buttonJouer, buttonInfos);

        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {

                Platform.exit();
            }
        });

        return scene;
    }

    /**
     * Génère la scène d'infos.
     * @return un Scene qui est la scène d'infos.
     */
    private Scene sceneInfos() {

        var root = new VBox();
        var scene = new Scene(root, WIDTH, HEIGHT);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(50);

        root.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

        var texteTitre = new Text("Squelette Espiègle");
        root.getChildren().add(texteTitre);
        texteTitre.setFont(new Font(35));

        genererNoms(root);

        var buttonRetour = new Button("Retour");
        buttonRetour.setOnAction(event -> {
            stage.setScene(sceneAccueil());
        });

        root.getChildren().add(buttonRetour);

        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {

                stage.setScene(sceneAccueil());
            }
        });

        return scene;
    }

    /**
     * Crée le texte des noms de l'équipe ayant une couleur au hazard.
     * @param root le VBox qui contient un nom d'un des membres de l'équipe.
     */
    private void genererNoms(VBox root) {

        var barreNom1 = new HBox();
        barreNom1.setAlignment(Pos.CENTER);
        var barreNom2 = new HBox();
        barreNom2.setAlignment(Pos.CENTER);
        var pre_nom1 = new Text("Par ");
        var pre_nom2 = new Text("et ");
        var nom1 = new Text("Joseph Keshishian");
        var nom2 = new Text("Jason Truong");

        // Banque couleurs
        nom1.setFill(ImageHelpers.couleurAuHasard());
        nom2.setFill(ImageHelpers.couleurAuHasard());

        pre_nom1.setFont(new Font(20));
        pre_nom2.setFont(new Font(20));
        nom1.setFont(new Font(25));
        nom2.setFont(new Font(25));

        barreNom1.getChildren().addAll(pre_nom1, nom1);
        barreNom2.getChildren().addAll(pre_nom2, nom2);

        var texteDescription = new Text("Travail remis à Nicolas Hurtubise. Graphismes adaptés de https://game-icons.net/. Développé dans le cadre du cours 420-203-RE. Développement de programmes dans un environnement graphique, au Collège de Bois-de-Boulogne.");
        texteDescription.setFont(new Font(15));
        texteDescription.setWrappingWidth(WIDTH - 30);
        texteDescription.setTextAlignment(TextAlignment.JUSTIFY);

        root.getChildren().addAll(barreNom1, barreNom2, texteDescription);
    }

    /**
     * Génère la scène du Jeu.
     * @return un Scene qui est la scène du Jeu.
     */
    private Scene sceneJeu() {

        var root = new Pane();
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        var canvas = new Canvas(WIDTH, HEIGHT);
        var scene = new Scene(root, WIDTH, HEIGHT);
        root.getChildren().add(canvas);

        var context = canvas.getGraphicsContext2D();

        var jeu = new Jeu(WIDTH, HEIGHT);

        var timer = new AnimationTimer() {

            private long lastTime = System.nanoTime();

            @Override
            public void handle(long now) {
                var deltaTime = (now - lastTime) * 1e-9;

                jeu.update(deltaTime, WIDTH, HEIGHT);
                jeu.draw(context, WIDTH, HEIGHT);

                if (jeu.isRetourAccueil() && !jeu.isQuitte()) {
                    jeu.setQuitte();
                    stage.setScene(sceneAccueil());
                }

                lastTime = now;

            }
        };
        timer.start();

        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {

                jeu.setRetourAccueil();
                jeu.setQuitte();

                stage.setScene(sceneAccueil());
            } else {
                Input.setKeyPressed(e.getCode(), true);
            }
        });
        scene.setOnKeyReleased((e) -> {
            Input.setKeyPressed(e.getCode(), false);
        });

        return scene;
    }
}

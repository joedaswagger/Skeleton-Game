package ca.qc.bdeb.inf203.tp2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Random;

/**
 * Classe qui correspond à une partie de Squelette Espiègle. Elle gère la gestion des paramètres du jeu, met à jour les
 * paramètres des divers objets générés par cette classe elle-même, et dessine ces objets et l'interface du jeu.
 */
public class Jeu {


    // Paramètres propres au jeu.

    /**
     * Variable du jeu qui indique le nombre de vies actuel.
     */
    private int nbVies;

    /**
     * Variable du jeu qui indique le niveau actuel.
     */
    private int niveau;

    /**
     * Variable du jeu qui indique le score actuel de la partie.
     */
    private int score;

    /**
     * Variable du jeu qui indique le nombre de monstres capturés.
     */
    private double nombreMonstreCapture;

    /**
     * Variable du jeu qui indique si la partie est considérée comme perdue.
     */
    private boolean partiePerdue;

    /**
     * Variable du jeu qui indique si la partie devrait être en pause ou non. Lorsqu'elle est en pause,
     * les éléments à dessiner doivent se figer et ne doivent pas être mis à jour durant cette période.
     */
    private boolean pause;

    /**
     * Variable du jeu qui indique si le retour vers la scène d'accueil est nécessaire.
     */
    private boolean retourAccueil;

    /**
     * Variable du jeu qui indique si la partie est effectivement quitté lorsque la scène est retournée à la scène d'accueil.
     */
    private boolean quitte;

    // Les internalTimers sont chacun un compteur du temps écoulé. Lorsqu'ils atteignent ou dépassent un temps spécifié,
    // l'événement désiré se déclenche et le timer est remis à 0.

    /**
     * Timer qui gère la création d'un monstre normal.
     */
    private double internalTimerMonstre;

    /**
     * Timer qui gère la création d'un monstre spécial.
     */
    private double internalTimerMonstreSpecial;

    /**
     * Timer qui gère le temps passé en pause lorsqu'on annonce un niveau.
     */
    private double internalTimerPauseNiveau;

    /**
     * Timer qui gère le temps passé lorsque la partie est terminée.
     */
    private double internalTimerPartiePerdue;

    /**
     * Timer qui gère le temps de délai pour éviter d'abuser à plusieurs reprises les commandes de débogage.
     */
    private double internalTimerTouche;

    /**
     *Timer qui gère le temps de délai pour éviter d'abuser à plusieurs reprises la commande à créer la magie.
     */
    private double internalTimerMagie;


    /**
     * Création du Squelette qu'on contrôle durant la partie.
     */
    GameObject squelette;

    /**
     * Conteneur qui garde en mémoire les monstres en jeu. Elle supprime ceux qui sont éliminés ou
     * ceux qui sont sortis de l'écran.
     */
    ArrayList<Monstre> monstres = new ArrayList<>();

    /**
     * Conteneur qui garde en mémoire les boules de magie actuels. Elle supprime ceux qui sont sortis de l'écran.
     */
    ArrayList<Magie> magies = new ArrayList<>();

    /**
     * Constructeur. À la création d'une nouvelle partie, on initialise avec les paramètres suivants avec ces valeurs.
     * @param width  longueur de l'écran.
     * @param height hauteur de l'écran.
     */
    public Jeu(int width, int height) {

        nbVies = 3;
        niveau = 1;
        partiePerdue = false;
        pause = true;
        retourAccueil = false;
        quitte = false;

        internalTimerMonstre = 0;
        internalTimerMonstreSpecial = 0;
        internalTimerPauseNiveau = 0;
        internalTimerPartiePerdue = 0;
        internalTimerTouche = 0.1;
        internalTimerMagie = 0.6;
        nombreMonstreCapture = 0;

        squelette = new Squelette(width, height);

    }

    /**
     * Mise à jour du jeu, c'est-à-dire les paramètres du jeu et met à jour les paramètres des divers objets existants dans
     * la partie (ArrayLists des monstres et de la magie et le squelette).
     *
     * Les touches de débogage sont gérées aussi ici, dans le cas où les touches sont enfoncées
     * lorsque la fonction est appelée.
     * @param deltaTime le temps écoulé depuis la dernière itération de l'appel de cette fonction.
     * @param width longueur de l'écran.
     * @param height hauteur de l'écran.
     */
    public void update(double deltaTime, int width, int height) {

        if (!pause) {
            internalTimerMonstre += deltaTime;
            internalTimerMagie += deltaTime;

            // Générer un monstre.
            if (internalTimerMonstre > 3) {
                var monstre = new Monstre(niveau, width, height);
                monstres.add(monstre);
                internalTimerMonstre = 0;
            }

            // Générer un monstre spécial.
            if (niveau > 1) {
                internalTimerMonstreSpecial += deltaTime;
            }

            if (internalTimerMonstreSpecial > 5 && niveau > 1) {

                Monstre monstreSpecial;
                var random = new Random();
                var monstreType = random.nextInt(2);

                if (monstreType == 1)  {
                    monstreSpecial = new Oeil(niveau, width, height);
                } else {
                    monstreSpecial = new Bouche(niveau, width, height);
                }

                monstres.add(monstreSpecial);
                internalTimerMonstreSpecial = 0;
            }

            // Mise à jour des GameObjects.

            // Squelette.
            squelette.update(deltaTime, width, height);

            // Magie, s'il y a lieu.
            for (var item: magies) {
                item.update(deltaTime, monstres, height);
            }

            // Monstres, s'il y a lieu. On regarde s'il y a certains qui ont sorti de l'écran ou sont éliminés pour
            // gérer le nombre de vies et le score.
            for (var item : monstres) {
                item.update(deltaTime, width, height);
                if (item.isSorti()) {
                    nbVies--;
                } else if (item.isElimine()) {
                    nombreMonstreCapture++;
                    score++;
                }
            }

            // On supprime si un monstre sort de l'écran ou est éliminé.
            for (var item : monstres) {
                if (item.isSorti() || item.isElimine()) {
                    monstres.remove(item);
                }
            }

            // On supprime une boule de magie si elle sort de l'écran.
            for (var item: magies) {
                if (item.isSorti()) {
                    magies.remove(item);
                }
            }

        }

        // Commandes de débogage et gestion d'une partie perdue.

        var h = Input.isKeyPressed(KeyCode.H);
        var j = Input.isKeyPressed(KeyCode.J);
        var k = Input.isKeyPressed(KeyCode.K);
        var l = Input.isKeyPressed(KeyCode.L);
        var space = Input.isKeyPressed(KeyCode.SPACE);

        // Gestion du temps de pause.

        if (pause) {
            internalTimerPauseNiveau += deltaTime;
        }

        if (internalTimerPauseNiveau > 3) {
            pause = false;
            internalTimerPauseNiveau = 0;
        }

        // "l" / partie perdue.

        if (nbVies <= 0 || l) {
            pause = true;
            partiePerdue = true;
            nbVies = 0;

            internalTimerPartiePerdue += deltaTime;

            if (internalTimerPartiePerdue > 3 && !quitte) {
                retourAccueil = true;
            }

            // h / changement de niveau.
        } else if (h || nombreMonstreCapture == 5) {

            if (h) {
                internalTimerTouche += deltaTime;
            }

            if (internalTimerTouche > 0.1) {
                niveau++;
                internalTimerTouche = 0;
                internalTimerPauseNiveau += deltaTime;
                pause = true;

                monstres.clear();
            }

            if (nombreMonstreCapture == 5) {
                niveau++;
                internalTimerPauseNiveau += deltaTime;
                pause = true;

                nombreMonstreCapture = 0;

                monstres.clear();
            }

        }
        // j / Augmenter le score.
        else if (j) {

            internalTimerTouche += deltaTime;

            if (internalTimerTouche > 0.1) {
                score++;
                internalTimerTouche = 0;
            }

            // k / augmenter le nombre de vies.
        } else if (k) {

            internalTimerTouche += deltaTime;

            if (internalTimerTouche > 0.1) {
                nbVies++;

                if (nbVies > 3) {
                    nbVies = 3;
                }

                internalTimerTouche = 0;
            }

            // Générer une boule de magie.
        } else if (space && internalTimerMagie > 0.6) {
            var magie = new Magie(squelette);
            magies.add(magie);
            internalTimerMagie = 0;
        }
    }

    /**
     * Fonction qui dessine tous les objets dans le jeu sur le Canvas.
     * @param context le contexte qui gère ce qui est à dessiner sur le Canvas.
     * @param width longueur de l'écran.
     * @param height hauteur de l'écran.
     */
    public void draw(GraphicsContext context, int width, int height) {

        context.clearRect(0, 0, width, height);
        context.setTextAlign(TextAlignment.CENTER);

        // Squelette.
        squelette.draw(context);

        // Nombre de vies.
        drawVies(context, nbVies, width, height);

        // Score.
        afficherScore(context, width, height);

        // Magie, s'il y a lieu.
        drawMagie(context);

        // Monstres. N'apparaissent pas si la partie est en pause.
        if (!pause) {

            for (var item : monstres) {

                if (!item.isSorti() || !item.isElimine()) {
                    item.draw(context);
                }
            }

        } else {
            // Un message apparait au lieu dans le cas s'il y a une pause.
            if (internalTimerPauseNiveau < 3) {
                afficherMessage(context, width, height);
            }
        }

    }

    /**
     * Fonction qui dessine le nombre de vies dans le jeu sur le Canvas.
     * @param context le contexte qui gère ce qui est à dessiner sur le Canvas.
     * @param nbVies le nombre de vies actuel.
     * @param width longueur de l'écran.
     * @param height hauteur de l'écran.
     */
    private void drawVies(GraphicsContext context, int nbVies, int width, int height) {

        var img = new Image("squelette.png");
        img = ImageHelpers.colorize(img, Color.FUCHSIA);

        if (nbVies >= 3) {
            context.drawImage(img, width * 1/2 + 45, height * 1/4, 30, 30);
        }
        if (nbVies >= 2) {
            context.drawImage(img, width * 1/2 - 15, height * 1/4, 30, 30);
        }
        if (nbVies >= 1) {
            context.drawImage(img, width * 1/2 - 75, height * 1/4, 30, 30);
        }
    }

    /**
     * Fonction qui dessine le score dans le jeu sur le Canvas.
     * @param context le contexte qui gère ce qui est à dessiner sur le Canvas.
     * @param width longueur de l'écran.
     * @param height hauteur de l'écran.
     */
    private void afficherScore(GraphicsContext context, int width, int height) {

        context.setFill(Color.WHITE);
        context.setFont(new Font(30));
        context.fillText(String.valueOf(score), width * 1/2, height * 1/6);

    }

    /**
     * Fonction qui dessine la magie dans le jeu sur le Canvas.
     * @param context le contexte qui gère ce qui est à dessiner sur le Canvas.
     */
    private void drawMagie(GraphicsContext context) {

        for (var item: magies) {
            item.draw(context);
        }

    }

    /**
     * Fonction qui dessine le message à afficher dans le jeu sur le Canvas.
     * @param context le contexte qui gère ce qui est à dessiner sur le Canvas.
     * @param width longueur de l'écran.
     * @param height hauteur de l'écran.
     */
    public void afficherMessage(GraphicsContext context, int width, int height) {

        context.setFont(new Font(75));

            if (partiePerdue) {
                context.setFill(Color.RED);
                context.fillText("FIN DE PARTIE", width * 1/2, height * 1/2);
            } else {
                context.setFill(Color.WHITE);
                context.fillText("NIVEAU " + niveau, width * 1/2, height * 1/2);
            }
    }



    /**
     * @return un boolean qui détermine si la partie veut retourner à la scène d'accueil.
     */
    public boolean isRetourAccueil() {
        return this.retourAccueil;
    }

    /**
     * @return un boolean qui détermine si la partie est quittée.
     */
    public boolean isQuitte() {
        return this.quitte;
    }

    /**
     * Change le boolean retourAccueil à true.
     */
    public void setRetourAccueil() {this.retourAccueil = true;}

    /**
     * Change le boolean retourAccueil à true.
     */
    public void setQuitte() {
       this.quitte = true;
    }

}

package ca.qc.bdeb.inf203.tp2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

/**
 * Classe qui correspond à l'objet qu'on contrôle à l'aide des touches qu'on enfonce.
 */
public class Squelette extends GameObject {

    /**
     * La banque d'images qui contiennent celles utilisées pour l'animation. Elle cycle parmi ceux-ci.
     */
    private Image[] frames = new Image[] {
            new Image("squelette/marche1.png"), new Image("squelette/marche2.png")
    };

    /**
     * L'image actuelle que le Squelette utilise actuellement.
     */
    private Image img;

    /**
     * compteur utilisé pour déterminer quelle image du cycle de l'animation du Squelette à utiliser.
     */
    private int cpt = 0;

    /**
     * Boolean qui indique si le squelette a déjà sauté. Il ne peut pas sauter à nouveau s'il a déjà sauté.
     */
    private boolean saute = false;

    /**
     * Timer qui gère le temps écoulé pour permettre la gestion de l'animation du Squelette.
     */
    private double internalTimer = 0;

    /**
     * Constructeur
     * @param width longueur de l'écran.
     * @param height hauteur de l'écran.
     */
    public Squelette(int width, int height) {

        w = 48;
        h = 96;

        x = (width / 2) - (w / 2);
        y = height - h;
        ax = 0;
        ay = 1200;

    }

    /**
     * Mise à jour des paramètres du Squelette. Elle gère aussi que faire dans le cas où on enfonce les touches
     * de déplacement du squelette.
     * @param dt le temps écoulé depuis la dernière itération de cette fonction.
     * @param width  longueur de l'écran.
     * @param height hauteur de l'écran.
     */
    public void update(double dt, int width, int height) {

        // Gestion des touches directionnelles.
        // Gauche / Droite.

        var left = Input.isKeyPressed(KeyCode.LEFT);
        var right = Input.isKeyPressed(KeyCode.RIGHT);

        if (left) {
            ax = -1000;
        }
        else if (right) {
            ax = 1000;
        } else {
            ax = 0;
        }

        // Amortissement.

        int signeVitesse;

        if (vx > 0) {
            signeVitesse = 1;
        } else {
            signeVitesse = -1;
        }

        double vitesseAmortissementX = -signeVitesse * 500;
        vx += dt * vitesseAmortissementX;

        int nouveauSigneVitesse;

        if (vx > 0) { nouveauSigneVitesse = 1;
        } else nouveauSigneVitesse = -1;

        if (nouveauSigneVitesse != signeVitesse) {
            vx = 0;
        }

        // Sauter (Haut).

        boolean jump = Input.isKeyPressed(KeyCode.UP);

        if (jump && !saute) {
            vy = -600;
            ay = 1200;
            saute = true;
        }

        if (vx > 300) {
            vx = 300;
        } else if (vx < -300) {
            vx = -300;
        }

        // Mise à jour de la physique.

        super.update(dt, width, height);

        // Évite de dépasser le "sol", étant le bas de l'écran.

        if (getBas() > height) {
            y = height - h;
            vy = 0;
            ay = 0;
            saute = false;
        }

        // Évite de dépasser les "murs", étant la gauche et la droite de l'écran.

        if (getDroite() > width || getGauche() < 0) {

            if (getDroite() > width) {
                vx = -vx;
                x = width - w;
            } else {
                vx = -vx;
                x = 0;
            }
        }

        // Animation du squelette, tant qu'il n'est pas immobile.

        if (vx != 0) {

            internalTimer += dt;

            if (internalTimer > 0.1) {

                cpt++;

                img = frames[(cpt % frames.length)];

                if (vx < 0) {
                    img = ImageHelpers.flop(img);
                }

                internalTimer = 0;
            }
        }

        // Lorsque le Squelette est immobile, on utilise cette image.

        if (vx == 0) {
            img = new Image("squelette/stable.png");
        }

    }

    /**
     * Fonction qui dessine le Squelette dans le jeu qui sur le Canvas.
     * @param context le contexte qui gère ce qui est à dessiner sur le Canvas.
     */
    public void draw(GraphicsContext context) {

        context.drawImage(img, x, y, w, h);

    }
}

package ca.qc.bdeb.inf203.tp2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Random;

/**
 * Classe qui représente un monstre qui, lorsqu'il sort de l'écran, diminue le nombre de vies par 1. Elle est éliminée
 * si une boule de Magie a une collision avec ce monstre.
 */
public class Monstre extends GameObject {

    // Monstres speciaux

    /**
     * Banque d'images possibles pour l'apparence des monstres normaux.
     */
    private Image[] monstres = new Image[] {
            new Image("monstres/0.png"),
            new Image("monstres/1.png"),
            new Image("monstres/2.png"),
            new Image("monstres/3.png"),
            new Image("monstres/4.png"),
            new Image("monstres/5.png"),
            new Image("monstres/6.png"),
            new Image("monstres/7.png"),

    };

    /**
     * Attribut d'un monstre qui correspond à son apparence choisie par la banque d'images.
     */
    protected Image img;

    /**
     * Attribut d'un monstre qui correspond à la taille du monstre.
     */
    protected double taille;

    /**
     * Attribut d'un monstre qui garde en mémoire sa position x initiale. Utile lorsqu'on doit vérifier si le monstre
     * est sorti de l'écran.
     */
    private double xInitial;

    /**
     * Attribut qui correspond à la position centrale de l'objet par rapport à l'écran au plan horizontal.
     */
    private double xCentral;

    /**
     * Attribut qui correspond à la position centrale du monstre au plan vertical.
     */
    private double yCentral;

    /**
     * Attribut qui correspond à la longueur du rayon du monstre.
     */
    private double rayon;

    /**
     * Attribut qui indique si le monstre est sorti de l'écran.
     */
    private boolean sorti = false;

    /**
     * Attribut qui indique si le monstre est éliminé.
     */
    private boolean elimine;

    /**
     * Constructeur. Plusieurs attributs sont données au monstre au hazard.
     * @param niveau le niveau du jeu au moment le monstre est créé.
     * @param width largeur de l'écran.
     * @param height hauteur de l'écran.
     */
    public Monstre(int niveau, int width, int height) {

        var random = new Random();

        taille = genererTaille();

        w = taille;
        h = taille;

        x = genererPositionX(width);
        xInitial = x;
        y = genererPositionY(height);
        ax = 0;
        ay = 100;

        vx = genererVitesseX(niveau);
        vy = genererVitesseY();

        xCentral = x + taille / 2;
        yCentral = y + taille / 2;
        rayon = taille / 2;

        img = monstres[random.nextInt(monstres.length)];
        img = ImageHelpers.colorize(img, ImageHelpers.couleurAuHasard());

        if (vx < 0) {
            this.img = ImageHelpers.flop(img);
        }

    }

    /**
     * Mise à jour des paramètres des monstres, et détermine si un monstre est sorti ou non selon leurs nouvelles positions.
     * @param deltaTemps le temps écoulé depuis la dernière itération de l'appel de cette fonction.
     * @param width longueur de l'écran.
     * @param height hauteur de l'écran.
     */
    public void update(double deltaTemps, int width, int height) {
        super.update(deltaTemps, width, height);

        xCentral = x + taille / 2;
        yCentral = y + taille / 2;

        if (xInitial < 0) {
            if (getGauche() > width || getBas() < 0) {
                this.sorti = true;

            }
        } else if (getDroite() < 0 || getBas() < 0) {
            this.sorti = true;
        }

    }

    /**
     * Fonction qui dessine le monstre dans le jeu qui sur le Canvas.
     * @param context le contexte qui gère ce qui est à dessiner sur le Canvas.
     */
    public void draw(GraphicsContext context) {

        context.drawImage(img, x, y, w, h);

    }

    /**
     * Génère une vitesse initiale X au pour un nouveau monstre selon le niveau du jeu actuel.
     * @param niveau le niveau du jeu actuel.
     * @return un double qui est la vitesse horizontale initiale du monstre.
     */
    private double genererVitesseX(int niveau) {

        var vitesse = 100 * Math.pow(niveau, 0.33) + 200;

        if (x < 0) {
            return vitesse;
        } else return -vitesse;
    }

    /**
     * Génère une vitesse initiale Y au hazard pour un nouveau monstre.
     * @return un double qui est la vitesse verticale initiale du monstre. (De -100 à -200.)
     */
    private double genererVitesseY() {
        return -((Math.random() * 100) + 100);
    }

    /**
     * Génère une position initiale X au hazard pour un nouveau monstre.
     * @param width largeur de l'écran.
     * @return un double qui est la position horizontale initiale du monstre. (Soit à gauche ou à droite, hors-écran.)
     */
    private double genererPositionX(int width) {

        var random = new Random();

        var x = random.nextInt(2);

        if (x == 1) {
            return (0 - getTaille());
        } else {
            return (width + getTaille());
        }
    }

    /**
     * Génère une position initiale Y au hazard pour un nouveau monstre.
     * @param height hauteur de l'écran.
     * @return un double qui est la position verticale initiale du monstre. (De 1/5 à 4/5 de la hauteur de l'écran.)
     */
    private double genererPositionY(int height) {
        return (((Math.random()) * 0.6 * height) + 0.2 * height);
    }

    /**
     * Génère une taille au hazard pour un nouveau monstre.
     * @return un double qui est la taille du monstre. (De 40 à 100.)
     */
    private double genererTaille() {
        return ((Math.random() * 60) + 40);
    }

    /**
     * @return la taille du monstre.
     */
    public double getTaille() {
        return taille;
    }

    /**
     * @return un boolean qui indique si le monstre est sorti.
     */
    public boolean isSorti() {
        return sorti;
    }

    /**
     * @return un boolean qui indique si le monstre est éliminé.
     */
    public boolean isElimine() {
        return elimine;
    }

    /**
     * Change le boolean at true, signalant que le monstre est éliminé.
     */
    public void setElimine() {
        this.elimine = true;
    }

    /**
     * @return un double qui est la position x central du monstre.
     */
    public double getxCentral() {
        return xCentral;
    }

    /**
     * @return un double qui est la position y central du monstre.
     */
    public double getyCentral() {
        return yCentral;
    }

    /**
     * @return un double qui est le rayon du monstre.
     */
    public double getRayon() {
        return rayon;
    }
}

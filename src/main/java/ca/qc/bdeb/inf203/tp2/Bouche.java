package ca.qc.bdeb.inf203.tp2;

import javafx.scene.image.Image;

/**
 * Classe qui représente l'un des deux monstres spéciaux possibles que le jeu peut générer.
 */
public class Bouche extends Monstre {

    /**
     * Variable qui représente la position y normale, sans tenir compte des variations causées par la fonction sin.
     */
    double yBase;

    /**
     * Variable qui représente le temps écoulé cumulatif depuis la création du monstre.
     */
    double tempsEcoule = 0;

    /**
     * Constructeur. Contrairement à un monstre, l'image est prédéfinie et elle ne possède pas d'accélération y.
     *
     * @param niveau le niveau du jeu au moment le monstre est créé.
     * @param width  largeur de l'écran.
     * @param height hauteur de l'écran.
     */
    public Bouche(int niveau, int width, int height) {
        super(niveau, width, height);

        img = new Image("bouche.png");
        if (vx < 0) {
            img = ImageHelpers.flop(img);
        }

        ay = 0;
        yBase = y;
    }

    /**
     * Mise à jour des paramètres de ce type de monstre s'il y en a un sur le Canvas.
     * @param deltaTemps le temps écoulé depuis la dernière itération de l'appel de cette fonction.
     * @param width      longueur de l'écran.
     * @param height     hauteur de l'écran.
     */
    public void update(double deltaTemps, int width, int height) {

        super.update(deltaTemps, width, height);

        tempsEcoule += deltaTemps;

        y = yBase + 50 * Math.sin(10 * tempsEcoule);

    }
}

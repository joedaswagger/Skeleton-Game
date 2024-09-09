package ca.qc.bdeb.inf203.tp2;

import javafx.scene.image.Image;

/**
 * Classe qui représente l'un des deux monstres spéciaux possibles que le jeu peut générer.
 */
public class Oeil extends Monstre {

    /**
     * Timer qui gère quand il faut que le monstre change de direction.
     */
    double internalTimer = 0;

    /**
     * Boolean qui indique si la variable a tourné de direction à 0.5 seconde du timer.
     */
    boolean tourne = false;

    /**
     * Constructeur. Contrairement à un monstre normal, un Oeil possède une vitesse x différente,
     * et aucune vitesse et accélération verticale. Son image est aussi prédéfinie.
     * @param niveau le niveau actuel du jeu.
     * @param width largeur de l'écran.
     * @param height hauteur de l'écran.
     */
    public Oeil(int niveau, int width, int height) {
        super(niveau, width, height);

        img = new Image("oeil.png");

        vx = 1.3 * super.vx;

        vy = 0;
        ay = 0;

    }

    /**
     * Mise à jour des paramètres de l'Oeil. Elle gère aussi si la vitesse doit change à un moment ou non.
     * @param deltaTemps le temps écoulé depuis la dernière itération de l'appel de cette fonction.
     * @param width      longueur de l'écran.
     * @param height     hauteur de l'écran.
     */
    public void update(double deltaTemps, int width, int height) {
        super.update(deltaTemps, width, height);

        this.internalTimer += deltaTemps;

        if (this.internalTimer > 0.5 && !tourne) {
           setVx();
           this.tourne = true;
        }
        if (this.internalTimer > 0.75) {
            setVx();
            this.tourne = false;
            this.internalTimer = 0;
        }
    }

    /**
     * Change la direction de la vitesse en le changeant à une vitesse négative ou positive, selon le cas.
     */
    private void setVx() {
        this.vx = -vx;
    }
}

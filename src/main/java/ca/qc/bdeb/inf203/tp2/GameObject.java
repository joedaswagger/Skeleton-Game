package ca.qc.bdeb.inf203.tp2;

import javafx.scene.canvas.GraphicsContext;

/**
 * Classe qui représente un objet qui est créé par un Jeu. Il correspond à un Monstre, un MonstreSpecial,
 * un Squelette, ou de la Magie. Pour tous ces objets, ils ont tous des paramètres communs, qui inclut leur position,
 * leur vitesse, leur accélération et plusieurs méthodes communes.
 */
public abstract class GameObject {

    /**
     * Variables de position. x = position horizontale; y = position verticale.
     */
    protected double x, y;
    /**
     * Variables de vitesse. vx = vitesse horizontale; vy = vitesse verticale.
     */
    protected double vx, vy;
    /**
     * Variables d'accélération. ax = accélération horizontale; ay = accélération verticale.
     */
    protected double ax, ay;
    /**
     * Variables de la taille de l'objet. w = longueur (width); h = hauteur (height).
     */
    protected double w, h;

    /**
     * Fonction qui dessine l'objet dans le jeu qui sur le Canvas.
     * @param context le contexte qui gère ce qui est à dessiner sur le Canvas.
     */
    public abstract void draw(GraphicsContext context);

    /**
     * Fonction qui met à jour les divers paramètres et états de l'objet.
     * @param deltaTemps le temps écoulé depuis la dernière itération de l'appel de cette fonction.
     * @param width longueur de l'écran.
     * @param height hauteur de l'écran.
     */
    public void update(double deltaTemps, int width, int height) {
        updatePhysique(deltaTemps);
    }

    /**
     * Fonction commune utilisée dans la fonction update pour mettre à jour les paramètres de position et de vitesse.
     * @param deltaTemps le temps écoulé depuis la dernière itération de l'appel de cette fonction.
     */
    public void updatePhysique(double deltaTemps) {
        vx += deltaTemps * ax;
        vy += deltaTemps * ay;
        x += deltaTemps * vx;
        y += deltaTemps * vy;
    }

    /**
     * Fonction qui retourne le haut de l'objet.
     * @return un double qui correspond à la position y et du haut de l'objet.
     */
    public double getHaut() {
        return y;
    }

    /**
     * Fonction qui retourne le bas de l'objet.
     * @return un double qui correspond le bas de l'objet.
     */
    public double getBas() {
        return y + h;
    }

    /**
     * Fonction qui retourne le bas de l'objet.
     * @return un double qui correspond à la position x et de la gauche de l'objet.
     */
    public double getGauche() {
        return x;
    }

    /**
     * Fonction qui retourne la droite de l'objet.
     * @return un double qui correspond la droite de l'objet.
     */
    public double getDroite() {
        return x + w;
    }

}


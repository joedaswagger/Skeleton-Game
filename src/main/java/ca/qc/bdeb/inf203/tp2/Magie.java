package ca.qc.bdeb.inf203.tp2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Classe qui représente une boule de magie qui, au contact d'un monstre, l'élimine.
 */
public class Magie extends GameObject {

    /**
     * Boolean qui indique si l'objet est sorti de l'écran.
     */
    private boolean sorti;

    /**
     * Variable qui correspond à la position centrale de l'objet par rapport à l'écran au plan horizontal.
     */
    private double xCentral;

    /**
     * Variable qui correspond à la position centrale de l'objet au plan vertical.
     */
    private double yCentral;

    /**
     * Variable qui correspond à la longueur du rayon de l'objet.
     */
    private double rayon;

    /**
     * Variable qui correspond à la couleur de l'objet.
     */
    private Color color;

    /**
     * Constructeur. À la création d'une nouvelle boule de magie, on initialise avec les paramètres suivants avec ces valeurs.
     * @param squelette le Squelette utilisé comme référence de position.
     */
    public Magie(GameObject squelette) {

        vx = 0;
        vy = -300;

        ax = 0;
        ay = 0;

        w = 35;
        h = 35;

        x = ((squelette.getDroite() - (squelette.getDroite() - squelette.getGauche()) / 2) - (w));
        y = squelette.getHaut() - h;

        sorti = false;

        xCentral = x + w;
        yCentral = y + h;
        rayon = 35;

        color = ImageHelpers.couleurAuHasard();

    }

    /**
     * Fonction qui dessine la magie dans le jeu qui sur le Canvas.
     * @param context le contexte qui gère ce qui est à dessiner sur le Canvas.
     */
    public void draw(GraphicsContext context) {

        context.setFill(this.color);
        context.fillOval(x, y, w * 2, h * 2);

    }

    /**
     * Mise à jour de la magie, soit les paramètres de chaque boule.
     * Détermine aussi si un monstre est éliminé ou non.
     * @param deltaTemps le temps écoulé depuis la dernière itération de l'appel de cette fonction.
     * @param monstres ArrayList qui content les monstres en jeu.
     * @param height hauteur de l'écran.
     */
    public void update(double deltaTemps, ArrayList<Monstre> monstres, int height) {
        updatePhysique(deltaTemps);

        yCentral = y + h;

        if (getBas() > height) {
            sorti = true;
        }

        for (var item: monstres) {
            if (collision(item)) {
                item.setElimine();
            }
        }
    }

    /**
     * @return un boolean qui détermine si la boule est sortie.
     */
    public boolean isSorti() {
        return sorti;
    }

    /**
     * Fonction qui teste avec un monstre s'il y a collision.
     * @param monstre le monstre du ArrayList à tester.
     * @return un boolean qui indique si il y a effectivement une collision ou non.
     */
    public boolean collision(Monstre monstre) {

        var dx = (this.xCentral - monstre.getxCentral());
        var dy = (this.yCentral - monstre.getyCentral());
        var dCarre = dx * dx + dy * dy;

        return dCarre < (this.rayon + monstre.getRayon()) * (this.rayon + monstre.getRayon());

    }

}

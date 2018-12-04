package procorp.applikobo.Models;

public class Partie {

    private String nom;
    private int nbManches;
    private int nbJoueurs;

    public Partie(String nom, int nbManches, int nbJoueurs) {
        this.nom = nom;
        this.nbManches = nbManches;
        this.nbJoueurs = nbJoueurs;
    }


    public String getNom() {
        return nom;
    }

    public int getNbManches() {
        return nbManches;
    }

    public int getNbJoueurs() {
        return nbJoueurs;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNbManches(int nbManches) {
        this.nbManches = nbManches;
    }

    public void setNbJoueurs(int nbJoueurs) {
        this.nbJoueurs = nbJoueurs;
    }

    @Override
    public String toString() {
        return "Partie{" +
                "nom='" + nom + '\'' +
                ", nbManches=" + nbManches +
                ", nbJoueurs=" + nbJoueurs +
                '}';
    }
}

package procorp.applikobo.Models;

public class Score {

    private String pseudo;
    private int points;

    public Score(String pseudo, int points) {
        this.pseudo = pseudo;
        this.points = points;
    }

    public String getPseudo() {
        return pseudo;
    }

    public int getPoints() {
        return points;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Score{" +
                "pseudo='" + pseudo + '\'' +
                ", points=" + points +
                '}';
    }
}

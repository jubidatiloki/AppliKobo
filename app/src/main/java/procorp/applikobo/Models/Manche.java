package procorp.applikobo.Models;

import java.util.ArrayList;


public class Manche {

    private int numManche;
    private ArrayList<Score> scores;

    public Manche(int numManche, ArrayList<Score> scores) {
        this.numManche = numManche;
        this.scores = scores;
    }

    public int getNumManche() {
        return numManche;
    }

    public ArrayList<Score> getScores() {
        return scores;
    }

    public void setNumManche(int numManche) {
        this.numManche = numManche;
    }

    public void setScores(ArrayList<Score> scores) {
        this.scores = scores;
    }

    @Override
    public String toString() {
        return "Manche{" +
                "numManche=" + numManche +
                ", scores=" + scores +
                '}';
    }
}

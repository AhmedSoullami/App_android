package com.my_app_perso;

import java.io.Serializable;

public class Note implements Serializable {
    private String Label;
    private double score;

    public Note(String label, double score) {
        Label = label;
        this.score = score;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}

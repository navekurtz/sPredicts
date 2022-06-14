package com.example.spredicts;

import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class Card {
    private String result, homename, awayname;
    private int id, homeScore, awayScore;

    public Card(String result, String homename, String awayname, int id, int homeScore, int awayScore) {
        this.result = result;
        this.homename = homename;
        this.awayname = awayname;
        this.id = id;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public String getResult() {
        return result;
    }

    public String getHomename() {
        return homename;
    }

    public String getAwayname() {
        return awayname;
    }

    public int getId() {
        return id;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setHomename(String homename) {
        this.homename = homename;
    }

    public void setAwayname(String awayname) {
        this.awayname = awayname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }
}

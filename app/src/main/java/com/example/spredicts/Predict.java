package com.example.spredicts;

public class Predict {
    private long id;
    private int homePredict;
    private int awayPredict;

    public Predict(long id, int homePredict, int awayPredict){
        this.id=id;
        this.homePredict=homePredict;
        this.awayPredict=awayPredict;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getHomePredict() {
        return homePredict;
    }

    public void setHomePredict(int homePredict) {
        this.homePredict = homePredict;
    }

    public int getAwayPredict() {
        return awayPredict;
    }

    public void setAwayPredict(int awayPredict) {
        this.awayPredict = awayPredict;
    }



}

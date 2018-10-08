package com.exemple.furlan.apptruco.Model;

public class Player {
    private String id;
    private String player;
    private String placarPlayer;
    private String oponente;
    private String placarOponente;

    public Player() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getPlacarPlayer() {
        return placarPlayer;
    }

    public void setPlacarPlayer(String placarPlayer) {
        this.placarPlayer = placarPlayer;
    }

    public String getOponente() {
        return oponente;
    }

    public void setOponente(String oponente) {
        this.oponente = oponente;
    }

    public String getPlacarOponente() {
        return placarOponente;
    }

    public void setPlacarOponente(String placarOponente) {
        this.placarOponente = placarOponente;
    }
}

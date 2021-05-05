package hu.alkfelj.model;

import javafx.beans.property.*;

public class Match {

    /*
    * id (szám, elsődleges kulcs)
    * gameId (szám, kötelező)
    * player név (szöveg, kötelező)
    * szimbólum id(szám, kötelező)
    * lépés(szöveg, kötelező)
     */

    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private final IntegerProperty gameId = new SimpleIntegerProperty(this, "gameId");
    private final StringProperty player = new SimpleStringProperty(this, "player");
    private final IntegerProperty symbolId = new SimpleIntegerProperty(this, "symbolId");
    private final ObjectProperty<Coords> step = new SimpleObjectProperty<>(this, "step");

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getGameId() {
        return gameId.get();
    }

    public IntegerProperty gameIdProperty() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId.set(gameId);
    }

    public String getPlayer() {
        return player.get();
    }

    public StringProperty playerProperty() {
        return player;
    }

    public void setPlayer(String player) {
        this.player.set(player);
    }

    public int getSymbolId() {
        return symbolId.get();
    }

    public IntegerProperty symbolIdProperty() {
        return symbolId;
    }

    public void setSymbolId(int symbolId) {
        this.symbolId.set(symbolId);
    }

    public Coords getStep() {
        return step.get();
    }

    public ObjectProperty<Coords> stepProperty() {
        return step;
    }

    public void setStep(Coords step) {
        this.step.set(step);
    }
}

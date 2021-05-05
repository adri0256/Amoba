package hu.alkfelj.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Game {
    private final IntegerProperty id = new SimpleIntegerProperty(this, "gameId");
    private final IntegerProperty boardSize = new SimpleIntegerProperty(this, "boardSize");
    private final StringProperty player = new SimpleStringProperty(this, "player");

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int gameId) {
        this.id.set(gameId);
    }

    public int getBoardSize() {
        return boardSize.get();
    }

    public IntegerProperty boardSizeProperty() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize.set(boardSize);
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
}

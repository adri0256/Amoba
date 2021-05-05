package hu.alkfelj.gameLogic;

import javafx.beans.property.IntegerProperty;

public interface GameLogic {
    void generateBoard(int boardSize);
    int getTile(int row, int column);
    void setTile(int row, int column, int symbolId);
    int checkWin(int row, int column, int symbolId);
    int getRoundCounter();
    void setRoundCounter(int roundCounter);
    IntegerProperty roundCounterProperty();
    void incrementRoundCounter();
}

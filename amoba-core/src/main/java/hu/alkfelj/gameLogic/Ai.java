package hu.alkfelj.gameLogic;

import hu.alkfelj.model.Coords;

public interface Ai {
    Coords nextTurn(int boardSize);
}

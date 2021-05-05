package hu.alkfelj.gameLogic;

import hu.alkfelj.model.Coords;

import java.util.Random;

public class AiImpl implements Ai{
    @Override
    public Coords nextTurn(int boardSize) {
        //Coords tryBoard = rndTile(boardSize);

        return rndTile(boardSize);
    }

    private Coords rndTile(int boardSize){
        Random r = new Random();
        int x = r.nextInt(boardSize);
        int y = r.nextInt(boardSize);

        return new Coords(x, y);
    }
}

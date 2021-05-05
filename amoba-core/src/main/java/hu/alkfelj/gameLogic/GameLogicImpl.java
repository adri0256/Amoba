package hu.alkfelj.gameLogic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class GameLogicImpl implements GameLogic {
    private int[][] gameTable;
    private int boardSize;
    private final IntegerProperty roundCounter = new SimpleIntegerProperty(this, "roundCounter");

    @Override
    public void generateBoard(int boardSize){
        this.boardSize = boardSize;
        gameTable = new int[boardSize][boardSize];

        for(int i = 0; i < boardSize; i++)
            for(int j = 0; j < boardSize; j++)
                gameTable[i][j] = 0;
    }

    @Override
    public int getRoundCounter() {
        return roundCounter.get();
    }

    public IntegerProperty roundCounterProperty() {
        return roundCounter;
    }

    public void setRoundCounter(int roundCounter) {
        this.roundCounter.set(roundCounter);
    }

    public void incrementRoundCounter() {
        setRoundCounter(getRoundCounter()+1);
    }

    public int getTile(int row, int column) {
        return gameTable[row][column];
    }

    @Override
    public void setTile(int row, int column, int symbolId){
        gameTable[row][column] = symbolId;
    }

    public int[][] getGameTable() {
        return gameTable;
    }

    @Override
    public int checkWin(int row, int column, int symbolId) {
        int sum = 0;

        /*Checks the given column*/
        for(int i = column; i <= boardSize-1; i++) {
            if(gameTable[row][i] != symbolId) {
                break;
            }
            if(gameTable[row][i] == symbolId) {
                sum++;
                if(sum > 5) {
                    return 1;
                }
            }
        }
        for (int i = column; i >= 0; i--) {
            if(gameTable[row][i] != symbolId) {
                break;
            }
            if(gameTable[row][i] == symbolId) {
                sum++;
                if(sum > 5) {
                    return 1;
                }
            }
        }

        sum = 0;

        /*Checks the given row*/
        for(int i = row; i <= boardSize-1; i++) {
            if(gameTable[i][column] != symbolId) {
                break;
            }
            if(gameTable[i][column] == symbolId) {
                sum++;
                if(sum > 5) {
                    return 1;
                }
            }
        }
        for (int i = row; i >= 0; i--) {
            if(gameTable[i][column] != symbolId) {
                break;
            }
            if(gameTable[i][column] == symbolId) {
                sum++;
                if(sum > 5) {
                    return 1;
                }
            }
        }

        sum = 0;

        /*Checks the given diagonal top right and bottom left*/
        for(int i = row, j = column; i <= boardSize-1 && j <= boardSize-1; i++, j++) {
            if(gameTable[i][j] != symbolId) {
                break;
            }
            if(gameTable[i][j] == symbolId) {
                sum++;
                if(sum > 5) {
                    return 1;
                }
            }
        }
        for(int i = row, j = column; i >= 0 && j >= 0; i--, j--) {
            if(gameTable[i][j] != symbolId) {
                break;
            }
            if(gameTable[i][j] == symbolId) {
                sum++;
                if(sum > 5) {
                    return 1;
                }
            }
        }

        sum = 0;

        /*Checks the given diagonal top left and bottom right*/
        for(int i = row, j = column; i <= boardSize-1 && j >= 0; i++, j--) {
            if(gameTable[i][j] != symbolId) {
                break;
            }
            if(gameTable[i][j] == symbolId) {
                sum++;
                if(sum > 5) {
                    return 1;
                }
            }
        }
        for(int i = row, j = column; i >= 0 && j <= boardSize-1; i--, j++) {
            if(gameTable[i][j] != symbolId) {
                break;
            }
            if(gameTable[i][j] == symbolId) {
                sum++;
                if(sum > 5) {
                    return 1;
                }
            }
        }

        return 0;
    }
}
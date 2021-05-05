package hu.alkfelj.dao;

import hu.alkfelj.model.Game;

import java.util.List;

public interface GameDAO {
    List<Game> findAll();
    void insert(Game game);
    Game find(int gameId);
    int getLastGameId();
}

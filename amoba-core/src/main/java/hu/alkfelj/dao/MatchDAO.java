package hu.alkfelj.dao;

import hu.alkfelj.model.Match;

import java.util.List;

public interface MatchDAO {
    List<Match> findAll();
    List<Match> findByGameId(int id);
    void insert(Match match);
    int getLastGameId();
}

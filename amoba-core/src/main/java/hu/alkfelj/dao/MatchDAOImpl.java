package hu.alkfelj.dao;

import hu.alkfelj.config.Configuration;
import hu.alkfelj.model.Coords;
import hu.alkfelj.model.Match;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchDAOImpl implements MatchDAO{
    private static final String FIND_ALL_MATCHES = "SELECT * FROM MATCH";
    private static final String INSERT_MATCH = "INSERT INTO MATCH(player, gameId, symbolId, step) VALUES(?,?,?,?)";
    private static final String FIND_LAST_GAME_ID = "SELECT gameId FROM Match ORDER BY gameId DESC limit 1";
    private static final String FIND_MATCH_BY_ID = "SELECT * FROM MATCH WHERE gameId = ?";

    private String connectionUrl;

    public MatchDAOImpl() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connectionUrl = Configuration.getValue("db.url");
    }

    @Override
    public List<Match> findAll() {
        List<Match> matches = new ArrayList<>();

        try (Connection c = DriverManager.getConnection(connectionUrl);
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(FIND_ALL_MATCHES);
        ) {
            while (rs.next()) {
                Match match = new Match();

                match.setId(rs.getInt("id"));
                match.setPlayer(rs.getString("player"));
                match.setSymbolId(rs.getInt("symbolId"));
                match.setStep(new Coords(rs.getString("step")));

                matches.add(match);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return matches;
    }

    @Override
    public List<Match> findByGameId(int id) {
        List<Match> matches = new ArrayList<>();

        try (Connection c = DriverManager.getConnection(connectionUrl);
             PreparedStatement stmt = c.prepareStatement(FIND_MATCH_BY_ID)
        ) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Match match = new Match();

                match.setId(rs.getInt("id"));
                match.setPlayer(rs.getString("player"));
                match.setSymbolId(rs.getInt("symbolId"));
                match.setStep(new Coords(rs.getString("step")));

                matches.add(match);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return matches;
    }

    @Override
    public void insert(Match match) {
        if (match == null)
            return;

        try (Connection c = DriverManager.getConnection(connectionUrl);
             PreparedStatement stmt = c.prepareStatement(INSERT_MATCH)
        ) {
            stmt.setString(1, match.getPlayer());
            stmt.setInt(2, match.getGameId());
            stmt.setInt(3, match.getSymbolId());
            stmt.setString(4, match.getStep().toString());

            stmt.executeUpdate();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public int getLastGameId(){
        int lastGameId = 0;

        try (Connection c = DriverManager.getConnection(connectionUrl);
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(FIND_LAST_GAME_ID);
        ) {
            lastGameId = rs.getInt("gameId");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return lastGameId;
    }
}

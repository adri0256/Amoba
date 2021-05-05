package hu.alkfelj.dao;

import hu.alkfelj.config.Configuration;
import hu.alkfelj.model.Game;
import hu.alkfelj.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GameDAOImpl implements GameDAO{
    private static final String FIND_ALL_MATCHES = "SELECT * FROM Game";
    private static final String INSERT_GAME = "INSERT INTO GAME(player, boardSize) VALUES(?, ?)";
    private static final String FIND_LAST_GAME_ID = "SELECT id FROM Game ORDER BY id DESC limit 1";
    private static final String FIND_GAME_BY_ID = "SELECT * from Game WHERE id = ?";

    private String connectionUrl;

    public GameDAOImpl() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connectionUrl = Configuration.getValue("db.url");
    }

    @Override
    public List<Game> findAll() {
        List<Game> games = new ArrayList<>();

        try (Connection c = DriverManager.getConnection(connectionUrl);
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(FIND_ALL_MATCHES);
        ) {
            while (rs.next()) {
                Game game = new Game();

                game.setId(rs.getInt("id"));
                game.setPlayer(rs.getString("player"));
                game.setBoardSize(rs.getInt("boardSize"));

                games.add(game);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return games;
    }

    @Override
    public void insert(Game game) {
        if (game == null)
            return;

        try (Connection c = DriverManager.getConnection(connectionUrl);
             PreparedStatement stmt = c.prepareStatement(INSERT_GAME)
        ) {
            stmt.setString(1, game.getPlayer());
            stmt.setInt(2, game.getBoardSize());

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
            lastGameId = rs.getInt("id");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return lastGameId;
    }

    @Override
    public Game find(int gameId) {
        Game game = new Game();

        try (Connection c = DriverManager.getConnection(connectionUrl);
             PreparedStatement stmt = c.prepareStatement(FIND_GAME_BY_ID)
        ) {
            stmt.setInt(1, gameId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                game.setId(gameId);
                game.setPlayer(rs.getString("player"));
                game.setBoardSize(rs.getInt("boardSize"));
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return game;
    }
}

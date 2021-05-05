package hu.alkfejl.controller;


import hu.alkfelj.dao.GameDAO;
import hu.alkfelj.dao.GameDAOImpl;
import hu.alkfelj.dao.MatchDAO;
import hu.alkfelj.dao.MatchDAOImpl;
import hu.alkfelj.model.Game;
import hu.alkfelj.model.Match;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/GameController")
public class GameController extends HttpServlet {
    private GameDAO gameDAO = new GameDAOImpl();
    private MatchDAO matchDAO = new MatchDAOImpl();
    private Game game;
    private static int counter = 0;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Match> currentMatch = matchDAO.findByGameId(game.getId());
        String pressedBtn = request.getParameter("id");

        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = response.getWriter();

        if(pressedBtn.equals("forward") && counter < currentMatch.size()) {
            int coordsId = currentMatch.get(counter).getStep().getX() * game.getBoardSize() + currentMatch.get(counter).getStep().getY();

            json.put("coords", coordsId);
            json.put("symbol", currentMatch.get(counter).getSymbolId());
            json.put("player", currentMatch.get(counter).getPlayer());

            if (counter < currentMatch.size()) {
                counter++;
                json.put("end", false);
                json.put("begin", false);
                if(counter >= currentMatch.size())
                    json.put("end", true);
            }
        }

        if(pressedBtn.equals("back")) {
            if(counter > 0) {
                counter--;
                json.put("begin", false);
                json.put("end", false);
            }
            if(counter == 0)
                json.put("begin", true);

            int coordsId = currentMatch.get(counter).getStep().getX() * game.getBoardSize() + currentMatch.get(counter).getStep().getY();

            json.put("coords", coordsId);
            json.put("symbol", currentMatch.get(counter).getSymbolId());
            json.put("player", currentMatch.get(counter).getPlayer());
        }



        out.write(json.toString());

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        game = gameDAO.find(MainController.gameId);
        counter = 0;

        request.setAttribute("player", game.getPlayer());
        request.setAttribute("boardSize", game.getBoardSize());
    }
}

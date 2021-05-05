package hu.alkfejl.controller;

import hu.alkfelj.dao.*;
import hu.alkfelj.model.Game;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/MainController")
public class MainController extends HttpServlet {
    private GameDAO gameDAO = new GameDAOImpl();
    public static int gameId;

    public MainController() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Game> allGames = gameDAO.findAll();

        request.setAttribute("games", allGames);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");

        gameId = Integer.parseInt(request.getParameter("gameId"));

        response.sendRedirect("pages/gameTable.jsp");
    }
}

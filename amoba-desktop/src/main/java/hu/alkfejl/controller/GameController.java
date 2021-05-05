package hu.alkfejl.controller;

import hu.alkfejl.App;
import hu.alkfelj.dao.*;
import hu.alkfelj.gameLogic.Ai;
import hu.alkfelj.gameLogic.AiImpl;
import hu.alkfelj.gameLogic.GameLogic;
import hu.alkfelj.gameLogic.GameLogicImpl;
import hu.alkfelj.model.Coords;
import hu.alkfelj.model.Game;
import hu.alkfelj.model.Match;
import hu.alkfelj.model.User;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class GameController implements Initializable {
    private static final Image IMG_X = new Image("/img/X.png", 35, 35, true, true);
    private static final Image IMG_O = new Image("/img/O.png", 35, 35, true, true);
    private static final int SYMBOL_ID_X = 1;
    private static final int SYMBOL_ID_O = 2;

    List<User> players = new ArrayList<>();

    MatchDAO matchDAO = new MatchDAOImpl();
    GameLogic gameLogic = new GameLogicImpl();
    GameDAO gameDAO = new GameDAOImpl();
    Ai ai = new AiImpl();
    Match match = new Match();

    private double roundTime;
    private double gameTime;
    public static boolean isAi = false;
    public int boardSize = 10;
    private boolean ended = false;

    private Task<Integer> taskGameTime = new Task<Integer>() {
        {
            this.setOnSucceeded(event ->{
                updateProgress(1,1);
            });
        }
        @Override
        protected Integer call() throws Exception {
            int i;
            for (i = (int) gameTime; i > 0; i--){
                String status = "Game Time: " + i/60 + ":" + i%60;

                this.updateMessage(status);
                this.updateValue(i);
                this.updateProgress(i, gameTime);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    if(this.isCancelled())
                        break;

                    e.printStackTrace();
                }
            }

            return i;
        }
    };

    Service taskRoundTime = new Service() {
        @Override
        protected Task createTask() {
            return new Task<Integer>() {
                @Override
                protected Integer call() throws Exception {
                    int i;
                    for (i = (int) roundTime; i > 0; i--){
                        if (isCancelled())
                        {
                            throw new InterruptedException();
                        }

                        String status = "Round Time: " + i + " sec";

                        this.updateMessage(status);
                        this.updateValue(i);
                        this.updateProgress(i, roundTime);

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            if(this.isCancelled())
                                break;

                            e.printStackTrace();
                        }
                    }
                    return i;
                }
            };
        }
    };

    @FXML
    private Label roundCounterLabel;

    @FXML
    private Label gameTimeLabel;

    @FXML
    private Label roundTimeLabel;

    @FXML
    GridPane gameTable;

    @FXML
    public void backToMenu() {
        taskRoundTime.cancel();
        taskGameTime.cancel();
        App.loadFXML("/fxml/main_window.fxml");
    }

    public void setGameTableSize(int width, int height) {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100f/width);

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(100f/height);

        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height; row++) {
                GridPane pane = new GridPane();

                int columnLocal = column;
                int rowLocal = row;

                pane.setOnMouseClicked(e -> {
                    mainLogic(rowLocal, columnLocal);
                });

                pane.setId(columnLocal + "_" + rowLocal);
                pane.setAlignment(Pos.CENTER);

                gameTable.add(pane, column, row);
            }
        }

        for (int column = 0; column < width; column++)
            gameTable.getColumnConstraints().add(column, columnConstraints);

        for (int row = 0; row < height; row++)
            gameTable.getRowConstraints().add(row, rowConstraints);
    }

    private void startGameTask(){
        Thread gameTime = new Thread(taskGameTime);
        gameTime.setDaemon(true);
        gameTime.start();
    }
    private void startRoundTask(){
        taskRoundTime.start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getDataDialog();

        gameTimeLabel.textProperty().bind(taskGameTime.messageProperty());
        roundTimeLabel.textProperty().bind(taskRoundTime.messageProperty());

        taskGameTime.setOnSucceeded(event ->{
            ended = true;
            taskRoundTime.cancel();
            taskGameTime.cancel();

            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Time is up");
            a.setHeaderText(null);
            a.setContentText("Game over");
            a.showAndWait();
            App.loadFXML("/fxml/main_window.fxml");
        });

        taskRoundTime.setOnSucceeded(event -> {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Round time is exceeded");
            a.setHeaderText(null);
            a.setContentText("Next Player's turn");
            a.showAndWait();

            if(isAi)
                aiTurn();

            taskRoundTime.reset();
            taskRoundTime.restart();
            gameLogic.incrementRoundCounter();

        });

        if(roundTime > 0 && gameTime > 0) {
            startGameTask();
            startRoundTask();


            Game game = new Game();

            game.setPlayer(players.get(0).getName());
            game.setBoardSize(boardSize);

            gameDAO.insert(game);

            int lastGameId = gameDAO.getLastGameId();

            match.setGameId(lastGameId);

            gameLogic.generateBoard(boardSize);
            gameLogic.setRoundCounter(0);

            setGameTableSize(boardSize, boardSize);
        }
    }

    private void mainLogic(int row, int column) {
        if(!isAi) {
            if(gameLogic.getTile(row, column) == 0) {
                if(gameLogic.getRoundCounter() % 2 == 0) {
                    setImage(row, column, IMG_X);
                    gameLogic.setTile(row, column, SYMBOL_ID_X);

                    match.setPlayer(players.get(0).getName());
                    match.setSymbolId(SYMBOL_ID_X);
                    match.setStep(new Coords(row, column));

                    matchDAO.insert(match);

                    if(gameLogic.checkWin(row, column, SYMBOL_ID_X) == 1) {
                        winDialog(SYMBOL_ID_X);
                    }
                } else {
                    setImage(row, column, IMG_O);
                    gameLogic.setTile(row, column, SYMBOL_ID_O);

                    match.setPlayer("Player2");
                    match.setSymbolId(SYMBOL_ID_O);
                    match.setStep(new Coords(row, column));

                    matchDAO.insert(match);

                    if(gameLogic.checkWin(row, column, SYMBOL_ID_O) == 1) {
                        winDialog(SYMBOL_ID_O);
                    }
                }
            }
        }

        if (isAi){
            if(gameLogic.getTile(row, column) == 0) {

                setImage(row, column, IMG_X);
                gameLogic.setTile(row, column, SYMBOL_ID_X);

                match.setPlayer(players.get(0).getName());
                match.setSymbolId(SYMBOL_ID_X);
                match.setStep(new Coords(row, column));

                matchDAO.insert(match);

                if(gameLogic.checkWin(row, column, SYMBOL_ID_X) == 1) {
                    winDialog(SYMBOL_ID_X);
                }

                if(!ended)
                    aiTurn();
            }
        }

        if(!ended) {
            taskRoundTime.restart();

            gameLogic.incrementRoundCounter();

            roundCounterLabel.textProperty().bind(Bindings.concat("Round: ", gameLogic.roundCounterProperty()));
        }
    }

    private void aiTurn(){
        gameTable.setDisable(true);

        while (true) {
            Coords coords = ai.nextTurn(boardSize);
            if(gameLogic.getTile(coords.getX(), coords.getY()) == 0) {
                setImage(coords.getX(), coords.getY(), IMG_O);
                gameLogic.setTile(coords.getX(), coords.getY(), SYMBOL_ID_O);

                if(gameLogic.checkWin(coords.getX(), coords.getY(), SYMBOL_ID_O) == 1) {
                    winDialog(SYMBOL_ID_O);
                }

                match.setPlayer("AI");
                match.setSymbolId(SYMBOL_ID_O);
                match.setStep(coords);

                matchDAO.insert(match);

                break;
            }
        }

        gameTable.setDisable(false);
    }

    private void winDialog(int symbolId){
        ended = true;
        taskRoundTime.cancel();
        taskGameTime.cancel();

        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Win");
        a.setHeaderText(null);
        String symbol = symbolId == SYMBOL_ID_X ? "X" : "O";
        String player = symbolId == SYMBOL_ID_X ? players.get(0).getName() : "Player2";

        a.setContentText("Player: " + player + " wins with " + symbol);

        a.showAndWait();

        App.loadFXML("/fxml/main_window.fxml");
    }

    private void setImage(int row, int column, Image img) {
        GridPane pane = (GridPane) gameTable.lookup("#" + column + "_" + row);

        ImageView imageView = new ImageView();
        imageView.setImage(img);

        pane.add(imageView,0, 0);
    }

    private void getDataDialog(){
        UserDAO userDAO = new UserDAOImpl();
        List<User> users = userDAO.findAll();

        Dialog<Results> dialog = new Dialog<>();
        dialog.setTitle("Profile, board size and times");
        dialog.setHeaderText("Game Settings");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK);

        Spinner<Integer> boardSizeTF = new Spinner<>();
        Spinner<Integer> gameTimeTF = new Spinner<>();
        Spinner<Integer> roundTimeTF = new Spinner<>();

        NumberFormat format = NumberFormat.getIntegerInstance();
        UnaryOperator<TextFormatter.Change> filter = c -> {
            if (c.isContentChange()) {
                ParsePosition parsePosition = new ParsePosition(0);
                format.parse(c.getControlNewText(), parsePosition);

                if (parsePosition.getIndex() == 0 ||
                        parsePosition.getIndex() < c.getControlNewText().length()) {
                    return null;
                }
            }
            return c;
        };
        TextFormatter<Integer> numFormatter = new TextFormatter<Integer>(
                new IntegerStringConverter(), 0, filter);
        TextFormatter<Integer> numFormatter2 = new TextFormatter<Integer>(
                new IntegerStringConverter(), 0, filter);
        TextFormatter<Integer> numFormatter3 = new TextFormatter<Integer>(
                new IntegerStringConverter(), 0, filter);

        boardSizeTF.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 20, Integer.parseInt("5")));
        boardSizeTF.setEditable(true);
        boardSizeTF.getEditor().setTextFormatter(numFormatter);

        gameTimeTF.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 20, Integer.parseInt("5")));
        gameTimeTF.setEditable(true);
        gameTimeTF.getEditor().setTextFormatter(numFormatter2);

        roundTimeTF.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 20, Integer.parseInt("5")));
        roundTimeTF.setEditable(true);
        roundTimeTF.getEditor().setTextFormatter(numFormatter3);

        ObservableList<User> options = FXCollections.observableArrayList(users);

        StringConverter<User> converter = new StringConverter<>() {
            @Override
            public String toString(User user) {
                return user != null ? user.getName() : "";
            }

            @Override
            public User fromString(String userLogin) {
                throw new UnsupportedOperationException("back conversion not supported from " + userLogin);
            }
        };

        ComboBox<User> comboBox = new ComboBox<>(options);
        comboBox.setConverter(converter);
        comboBox.getSelectionModel().selectFirst();

        Label boardSizeLabel = new Label("Board Size (n)");
        Label gameTimeLabel = new Label("Game Time (minutes)");
        Label roundTimeLabel = new Label("Round Time (seconds)");

        dialogPane.setContent(new VBox(8,boardSizeLabel, boardSizeTF, gameTimeLabel, gameTimeTF, roundTimeLabel, roundTimeTF, comboBox));

        Platform.runLater(boardSizeTF::requestFocus);
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return new Results(
                        boardSizeTF.getValue(),
                        comboBox.getValue(),
                        gameTimeTF.getValue(),
                        roundTimeTF.getValue()
                );
            }
            return null;
        });

        Optional<Results> optionalResult = dialog.showAndWait();
        optionalResult.ifPresent((Results results) -> {
            players.add(results.user);
            boardSize = results.boardSize;

            roundTime = results.roundTime;
            gameTime = results.gameTime * 60;

            System.out.println(results.toString());
        });
    }

    private static class Results {
        int boardSize;
        int gameTime;
        int roundTime;
        User user;

        public Results(int boardSize, User user, int gameTime, int roundTime) {
            this.boardSize = boardSize;
            this.gameTime = gameTime;
            this.roundTime = roundTime;
            this.user = user;
        }

        @Override
        public String toString() {
            return "Size: " + boardSize + " User: " + user.getName() + " RoundTime: " + roundTime + " GameTime: " + gameTime;
        }
    }
}
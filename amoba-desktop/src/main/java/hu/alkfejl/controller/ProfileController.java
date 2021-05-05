package hu.alkfejl.controller;

import hu.alkfejl.App;
import hu.alkfelj.dao.UserDAO;
import hu.alkfelj.dao.UserDAOImpl;
import hu.alkfelj.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    UserDAO userDAO = new UserDAOImpl();
    List<User> users;

    @FXML
    private TableView<User> profiles;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, Void> actionsColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshTable();

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        actionsColumn.setCellFactory(actionsTableColumn -> new TableCell<>(){
            private final Button deleteBtn = new Button("Delete");
            private final Button editBtn = new Button("Edit");

            {
                deleteBtn.setOnAction(event -> {
                    User u = getTableRow().getItem();
                    deleteUser(u);

                    refreshTable();
                });

                editBtn.setOnAction(event -> {
                    User u = getTableRow().getItem();
                    editUser(u);

                    refreshTable();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if(empty){
                    setGraphic(null);
                } else {
                    HBox container = new HBox();
                    container.getChildren().addAll(editBtn, deleteBtn);
                    container.setSpacing(10f);
                    setGraphic(container);
                }
            }
        });
    }

    @FXML
    public void onCancel(){
        App.loadFXML("/fxml/main_window.fxml");
    }

    @FXML
    public void onNewProfile(){
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("New Profile");
        dialog.setHeaderText("Create a new profile");
        dialog.setContentText("Name: ");

        Optional<String> res = dialog.showAndWait();

        res.ifPresent(userName -> {
            User user = new User();
            user.setName(userName);
            user.setCreationDate(LocalDate.now());

            userDAO.insert(user);
        });

        refreshTable();
    }

    private void editUser(User u) {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Edit Profile");
        dialog.setHeaderText("Edit Profile");
        dialog.setContentText("New Name: ");

        Optional<String> res = dialog.showAndWait();

        res.ifPresent(userName -> {
            u.setName(userName);
            u.setCreationDate(LocalDate.now());

            userDAO.update(u);
        });
    }

    private void deleteUser(User u) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are sure you want to delete this profile: " + u.getName(), ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(buttonType -> {
            if(buttonType.equals(ButtonType.YES))
                userDAO.delete(u);
        });
    }

    private void refreshTable(){
        users = userDAO.findAll();
        profiles.getItems().setAll(users);
    }
}

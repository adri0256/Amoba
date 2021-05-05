package hu.alkfelj.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class User {
    /*
    * id (szám, elsődleges kulcs)           // Egyedi azonosító
    * név (szöveg, kötelező)                // A játékos felhasználó neve
    * létrehozás ideje (szöveg, kötelező)   // Létrehozás ideje
    */

    private IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private StringProperty name = new SimpleStringProperty(this, "name");
    private ObjectProperty<LocalDate> creationDate = new SimpleObjectProperty<>(this, "creationDate");

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public LocalDate getCreationDate() {
        return creationDate.get();
    }

    public ObjectProperty<LocalDate> creationDateProperty() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate.set(creationDate);
    }

    @Override
    public String toString() {
        return name.toString();
    }
}

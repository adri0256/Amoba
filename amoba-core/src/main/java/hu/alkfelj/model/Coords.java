package hu.alkfelj.model;

public class Coords {
    private int x;
    private int y;

    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coords(String coords) {
        toCoords(coords);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return x + ":" + y;
    }

    public void toCoords(String coords) {
        String[] splittedCoords = coords.split(":");

        if (splittedCoords.length == 2) {
            this.x = Integer.parseInt(splittedCoords[0]);
            this.y = Integer.parseInt(splittedCoords[1]);
        }
    }
}

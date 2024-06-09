package data;

public class DVM {

    public DVM(int x, int y, String address) {
        this.coordX = x;
        this.coordY = y;
        this.address = address;
    }

    private final int coordX;

    private final int coordY;

    private final String address;

    public int getX() {
        return coordX;
    }

    public int getY() {
        return coordY;
    }

    public String getAddress() {
        return address;
    }

}
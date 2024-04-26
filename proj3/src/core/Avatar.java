package core;

public class Avatar {

    private int xA;
    private int yA;

    public Avatar(int xA, int yA) {
        this.xA = xA;
        this.yA = yA;
    }

    public Avatar up() {
        Avatar newALocate = new Avatar(xA, yA + 1);
        return newALocate;
    }

    public Avatar down() {
        Avatar newALocate = new Avatar(xA, yA - 1);
        return newALocate;
    }

    public Avatar left() {
        Avatar newALocate = new Avatar(xA - 1, yA);
        return newALocate;
    }

    public Avatar right() {
        Avatar newALocate = new Avatar(xA + 1, yA);
        return newALocate;
    }

    public int getxA() {
        return xA;
    }

    public int getyA() {
        return yA;
    }
}

package core;

public class Main {
    public static void main(String[] args) {
        // Display game menu
        Interact interact = new Interact();
        interact.displayMenu();
        interact.handleKeyBoardUserInput();
        interact.runGame();
        interact.finishGame();
        System.exit(1);
    }
}

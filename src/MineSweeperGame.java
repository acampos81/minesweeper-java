import java.util.ArrayList;

public class MineSweeperGame {
    public static void main(String[] args)
    {

        clearScreen();

        MessageDisplay messageDisplay = new MessageDisplay();

        MineFieldGenerator generator = new MineFieldGenerator();
        generator.registerMessageListener(messageDisplay);

        Minefield mineField = new Minefield(generator);

        GameManager gameManager = new GameManager();
        gameManager.setMineField(mineField);
        gameManager.registerMessageListener(messageDisplay);

        InputHandler inputHandler = new InputHandler();
        inputHandler.registerCommandListener(gameManager);
        inputHandler.registerMessageListener(messageDisplay);

        DisplayManager displayManager = new DisplayManager();
        ArrayList<Displayable> displayables = new ArrayList<>();
        displayables.add(gameManager);
        displayables.add(messageDisplay);
        displayables.add(inputHandler);
        displayManager.setDisplayables(displayables);

        while(gameManager.getState() != GameState.QUIT)
        {
            displayManager.display();
            inputHandler.readInput();
            clearScreen();
        }

        clearScreen();
        System.out.println("Thanks for playing!");
    }

    public static void clearScreen()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}

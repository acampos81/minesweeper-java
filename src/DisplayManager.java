import java.util.ArrayList;

public class DisplayManager {

    private ArrayList<Displayable> displayables;

    public DisplayManager()
    {

    }

    public void setDisplayables(ArrayList<Displayable> displayables)
    {
        this.displayables = displayables;
    }

    public void display()
    {
        if(displayables == null)
            return;

        displayHeader();
        displayHelp();

        System.out.println();

        for(int i=0; i<displayables.size(); i++)
        {
            displayables.get(i).display();
        }
    }

    private void displayHeader()
    {
        System.out.println("---------------------------------");
        System.out.println("* * * M I N E S W E E P E R * * *");
        System.out.println("---------------------------------");
        System.out.println();
    }

    private void displayHelp()
    {
        System.out.println("new 8 10    - Starts a new game of size 8x8 with 10 bombs. Max size is 20x20");
        System.out.println("reveal 3 5  - Reveals the tile at column 3, row 5.");
        System.out.println("mark 5 6    - Marks a mine at column 5, row 6.");
        System.out.println("exit        - Exits the game.");
        System.out.println();
    }
}

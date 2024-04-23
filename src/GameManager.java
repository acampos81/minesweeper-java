import java.util.ArrayList;

public class GameManager implements CommandListener, GameStateListener, Displayable{

    private Minefield mineField;
    private GameState state;
    private MessageListener messageListener;

    public GameManager()
    {
        updateGameState(GameState.IDLE);
    }

    public void setMineField(Minefield mineField)
    {
        this.mineField = mineField;
        this.mineField.registerGameStateListener(this);
    }

    public GameState getState()
    {
        return state;
    }

    public void registerMessageListener(MessageListener listener)
    {
        messageListener = listener;;
    }

    @Override
    public void handleCommand(String commandString, int val1, int val2) {
        switch(commandString)
        {
            case "new":
                createNewGame(val1,val2);
                break;
            case "reveal":
                revealTile(val1, val2);
                break;
            case "mark":
                markTile(val1, val2);
                break;
            case "exit":
                updateGameState(GameState.QUIT);
                break;
        }
    }

    @Override
    public void updateGameState(GameState state)
    {
        this.state = state;

        if(state == GameState.WIN)
        {
            mineField.revealAllMines();
            messageListener.HandleMessage("YOU WIN!");
        }

        if(state == GameState.LOSE)
        {
            mineField.revealAllMines();
            messageListener.HandleMessage("GAME OVER!");
        }
    }

    private void createNewGame(int size, int mines)
    {
        updateGameState(GameState.PLAYING);
        mineField.reset(size, mines);
    }

    private void revealTile(int column, int row)
    {
        if(state != GameState.PLAYING)
            return;

        if(!mineField.reveal(column, row))
        {
            messageListener.HandleMessage("Tile "+column+" "+row+" already revealed.");
            return;
        }

        int tilesWithoutMines = mineField.getTotalTiles() - mineField.getTotalMines();
        int revealedTiles = mineField.getRevealedTiles();
        if(revealedTiles == tilesWithoutMines)
        {
            updateGameState(GameState.WIN);
        }
    }

    private void markTile(int column, int row)
    {
        if(state != GameState.PLAYING)
            return;

        if(!mineField.mark(column, row))
        {
            messageListener.HandleMessage("Can't mark revealed tile at "+column+" "+row);
            return;
        }

        if(checkMarkedMines())
        {
            updateGameState(GameState.WIN);
        }
    }

    private boolean checkMarkedMines()
    {
        ArrayList<MineLocation> markedLocations = mineField.getMarkedLocations();
        ArrayList<ArrayList<Tile>> tileMap = mineField.getTileMap();
        int totalMines = mineField.getTotalMines();

        if(markedLocations.size() != totalMines)
        {
            return false;
        }

        int correctlyMarkedTiles = 0;
        for(MineLocation location : markedLocations)
        {
            int i = location.getColumn();
            int j = location.getRow();
            if(tileMap.get(i).get(j).getHasMine())
            {
                correctlyMarkedTiles++;
            }
        }

        //The player has marked only tiles with mines.
        return correctlyMarkedTiles == totalMines;
    }

    @Override
    public void display()
    {
        ArrayList<ArrayList<Tile>> tileMap = mineField.getTileMap();

        //top left corner space
        System.out.print("  ");

        //print column numbers
        for(int i=0; i<tileMap.size(); i++)
        {
            //needed for alignment
            if(i < 10)
            {
                System.out.print(" "+i+" ");
            }else
            {
                System.out.print(i+" ");
            }

        }
        System.out.println();

        for(int i=0; i<tileMap.size(); i++)
        {
            for(int j=0; j<tileMap.size(); j++)
            {
                String spacing = i < 10 ? " " : "";
                //prints row number on the left
                if(j == 0)
                    System.out.print(spacing+i);

                System.out.print(tileMap.get(j).get(i).getDisplayState());
            }
            System.out.println();
        }
        System.out.println();
    }
}

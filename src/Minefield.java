import java.util.ArrayList;

public class Minefield {

    private MineFieldData data;
    private GameStateListener gameStateListener;
    private ArrayList<MineLocation> markedLocations;
    private int revealedTiles;
    private MineFieldGenerator mineFieldGenerator;

    public Minefield (MineFieldGenerator generator)
    {
        data = new MineFieldData();
        mineFieldGenerator = generator;
        markedLocations = new ArrayList<>();
    }

    public ArrayList<ArrayList<Tile>> getTileMap()
    {
        return data.tileMap;
    }

    public int getTotalTiles()
    {
        return data.totalTiles;
    }

    public int getTotalMines()
    {
        return data.totalMines;
    }

    public int getRevealedTiles()
    {
        return revealedTiles;
    }

    public ArrayList<MineLocation> getMarkedLocations() {
        return markedLocations;
    }

    public void registerGameStateListener(GameStateListener listener)
    {
        gameStateListener = listener;
    }

    public void reset(int size, int mines)
    {
        revealedTiles = 0;
        data = mineFieldGenerator.generate(size, mines);
    }
    
    public boolean reveal(int column, int row)
    {
        Tile tile = data.tileMap.get(column).get(row);

        if(tile.getRevealed())
        {
            return false;
        }

        if(tile.getHasMine())
        {
            tile.reveal();
            gameStateListener.updateGameState(GameState.LOSE);
        }else
        {
            checkTiles(column, row);
        }

        return true;
    }

    public void revealAllMines()
    {
        for(MineLocation location : data.mineLocations)
        {
            int i = location.getColumn();
            int j = location.getRow();
            data.tileMap.get(i).get(j).reveal();
        }
    }
    
    public boolean mark(int column, int row)
    {
        Tile tile = data.tileMap.get(column).get(row);

        if(tile.getRevealed())
        {
            return false;
        }

        if(tile.getIsMarked())
        {
            tile.unmark();
            removeMarkedLocation(column, row);
        }else
        {
            tile.mark();
            markedLocations.add(new MineLocation(column, row));
        }

        return true;
    }

    private void removeMarkedLocation(int column, int row)
    {
        MineLocation removeLocation = null;
        for(MineLocation location : markedLocations)
        {
            if(location.getColumn() == column && location.getRow() == row)
            {
                removeLocation = location;
                break;
            }
        }

        if(removeLocation != null)
            markedLocations.remove(removeLocation);
    }

    //Recursive Greedy BFS algorithm to reveal neighboring tiles
    private void checkTiles(int i, int j)
    {
       //out of bounds
       if(i < 0 || i >= data.tileMap.size())
           return;

       //out of bounds
       if(j < 0 || j >= data.tileMap.get(i).size())
           return;

       Tile tile = data.tileMap.get(i).get(j);

       //skip mine tiles
       if(tile.getHasMine())
           return;

       //skip revealed tiles
       if(tile.getRevealed())
           return;

       //skip marked tiles
       if(tile.getIsMarked())
           return;

       //reveal current tile
        tile.reveal();

        revealedTiles++;

        //skip additional searches if tile contains a number
        if(tile.getAdjacentMines() > 0)
            return;

        //search top left
        checkTiles(i-1,j-1);

        //search top center
        checkTiles(i-1,j);

        //search top right
        checkTiles(i-1,j+1);

        //search center right
        checkTiles(i,j+1);

        //search bottom right
        checkTiles(i+1,j+1);

        //search bottom center
        checkTiles(i+1,j);

        //search bottom left
        checkTiles(i+1,j-1);

        //search center left
        checkTiles(i,j-1);
    }
}

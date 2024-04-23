import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MineFieldGenerator
{
    private static final int MIN_FIELD_SIZE = 4;
    private static final int MAX_FIELD_SIZE = 20;
    private static final long MINE_GEN_MAX_TIME = 2000L;
    private static final float ROW_MINE_RATIO = .33f;

    private MessageListener messageListener;

    public MineFieldGenerator()
    {
    }

    public void registerMessageListener(MessageListener listener)
    {
        messageListener = listener;
    }

    public MineFieldData generate(int size, int mines)
    {
        if(size < MIN_FIELD_SIZE)
        {
            size = MIN_FIELD_SIZE;
            messageListener.HandleMessage("Size to small. Setting to minim size "+size);
        }

        if(size > MAX_FIELD_SIZE)
        {
            size = MAX_FIELD_SIZE;
            messageListener.HandleMessage("Size too big.  Setting to max size "+size);
        }

        ArrayList<Integer> minesPerRow = generateMinesPerRow(size, mines);
        ArrayList<ArrayList<Integer>> mineField = generateMineField(size, minesPerRow);
        MineFieldData data = generateData(mineField);
        data.tileMap = generateTileMap(size, mineField);
        return data;
    }

    private ArrayList<Integer> generateMinesPerRow(int size, int mines)
    {
        //Initialize each row's mine count to 0
        ArrayList<Integer> minesPerRow = new ArrayList<>();
        for(int i=0; i<size; i++)
        {
            minesPerRow.add(0);
        }

        Random rand = new Random();
        int rowMineMax = Math.round(size * ROW_MINE_RATIO);
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0L;

        //1. select a random row index
        //2. get the current number of mines for that row
        //3. if the number is under the max amount, add 1 mine
        //4. repeat until end conditions are met.

        while(mines > 0 && elapsedTime < MINE_GEN_MAX_TIME)
        {
            int randomIndex = rand.nextInt(size);
            Integer rowMines = minesPerRow.get(randomIndex);
            if(rowMines < rowMineMax) {
                rowMines++;
                minesPerRow.set(randomIndex, rowMines);
                mines--;
            }
            elapsedTime = System.currentTimeMillis() - startTime;
        }

        //if there are any requested mines left, the user asked for too many.
        if(mines > 0)
        {
            messageListener.HandleMessage("Map size too small. "+mines+" mines not placed.");
        }

        return minesPerRow;
    }

    private ArrayList<ArrayList<Integer>> generateMineField(int size, ArrayList<Integer> minesPerRow)
    {
        ArrayList<ArrayList<Integer>> mineField = new ArrayList<>();

        //Rows
        for(int i=0; i<size; i++)
        {
            ArrayList<Integer> row = new ArrayList<>();
            int mineCount = minesPerRow.get(i);

            //Columns
            for(int j=0; j<size; j++)
            {
                if(mineCount > 0)
                {
                    mineCount--;
                    row.add(1);
                }else
                {
                    row.add(0);
                }
            }
            Collections.shuffle(row);
            mineField.add(row);
        }

        return mineField;
    }

    private ArrayList<ArrayList<Tile>> generateTileMap(int size, ArrayList<ArrayList<Integer>> mineField)
    {
        ArrayList<ArrayList<Tile>> gameBoard = new ArrayList<>();

        //Initialize the 2D Tile Array
        for(int i=0; i<size; i++)
        {
            ArrayList<Tile> tileRow = new ArrayList<>();
            for(int j=0; j<size; j++)
            {
                Tile tile = new Tile(mineField.get(i).get(j) == 1);
                tile.contents = tile.getHasMine() ? Tile.MINE : Tile.EMPTY;
                tileRow.add(tile);
            }
            gameBoard.add(tileRow);
        }

        //Increment adjacent mine count for all neighbors of a tile containing a mine
        for(int i=0; i<size; i++)
        {
            for(int j=0; j<size; j++)
            {
                if(gameBoard.get(i).get(j).getHasMine())
                {
                    //top left neighbor
                    if(i-1 >= 0 && j-1 >=0)
                    {
                        updateNeighbor(gameBoard,i-1,j-1);
                    }

                    //top center neighbor
                    if(i-1 >= 0)
                    {
                        updateNeighbor(gameBoard,i-1,j);
                    }

                    //top right neighbor
                    if(i-1 >= 0 && j+1 <= size-1)
                    {
                        updateNeighbor(gameBoard,i-1,j+1);
                    }

                    //center right neighbor
                    if(j+1 <= size-1)
                    {
                        updateNeighbor(gameBoard,i,j+1);
                    }

                    //bottom right neighbor
                    if(i+1 <= size-1 && j+1 <= size-1)
                    {
                        updateNeighbor(gameBoard,i+1,j+1);
                    }

                    //bottom center neighbor
                    if(i+1 <= size-1)
                    {
                        updateNeighbor(gameBoard,i+1,j);
                    }

                    //bottom left neighbor
                    if(i+1 <= size-1 && j-1 >= 0)
                    {
                        updateNeighbor(gameBoard,i+1,j-1);
                    }

                    //center left neighbor
                    if(j-1 >= 0)
                    {
                        updateNeighbor(gameBoard,i,j-1);
                    }
                }
            }
        }

        return gameBoard;
    }
    
    private void updateNeighbor(ArrayList<ArrayList<Tile>> gameBoard, int i, int j)
    {
        Tile neighbor = gameBoard.get(i).get(j);
        if(neighbor != null)
        {
            if(!neighbor.getHasMine())
            {
                Integer adjacentMines = neighbor.getAdjacentMines();
                adjacentMines++;
                neighbor.setAdjacentMines(adjacentMines);
            }
        }else
        {
            messageListener.HandleMessage("Error: Unable to get neighbor for tile at ("+i+","+j+")");
        }
    }


    private MineFieldData generateData(ArrayList<ArrayList<Integer>> mineField)
    {
        MineFieldData data = new MineFieldData();
        data.totalTiles = mineField.size() * mineField.size();
        data.mineLocations = new ArrayList<>();

        for(int i=0; i<mineField.size(); i++)
        {
            for(int j=0; j<mineField.get(i).size(); j++)
            {
                if(mineField.get(i).get(j) == 1)
                {
                    data.totalMines++;
                    data.mineLocations.add(new MineLocation(i, j));
                }
            }
        }
        return data;
    }
}

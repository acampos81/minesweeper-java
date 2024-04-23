public class Tile {

    public static final String SQUARE  = "\u25A0";
    public static final String MINE = "*";
    public static final String FLAG = "P";
    public static final String EMPTY = " ";

    private final boolean hasMine;
    private boolean revealed;
    private boolean isMarked;
    private Integer adjacentMines;
    private String displayState;
    public String contents;

    public Tile(boolean hasMine)
    {
        this.hasMine = hasMine;
        adjacentMines = 0;
        displayState = SQUARE;
        contents = "";
    }

    public boolean getHasMine()
    {
        return hasMine;
    }

    public boolean getRevealed()
    {
        return revealed;
    }

    public boolean getIsMarked() {
        return isMarked;
    }

    public Integer getAdjacentMines()
    {
        return adjacentMines;
    }

    public String getDisplayState()
    {
        return "["+displayState+"]";
    }

    public void setAdjacentMines(Integer value)
    {
        adjacentMines = value;
        contents = adjacentMines.toString();
    }

    public void reveal()
    {
        revealed = true;
        displayState = contents;
    }

    public void mark()
    {
        isMarked = true;
        displayState = FLAG;
    }

    public void unmark()
    {
        isMarked = false;
        displayState = SQUARE;
    }
}

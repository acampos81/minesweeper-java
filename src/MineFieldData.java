import java.util.ArrayList;

public class MineFieldData {
    public int totalTiles;
    public int totalMines;
    public ArrayList<MineLocation> mineLocations;
    public ArrayList<ArrayList<Tile>> tileMap;

    public MineFieldData()
    {
        mineLocations = new ArrayList<>();
        tileMap = new ArrayList<>();
    }
}

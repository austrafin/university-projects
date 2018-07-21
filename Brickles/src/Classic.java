
/**
 * @author Matti Syrjanen
 * Classic brickles where there is a bat on the bottom and it hits bricks on the top.
 *
 */
public class Classic extends BrickGame {

    public Classic(int difficulty, int balls, int rows) {
        super(difficulty, balls, rows);
    }

    public Classic() {

    }

    @Override
    public int getGameMode() {
        return 0;
    }

    @Override
    public void setGameModeSettings(int noOfRows) {
        firstRow = 3;
        lastRow = noOfRows + firstRow - 1;
        firstCol = 1;
        lastCol = wall.getColumns();
        WALL_NORTH = true;
        WALL_SOUTH = false;
        WALL_EAST = true;
        WALL_WEST = true;
    }
}

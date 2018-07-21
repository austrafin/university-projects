
/**
 * @author Matti Syrjanen
 * Game mode where there is a bat on each side of the game field and bricks in the middle
 */
public class AllIn extends BrickGame {

    public AllIn(int difficulty, int balls, int rows) {
        super(difficulty, balls, rows);
    }

    public AllIn() {

    }

    @Override
    public void setGameModeSettings(int noOfRows) {
        firstRow = (wall.getRows() - maxRows) / 2 + 1;
        lastRow = noOfRows + firstRow - 1;
        firstCol = 4;
        lastCol = wall.getColumns() - firstCol + 1;
        WALL_NORTH = false;
        WALL_SOUTH = false;
        WALL_EAST = false;
        WALL_WEST = false;
    }

    @Override
    public int getGameMode() {
        return 2;
    }
}

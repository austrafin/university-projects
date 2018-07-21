
import java.awt.Color;

/**
 * @author Matti Syrjanen A type of game with bricks
 */
public abstract class BrickGame extends GameMode {

    public BrickGame(int difficulty, int balls, int rows) {
        super(difficulty, balls, rows);
        ballsLabelText = "Balls left: ";
    }

    public BrickGame() {
        ballsLabelText = "Balls left: ";
    }

    @Override
    public void resetScore() {
        scoreLabel.setText("Score: " + score);
    }

    @Override
    public void collisionDetected(CollisionEvent e) {
        switch (e.getTarget()) {
            case CollisionEvent.BRICK:
                int row = e.getRow();
                int col = e.getColumn();
                Color brickColor = wall.getBrick(row, col);
                wall.setBrick(e.getRow(), e.getColumn(), null); // remove the brick

                /**
                 * As the number of bricks decreases, the difficulty increases.
                 * Increase speed in intervals of ten bricks.
                 */
                if (--noOfBricks % 10 == 0) {
                    wall.setBallSpeed(++ballSpeed);
                }
                if (brickColor.equals(Color.red)) {
                    score += 10;
                }
                else if (brickColor.equals(Color.yellow)) {
                    score += 5;
                }
                else if (brickColor.equals(Color.green)) {
                    score += 2;
                }
                else {
                    ++score;
                }
                scoreLabel.setText("Score " + score);

                /**
                 * Increase difficulty by reducing the bat size after all the
                 * bricks have been destroyed.
                 */
                if (noOfBricks == 0) {
                    if (batSize > 4) {
                        batSize -= 4;
                    }
                    reset();
                }
                break;
            case CollisionEvent.MISS:
                ballsLabel.setText("Balls left: " + --roundsRemaining);
                gameOver();
        }
    }

    @Override
    public abstract void setGameModeSettings(int noOfRows);

    @Override
    public abstract int getGameMode();
    
    @Override
    protected void promptForHighScore() {
        Scores.setNewHighScore(score);
    }

    @Override
    protected String getGameOverText() {
        return "Game Over! Do you want to start a new game?";
    }
}

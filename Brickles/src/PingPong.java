
/**
 * @author Matti Syrjanen
 * Game where two bats are playing "tennis" against each other
 */
public class PingPong extends GameMode {
    
    public PingPong(int difficulty, int rounds, int rows) {
        super(difficulty, rounds, rows);
        ballsLabelText = "Rounds left: ";
    }

    public PingPong() {

    }

    @Override
    public void resetScore() {
        scoreLabel.setText("Score: " + player_1_score + " - " + player_2_score);
    }

    @Override
    public void setGameModeSettings(int x) {
        firstRow = 0;
        lastRow = 0;
        WALL_NORTH = false;
        WALL_SOUTH = false;
        WALL_EAST = true;
        WALL_WEST = true;
    }

    @Override
    public void collisionDetected(CollisionEvent e) {
        switch (e.getTarget()) {
            case CollisionEvent.MISS:
                --roundsRemaining;
                ballsLabel.setText("Rounds left: " + roundsRemaining);

                if (e.getRow() == 0) {
                    ++player_1_score;
                }
                else {
                    ++player_2_score;
                }
                resetScore();
                gameOver();
        }
    }

    @Override
    public int getGameMode() {
        return 1;
    }
    
    @Override
    protected String getGameOverText() {
        String s;
        if(player_1_score > player_2_score) {
            s = "Player 1 wins!";
        }
        else if(player_2_score > player_1_score) {
            s = "Player 1 wins!";
        }
        else {
            s = "It's a tie!";
        }
        return s + " Do you want to start a new game?";
    }

    @Override
    protected void promptForHighScore() {
        //System.err.println("High Scores not applicable in Ping Pong.");
    }
}

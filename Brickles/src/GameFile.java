import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Matti Syrjanen
 * Parses a saved game file.
 */
public class GameFile {

    public ArrayList<Integer> brickColors = new ArrayList();
    public int score;
    public int firstRow;
    public int lastRow;
    public int noOfBricks = 0;
    public int roundsRemaining;
    public int initialBalls;
    public int ballSpeed;
    public int batSize;
    public int gameMode;
    public int theme;
    public int player_1_score;
    public int player_2_score;

    public GameFile(String fileName) {
        try {
            String s;
            Scanner fileScanner = new Scanner(new FileInputStream(fileName));
            firstRow = fileScanner.nextInt();
            lastRow = fileScanner.nextInt();
            score = fileScanner.nextInt();
            roundsRemaining = fileScanner.nextInt();
            ballSpeed = fileScanner.nextInt();
            batSize = fileScanner.nextInt();
            gameMode = fileScanner.nextInt();
            theme = fileScanner.nextInt();
            player_1_score = fileScanner.nextInt();
            player_2_score = fileScanner.nextInt();

            while (fileScanner.hasNext()) {
                s = fileScanner.next();
                if (!s.equals("null")) {
                    brickColors.add(Integer.parseInt(s));
                    ++noOfBricks;
                }
                else {
                    brickColors.add(null);
                }
            }
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(GameFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

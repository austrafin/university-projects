package assignment03;

import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * @author Matti Syrjanen
 */
public class LifeProcessorThread implements Runnable {

    private final boolean[][] gameBoard;
    private final int[] birth;
    private final int[] survives;
    private final int i;
    private final ArrayList<Point> point;
    private CountDownLatch latch;

    /**
     * For multiple threads
     * @param point
     * @param gameBoard
     * @param birth
     * @param survives
     * @param i
     * @param latch
     */
    public LifeProcessorThread(ArrayList<Point> point, boolean[][] gameBoard, int[] birth, int[] survives, int i, CountDownLatch latch) {
        this.gameBoard = gameBoard;
        this.birth = birth;
        this.survives = survives;
        this.i = i;
        this.point = point;
        this.latch = latch;
    }

    /**
     * For single threads
     *
     * @param point
     * @param gameBoard
     * @param birth
     * @param survives
     * @param i
     */
    public LifeProcessorThread(ArrayList<Point> point, boolean[][] gameBoard, int[] birth, int[] survives, int i) {
        this.gameBoard = gameBoard;
        this.birth = birth;
        this.survives = survives;
        this.i = i;
        this.point = point;
    }

    public void compute() {
        for (int j = 1; j < gameBoard[0].length - 1; j++) {
            int surrounding = 0;
            if (gameBoard[i - 1][j - 1]) {
                surrounding++;
            }
            if (gameBoard[i - 1][j]) {
                surrounding++;
            }
            if (gameBoard[i - 1][j + 1]) {
                surrounding++;
            }
            if (gameBoard[i][j - 1]) {
                surrounding++;
            }
            if (gameBoard[i][j + 1]) {
                surrounding++;
            }
            if (gameBoard[i + 1][j - 1]) {
                surrounding++;
            }
            if (gameBoard[i + 1][j]) {
                surrounding++;
            }
            if (gameBoard[i + 1][j + 1]) {
                surrounding++;
            }
            if (gameBoard[i][j]) {
                // Cell is alive, Can the cell live? (Conway, 2-3)
                for (int si = 0; si < this.survives.length; si++) {
                    if (this.survives[si] == surrounding) {
                        // survivial!!
                        synchronized (point) { // TWL: ah, adding directly to point, that's clever...well cleverer than me!
                            point.add(new Point(i - 1, j - 1));
                        }
                        break;
                    }
                }

            }
            else // Cell is dead, will the cell be given birth? (Conway, 3)
            {
                for (int bi = 0; bi < this.birth.length; bi++) {
                    if (this.birth[bi] == surrounding) {
                        // survivial!!
                        synchronized (point) {
                            point.add(new Point(i - 1, j - 1));
                        }
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        compute();
        latch.countDown();
    }
}

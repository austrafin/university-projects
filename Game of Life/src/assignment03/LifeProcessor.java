package assignment03;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lewi0146
 */
public class LifeProcessor {

    protected Dimension gameBoardSize = null;
    protected ArrayList<Point> point = new ArrayList(0);
    protected AtomicBoolean keepLiving = new AtomicBoolean();  // TWL: YES! An AtomicBoolean!!!

    int[] birth;
    int[] survives;
    protected ArrayList<LifeListener> listeners;
    private final GameOfLifeGUI gui;

    /**
     * "B3/S23"
     *
     * @param birth
     * @param survives
     * @param point
     * @param gameBoardSize
     * @param gui
     */
    public LifeProcessor(int[] birth, int[] survives, ArrayList<Point> point, Dimension gameBoardSize, GameOfLifeGUI gui) {
        this.birth = birth;
        this.survives = survives;
        this.point = point;
        this.gameBoardSize = gameBoardSize;
        this.gui = gui;
        this.listeners = new ArrayList<>();
    }

    public void stopLife() {
        this.keepLiving.set(false);
    }

    public void processLife(int generations, int noOfThreads) {

        this.keepLiving.set(true);
        int progressBarMax = gui.generationProgress.getMaximum();
        int ilive = 0;
        int movesPerSecond = 0;

        /**
         * When I run Runtime.getRuntime().availableProcessors() I get 4 so have
         * 4 as max threads (Check Intel i5-6200U specs - 2 cores, 4 threads) -
         * Matti. // TWL: Me too!
         */
        //ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); // for max threads
        ExecutorService executor = Executors.newFixedThreadPool(noOfThreads);

        if (generations < 0) {
            movesPerSecond = -generations;
            ilive = generations - 1; // ignore the ilive (go until keepLiving is false)
        }

        while (keepLiving.get() && ilive < generations) {
            try {
                boolean[][] gameBoard = new boolean[gameBoardSize.width + 2][gameBoardSize.height + 2];
                synchronized (point) { // TWL: good synchro!!
                    for (int i = 0; i < point.size(); i++) {
                        Point current = point.get(i);
                        gameBoard[current.x + 1][current.y + 1] = true;
                    }
                    point.clear();
                }

                switch (noOfThreads) {
                    case 1:
                        compute_java_single(gameBoard);
                        break;
                    default:
                        compute_java_multi(gameBoard, executor);
                }

                // notify listeners
                for (LifeListener l : listeners) {
                    l.lifeUpdated();
                }
                
                if (keepLiving.get()) // do nothing if program stopped while being paused
                {
                    if (gui.pause) {
                        synchronized (gui) {
                            gui.wait();
                        }
                    }

                    if (generations > 0) {
                        ++ilive;
                    }
                    else {
                        try {
                            Thread.sleep(1000 / movesPerSecond);
                        }
                        catch (InterruptedException ex) {
                            break;
                        }
                    }
                    // TWL: the below needs to be called from the EDT, e.g. using SwingUtilities.invokeLater
                    gui.generationProgress.setValue((int) ((double) ilive / generations * progressBarMax));
                }
            }
            catch (InterruptedException ex) {
                Logger.getLogger(LifeProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // TWL: Noooo! the below needs to be called from the EDT, e.g. using SwingUtilities.invokeLater
        gui.jButtonPlay.setEnabled(false);
        gui.jButtonPause.setEnabled(false);
        gui.jButtonCancel.setEnabled(false);
        gui.jButtonStop.setEnabled(false);
        gui.jButtonNewGame.setEnabled(true);
        gui.generationProgress.setValue(0);
        gui.generationProgress.setStringPainted(false); // Removes text 'Infinity'
    }

    private void compute_java_single(boolean[][] gameBoard) {
        for (int i = 1; i < gameBoard.length - 1; i++) {
            LifeProcessorThread pt = new LifeProcessorThread(point, gameBoard, birth, survives, i);
            pt.compute(); // TWL: ah, I see, calling it on the same thread. smart reuse of code!
        }
    }

    private void compute_java_multi(boolean[][] gameBoard, ExecutorService executor) {
        CountDownLatch latch = new CountDownLatch(gameBoard.length - 2);
        // Iterate through the array, follow game of life rules
        for (int i = 1; i < gameBoard.length - 1; i++) {
            executor.execute(new LifeProcessorThread(point, gameBoard, birth, survives, i, latch));
        }

        try {
            latch.await();
        }
        catch (InterruptedException ex) {
            Logger.getLogger(LifeProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addLifeListener(LifeListener l) {
        this.listeners.add(l);
    }
}

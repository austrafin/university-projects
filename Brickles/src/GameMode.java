
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.ListIterator;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Matti Syrjanen
 */
public abstract class GameMode extends JFrame implements CollisionListener {

    // ****** constants
    protected boolean WALL_EAST = true;
    protected boolean WALL_WEST = true;
    protected boolean WALL_NORTH = true;
    protected boolean WALL_SOUTH = false;

    private final int THEME_DEFAULT = 0;
    private final int THEME_MEADOW = 1;
    private final int THEME_ROYAL = 2;
    //*******

    protected BrickWall wall = new BrickWall();
    private final JPanel gamePanel = new JPanel(new GridLayout());
    protected JLabel scoreLabel = new JLabel();
    protected JLabel ballsLabel = new JLabel();

    private boolean exit = false;

    protected int score = 0;
    protected int firstRow;
    protected int lastRow;
    protected int firstCol;
    protected int lastCol;
    protected int noOfBricks;
    protected int roundsRemaining;
    protected int ballSpeed;
    protected int batSize = 20;
    protected int maxRows = 4;
    protected int player_1_score = 0;
    protected int player_2_score = 0;

    protected String ballsLabelText;

    private int theme = 0;
    private int initialDifficulty, initialRows, initialBalls;

    /**
     * For a new game
     *
     * @param difficulty
     * @param rounds
     * @param rows
     */
    public GameMode(int difficulty, int rounds, int rows) {
        this.initialDifficulty = difficulty;
        this.initialBalls = rounds;
        this.initialRows = rows;
        initialise();
    }

    /**
     * Mainly for game loaded from a file.
     */
    public GameMode() {
        initialise();
    }

    /**
     * Sets the components of the game field.
     */
    private void initialise() {
        JPanel mainPanel = new JPanel();
        JPanel scorePanel = new JPanel();

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game Menu");
        JMenu themes = new JMenu("Themes");
        JMenu lookAndFeelMenu = new JMenu("Look & Feel");
        JMenu helpMenu = new JMenu("Help");
        JMenuItem newMenuItem = new JMenuItem("New Game");
        JMenuItem resetMenuItem = new JMenuItem("Reset");
        JMenuItem openMenuItem = new JMenuItem("Load");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem scoreBoard = new JMenuItem("Hall of Fame");
        JMenuItem exitToMainMenuItem = new JMenuItem("Exit to Main Menu");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        JMenuItem themeDefault = new JMenuItem("Default");
        JMenuItem themeMeadow = new JMenuItem("Meadow");
        JMenuItem themeRoyal = new JMenuItem("Royal");
        JMenuItem userManualMenuItem = new JMenuItem("User Manual");
        JMenuItem metalLookAndFeelMenuItem = new JMenuItem("Metal");
        JMenuItem motifLookAndFeelMenuItem = new JMenuItem("Motif");
        JMenuItem nativeLookAndFeelMenuItem = new JMenuItem("Native");
        JMenuItem nimbusLookAndFeelMenuItem = new JMenuItem("Nimbus");

        // Add to components
        getContentPane().add(mainPanel);

        // This should come first because the number of initial balls is determined by the starting dialog
        mainPanel.add(scorePanel);
        mainPanel.add(gamePanel);

        scorePanel.add(ballsLabel);
        scorePanel.add(scoreLabel);

        gamePanel.add(wall);

        menuBar.add(gameMenu);
        menuBar.add(helpMenu);

        gameMenu.add(newMenuItem);
        gameMenu.add(openMenuItem);
        gameMenu.add(saveMenuItem);
        gameMenu.add(resetMenuItem);
        gameMenu.add(themes);
        gameMenu.add(scoreBoard);
        gameMenu.add(lookAndFeelMenu);
        gameMenu.add(exitToMainMenuItem);
        gameMenu.add(exitMenuItem);
        themes.add(themeDefault);
        themes.add(themeMeadow);
        themes.add(themeRoyal);
        lookAndFeelMenu.add(metalLookAndFeelMenuItem);
        lookAndFeelMenu.add(motifLookAndFeelMenuItem);
        lookAndFeelMenu.add(nativeLookAndFeelMenuItem);
        lookAndFeelMenu.add(nimbusLookAndFeelMenuItem);
        helpMenu.add(userManualMenuItem);

        // Set Layouts
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        // Add listeners
        WindowAdapter wl = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(null, "Do you want to save the game before exiting?",
                        "Quit", JOptionPane.YES_NO_CANCEL_OPTION);
                if ((option == JOptionPane.YES_OPTION && saveGame() == true) || option == JOptionPane.NO_OPTION) {
                    if (exit) {
                        dispose();
                    }
                    else {
                        exitToMainMenu();
                    }
                    exit = false;
                }
            }
        };

        newMenuItem.addActionListener((ActionEvent arg0) -> {
            MainMenu.newGame(this);
        });

        resetMenuItem.addActionListener((ActionEvent arg0) -> {
            if (JOptionPane.showConfirmDialog(null, "Do you really want to reset the game?",
                    "Reset Game", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                reset();
                score = 0;
                roundsRemaining = initialBalls;
                scoreLabel.setText("Score: " + score);
                ballsLabel.setText("Balls left: " + roundsRemaining);
            }
        });

        openMenuItem.addActionListener((ActionEvent arg0) -> {
            MainMenu.loadGame(this);
        });

        saveMenuItem.addActionListener((ActionEvent arg0) -> {
            saveGame();
        });

        scoreBoard.addActionListener((ActionEvent arg0) -> {
            Scores.showHallOfFame();
        });

        themeDefault.addActionListener((ActionEvent ae) -> {
            setTheme(THEME_DEFAULT);
        });

        themeMeadow.addActionListener((ActionEvent ae) -> {
            setTheme(THEME_MEADOW);
        });

        themeRoyal.addActionListener((ActionEvent ae) -> {
            setTheme(THEME_ROYAL);
        });

        metalLookAndFeelMenuItem.addActionListener((ActionEvent arg0) -> {
            MainMenu.setLookFeel(MainMenu.METAL_LF, GameMode.this);
        });

        nativeLookAndFeelMenuItem.addActionListener((ActionEvent arg0) -> {
            MainMenu.setLookFeel(MainMenu.NATIVE_LF, GameMode.this);
        });

        nimbusLookAndFeelMenuItem.addActionListener((ActionEvent arg0) -> {
            MainMenu.setLookFeel(MainMenu.NIMBUS_LF, GameMode.this);
        });

        motifLookAndFeelMenuItem.addActionListener((ActionEvent arg0) -> {
            MainMenu.setLookFeel(MainMenu.MOTIF_LF, GameMode.this);
        });

        exitToMainMenuItem.addActionListener((ActionEvent arg0) -> {
            dispatchEvent(new WindowEvent(GameMode.this, WindowEvent.WINDOW_CLOSING));
        });

        exitMenuItem.addActionListener((ActionEvent arg0) -> {
            exit = true;
            dispatchEvent(new WindowEvent(GameMode.this, WindowEvent.WINDOW_CLOSING));
        });

        userManualMenuItem.addActionListener((ActionEvent arg0) -> {
            MainMenu.showUserManual();
        });

        this.addWindowListener(wl);
        setJMenuBar(menuBar);
    }

    /**
     * If no more rounds remaining, display game over dialog. Prompt for a new
     * high score.
     */
    protected void gameOver() {
        if (roundsRemaining <= 0) {
            promptForHighScore();
            if (JOptionPane.showConfirmDialog(null, getGameOverText(),
                    "Game Over", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                MainMenu.newGame(this);
            }
            else {
                exitToMainMenu();
            }
        }
    }

    protected abstract void promptForHighScore();

    /**
     * Resets the game field to the original state of when the game was started.
     */
    protected void reset() {
        ArrayList<Color> brickColors = new ArrayList();
        Color color;

        gamePanel.remove(wall);

        // for some reason the MISS event is onvoked after removing the game field so this fixes that problem
        wall.setReportMask(CollisionEvent.BRICK);
        wall = new BrickWall();
        wall.addCollisionListener(this);
        wall.buildWall(firstRow, lastRow, firstCol, lastCol, Color.GRAY);
        wall.setWalls(WALL_NORTH, WALL_SOUTH, WALL_EAST, WALL_EAST);
        wall.setBallSpeed(ballSpeed);
        wall.setReportMask(CollisionEvent.WALL | CollisionEvent.BAT | CollisionEvent.BRICK | CollisionEvent.MISS);
        wall.setBatSize(batSize);

        brickColors.add(Color.red);
        brickColors.add(Color.yellow);
        brickColors.add(Color.green);
        brickColors.add(Color.blue);

        resetScore();

        noOfBricks = (lastRow + 1 - firstRow) * (lastCol - firstCol + 1);

        for (int row = firstRow; row <= lastRow; ++row) {
            color = brickColors.get(row - firstRow);

            for (int col = firstCol; col <= lastCol; ++col) {
                wall.setBrick(row, col, color);
            }
        }

        setTheme(theme);
        gamePanel.add(wall);
        gamePanel.revalidate();
        gamePanel.repaint();
    }

    /**
     * Sets the necessary game parameters and starts a new game.
     */
    public final void startGame() {
        batSize = 20;
        switch (initialDifficulty) {
            case 0:
                ballSpeed = 2;
                break;
            case 1:
                ballSpeed = 5;
                break;
            case 2:
                ballSpeed = 10;
                break;
        }
        setGameModeSettings(initialRows);
        this.roundsRemaining = initialBalls;
        initialBalls = roundsRemaining;

        reset();
        ballsLabel.setText(ballsLabelText + roundsRemaining);
        this.setTitle("Brickles");
        this.setVisible(true);
    }

    /**
     * Uses existing game parameters from a file and starts a new game.
     *
     * @param gamefile stores the existing game parameters
     */
    public void loadGame(GameFile gamefile) {
        Integer s;
        firstRow = gamefile.firstRow;
        lastRow = gamefile.lastRow;
        score = gamefile.score;
        roundsRemaining = gamefile.roundsRemaining;
        ballsLabel.setText("Balls left: " + roundsRemaining);
        ballSpeed = gamefile.ballSpeed;
        wall.setBallSpeed(ballSpeed);
        batSize = gamefile.batSize;
        player_1_score = gamefile.player_1_score;
        player_2_score = gamefile.player_2_score;
        wall.setBatSize(batSize);
        setTheme(gamefile.theme);
        setGameModeSettings(lastRow - firstRow + 1);
        reset();
        noOfBricks = 0;
        ListIterator<Integer> it = gamefile.brickColors.listIterator();
        for (int row = 1; row <= wall.getRows(); ++row) {
            for (int col = 1; col <= wall.getColumns(); ++col) {
                s = it.next();
                if (s != null) {
                    wall.setBrick(row, col, new Color(s));
                    ++noOfBricks;
                }
                else {
                    wall.setBrick(row, col, null);
                }
            }
        }
        revalidate();
        repaint();
        this.setTitle("Brickles");
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Saves the game instance into a file.
     *
     * @return true if the saving was successful.
     */
    private boolean saveGame() {
        JFileChooser browse = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("Brick file", "brick", "BRICK");
        browse.addChoosableFileFilter(filter);
        browse.setFileFilter(filter);

        if (browse.showSaveDialog(GameMode.this) == JFileChooser.APPROVE_OPTION) {
            File file = new File(browse.getSelectedFile().toString() + ".brick");

            try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
                writer.print(firstRow + " "
                        + lastRow + " "
                        + score + " "
                        + roundsRemaining + " "
                        + ballSpeed + " "
                        + batSize + " "
                        + getGameMode() + " "
                        + theme + " "
                        + player_1_score + " "
                        + player_2_score + " ");

                for (int row = 1; row <= wall.getRows(); ++row) {
                    for (int col = 1; col <= wall.getColumns(); ++col) {
                        if (wall.getBrick(row, col) == null) {
                            writer.print("null ");
                        }
                        else {
                            writer.print(wall.getBrick(row, col).getRGB() + " ");
                        }
                    }
                }

                return true;
            }

            catch (IOException e) {
                System.out.println("Error in saving file.");
            }
        }
        return false;
    }

    /**
     * Closes the game field and displays the Main Menu.
     */
    private void exitToMainMenu() {
        dispose();
        MainMenu mainmenu = new MainMenu();
        mainmenu.setLocationRelativeTo(null);
        mainmenu.setVisible(true);
    }

    /**
     * Sets the colours of bat, walls and ball.
     *
     * @param t theme id
     */
    private void setTheme(int t) {
        theme = t;
        switch (t) {
            case THEME_DEFAULT:
                wall.setBatColor(Color.black);
                wall.setWallColor(Color.black);
                wall.setBallColor(Color.black);
                break;
            case THEME_MEADOW:
                wall.setBatColor(Color.blue);
                wall.setWallColor(Color.green);
                wall.setBallColor(Color.yellow);
                break;
            case THEME_ROYAL:
                wall.setBatColor(Color.red);
                wall.setWallColor(Color.yellow);
                wall.setBallColor(Color.blue);
        }
    }

    /**
     * Handler for {@link CollisionEvent CollisionEvents}.
     *
     * @param e the event
     */
    @Override
    public abstract void collisionDetected(CollisionEvent e);

    /**
     * @return the game mode
     */
    protected abstract int getGameMode();

    /**
     * Resets the score display
     */
    protected abstract void resetScore();

    /**
     * Sets the game mode specific parameters
     *
     * @param noOfRows number of rows in the game field
     */
    protected abstract void setGameModeSettings(int noOfRows);

    protected abstract String getGameOverText();
}

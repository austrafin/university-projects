
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Matti Syrjanen
 */
public class MainMenu extends JFrame {

    //********** constants ******************
    public static final int METAL_LF = 0;
    public static final int NATIVE_LF = 1;
    public static final int NIMBUS_LF = 2;
    public static final int MOTIF_LF = 3;

    public static final int CLASSIC = 0;
    public static final int PINGPONG = 1;
    public static final int ALL_IN = 2;
    //***************************************

    public MainMenu() {
        JPanel mainPanel = new JPanel();
        JPanel newGamePanel = new JPanel(new GridLayout());
        JPanel loadPanel = new JPanel(new GridLayout());
        JPanel lookAndFeelPanel = new JPanel(new GridLayout());
        JPanel hallOfFamePanel = new JPanel(new GridLayout());
        JPanel userManualPanel = new JPanel(new GridLayout());
        JPanel exitPanel = new JPanel(new GridLayout());

        JButton newGameButton = new JButton("NEW GAME");
        JButton loadButton = new JButton("LOAD");
        JButton lookAndFeelButton = new JButton("SET LOOK AND FEEL");
        JButton hallOfFameButton = new JButton("HALL OF FAME");
        JButton userManualButton = new JButton("USER MANUAL");
        JButton exitButton = new JButton("EXIT");

        Border margins = BorderFactory.createEmptyBorder(5, 5, 5, 5);

        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setTitle("Brickles - Main Menu");

        // Add to components
        this.add(mainPanel);
        mainPanel.add(newGamePanel);
        mainPanel.add(loadPanel);
        mainPanel.add(lookAndFeelPanel);
        mainPanel.add(hallOfFamePanel);
        mainPanel.add(userManualPanel);
        mainPanel.add(exitPanel);

        newGamePanel.add(newGameButton);
        loadPanel.add(loadButton);
        lookAndFeelPanel.add(lookAndFeelButton);
        hallOfFamePanel.add(hallOfFameButton);
        userManualPanel.add(userManualButton);
        exitPanel.add(exitButton);

        // Set sizes
        this.setSize(new Dimension(400, 400));

        // Set Borders
        newGamePanel.setBorder(margins);
        loadPanel.setBorder(margins);
        lookAndFeelPanel.setBorder(margins);
        userManualPanel.setBorder(margins);
        hallOfFamePanel.setBorder(margins);
        exitPanel.setBorder(margins);

        // Set Layouts
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        // Add action listeners
        newGameButton.addActionListener((ActionEvent ae) -> {
            newGame(this);
        });

        loadButton.addActionListener((ActionEvent ae) -> {
            loadGame(this);
        });

        lookAndFeelButton.addActionListener((ActionEvent ae) -> {
            JDialog lookAndFeelDialog = new JDialog();
            JPanel lookAndFeelDialogMainPanel = new JPanel();
            JPanel selectionPanel = new JPanel(new GridLayout());
            JPanel buttonPanel = new JPanel(new GridLayout());

            JButton okButton = new JButton("OK");
            JButton cancelButton = new JButton("CANCEL");
            JComboBox lookAndFeelCB = new JComboBox();

            lookAndFeelDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            lookAndFeelDialog.setMinimumSize(new Dimension(320, 130));
            lookAndFeelDialog.setModal(true);
            lookAndFeelDialog.setLocationRelativeTo(null);
            lookAndFeelDialog.setTitle("Brickles - Set Look & Feel");

            // Add to components
            lookAndFeelDialog.add(lookAndFeelDialogMainPanel);
            lookAndFeelDialogMainPanel.add(selectionPanel);
            lookAndFeelDialogMainPanel.add(buttonPanel);

            selectionPanel.add(new JLabel("Look & Feel:"));
            selectionPanel.add(lookAndFeelCB);

            buttonPanel.add(okButton);
            buttonPanel.add(Box.createHorizontalStrut(0));
            buttonPanel.add(cancelButton);

            lookAndFeelCB.addItem("Metal");
            lookAndFeelCB.addItem("Native");
            lookAndFeelCB.addItem("Nimbus");
            lookAndFeelCB.addItem("Motif");
            lookAndFeelCB.setSelectedItem(UIManager.getLookAndFeel());

            // Set layouts
            lookAndFeelDialogMainPanel.setLayout(new BoxLayout(lookAndFeelDialogMainPanel, BoxLayout.PAGE_AXIS));

            // Set borders
            lookAndFeelDialogMainPanel.setBorder(margins);
            selectionPanel.setBorder(margins);
            buttonPanel.setBorder(margins);

            // Add action listeners
            okButton.addActionListener((ActionEvent a) -> {
                setLookFeel(lookAndFeelCB.getSelectedIndex(), MainMenu.this);
                lookAndFeelDialog.dispose();
            });

            cancelButton.addActionListener((ActionEvent a) -> {
                lookAndFeelDialog.dispose();
            });

            lookAndFeelDialog.setVisible(true);
        });

        hallOfFameButton.addActionListener((ActionEvent ae) -> {
            Scores.showHallOfFame();
        });

        userManualButton.addActionListener((ActionEvent ae) -> {
            showUserManual();
        });

        exitButton.addActionListener((ActionEvent ae) -> {
            this.dispose();
        });

        // get the look and feel stored in the system config file
        try {
            Scanner fileScanner = new Scanner(new FileInputStream("./system.txt"));
            if (fileScanner.hasNextInt()) {
                setLookFeel(fileScanner.nextInt(), MainMenu.this);
            }
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * For loading a saved game.
     *
     * @param w window where the dialog appears
     */
    public static void loadGame(Window w) {
        String fileName;
        if ((fileName = openFile(w)) != null) {
            w.dispose();
            GameFile gamefile = new GameFile(fileName);
            GameMode game;
            switch (gamefile.gameMode) {
                case CLASSIC:
                    game = new Classic();
                    break;
                case PINGPONG:
                    game = new PingPong();
                    break;
                case ALL_IN:
                    game = new AllIn();
                    break;
                default:
                    throw new UnsupportedOperationException("Error, no game mode found");
            }
            game.setSize(new Dimension(600, 600));
            game.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            game.loadGame(gamefile);
        }
    }

    /**
     * Shows the options for a new game and starts it.
     *
     * @param w window to be removed
     */
    public static void newGame(Window w) {
        w.dispose();
        int maxBalls = 5;
        int maxRows = 4;
        JDialog newGameDialog = new JDialog();
        JPanel dialogMainPanel = new JPanel(new GridLayout());
        JPanel rowsSpinnerPanel = new JPanel(new GridLayout());
        JPanel ballsSpinnerPanel = new JPanel(new GridLayout());
        JPanel difficultyPanel = new JPanel(new GridLayout());
        JPanel gameModePanel = new JPanel(new GridLayout());
        JPanel dialogButtonPanel = new JPanel(new GridLayout());

        JLabel rowsLabel = new JLabel("Number of rows:");

        SpinnerNumberModel rowModel = new SpinnerNumberModel(4, 1, maxRows, 1);
        SpinnerNumberModel ballsModel = new SpinnerNumberModel(3, 1, maxBalls, 1);

        JSpinner rowSpinner = new JSpinner(rowModel);
        JSpinner ballsSpinner = new JSpinner(ballsModel);
        rowSpinner.setEditor(new JSpinner.DefaultEditor(rowSpinner));
        ballsSpinner.setEditor(new JSpinner.DefaultEditor(ballsSpinner));

        JButton startButton = new JButton("START GAME");
        JButton backButton = new JButton("<html>BACK TO<br>MAIN MENU</html>");

        JComboBox difficultyCB = new JComboBox();
        JComboBox gameTypeCB = new JComboBox();

        newGameDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        newGameDialog.setMinimumSize(new Dimension(400, 300));
        newGameDialog.setModal(true);
        newGameDialog.setLocationRelativeTo(null);
        newGameDialog.setTitle("Brickles - New Game");

        dialogMainPanel.setLayout(new BoxLayout(dialogMainPanel, BoxLayout.PAGE_AXIS));

        // Set borders
        Border margins = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        dialogMainPanel.setBorder(margins);
        rowsSpinnerPanel.setBorder(margins);
        ballsSpinnerPanel.setBorder(margins);
        difficultyPanel.setBorder(margins);
        dialogButtonPanel.setBorder(margins);
        gameModePanel.setBorder(margins);

        // Add to components
        difficultyCB.addItem("Easy");
        difficultyCB.addItem("Medium");
        difficultyCB.addItem("Hard");
        difficultyCB.setSelectedItem("Medium");

        gameTypeCB.addItem("Classic");
        gameTypeCB.addItem("Ping Pong");
        gameTypeCB.addItem("All In");
        gameTypeCB.setSelectedItem("Classic");

        newGameDialog.getContentPane().add(dialogMainPanel);

        dialogMainPanel.add(rowsSpinnerPanel);
        dialogMainPanel.add(ballsSpinnerPanel);
        dialogMainPanel.add(difficultyPanel);
        dialogMainPanel.add(gameModePanel);
        dialogMainPanel.add(dialogButtonPanel);

        rowsSpinnerPanel.add(rowsLabel);
        rowsSpinnerPanel.add(rowSpinner);

        ballsSpinnerPanel.add(new JLabel("Number of rounds:"));
        ballsSpinnerPanel.add(ballsSpinner);

        difficultyPanel.add(new JLabel("Difficulty Level:"));
        difficultyPanel.add(difficultyCB);

        gameModePanel.add(new JLabel("Game Mode:"));
        gameModePanel.add(gameTypeCB);

        dialogButtonPanel.add(startButton);
        dialogButtonPanel.add(Box.createHorizontalStrut(0));
        dialogButtonPanel.add(backButton);

        startButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            newGameDialog.dispose();

            GameMode game;
            switch (gameTypeCB.getSelectedIndex()) {
                case CLASSIC:
                    game = new Classic(difficultyCB.getSelectedIndex(), (int) ballsSpinner.getValue(), (int) rowSpinner.getValue());
                    break;
                case PINGPONG:
                    game = new PingPong(difficultyCB.getSelectedIndex(), (int) ballsSpinner.getValue(), (int) rowSpinner.getValue());
                    break;
                case ALL_IN:
                    game = new AllIn(difficultyCB.getSelectedIndex(), (int) ballsSpinner.getValue(), (int) rowSpinner.getValue());
                    break;
                default:
                    throw new UnsupportedOperationException("Error, no game chosen");
            }

            game.setSize(new Dimension(600, 600));
            game.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            game.setLocationRelativeTo(null);
            game.startGame();
        });

        backButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            newGameDialog.dispose();
            MainMenu mainmenu = new MainMenu();
            mainmenu.setLocationRelativeTo(null);
            mainmenu.setVisible(true);
        });

        gameTypeCB.addItemListener((ItemEvent ie) -> {
            switch (gameTypeCB.getSelectedIndex()) {
                case PINGPONG:
                    rowSpinner.setEnabled(false);
                    rowsLabel.setEnabled(false);
                    break;
                default:
                    rowSpinner.setEnabled(true);
                    rowsLabel.setEnabled(true);
            }
        });

        newGameDialog.setVisible(true);
    }

    /**
     * Gets the filename user specifies in the file chooser dialog.
     *
     * @param w window were the dialog appears
     * @return filename if the user chooses a file, else return null
     */
    public static String openFile(Window w) {
        JFileChooser browse = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("Brick file", "brick", "BRICK");

        browse.addChoosableFileFilter(filter);
        browse.setFileFilter(filter);

        if (browse.showOpenDialog(w) == JFileChooser.APPROVE_OPTION) {
            return browse.getSelectedFile().toString();
        }
        return null;
    }

    public static void showUserManual() {
        JDialog userManualDialog = new JDialog();
        JEditorPane userManualPane = new JEditorPane();

        userManualDialog.setTitle("Brickles - User Manual");
        userManualDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        userManualDialog.setSize(new Dimension(930, 600));
        userManualDialog.setLocationRelativeTo(null);
        userManualDialog.setVisible(true);
        userManualPane.setEditable(false);
        userManualPane.setContentType("text/html");

        final JScrollPane sp = new JScrollPane(userManualPane);
        userManualDialog.add(sp);
        userManualPane.addHyperlinkListener((final HyperlinkEvent pE) -> {
            if (HyperlinkEvent.EventType.ACTIVATED == pE.getEventType()) {
                String desc = pE.getDescription();
                if (desc == null || !desc.startsWith("#")) {
                    return;
                }
                desc = desc.substring(1);
                userManualPane.scrollToReference(desc);
            }
        });

        try {
            StringBuilder manualString;
            try (Scanner fileScanner = new Scanner(new FileInputStream("./user_manual/user_manual.html"))) {
                manualString = new StringBuilder();
                while (fileScanner.hasNextLine()) {
                    manualString.append(fileScanner.nextLine());
                }
            }
            userManualPane.setText(manualString.toString());
            userManualPane.setCaretPosition(0);
        }
        catch (FileNotFoundException ex) {

        }
    }

    /**
     * Sets the look and feel of the program.
     *
     * @param lookAndFeel look and feel id
     * @param f
     */
    public static void setLookFeel(int lookAndFeel, Window f) {
        try {
            switch (lookAndFeel) {
                case METAL_LF:
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                    break;
                case NATIVE_LF:
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                case NIMBUS_LF:
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                    break;
                case MOTIF_LF:
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                    break;
            }
            SwingUtilities.updateComponentTreeUI(f);
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Store new look and feel setting in the system config file.
        try (FileWriter fooWriter = new FileWriter("./system.txt", false)) {
            fooWriter.write(String.valueOf(lookAndFeel));
        }
        catch (IOException ex) {
            Logger.getLogger(Scores.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

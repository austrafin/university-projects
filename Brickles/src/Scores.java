import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * @author Matti Syrjanen
 * Class to handle scoring in the game
 */
public class Scores extends JDialog {

    public static void showHallOfFame() {
        TreeMap<Integer, ArrayList<String>> scores = readScoresFromFile();
        JDialog scoreBoardDialog = new JDialog();

        JPanel highScoreDisplayPanel = new JPanel(new GridLayout());
        JPanel highScoreMainPanel = new JPanel(new GridLayout());
        JPanel highScoreButtonPanel = new JPanel(new GridLayout());

        JButton highScoreResetButton = new JButton("RESET SCORES");
        JButton closeButton = new JButton("CLOSE");
        JTextArea highScores = new JTextArea();

        String header = "Hall of Fame\n\nRank\tName\tScore\tDate\tTime\n-----------------------------------------------------------------------------------------------------\n";

        scoreBoardDialog.setTitle("Brickles - Hall of Fame");
        scoreBoardDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        scoreBoardDialog.setModal(true);

        highScores.setEditable(false);
        highScores.setMargin(new Insets(2, 5, 5, 2));

        // Add to components
        scoreBoardDialog.add(highScoreMainPanel);

        highScoreMainPanel.add(highScoreDisplayPanel);
        highScoreMainPanel.add(highScoreButtonPanel);
        highScoreDisplayPanel.add(highScores);
        highScoreButtonPanel.add(highScoreResetButton);
        highScoreButtonPanel.add(Box.createHorizontalStrut(0));
        highScoreButtonPanel.add(closeButton);

        highScoreMainPanel.setLayout(new BoxLayout(highScoreMainPanel, BoxLayout.PAGE_AXIS));

        // Set sizes
        scoreBoardDialog.setMinimumSize(new Dimension(450, 400));
        highScoreButtonPanel.setMaximumSize(new Dimension(500, 60));

        // Set borders
        Border margins = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        highScoreButtonPanel.setBorder(margins);

        highScoreResetButton.addActionListener((ActionEvent ae) -> {
            try {
                FileOutputStream writer = new FileOutputStream("./highScores.txt");
            }
            catch (FileNotFoundException ex) {
                Logger.getLogger(GameMode.class.getName()).log(Level.SEVERE, null, ex);
            }
            scores.clear();
            highScores.setText(header);
            highScores.revalidate();
            highScores.repaint();
        });

        closeButton.addActionListener((ActionEvent ae) -> {
            scoreBoardDialog.dispose();
        });

        // Inefficient but I'm feeling lazy!!
        int rank = 0;
        for (ArrayList arr : scores.values()) {
            for (int i = 0; i < arr.size(); ++i) {
                ++rank;
            }
        }
        for (ArrayList arr : scores.values()) {
            for (int i = 0; i < arr.size(); ++i) {
                highScores.insert(rank-- + "\t" + arr.get(i).toString() + "\n", 0);
            }

        }

        highScores.insert(header, 0);
        scoreBoardDialog.setLocationRelativeTo(null);
        scoreBoardDialog.setVisible(true);
    }

    /**
     * Reads high scores from a file.
     * @return the map of scores
     */
    private static TreeMap<Integer, ArrayList<String>> readScoresFromFile() {
        TreeMap<Integer, ArrayList<String>> scores = new TreeMap();
        String scoreString = "";

        try (Scanner fileScanner = new Scanner(new FileInputStream("./highscores.txt"))) {
            int scoreInt;
            while (fileScanner.hasNext()) {
                scoreString += fileScanner.next() + "\t";
                scoreInt = fileScanner.nextInt();
                scoreString += String.valueOf(scoreInt) + "\t";
                scoreString += fileScanner.next() + "\t";
                scoreString += fileScanner.next() + "\t";
                if (scores.get(scoreInt) == null) {
                    scores.put(scoreInt, new ArrayList());
                }
                scores.get(scoreInt).add(scoreString);
                scoreString = "";
            }
        }
        catch (FileNotFoundException ex) {
            System.out.println("File corrupted or does not exist.");
        }

        return scores;
    }

    /**
     * Updates the Hall of Fame with a new high score if valid.
     *
     * @param newScore new potential high score
     * @return returns false if the score provided is not high enough to be in
     * the Hall of Fame.
     */
    public static boolean setNewHighScore(int newScore) {
        TreeMap<Integer, ArrayList<String>> scores = readScoresFromFile();
        int noOfScores = 0;
        int max = 10; // maximum number of scores listed in hall of fame

        // Check how many scores have been stored. Again, inefficient.
        for (ArrayList arr : scores.values()) {
            for (int i = 0; i < arr.size(); ++i) {
                ++noOfScores;
            }
        }

        if (noOfScores < max || newScore > scores.firstKey()) {
            JDialog enterScoreDialog = new JDialog();
            JPanel enterScoreMainPanel = new JPanel();
            JPanel labelPanel = new JPanel(new GridLayout());
            JPanel playerNamePanel = new JPanel(new GridLayout());
            JPanel buttonPanel = new JPanel(new GridLayout());

            JButton okButton = new JButton("OK");
            JTextField playerNameTF = new JTextField();
            String playerName = "Anon";

            enterScoreDialog.setTitle("New High Score");
            enterScoreDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            enterScoreDialog.setModal(true);

            // Add to components
            enterScoreDialog.add(enterScoreMainPanel);

            enterScoreMainPanel.add(labelPanel);
            enterScoreMainPanel.add(playerNamePanel);
            enterScoreMainPanel.add(buttonPanel);

            labelPanel.add(new JLabel("<html>Congratulations! You made it to the Hall of Fame!<br>Please enter your name below.</html>"));
            playerNamePanel.add(playerNameTF);
            buttonPanel.add(okButton);

            // Set Layouts
            enterScoreMainPanel.setLayout(new BoxLayout(enterScoreMainPanel, BoxLayout.PAGE_AXIS));

            // Set sizes
            enterScoreDialog.setMinimumSize(new Dimension(350, 200));

            // Set borders
            Border margins = BorderFactory.createEmptyBorder(5, 5, 5, 5);
            enterScoreMainPanel.setBorder(margins);
            labelPanel.setBorder(margins);
            playerNamePanel.setBorder(margins);
            buttonPanel.setBorder(margins);

            okButton.addActionListener((ActionEvent ae) -> {
                enterScoreDialog.dispose();
            });

            enterScoreDialog.setVisible(true);

            String s = playerNameTF.getText();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm:ss");

            if (!s.equals("")) {
                playerName = s;
            }

            s = playerName + " " + String.valueOf(newScore) + " " + dtf.format(LocalDateTime.now()) + " ";
            String fileName = "./highscores.txt";

            /**
             * If the Hall of Fame is full, remove the lowest score and replace
             * with the new score, else just append to the text file
             */
            if (noOfScores >= max) {
                ArrayList temp = scores.get(scores.firstKey());
                temp.remove(temp.size() - 1);

                String newScores = "";

                for (ArrayList arr : scores.values()) {
                    for (int i = 0; i < arr.size(); ++i) {
                        newScores += arr.get(i);
                    }
                }
                newScores += s;

                try (FileWriter fooWriter = new FileWriter(fileName, false)) {
                    fooWriter.write(newScores);
                }
                catch (IOException ex) {
                    Logger.getLogger(Scores.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else {
                try (FileWriter writer = new FileWriter(fileName, true)) {
                    writer.append(s);
                }

                catch (IOException ex) {
                    Logger.getLogger(GameMode.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            showHallOfFame();
            return true;
        }
        return false;
    }
}

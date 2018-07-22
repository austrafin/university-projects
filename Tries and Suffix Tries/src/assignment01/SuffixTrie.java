package assignment01;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Dictionary;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

public class SuffixTrie implements PropertyChangeListener {

    private javax.swing.JTextArea originalText;
    private int noNodes = 0;
    private SuffixTrieNode root = new SuffixTrieNode();
    private static StringBuilder text;
    private static Task task;
    private JDialog progressDialog;
    private JProgressBar progressBar;
    private int progress;
    private JLabel progressLabel;

    // Constructor for command line version
    public SuffixTrie() {
    }

    // Constructor for GUI version
    public SuffixTrie(JTextArea results) {
        this.originalText = results;
    }

    /* This class is used to update the progress bar while building the suffix trie */
    class Task extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() {
            String[] keys = text.toString().split("\\.|\\?|\\!");
            int threshold = keys.length / 100; // represents 1% of the progress bar
            progress = 0;

            for (int i = 0; i < keys.length; ++i) {

                if (!progressDialog.isVisible()) { // Trie building aborted, roll back
                    noNodes = 0;
                    root = new SuffixTrieNode();
                    progressLabel.setText("Cancelling");
                    return null;
                }

                insert(keys[i].toUpperCase(), String.valueOf(i + 1));
                if (i > (progress + 1) * threshold && progress < 99) {
                    ++progress;
                    setProgress(progress);
                }
            }
            setProgress(100); // Built successful, invoke done()
            return null;
        }

        @Override
        public void done() {
            if (progressDialog.isVisible()) {
                originalText.setText(getText());
                originalText.setCaretPosition(0);
            }
            progressDialog.dispose();
            System.out.println("Number of nodes: " + String.valueOf(noNodes));
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        progressBar.setValue(progress);
    }

    public String getText() {
        return text.toString();
    }

    /**
     * Insert a String into the suffix trie. For the assignment the string str
     * is a sentence from the given text file. Example: inserting "banana"
     * inserts "banana", "anana", "nana", "ana, "na" & "a"
     *
     * @param str the sentence to insert
     * @param startIndex the starting index of the sentence
     * @return the final node inserted
     */
    public SuffixTrieNode insert(String str, String startIndex) {
        char characters[] = str.toCharArray();
        SuffixTrieNode currentNode;
        currentNode = root;

        for (int ssStart = 0; ssStart < characters.length; ++ssStart) {
            for (int i = ssStart; i < characters.length; ++i) {
                // If a key of that character already exists, set that as the current node
                if (currentNode.getChild(characters[i]) != null) {
                    currentNode = currentNode.getChild(characters[i]);
                }
                // Else add a new node with that character key
                else {
                    currentNode.addChild(characters[i], new SuffixTrieNode());
                    ++noNodes;
                    currentNode = currentNode.getChild(characters[i]);
                }
                currentNode.addData(startIndex + "-" + String.valueOf(ssStart + 1));
                if (i == characters.length - 1) {
                    currentNode.setTerminal(true);
                }
            }
            currentNode = root;
        }
        return currentNode;
    }

    /**
     * Get the suffix trie node associated with the given (sub)string.
     *
     * @param str the (sub)string to search for
     * @return the final node in the (sub)string
     */
    public SuffixTrieNode get(String str) {
        SuffixTrieNode node = root;
        str = str.toUpperCase();

        for (int i = 0; i < str.length(); ++i) {
            node = node.getChild(str.charAt(i));
            if (node == null) {
                return null; // substring does not exist
            }
        }
        return node; // substring exists
    }

    /**
     * Helper/Factory method to build a SuffixTrie object from the text in the
     * given file. The text file is broken up into sentences and each sentence
     * is inserted into the suffix trie.
     *
     * It is called in the following way
     * <code>SuffixTrie st = SuffixTrie.readInFromFile("Frank.txt");</code> This
     * is for the command line version.
     */
    public static SuffixTrie readInFromFile(String fileName) {
        Scanner fileScanner;
        SuffixTrie t = new SuffixTrie();
        text = new StringBuilder();
        try {
            // use a FileInputStream to ensure correct reading end-of-file
            fileScanner = new Scanner(new FileInputStream(fileName));

            while (fileScanner.hasNextLine()) {
                text.append(fileScanner.nextLine());
            }
            String[] keys = text.toString().split("\\.|\\?|\\!");

            for (int i = 0; i < keys.length; ++i) {
                t.insert(keys[i].toUpperCase(), String.valueOf(i + 1));

            }
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return t;
    }

    /**
     * For the description of the functionality, refer to readInFromFile() This
     * is intended to work the same way, but with GUI. Displays a progress bar
     * while importing text and building the suffix trie.
     */
    public void readInFromFileGUI(String fileName) {
        Scanner fileScanner;
        text = new StringBuilder();
        progressDialog = new JDialog();
        progressBar = new JProgressBar();
        progressLabel = new JLabel("Importing the text");
        JPanel progressPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        JPanel controlPanel = new JPanel();
        JButton progressButton = new JButton("Cancel");

        // Misc actions
        progressDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        progressDialog.setSize(new Dimension(300, 140));
        progressDialog.setTitle("Import Text");
        progressDialog.setResizable(false);
        progressPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        progressBar.setPreferredSize(new Dimension(300, 80));
        progressBar.setIndeterminate(true); // while importing text

        // Set layouts
        progressDialog.getContentPane().add(progressPanel, java.awt.BorderLayout.CENTER);
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.PAGE_AXIS));
        bottomPanel.setLayout(new BorderLayout());
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));

        // Add components
        progressPanel.add(progressBar);
        progressPanel.add(bottomPanel);
        bottomPanel.add(controlPanel);
        controlPanel.add(progressLabel);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(progressButton);

        // Add listeners
        progressButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                progressDialog.dispose();
            }
        });

        progressDialog.setVisible(true);

        try {
            // use a FileInputStream to ensure correct reading end-of-file
            fileScanner = new Scanner(new FileInputStream(fileName));

            while (fileScanner.hasNextLine()) {
                text.append(fileScanner.nextLine());
            }
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(SuffixTrie.class.getName()).log(Level.SEVERE, null, ex);
        }
        progressBar.setIndeterminate(false);
        progressLabel.setText("Building the suffix trie");
        task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();
    }
}

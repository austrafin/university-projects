package assignment01;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

/**
 * @author syrj0001
 */
public class SuffixTrieGUI extends javax.swing.JFrame {

    private final JPanel mainPanel = new JPanel();
    private final JPanel consoleLabelPanel = new JPanel();
    private final JPanel textLabelPanel = new JPanel();
    private final JPanel searchPanel = new JPanel();
    private final JPanel resultsPanel = new JPanel();
    private final JPanel consolePanel = new JPanel();
    private final JButton importButton = new JButton();
    private final JTextField searchBar = new JTextField();
    private final JTextArea originalText = new JTextArea();
    private final JTextArea indexingTextArea = new JTextArea();
    private final JFileChooser browse = new JFileChooser();
    private final JLabel consoleLabel = new JLabel("Index Positions");
    private final JLabel textLabel = new JLabel("Original Text");
    private SuffixTrie st;

    public SuffixTrieGUI() {
        Dimension buttonDim = new Dimension(80, 28);
        Dimension textFieldDim = new Dimension(100, 300);
        JScrollPane scrollText = new JScrollPane(originalText);
        JScrollPane scrollConsole = new JScrollPane(indexingTextArea);

        // Set layouts
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.LINE_AXIS));
        resultsPanel.setLayout(new GridLayout());
        consolePanel.setLayout(new GridLayout());
        consoleLabelPanel.setLayout(new BorderLayout());
        textLabelPanel.setLayout(new BorderLayout());

        // Add components
        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);
        mainPanel.add(searchPanel);
        mainPanel.add(textLabelPanel);
        mainPanel.add(resultsPanel);
        mainPanel.add(consoleLabelPanel);
        mainPanel.add(consolePanel);
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(searchBar);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(importButton);
        resultsPanel.add(scrollText);
        consolePanel.add(scrollConsole);
        textLabelPanel.add(textLabel);
        consoleLabelPanel.add(consoleLabel);

        // Set sizes
        searchBar.setPreferredSize(new Dimension(100, 28));
        resultsPanel.setPreferredSize(textFieldDim);
        consolePanel.setPreferredSize(textFieldDim);
        consoleLabel.setPreferredSize(new Dimension(50, 25));
        mainPanel.setPreferredSize(new Dimension(600, 700));
        importButton.setPreferredSize(buttonDim);
        searchPanel.setMaximumSize(new Dimension(400000, 28));

        // Set borders
        searchBar.setBorder(BorderFactory.createLineBorder(Color.black));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Set texts
        importButton.setText("Import");

        // Misc actions
        originalText.setLineWrap(true);
        originalText.setEditable(false);
        indexingTextArea.setEditable(false);
        originalText.setWrapStyleWord(true);
        originalText.setMargin(new Insets(2, 5, 5, 2));
        indexingTextArea.setMargin(new Insets(2, 5, 5, 2));

        // Add listeners
        importButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                FileFilter filter = new FileNameExtensionFilter("Text file", "txt", "TXT");
                browse.addChoosableFileFilter(filter);
                browse.setFileFilter(filter);

                if (browse.showOpenDialog(SuffixTrieGUI.this) == JFileChooser.APPROVE_OPTION) {
                    File file = new File(browse.getSelectedFile().toString());

                    st = new SuffixTrie(originalText);
                    st.readInFromFileGUI(file.toString());

                }
            }
        });

        searchBar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                String sbText = searchBar.getText();
                Highlighter highlighter = originalText.getHighlighter();
                highlighter.removeAllHighlights();
                indexingTextArea.setText("");

                /**
                 * This highlights the searched/found substrings in the textarea
                 * and also adds the index positions in the indexingTextArea.
                 */
                if (st != null && st.get(sbText) != null && sbText.length() > 0) {
                    String sentences[] = st.getText().split("\\.|\\?|\\!");
                    ArrayList<String> indecesString = st.get(sbText).getstartIndexes();
                    int charIndex, sentenceIndex, sentenceOffset = 0, sentenceIndexPrevious = 0;
                    HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);

                    indexingTextArea.setText("Sentence Position:\tCharacter Position:\n");
                    for (int i = 0; i < indecesString.size(); ++i) {
                        /**
                         * The indeces are stored in format XX-YY where XX is
                         * the sentence index position and YY is the character
                         * index positions in that sentence.
                         */
                        sentenceIndex = Integer.parseInt(indecesString.get(i).split("-")[0]) - 1;
                        indexingTextArea.append(indecesString.get(i).split("-")[0] + "\t\t");
                        indexingTextArea.append(indecesString.get(i).split("-")[1] + "\n");

                        /**
                         * If the previous sentences did not have the substring,
                         * the offset (number of characters) in a sentence) must
                         * be added for each of those sentences.
                         */
                        if (sentenceIndexPrevious < sentenceIndex) {
                            while (sentenceIndexPrevious < sentenceIndex) {
                                sentenceOffset += sentences[sentenceIndexPrevious].length() + 1;
                                ++sentenceIndexPrevious;
                            }
                        }
                        charIndex = Integer.parseInt(indecesString.get(i).split("-")[1]) + sentenceOffset - 1;

                        try {
                            highlighter.addHighlight(charIndex, charIndex + sbText.length(), painter);
                        }
                        catch (BadLocationException ex) {
                            Logger.getLogger(SuffixTrieGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    indexingTextArea.setCaretPosition(0);
                }
            }

            @Override
            public void insertUpdate(DocumentEvent de) {
                changedUpdate(de);
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                changedUpdate(de);
            }
        });
        pack();
    }

    public SuffixTrieGUI getUI() {
        return this;
    }
}

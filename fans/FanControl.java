import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Matti Syrjanen
 */
public class FanControl extends javax.swing.JFrame {

    private final JPanel mainPanel, fanAndGroupPanel, fanDisplayPanel, controlPanel,
            addFanPanel, indControlsPanel, sliderPanel, buttonPanel, themePanel, groupControlLabelPanel;
    private final ArrayList<Fan> fans = new ArrayList();
    private final ArrayList<JPanel> horizontalFanPanels = new ArrayList();
    private final ArrayList<JPanel> indControlsSubPanels = new ArrayList();
    private final JButton addFanButton, onButton, offButton, reverseButton;
    private final JSpinner fansToAdd;
    private final JSlider speedSlider;

    private final ButtonGroup themes;
    private final JRadioButton themeDefault, themeFinland, themeAustralia, themeSweden;
    private final Component glue;

    final private int maxValue; // maximum value for a slider
    private int row = 0, column = 0, rowMax = 0, colMax = 0; // for fan positions
    private int noOfFans = 0; // number of fans available at any given time
    private int maxFans;
    private static int noOfInstances = 0;

    // determines whether the layout of fans needs to be adjusted after adding or removing a fan
    private boolean reOrganise = false;
    boolean fileOpened = false;

    public FanControl() {
        // Initialise 

        maxValue = 20;
        maxFans = 9;

        mainPanel = new JPanel();
        fanAndGroupPanel = new JPanel();
        fanDisplayPanel = new JPanel();
        addFanPanel = new JPanel();
        indControlsPanel = new JPanel();
        controlPanel = new JPanel(new GridLayout());
        sliderPanel = new JPanel();
        buttonPanel = new JPanel();
        themePanel = new JPanel(new BorderLayout());
        groupControlLabelPanel = new JPanel(new BorderLayout());

        addFanButton = new JButton("Add");
        onButton = new JButton("ON");
        offButton = new JButton("OFF");
        reverseButton = new JButton("REVERSE");
        glue = Box.createVerticalGlue();
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, maxFans, 1);
        fansToAdd = new JSpinner(model);
        fansToAdd.setEditor(new JSpinner.DefaultEditor(fansToAdd));

        speedSlider = new JSlider(JSlider.HORIZONTAL, 0, 30, 5);
        speedSlider.setValue(0);
        speedSlider.setMaximum(maxValue);

        themes = new ButtonGroup();
        themeDefault = new JRadioButton("Default");
        themeFinland = new JRadioButton("Finland");
        themeAustralia = new JRadioButton("Australia");
        themeSweden = new JRadioButton("Sweden");
        themeDefault.setSelected(true);

        // Add to components and panels
        getContentPane().add(mainPanel);
        mainPanel.add(indControlsPanel);
        mainPanel.add(fanAndGroupPanel);

        fanAndGroupPanel.add(fanDisplayPanel);
        fanAndGroupPanel.add(addFanPanel);
        fanAndGroupPanel.add(controlPanel);

        addFanPanel.add(new JLabel("Add fans:   "));
        addFanPanel.add(fansToAdd);
        addFanPanel.add(Box.createHorizontalStrut(10));
        addFanPanel.add(addFanButton);

        sliderPanel.add(speedSlider);

        buttonPanel.add(onButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(offButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(reverseButton);

        themePanel.add(themeDefault);
        themePanel.add(themeAustralia);
        themePanel.add(themeFinland);
        themePanel.add(themeSweden);

        controlPanel.add(groupControlLabelPanel);
        groupControlLabelPanel.add(new JLabel("Group Controls"));

        controlPanel.add(sliderPanel);
        controlPanel.add(buttonPanel);
        controlPanel.add(themePanel);

        themes.add(themeDefault);
        themes.add(themeFinland);
        themes.add(themeAustralia);
        themes.add(themeSweden);

        // Set Layouts
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
        fanAndGroupPanel.setLayout(new BoxLayout(fanAndGroupPanel, BoxLayout.PAGE_AXIS));
        fanDisplayPanel.setLayout(new BoxLayout(fanDisplayPanel, BoxLayout.PAGE_AXIS));
        indControlsPanel.setLayout(new BoxLayout(indControlsPanel, BoxLayout.PAGE_AXIS));
        addFanPanel.setLayout(new BoxLayout(addFanPanel, BoxLayout.LINE_AXIS));
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        themePanel.setLayout(new BoxLayout(themePanel, BoxLayout.LINE_AXIS));
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));
        groupControlLabelPanel.setLayout(new BoxLayout(groupControlLabelPanel, BoxLayout.LINE_AXIS));

        // Set sizes
        Dimension buttonDim = new Dimension(70, 40);
        groupControlLabelPanel.setPreferredSize(new Dimension(200, 20));
        mainPanel.setPreferredSize(new Dimension(1200, 500));
        fanDisplayPanel.setPreferredSize(new Dimension(100, 800));
        addFanPanel.setPreferredSize(new Dimension(250, 40));
        fanAndGroupPanel.setPreferredSize(new Dimension(200, 500));
        controlPanel.setMaximumSize(new Dimension(320, 110));
        addFanPanel.setMaximumSize(new Dimension(220, 80));
        addFanButton.setPreferredSize(buttonDim);
        onButton.setPreferredSize(buttonDim);
        offButton.setPreferredSize(buttonDim);
        fansToAdd.setMaximumSize(new Dimension(40, 40));

        // Set borders
        //addFanPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        indControlsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        fanDisplayPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
        addFanPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        speedSlider.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));

        // Add listeners
        speedSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                for (int i = 0; i < fans.size(); ++i) {
                    fans.get(i).setSpeed(speedSlider.getValue());
                }
            }
        });

        onButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                for (int i = 0; i < fans.size(); ++i) {
                    fans.get(i).turnOn();
                }
            }
        });

        offButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                for (int i = 0; i < fans.size(); ++i) {
                    fans.get(i).turnOff();
                }
            }
        });

        reverseButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                for (int i = 0; i < fans.size(); ++i) {
                    fans.get(i).reverse();
                }
            }
        });

        themeDefault.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                for (int i = 0; i < fans.size(); ++i) {
                    fans.get(i).setTheme(0);
                }
            }
        });

        themeAustralia.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                for (int i = 0; i < fans.size(); ++i) {
                    fans.get(i).setTheme(1);
                }
            }
        });

        themeFinland.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                for (int i = 0; i < fans.size(); ++i) {
                    fans.get(i).setTheme(2);
                }
            }
        });

        themeSweden.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                for (int i = 0; i < fans.size(); ++i) {
                    fans.get(i).setTheme(3);
                }
            }
        });

        // Add listeners
        addFanButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFans((int) fansToAdd.getValue());
            }
        });

        pack();

        /**
         * A dialog where the user specifies the initial number of fans is
         * presented every time a new fan group instance is added.
         */
        JDialog howManyFans = new JDialog();
        JPanel dialogMainPanel = new JPanel();
        JPanel dialogSpinnerPanel = new JPanel();

        JPanel dialogButtonPanel = new JPanel(new GridLayout());
        SpinnerNumberModel m = new SpinnerNumberModel(1, 1, maxFans, 1);
        JSpinner initialFansSpinner = new JSpinner(m);
        JButton okButton = new JButton("START NEW");
        JButton openButton = new JButton("OPEN FILE");
        JButton exitButton = new JButton("EXIT");

        howManyFans.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        howManyFans.setSize(new Dimension(600, 120));
        howManyFans.setModal(true);
        howManyFans.setLocationRelativeTo(null);
        howManyFans.setTitle("Specify Initial Number of Fans - Fan Control");

        dialogMainPanel.setLayout(new BoxLayout(dialogMainPanel, BoxLayout.PAGE_AXIS));
        dialogSpinnerPanel.setLayout(new BoxLayout(dialogSpinnerPanel, BoxLayout.LINE_AXIS));

        dialogMainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        initialFansSpinner.setEditor(new JSpinner.DefaultEditor(initialFansSpinner));

        howManyFans.getContentPane().add(dialogMainPanel);

        dialogMainPanel.add(dialogSpinnerPanel);
        dialogMainPanel.add(Box.createVerticalStrut(10));
        dialogMainPanel.add(dialogButtonPanel);
        dialogSpinnerPanel.add(new JLabel("Specify the initial number of fans:   "));
        dialogSpinnerPanel.add(initialFansSpinner);
        dialogButtonPanel.add(okButton);
        dialogButtonPanel.add(Box.createHorizontalStrut(1));
        dialogButtonPanel.add(openButton);
        dialogButtonPanel.add(Box.createHorizontalStrut(1));
        dialogButtonPanel.add(exitButton);

        WindowAdapter wl = new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                howManyFans.dispose();
                FanControl.this.dispose();
            }
        };
        howManyFans.addWindowListener(wl);

        okButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                howManyFans.removeWindowListener(wl);
                howManyFans.dispose();
            }
        });

        openButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileOpened = true;
                if (noOfFans < 1) {
                    addFans(1);
                }
                if (openFile(FanControl.this)) {
                    howManyFans.removeWindowListener(wl);
                    howManyFans.dispose();
                }
                else {
                    fileOpened = false;
                }
            }
        });

        exitButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                howManyFans.dispose();
                FanControl.this.dispose();
            }
        });

        howManyFans.setVisible(true);

        if (!fileOpened) {
            addFans((int) initialFansSpinner.getValue() - noOfFans);
        }
        fileOpened = false;

    }

    /**
     * Adds new fans
     *
     * @param noOfNewFans number of new fans to add
     */
    private void addFans(int noOfNewFans) {
        if (noOfNewFans + noOfFans <= maxFans) {
            for (int i = 0; i < noOfNewFans; ++i) {
                Fan newFan = new Fan(FanControl.this, noOfFans, maxValue, "Fan " + String.valueOf(noOfFans + 1));
                fans.add(newFan);
                ++noOfFans;
                insertFan(newFan);
            }
        }
        else {
            JOptionPane.showMessageDialog(new JFrame(),
                    "The number of fans you are trying to enter exceeds the maximum number allowed ("
                    + String.valueOf(maxFans) + ")", "Maximum exceeded", JOptionPane.ERROR_MESSAGE);
        }
        revalidate();
    }

    private void insertFan(Fan newFan) {
        // If re-arranging is required, these determine whether a new row is needed
        int rowCount = 0, colCount = 0;
        JPanel fp;
        JPanel fanLabelPanel;
        JLabel fanLabel;

        // Add a horizontal panel where the fans are placed
        if (horizontalFanPanels.isEmpty()) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
            fanDisplayPanel.add(panel);
            horizontalFanPanels.add(panel);
        }

        if (reOrganise) {
            fanDisplayPanel.removeAll();
            for (int i = 0; i < horizontalFanPanels.size(); ++i) {
                horizontalFanPanels.get(i).removeAll();
            }
            int fanCount = 0;
            row = 0;
            column = 0;

            // Need to add a horizontal panel since we removed them all
            fanDisplayPanel.add(horizontalFanPanels.get(row));
            ++rowCount;

            //Insert all existing fans to the display panel in ascending order
            while (fanCount < noOfFans - 1) { // Do not insert the latest fan
                fp = new JPanel(new BorderLayout());
                fanLabelPanel = new JPanel(new BorderLayout());
                fanLabel = new JLabel(fans.get(fanCount).getLabel().getText());

                fanLabelPanel.setPreferredSize(new Dimension(200, 20));
                fanLabel.setHorizontalAlignment(JLabel.CENTER);

                fp.add(fans.get(fanCount));
                fp.add(fanLabel, BorderLayout.SOUTH);
                horizontalFanPanels.get(row).add(fp);

                fp.add(fanLabelPanel, BorderLayout.SOUTH);
                fanLabelPanel.add(fanLabel, BorderLayout.CENTER);

                ++colCount;
                ++fanCount;
                ++column;

                // end of row reached, start from a new row
                if (column > colMax) {
                    column = 0;
                    ++row;
                    ++rowCount;
                    colCount = 0;
                    fanDisplayPanel.add(horizontalFanPanels.get(row));
                }
            }
            reOrganise = false;
        }

        fp = new JPanel(new BorderLayout());
        fanLabelPanel = new JPanel(new BorderLayout());
        fanLabel = new JLabel(newFan.getLabel().getText());

        fanLabelPanel.setPreferredSize(new Dimension(200, 20));
        fanLabel.setHorizontalAlignment(JLabel.CENTER);

        horizontalFanPanels.get(row).add(fp);

        fp.add(newFan);
        fp.add(fanLabelPanel, BorderLayout.SOUTH);
        fanLabelPanel.add(fanLabel, BorderLayout.CENTER);

        if (row == column && rowCount == colCount) {
            /**
             * Equal number of fans in both column and row direction. Need to
             * readjust so that an order where top of the display has more
             * panels than bottom is maintained, and that the fans are in
             * ascending order based on their index numbers.
             */
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
            fanDisplayPanel.add(panel);
            horizontalFanPanels.add(panel);

            reOrganise = true; // next time when inserting, reorganise fans to that they will be in correct order
            ++rowMax;
            ++colMax;
        }
        else if (column == colMax) { // end of row reached
            ++row; // start a new row
            if (row == rowMax) {
                column = 0;
            }
            if (!reOrganise) {
                fanDisplayPanel.add(horizontalFanPanels.get(row));
            }
        }
        else {
            ++column;
        }

        // Add individual control panel (two panels in one row)
        if (noOfFans % 2 != 0) {
            if (noOfFans == 1) {
                JPanel controlLabelPanel = new JPanel();
                controlLabelPanel.setLayout(new BoxLayout(controlLabelPanel, BoxLayout.LINE_AXIS));
                controlLabelPanel.setPreferredSize(new Dimension(200, 20));

                indControlsPanel.add(controlLabelPanel);
                controlLabelPanel.add(new JLabel("Individual Fan Controls"));
            }
            JPanel subPanel = new JPanel();
            subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.LINE_AXIS));
            subPanel.setMaximumSize(new Dimension(1000, 100));
            indControlsPanel.remove(glue);

            indControlsSubPanels.add(subPanel);
            subPanel.add(newFan.getControlPanel());
            subPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            indControlsPanel.add(subPanel);

            indControlsPanel.add(glue);
        }
        else {
            JPanel p = indControlsSubPanels.get(indControlsSubPanels.size() - 1);
            p.add(Box.createHorizontalStrut(10));
            p.add(newFan.getControlPanel());
        }
    }

    public void removeFan(int fanIndex) {
        if (noOfFans == 1) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "At least one fan is required.", "Minimum number of fans", JOptionPane.ERROR_MESSAGE);
        }
        else {
            fans.remove(fanIndex);
            for (int j = fanIndex; j < fans.size(); ++j) {
                Fan fan = fans.get(j);
                fan.setIndex(fan.getIndex() - 1);
                fan.setLabel("Fan " + String.valueOf(fan.getIndex() + 1));
            }
            fanDisplayPanel.removeAll();
            horizontalFanPanels.clear();
            indControlsPanel.removeAll();
            indControlsSubPanels.clear();

            row = 0;
            column = 0;
            rowMax = 0;
            colMax = 0;
            noOfFans = 0;
            reOrganise = false;
            int size = fans.size();
            for (int i = 0; i < size; ++i) {
                ++noOfFans;
                insertFan(fans.get(i));
            }
            revalidate();
            repaint();
        }
    }

    /**
     * Opens a FAN file. returns true if a file was selected. If the operation
     * was cancelled, returns false.
     */
    private static boolean openFile(FanControl frame) {
        JFileChooser browse = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("Fan file", "fan", "FAN");

        browse.addChoosableFileFilter(filter);
        browse.setFileFilter(filter);

        if (browse.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            String fileName = browse.getSelectedFile().toString();

            while (frame.fans.size() > 1) { // leave one fan
                frame.removeFan(0);
            }

            try {
                Scanner fileScanner = new Scanner(new FileInputStream(fileName));
                ArrayList<String> fileParameters = new ArrayList();

                while (fileScanner.hasNext()) {
                    fileParameters.add(fileScanner.next());
                }

                int fansFromFileSize = Integer.parseInt(fileParameters.get(0));
                frame.addFans(fansFromFileSize - 1);

                for (int i = 0; i < fansFromFileSize; ++i) {
                    frame.fans.get(i).setSpeed(Integer.parseInt(fileParameters.get(i * 4 + 1)));

                    if ("0".equals(fileParameters.get(i * 4 + 2))) {
                        frame.fans.get(i).setTheme(0);
                    }
                    else if ("1".equals(fileParameters.get(i * 4 + 2))) {
                        frame.fans.get(i).setTheme(1);
                    }
                    else if ("2".equals(fileParameters.get(i * 4 + 2))) {
                        frame.fans.get(i).setTheme(2);

                    }
                    else if ("3".equals(fileParameters.get(i * 4 + 2))) {
                        frame.fans.get(i).setTheme(3);
                    }

                    if ("1".equals(fileParameters.get(i * 4 + 3))) {
                        frame.fans.get(i).turnOn();
                    }
                    if ("0".equals(fileParameters.get(i * 4 + 4))) {
                        frame.fans.get(i).reverse();
                    }
                }
            }
            catch (FileNotFoundException ex) {

            }
            return true;
        }
        return false;
    }

    /**
     * Adds a new fan group instance
     */
    private static void addNewFanInstance() {
        FanControl frame = new FanControl();
        Dimension windowSize = new Dimension(1100, 635);
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu lookAndFeelMenu = new JMenu("Look & Feel");
        JMenu helpMenu = new JMenu("Help");
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        JMenuItem userManualMenuItem = new JMenuItem("User Manual");

        JMenuItem metalLookAndFeelMenuItem = new JMenuItem("Metal");
        JMenuItem motifLookAndFeelMenuItem = new JMenuItem("Motif");
        JMenuItem nativeLookAndFeelMenuItem = new JMenuItem("Native");
        JMenuItem nimbusLookAndFeelMenuItem = new JMenuItem("Nimbus");

        JFileChooser browse = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("Fan file", "fan", "FAN");

        browse.addChoosableFileFilter(filter);
        browse.setFileFilter(filter);

        menuBar.add(fileMenu);
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(lookAndFeelMenu);
        fileMenu.add(exitMenuItem);
        lookAndFeelMenu.add(metalLookAndFeelMenuItem);
        lookAndFeelMenu.add(motifLookAndFeelMenuItem);
        lookAndFeelMenu.add(nativeLookAndFeelMenuItem);
        lookAndFeelMenu.add(nimbusLookAndFeelMenuItem);

        menuBar.add(helpMenu);
        helpMenu.add(userManualMenuItem);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ++noOfInstances;

        WindowAdapter wl = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JDialog saveBeforeExit = new JDialog();
                JPanel saveBeforeExitMainPanel = new JPanel();
                JButton yesButton = new JButton("YES");
                JButton noButton = new JButton("NO");
                JButton cancelButton = new JButton("CANCEL");
                JPanel msgPanel = new JPanel(new BorderLayout());
                JPanel saveBeforeExitButtonPanel = new JPanel(new GridLayout());
                JLabel l = new JLabel("Do you want to save before exiting?");

                saveBeforeExitMainPanel.setLayout(new BoxLayout(saveBeforeExitMainPanel, BoxLayout.PAGE_AXIS));
                saveBeforeExitButtonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                saveBeforeExit.setTitle("Exit");
                saveBeforeExit.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                saveBeforeExit.setLocationRelativeTo(null);

                saveBeforeExit.add(saveBeforeExitMainPanel);
                saveBeforeExitMainPanel.add(msgPanel);
                saveBeforeExitMainPanel.add(saveBeforeExitButtonPanel);

                l.setHorizontalAlignment(JLabel.CENTER);
                msgPanel.add(l, BorderLayout.CENTER);

                saveBeforeExitButtonPanel.add(yesButton);
                saveBeforeExitButtonPanel.add(Box.createHorizontalStrut(0));
                saveBeforeExitButtonPanel.add(noButton);
                saveBeforeExitButtonPanel.add(Box.createHorizontalStrut(0));
                saveBeforeExitButtonPanel.add(cancelButton);

                saveBeforeExit.setSize(new Dimension(100, 100));
                saveBeforeExit.setVisible(true);
                saveBeforeExit.pack();

                yesButton.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        saveMenuItem.doClick();
                        if (noOfInstances > 1) {
                            saveBeforeExit.dispose();
                            frame.dispose();
                            --noOfInstances;
                        }
                        else {
                            System.exit(0);
                        }

                    }
                });

                noButton.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        if (noOfInstances > 1) {
                            saveBeforeExit.dispose();
                            frame.dispose();
                            --noOfInstances;
                        }
                        else {
                            System.exit(0);
                        }
                    }
                });

                cancelButton.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        saveBeforeExit.dispose();
                    }
                });
            }
        };
        frame.addWindowListener(wl);

        frame.setTitle("Fan Control");
        frame.setSize(windowSize);
        frame.setMinimumSize(windowSize);
        frame.setJMenuBar(menuBar);

        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                addNewFanInstance();
            }
        });

        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                frame.fansToAdd.setValue(1);
                openFile(frame);
            }
        });

        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (browse.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    File file = new File(browse.getSelectedFile().toString() + ".fan");
                    try {
                        PrintWriter writer = new PrintWriter(file, "UTF-8");
                        writer.print(frame.fans.size() + " ");
                        for (int i = 0; i < frame.fans.size(); ++i) {
                            writer.print(frame.fans.get(i).getSpeed() + " ");
                            writer.print(frame.fans.get(i).getThemeSelected() + " ");

                            if (frame.fans.get(i).isTurnedOn()) {
                                writer.print(1 + " ");
                            }
                            else {
                                writer.print(0 + " ");
                            }

                            writer.print(frame.fans.get(i).getFanDirection() + " ");
                        }
                        writer.print(frame.speedSlider.getValue() + " ");
                        if (frame.themeDefault.isSelected()) {
                            writer.print(0 + " ");
                        }
                        else if (frame.themeAustralia.isSelected()) {
                            writer.print(1 + " ");
                        }
                        else if (frame.themeFinland.isSelected()) {
                            writer.print(2 + " ");
                        }
                        else if (frame.themeSweden.isSelected()) {
                            writer.print(3 + " ");
                        }
                        else {
                            writer.print(-1 + " ");
                        }
                        writer.close();
                    }
                    catch (IOException e) {
                        System.out.println("Error in saving file.");
                    }
                }
            }
        });

        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });

        metalLookAndFeelMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                setLookFeel("javax.swing.plaf.metal.MetalLookAndFeel", frame);
            }
        });

        nativeLookAndFeelMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                setLookFeel(UIManager.getSystemLookAndFeelClassName(), frame);
            }
        });

        nimbusLookAndFeelMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                setLookFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel", frame);
            }
        });

        motifLookAndFeelMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                setLookFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel", frame);
            }
        });

        userManualMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JDialog userManualDialog = new JDialog();
                JEditorPane userManualPane = new JEditorPane();

                userManualDialog.setTitle("User Manual = Fan Control");
                userManualDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                userManualDialog.setSize(new Dimension(930, 600));
                userManualDialog.setVisible(true);
                userManualPane.setEditable(false);
                userManualPane.setContentType("text/html");

                final JScrollPane sp = new JScrollPane(userManualPane);
                userManualDialog.add(sp);
                userManualPane.addHyperlinkListener(new HyperlinkListener() {
                    @Override
                    public void hyperlinkUpdate(final HyperlinkEvent pE) {
                        if (HyperlinkEvent.EventType.ACTIVATED == pE.getEventType()) {
                            String desc = pE.getDescription();
                            if (desc == null || !desc.startsWith("#")) {
                                return;
                            }
                            desc = desc.substring(1);
                            userManualPane.scrollToReference(desc);
                        }
                    }
                });

                try {
                    Scanner fileScanner = new Scanner(new FileInputStream("./user_manual/user_manual.html"));
                    StringBuilder manualString = new StringBuilder();

                    while (fileScanner.hasNextLine()) {
                        manualString.append(fileScanner.nextLine());
                    }
                    fileScanner.close();
                    userManualPane.setText(manualString.toString());
                    userManualPane.setCaretPosition(0);
                }
                catch (FileNotFoundException ex) {

                }
            }
        });
    }

    private static void setLookFeel(String lookAndFeel, JFrame f) {
        try {
            UIManager.setLookAndFeel(lookAndFeel);
            SwingUtilities.updateComponentTreeUI(f);
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(FanControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex) {
            Logger.getLogger(FanControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex) {
            Logger.getLogger(FanControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(FanControl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                addNewFanInstance();
            }
        });
    }
}

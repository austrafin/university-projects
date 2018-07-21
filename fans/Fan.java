import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;

/**
 * Class for fan which includes the fan graphics as well as its controls
 */
public class Fan extends JPanel implements ActionListener {

    // needed so that when the REMOVE button is pushed, the main class can remove the fan and its controls
    private FanControl fanControl;

    private final JPanel controlPanel, sliderPanel, buttonPanel, themePanel;
    private final JButton onButton, offButton, reverseButton, removeButton;
    private final JSlider speedSlider;
    private final JLabel label;
    private final ButtonGroup themes;
    private final JRadioButton themeDefault, themeFinland, themeAustralia, themeSweden;
    private final Timer timer;
    private Color colourBlades = new Color(0, 0, 0); // default colour black
    private Color colourFrame = new Color(0, 0, 0); // default colour black

    private int fanIndex; // index number for capturing mouse events
    private int p1 = 0, p2 = 90, p3 = 180, p4 = 270;
    private int previousSliderValue = 0;
    private int currentSpeed = 0;
    private int angle = 2;

    private boolean running = false;

    /**
     * This is needed so that when changing the theme programatically and
     * updating the radio buttons, the ItemListener of the radio buttons won't
     * interfere and cause an eternal loop.
     */
    private boolean invokeThemeSelection = true;

    final private int minDelay = 1; // maximum speed for a fan
    private final int maxDelay;

    public Fan(FanControl fc, int index, int md, String l) {
        this.fanControl = fc;
        this.fanIndex = index;
        this.maxDelay = md;

        sliderPanel = new JPanel();
        buttonPanel = new JPanel();
        themePanel = new JPanel(new BorderLayout());
        controlPanel = new JPanel();

        onButton = new JButton("ON");
        offButton = new JButton("OFF");
        reverseButton = new JButton("REVERSE");
        removeButton = new JButton("REMOVE");

        speedSlider = new JSlider(JSlider.HORIZONTAL, 0, 30, 5);
        themes = new ButtonGroup();
        themeDefault = new JRadioButton("Default");
        themeFinland = new JRadioButton("Finland");
        themeAustralia = new JRadioButton("Australia");
        themeSweden = new JRadioButton("Sweden");

        timer = new Timer(maxDelay, this); // the slowest speed is default
        label = new JLabel(l);

        speedSlider.setValue(0);
        speedSlider.setMaximum(maxDelay);
        setTheme(0);

        // Add to panels and components
        sliderPanel.add(label);
        sliderPanel.add(speedSlider);
        buttonPanel.add(onButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(offButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(reverseButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(removeButton);
        themePanel.add(themeDefault);
        themePanel.add(themeAustralia);
        themePanel.add(themeFinland);
        themePanel.add(themeSweden);
        controlPanel.add(sliderPanel);
        controlPanel.add(buttonPanel);
        controlPanel.add(themePanel);

        themes.add(themeDefault);
        themes.add(themeFinland);
        themes.add(themeAustralia);
        themes.add(themeSweden);

        // Set layouts
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        themePanel.setLayout(new BoxLayout(themePanel, BoxLayout.LINE_AXIS));

        // Set sizes
        controlPanel.setMaximumSize(new Dimension(320, 110)); // Changing this size has different outcomes on Linux compared to Windows!
        onButton.setPreferredSize(new Dimension(70, 40));
        offButton.setPreferredSize(new Dimension(70, 40));

        // Set borders
        speedSlider.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));

        // Add listeners
        speedSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentSpeed = speedSlider.getValue();
                SmoothFan smooth = new SmoothFan(); // fix later
                smooth.start();
            }
        });

        onButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                turnOn();
            }
        });

        offButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                turnOff();
            }
        });

        reverseButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reverse();
            }
        });

        removeButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fanControl.removeFan(fanIndex);
            }
        });

        themeDefault.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (invokeThemeSelection && themeDefault.isSelected()) {
                    setTheme(0);
                }
            }
        });

        themeAustralia.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (invokeThemeSelection && themeAustralia.isSelected()) {
                    setTheme(1);
                }
            }
        });

        themeFinland.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (invokeThemeSelection && themeFinland.isSelected()) {
                    setTheme(2);
                }
            }
        });

        themeSweden.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (invokeThemeSelection && themeSweden.isSelected()) {
                    setTheme(3);
                }
            }
        });
    }

    public int getThemeSelected() {
        if (themeDefault.isSelected()) {
            return 0;
        }
        else if (themeAustralia.isSelected()) {
            return 1;
        }
        else if (themeFinland.isSelected()) {
            return 2;
        }
        else if (themeSweden.isSelected()) {
            return 3;
        }
        else {
            return -1; // no theme selected
        }
    }

    public boolean isTurnedOn() {
        return timer.isRunning();
    }

    public int getFanDirection() {
        if (angle >= 0) { // left
            return 1;
        }
        else {
            return 0; // right
        }
    }

    public JPanel getControlPanel() {
        return controlPanel;
    }

    public JLabel getLabel() {
        return label;
    }

    public int getIndex() {
        return fanIndex;
    }

    public void setSpeed(int sliderValue) {
        speedSlider.setValue(sliderValue);
    }

    public int getSpeed() {
        return speedSlider.getValue();
    }

    public void turnOn() {
        if (!running) {
            running = true;
            previousSliderValue = 0;
            currentSpeed = speedSlider.getValue();
            SmoothFan smooth = new SmoothFan();
            smooth.start();
            timer.start();
        }
    }

    public void turnOff() {
        if (running) {
            running = false;
            previousSliderValue = speedSlider.getValue();
            currentSpeed = 0;
            SmoothFan smooth = new SmoothFan(); // fix later
            smooth.start();
        }
    }

    public void reverse() {
        angle *= -1;
    }

    public void setTheme(int theme) {
        
        Color c;
        invokeThemeSelection = false;
        switch (theme) {
            case 0:
                c = new Color(0, 0, 0);
                setBackground(UIManager.getColor("Panel.background"));
                colourBlades = c;
                colourFrame = c;
                themeDefault.setSelected(true);
                break;
            case 1:
                setBackground(new Color(0, 0, 139));
                colourBlades = Color.white;
                colourFrame = Color.red;
                themeAustralia.setSelected(true);
                break;
            case 2:
                c = new Color(0, 62, 145);
                setBackground(Color.white);
                colourBlades = c;
                colourFrame = c;
                themeFinland.setSelected(true);
                break;
            case 3:
                c = new Color(255, 204, 0);
                setBackground(new Color(0, 79, 183));
                colourBlades = c;
                colourFrame = c;
                themeSweden.setSelected(true);
            default:
                break;
        }
        invokeThemeSelection = true;
    }

    public void setIndex(int i) {
        fanIndex = i;
    }

    public void setLabel(String l) {
        label.setText(l);
    }

    private void setDelay(int increment) {
        int delay = 200;
        timer.setDelay(maxDelay - increment + minDelay);

        try {
            Thread.sleep(delay);
        }
        catch (InterruptedException ex) {
            Logger.getLogger(FanControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int xCenter = getWidth() / 2;
        int yCenter = getHeight() / 2;
        int radius = (int) (Math.min(getWidth(), getHeight()) * 0.4);

        int x = xCenter - radius;
        int y = yCenter - radius;

        p1 += angle;
        p2 += angle;
        p3 += angle;
        p4 += angle;

        // Draw the frame
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(colourFrame);
        g2D.setStroke(new BasicStroke(3));
        g.drawOval(x - radius / 6, y - radius / 6, radius * 2 + radius / 3, radius * 2 + radius / 3);

        // Draw the blades
        g.setColor(colourBlades);
        g.fillArc(x, y, 2 * radius, 2 * radius, p1, 30);
        g.fillArc(x, y, 2 * radius, 2 * radius, p2, 30);
        g.fillArc(x, y, 2 * radius, 2 * radius, p3, 30);
        g.fillArc(x, y, 2 * radius, 2 * radius, p4, 30);
    }

    /**
     * Smoothens the transition from one speed to another by
     * accelerating/decelerating the fan.
     */
    private class SmoothFan extends Thread {

        @Override
        public void run() {
            if (previousSliderValue > currentSpeed) {
                for (int i = previousSliderValue; i > currentSpeed; --i) {
                    setDelay(i);
                }
            }
            else {
                for (int i = previousSliderValue; i <= currentSpeed; ++i) {
                    setDelay(i);
                }
            }
            if (running) {
                currentSpeed = speedSlider.getValue();
                previousSliderValue = currentSpeed;
            }
            else {
                timer.stop();
            }
        }
    }
}

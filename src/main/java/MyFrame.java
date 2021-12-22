package main.java;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Constructor Class JFrame
class MyFrame extends JFrame implements Serializable {

    private final JFrame frame = new JFrame();
    protected int tabIndex;
    protected int preTabIndex;
    protected String tabName;
    protected JPanel starGroup;
    protected JPanel spectral;
    protected JPanel magnitude;
    protected JLayeredPane graph;
    protected JTabbedPane tabbedPane;
    protected JPanel menu;
    protected Border border = BorderFactory.createBevelBorder(1);


    //Constructor
    public MyFrame() {
        tabIndex = 0;
        preTabIndex = 0;

        //Set panels
        setGraph();
        createTabbedPane();
        setStarGroup();
        setMenu();

    }

    // sizes images, soon all images will be correct size and this will be removed.
    public static ImageIcon image(ImageIcon imageIcon, int width, int height) {
        Image image = imageIcon.getImage(); // transform it
        Image newImg = image.getScaledInstance(width, height, Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newImg);  // transform it back
        return imageIcon;
    }

    // visual settings for JFrame
    public void setFrame(JFrame frame) {
        frame.setTitle("MyFrame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setSize(1305, 725);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.LIGHT_GRAY);
    }

    // The Graph's JLayered Pane
    public void setGraph() {
        JLayeredPane graphPanel = new JLayeredPane();
        JLabel graphLabels = new JLabel();
        File file = new File("src/main/resources/Images/HRColor.png");
        ImageIcon graphImage =
                new ImageIcon(file.getAbsolutePath());

        graphLabels.setIcon(graphImage);
        graphLabels.setBounds(0, 0, 1050, 375);

        graphPanel.add(graphLabels, Integer.valueOf(0));
        graphPanel.setBounds(45, 5, 1050, 375);

        this.graph = graphPanel;
        this.graph.setBorder(border);

        JPanel spectralInc = new JPanel();
        JLabel spectral = new JLabel();
        File file1 = new File("src/main/resources/Images/Spectral.png");
        ImageIcon spectralImage =
                new ImageIcon(file1.getAbsolutePath());

        spectral.setIcon(spectralImage);
        spectral.setBounds(0, 0, 1050, 40);

        spectralInc.add(spectral);
        spectralInc.setBounds(45, 375, 1050, 50);

        this.spectral = spectralInc;

        JPanel magnitudeInc = new JPanel();
        JLabel magnitude = new JLabel();
        File file3 = new File("src/main/resources/Images/magnitude.png");
        ImageIcon magnitudeImage =
                new ImageIcon(file3.getAbsolutePath());

        magnitude.setIcon(magnitudeImage);
        magnitude.setBounds(0, 0, 40, 375);

        magnitudeInc.add(magnitude);
        magnitudeInc.setBounds(5, 0, 40, 380);

        this.magnitude = magnitudeInc;


    }

    // Menu panel
    public void setMenu() {
        JLabel[] inputLabels =
                {new JLabel("Enter Spectral Class"),
                        new JLabel("Enter Absolute Magnitude")};

        JTextField[] inputTextFields =
                {new JTextField(20),
                        new JTextField(20)};

        JButton[] inputButtons =
                {new JButton("New Tab"),//0
                        new JButton("Add Star"),//1
                        new JButton("Save"),//2
                        new JButton("Load"),//3
                        new JButton("Clear"),//4
                        new JButton("Tutorial")};//4

        // Add tab from buttons panel
        inputButtons[0].addActionListener(ae -> {
            String name =
                    JOptionPane.showInputDialog(graph, "Enter Group Name?");

            if (name != null) {
                addPanel(name, null);
            }
        });

        // Add Star from buttons panel
        inputButtons[1].addActionListener(ae -> {
            String spectral = inputTextFields[0].getText().toUpperCase();
            String magnitude = inputTextFields[1].getText();

            //Make sure something was entered for magnitude.
            boolean magRange = false;
            if (!(magnitude.isEmpty())) {
                magRange = Integer.parseInt(magnitude) < 16 && Integer.parseInt(magnitude) > (-11);
            }

            //make sure the text entered is in correct format
            if (this.tabbedPane.getSelectedComponent() != null &&
                    spectral.matches("([OBAFGKM][0-9])") &&
                    magRange) {

                setTabIndex();
                setTabName();
                addStarToHistory(spectral, magnitude);

            } else {
                System.out.println(spectral);
                if (!spectral.matches("[OBAFGKM][0-9]") && this.tabbedPane.getSelectedComponent() != null) {
                    JOptionPane.showMessageDialog(this.graph, "please enter a correct Spectral Class in " +
                            "format O, A, B, F, G, K, or M followed by a number 0-9.");

                } else if (!magRange) {
                    JOptionPane.showMessageDialog(this.graph, "please enter a Magnitude between -10 and 15.");

                } else {
                    JOptionPane.showMessageDialog(this.graph, "please select a tab first.");
                }
            }
        });

        // save
        inputButtons[2].addActionListener(ae -> {

            setTabIndex();
            setTabName();
            writeObject();

        });

        // load
        inputButtons[3].addActionListener(ae -> readObject());

        // clear history
        inputButtons[4].addActionListener(ae -> clearHistory());
        inputButtons[5].addActionListener(ae -> {
            try {
                new main.java.Help(new String[]{"How to", "Spectral Class", "Absolute Magnitude", "History", "Glossary"});
            } catch (NullPointerException | IOException e) {
                System.out.print("NullPointerException Caught");
            }
        });

        //Create JPanel
        JPanel inputPanel = new JPanel();
        inputPanel.setBounds(1100, 90, 200, 340);
        inputPanel.setBackground(Color.DARK_GRAY);

        for (int i = 0; i < inputLabels.length; i++) {
            inputLabels[i].setForeground(Color.white);
            inputLabels[i].setBorder(border);
            inputPanel.add(inputLabels[i]);
            inputPanel.add(inputTextFields[i]);
        }
        for (JButton inputButton : inputButtons) {
            inputPanel.add(inputButton);
        }

        menu = inputPanel;
        menu.setBorder(border);
        menu.setLayout(new GridLayout(11, 1, 0, 5));

    }

    // Focused star group displayed on upper right
    public void setStarGroup() {
        JPanel currentTab = new JPanel();
        currentTab.setBounds(1100, 5, 200, 80);
        currentTab.setBackground(Color.DARK_GRAY);

        this.starGroup = currentTab;
        this.starGroup.setBorder(border);
        this.starGroup.setLayout(new GridLayout(2, 1, 0, 5));
    }

    // Make and send a history panel
    public JPanel getHistory() {
        JPanel historyPanels = new JPanel();
        historyPanels.setLayout(new FlowLayout(FlowLayout.LEADING));
        historyPanels.setBounds(5, 435, 1300, 255);
        historyPanels.setBackground(Color.WHITE);
        historyPanels.validate();

        return historyPanels;
    }

    // History tabbed panel
    public void createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.LEFT);
        tabbedPane.setBounds(5, 435, 1600, 255);
        tabbedPane.setBackground(Color.DARK_GRAY);
        tabbedPane.setForeground(Color.BLACK);
        tabbedPane.setOpaque(true);

        this.tabbedPane = tabbedPane;

        //Change listener when switching tabs
        this.tabbedPane.addChangeListener(e -> {
            if (e.getSource() instanceof JTabbedPane) {
                int selectedIndex = this.tabbedPane.getSelectedIndex();
                System.out.println(preTabIndex + "   " + selectedIndex);
                if (preTabIndex != selectedIndex && selectedIndex != -1) {
                    // for setting the group name upper right
                    setTabIndex();
                    setTabName();
                    setGroup();

                    // clear and redraw stars when switching panels
                    JPanel historyPanels = (JPanel) this.tabbedPane.getSelectedComponent();
                    JLabel historyLabel;
                    clearGraph();

                    if (historyPanels.getComponentCount() > 0) {
                        for (int i = 0; i < historyPanels.getComponentCount(); i++) {
                            historyLabel = (JLabel) historyPanels.getComponent(i);
                            this.getStarLabel(historyLabel.getText());
                        }
                    }

                    // for tracking tab changes
                    preTabIndex = selectedIndex;
                }
            }

        });
        this.tabbedPane.validate();

    }

    public JScrollPane getScrollPane() {
        // the scroll pane
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(5, 435, 1295, 255);
        return scrollPane;
    }

    // display current star group in upper right
    public void setGroup() {
        this.starGroup.removeAll();
        JLabel groupName = new JLabel(("Current Group"));
        groupName.setForeground(Color.WHITE);

        JLabel groupLabel = new JLabel(this.tabName);
        JPanel groupPanel = new JPanel();
        groupLabel.setForeground(Color.BLACK);
        groupLabel.setFont(new Font("Serif", Font.PLAIN, 25));
        groupPanel.setBackground(Color.WHITE);
        groupPanel.add(groupLabel);

        this.starGroup.add(groupName);
        this.starGroup.add(groupPanel);
        this.starGroup.revalidate();
        this.starGroup.repaint();
    }

    //set star x coordinate
    public int setX(String text) {
        int x;
        String replacement = "";
        char letter;
        int level;

        //Find the Spectral class in the historyPanel label
        String regex = "\\s[OBAFGKM][0-9]\\s";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);

        while (m.find()) {
            replacement = m.group();
        }
        // split letters and number
        letter = replacement.charAt(1);
        level = Integer.parseInt(replacement.substring(2, 3));
        level = (level * 15);

        // Calculate the x coordinate
        // there is seven classes of stars (there are more but usually just 7 on diagram)
        // each classes has 9 subclasses about 15 pixels each on this graph.
        // The Diagram goes from hottest to coldest, blue -> hottest
        switch (letter) {
            case 'O': x = level;
            break;
            case 'B': x = 150 + level;
            break;
            case 'A': x = 300 + level;
            break;
            case 'F': x = 450 + level;
            break;
            case 'G': x = 600 + level;
            break;
            case 'K': x = 750 + level;
            break;
            case 'M': x = 900 + level;
            break;
            default:
                JOptionPane.showMessageDialog(this.frame, "Invalid entry");
                x = -1;

        }
            return x - (36 / 2);


    }

    //set y coordinate
    public int setY(String text) {
        int y;
        int multi;
        String replacement = "";

        //looking for number in string - has to be double number with 0 place holder

        String regex = "\\s-?[0-1][0-9]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);

        while (m.find()) {
            replacement = m.group();
            System.out.println(replacement);
        }
        // When loading the txt is not checked for format so this should catch any mistakes
        // wish I could just delete offending label
        if(replacement != null) {
            multi = Integer.parseInt(replacement.trim());
            System.out.println("multi = " + multi);
            y = 150 + (15 * multi);
            System.out.println(y);
            return (y) - (50 / 2);
        }else {
            return -1;
        }
    }

    //Star Image
    public void getStarLabel(String text) {
        JLabel starLabel = new JLabel();
        if(text != null) {
            int x = setX(text);
            int y = setY(text);

            File file = new File("src/main/resources/Images/star.png");
            ImageIcon starImage =
                    new ImageIcon(file.getAbsolutePath());

            starLabel.setIcon(starImage);
            starLabel.setText(text.substring(0, text.indexOf(" ")) + this.tabName);
            starLabel.setHorizontalTextPosition(JLabel.CENTER);
            starLabel.setVerticalTextPosition(JLabel.BOTTOM);
            starLabel.setFont(new Font("Serif", Font.PLAIN, 8));
            starLabel.setBounds(x, y, 36, 56);
            starLabel.setForeground(Color.white);
            this.graph.add(starLabel, Integer.valueOf(1));
        }

    }

    // Add a star to the history panel
    public void addStarToHistory(String spectral, String magnitude) {
        // makes a history panel
        JPanel historyPanels = (JPanel) this.tabbedPane.getSelectedComponent();
        this.tabbedPane.requestFocusInWindow();
        this.tabbedPane.addNotify();
        int labelCount = historyPanels.getComponentCount() + 1;

        JLabel historyLabel = new JLabel();

        String name = this.tabName;

        //Resize image
        File file = new File("src/main/resources/Images/singlestar.jpeg");
        ImageIcon historyImage =
                new ImageIcon(file.getAbsolutePath());
        ImageIcon gImageResized = image(historyImage, 200, 200);

        //Adding image to label and label to panel
        historyLabel.setIcon(gImageResized);
        historyLabel.setText(labelCount + ". " + name + " - " + spectral + " " + magnitude);
        historyLabel.setHorizontalTextPosition(JLabel.CENTER);
        historyLabel.setVerticalTextPosition(JLabel.BOTTOM);
        historyPanels.add(historyLabel);
        getStarLabel(historyLabel.getText());

    }

    // creates a panel for history
    public void addPanel(String name, JPanel panel) {
        this.tabbedPane.requestFocusInWindow();
        this.tabbedPane.addNotify();

        JPanel historyPanels;
        JLabel historyLabel;

        if (panel == null) {
            historyPanels = getHistory();
        } else {
            historyPanels = panel;
            if (historyPanels.getComponentCount() > 0) {
                for (int i = 0; i < historyPanels.getComponentCount(); i++) {
                    historyLabel = (JLabel) historyPanels.getComponent(i);
                    this.getStarLabel(historyLabel.getText());
                }
            }
        }

        //setTabIndex();
        this.tabbedPane.addTab(name, historyPanels);
        this.tabbedPane.setBackgroundAt(this.tabIndex, Color.WHITE);
        this.tabbedPane.setSelectedIndex(this.tabIndex);
        setGroup();
        preTabIndex = this.tabIndex;


    }

    //Clears graph
    public void clearGraph() {
        graph.removeAll();
        JLabel graphLabels = new JLabel();
        File file = new File("src/main/resources/Images/HRColor.png");
        ImageIcon graphImage =
                new ImageIcon(file.getAbsolutePath());

        graphLabels.setIcon(graphImage);
        graphLabels.setBounds(0, 0, 1050, 375);

        graph.add(graphLabels, Integer.valueOf(0));
        graph.revalidate();
        graph.repaint();
    }

    // clears history panel
    public void clearHistory() {
        this.tabbedPane.removeAll();
        this.tabbedPane.revalidate();
        this.tabbedPane.repaint();
        clearGraph();
        this.tabIndex = 0;

    }

    // Delete button - coming soon
    public void deleteStar() {
        clearGraph();
    }

    // set Tab Index with current index
    public void setTabIndex() {
        this.tabIndex = this.tabbedPane.getSelectedIndex();
    }

    // set name with current tab name
    public void setTabName() {
        if (tabIndex != -1)
            this.tabName = this.tabbedPane.getTitleAt(this.tabIndex);
    }

    // Serialize the JPanel for saving
    public void writeObject() {
        // file location
        String filename = "src/main/resources/TextFile/SavedStars" + tabName;
        JPanel historyPanels = (JPanel) this.tabbedPane.getSelectedComponent();
        this.tabbedPane.requestFocusInWindow();
        this.tabbedPane.addNotify();

        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(historyPanels);

            out.close();
            file.close();

            JOptionPane.showMessageDialog(this.frame, "Star life cycle has been saved");

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this.frame, "IOException is caught");
        }
    }

    // reads object and sends to addStarsToHistory()
    public void readObject() {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(new File("src/main/resources/TextFile/SavedStars"));
        int response = fileChooser.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            try {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                String filename = fileChooser.getSelectedFile().getName();

                // Reading the object from a file
                FileInputStream file = new FileInputStream(path);
                ObjectInputStream in = new ObjectInputStream(file);

                // Method for deserialization of object
                JPanel historyPanels = (JPanel) in.readObject();

                in.close();
                file.close();


                addPanel(filename, historyPanels);


            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this.frame, "IOException is caught");
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this.frame, "ClassNotFoundException is caught");
            }
        }
    }


}


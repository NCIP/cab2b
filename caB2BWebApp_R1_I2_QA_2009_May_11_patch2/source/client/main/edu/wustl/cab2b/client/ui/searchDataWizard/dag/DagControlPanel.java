package edu.wustl.cab2b.client.ui.searchDataWizard.dag;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.IconButton;
import edu.wustl.cab2b.client.ui.util.CommonUtils.DagImages;

/**
 * Control panel which holds buttons for operations performed on DAG.  
 * @author Pratibha Dhok
 */
public class DagControlPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;

    /** Event name that notifies that User has pressed Magnifying-Glass button...      */
    public static final String EVENT_RESET_BUTTON_CLICKED = "EVENT_RESET_BUTTON_CLICKED";

    private JButton selectNodeButton;

    private JButton connectNodeButton;

    private JButton parenthesislButton;

    private JButton autoconnectButton;

    private Cab2bButton resetButton;

    private Cab2bHyperlink clearAllPathsLink;

    private final String AUTOCONNECTLABEL = "Auto Connect";

    private final String CLEARALLPATHS = "<HTML> <u>Clear All Paths</u>";

    private MainDagPanel mainDagPanel;

    private Map<DagImages, Image> dagImageMap;

    /**
     * Constructor for control panel 
     */
    public DagControlPanel(MainDagPanel parentPanel, Map<DagImages, Image> dagImageMap) {
        mainDagPanel = parentPanel;
        this.dagImageMap = dagImageMap;
        initGUI();
    }

    private void initGUI() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(getLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);

        selectNodeButton = getButton(DagImages.SelectIcon, DagImages.selectMOIcon, "Select Node", false);
        leftPanel.add(selectNodeButton);

        connectNodeButton = getButton(DagImages.ArrowSelectIcon, DagImages.ArrowSelectMOIcon, "Connect Nodes",
                                      true);
        connectNodeButton.addActionListener(this);
        leftPanel.add(connectNodeButton);

        parenthesislButton = getButton(DagImages.ParenthesisIcon, DagImages.ParenthesisMOIcon, "Add Parenthesis",
                                       false);
        leftPanel.add(parenthesislButton);

        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        clearAllPathsLink = new Cab2bHyperlink();
        clearAllPathsLink.setText(CLEARALLPATHS);
        autoconnectButton = new JButton(AUTOCONNECTLABEL);
        autoconnectButton.addActionListener(this);
        autoconnectButton.setEnabled(true);       

        rightPanel.setLayout(getLayout(FlowLayout.RIGHT));
        rightPanel.add(autoconnectButton);
        JLabel seperatorTwo = new JLabel("|");
        rightPanel.add(seperatorTwo);

        resetButton = new Cab2bButton("Reset");
        rightPanel.add(resetButton);
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                firePropertyChange(EVENT_RESET_BUTTON_CLICKED, -1, 0);
            }
        });

        JLabel seperator = new JLabel("|");
        rightPanel.add(seperator);
        rightPanel.add(clearAllPathsLink);
        clearAllPathsLink.addActionListener(this);

        /* //Adding Reset button 
         JLabel seperatorTwo = new JLabel("|");
         rightPanel.add(seperatorTwo);
         resetButton = new Cab2bButton("Reset");*/
        //  rightPanel.add(resetButton);
        setLayout(new BorderLayout());
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    private FlowLayout getLayout(int alignment) {
        FlowLayout layout = new FlowLayout();
        layout.setHgap(10);
        layout.setAlignment(alignment);
        return layout;
    }

    private IconButton getButton(DagImages normal, DagImages mouseOver, String tooltip, boolean enabled) {
        IconButton button = new IconButton(dagImageMap.get(normal), dagImageMap.get(mouseOver));
        button.setToolTipText(tooltip);
        button.setEnabled(enabled);
        return button;
    }

    /**
     * @see java.awt.Component#isOpaque()
     */
    public boolean isOpaque() {
        return true;
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    public void paintComponent(Graphics g) {
        Graphics2D gd = (Graphics2D) g;
        Rectangle bounds = getBounds();
        // Set Paint for filling Shape
        Paint gradientPaint = new GradientPaint(0, 0, Color.lightGray, bounds.width, bounds.height, Color.WHITE);
        gd.setPaint(gradientPaint);
        gd.fillRect(0, 0, bounds.width, bounds.height);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent event) {
        // Handle connect nodes event
        if (event.getSource() == connectNodeButton) {
            // call parents connect node method
            mainDagPanel.linkNodes();
        } else if (event.getSource() == clearAllPathsLink) {
            // Clear all the paths
            mainDagPanel.clearAllPaths();
        } else if (event.getSource() == autoconnectButton) {
            // Perform auto-connect functionality on selected classes
            mainDagPanel.performAutoConnect();
        }
        revalidate();
    }

    /**
     * This method enable or disables the AutoConnect button
     * @param enable
     */
    public void enableAutoConnectButton(boolean enable) {
        autoconnectButton.setEnabled(enable);
    }

}
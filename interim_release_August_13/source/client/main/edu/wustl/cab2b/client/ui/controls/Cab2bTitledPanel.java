package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXTitledPanel;

import edu.wustl.cab2b.client.ui.WindowUtilities;

public class Cab2bTitledPanel extends JXTitledPanel {
    private static final long serialVersionUID = 1L;

    public Cab2bTitledPanel() {
        this("");
        this.setBorder(null);
    }

    public Cab2bTitledPanel(String title) {
        this(title, new Cab2bPanel());
        this.setBorder(null);
    }

    public Cab2bTitledPanel(String title, Container container) {
        super(title, container);
        this.setBackground(Color.WHITE);
        this.setBorder(null);
    }

    public static void main(String[] args) {
        Cab2bPanel parentPanel = new Cab2bPanel();

        Cab2bTitledPanel titledPanel = new Cab2bTitledPanel("Titled Panel");

        parentPanel.add(new JLabel("This is a sample Label"));

        JTextField textField = new JTextField();
        textField.setColumns(10);
        parentPanel.add(textField);
        parentPanel.add(new JButton("Button"));

        titledPanel.add(parentPanel);

        WindowUtilities.showInFrame(titledPanel, "Titskldjfdf ");
    }

}

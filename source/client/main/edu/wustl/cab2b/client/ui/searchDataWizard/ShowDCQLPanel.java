package edu.wustl.cab2b.client.ui.searchDataWizard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;

import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.util.WindowUtilities;
import edu.wustl.cab2b.common.domain.DCQL;

public class ShowDCQLPanel extends Cab2bPanel {

    /* JDialog in which the Panel is displayed */
    private JDialog dialog;

    final private Cab2bLabel xmlTextPane = new Cab2bLabel();

    final Cab2bPanel messagePanel = new Cab2bPanel();

    public ShowDCQLPanel(DCQL dcql) {
        initGUI(dcql);
    }

    private void initGUI(DCQL dcql) {
        String xmlText = new XmlParser().parseXml(dcql.getDcqlQuery());

        xmlTextPane.setText(xmlText.toString());
        xmlTextPane.setBackground(Color.WHITE);

        Cab2bPanel xmlPanel = new Cab2bPanel();
        xmlPanel.add(xmlTextPane);
        xmlPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().add(xmlPanel);

        Cab2bPanel xmlNavigationPanel = new Cab2bPanel();
        Cab2bButton exportButton = new Cab2bButton("Export");
        Cab2bButton cancelButton = new Cab2bButton("Cancel");

        // Action Listener for Export Button
        exportButton.addActionListener(new ExportButtonListner());

        // Action Listener for Cancel Button
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                dialog.dispose();
            }
        });

        Cab2bPanel buttonPanel = new Cab2bPanel();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.RIGHT);
        buttonPanel.setLayout(flowLayout);
        buttonPanel.add(exportButton);
        buttonPanel.add(cancelButton);
        buttonPanel.setBackground(new Color(240, 240, 240));

        xmlNavigationPanel.add("br left", messagePanel);
        xmlNavigationPanel.add("hfill", buttonPanel);
        xmlNavigationPanel.setPreferredSize(new Dimension(880, 50));
        xmlNavigationPanel.setBackground(new Color(240, 240, 240));

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(xmlNavigationPanel, BorderLayout.SOUTH);
    }

    //JDialog for showing DCQL XML Details Panel
    public JDialog showInDialog() {
        Dimension dimension = MainFrame.getScreenDimesion();
        dialog = WindowUtilities.setInDialog(NewWelcomePanel.getMainFrame(), this, "DCQL Xml", new Dimension(
                (int) (dimension.width * 0.77), (int) (dimension.height * 0.65)), true, false);
        dialog.setVisible(true);

        return dialog;
    }

    /*
     * @param File name,String to write to file It writes the text to the file
     * and saves it in given location
     */
    private boolean writeFile(File file, String dataString) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.print(dataString);
            out.flush();
            out.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    class ExportButtonListner implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue;
            // A call to JFileChooser's ShowSaveDialog PopUp
            returnValue = fileChooser.showSaveDialog(NewWelcomePanel.getMainFrame());
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                // Function call for writing the File and saving it
                boolean saveReturnValue = writeFile(file, xmlTextPane.getText());
                if (saveReturnValue == true) {
                    Cab2bLabel successResultLabel = new Cab2bLabel("File Saved Successfully");
                    successResultLabel.setForeground(Color.GREEN);
                    messagePanel.add(successResultLabel);
                    messagePanel.repaint();
                }
            }
        }
    }
}

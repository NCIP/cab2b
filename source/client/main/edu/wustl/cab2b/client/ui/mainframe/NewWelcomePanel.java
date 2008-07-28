package edu.wustl.cab2b.client.ui.mainframe;

import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bStandardFonts;
import edu.wustl.cab2b.client.ui.util.CommonUtils;

/**
 * Loads a html page from the local file system and shows it in the editor pane.
 * 
 * @author chetan_bh
 */
public class NewWelcomePanel extends Cab2bPanel {

    private static final long serialVersionUID = 1L;

    private JEditorPane welcomePageEditorPane;

    private static MainFrame mainFrame;

    public NewWelcomePanel(MainFrame newMainFrame) {
        mainFrame = newMainFrame;
        initGUI();
    }

    private void initGUI() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.WHITE);
        welcomePageEditorPane = createEditorPane();
        welcomePageEditorPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        welcomePageEditorPane.setFont(Cab2bStandardFonts.ARIAL_PLAIN_12);
        welcomePageEditorPane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    String className = e.getDescription();
                    if (className.equalsIgnoreCase("MainSearchPanel")) {
                        CommonUtils.launchSearchDataWizard();
                    } else {
                        JOptionPane.showMessageDialog(mainFrame,
                                                      "This link will take you to Analyze and Visualize Data screen.\nThis feature is not yet implemented.");
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(welcomePageEditorPane);
        scrollPane.setBorder(null);
        this.add(scrollPane);
    }

    private JEditorPane createEditorPane() {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        try {
            java.net.URL helpURL = this.getClass().getClassLoader().getResource("welcomepage.html");
            editorPane.setPage(helpURL);
        } catch (MalformedURLException malExp) {
            CommonUtils.handleException(malExp, mainFrame, true, true, false, false);
        } catch (IOException e) {
            CommonUtils.handleException(e, mainFrame, true, true, false, false);
        }
        return editorPane;
    }

    /**
     * @return the mainFrame
     */
    public static MainFrame getMainFrame() {
        return mainFrame;
    }

    /**
     * @param mainFrame the mainFrame to set
     */
    public static void setMainFrame(MainFrame mainFrame) {
        NewWelcomePanel.mainFrame = mainFrame;
    }
}
package edu.wustl.cab2b.client.ui.mainframe;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import edu.wustl.cab2b.client.ui.MainSearchPanel;
import edu.wustl.cab2b.client.ui.SaveDatalistPanel;
import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bStandardFonts;
import edu.wustl.cab2b.client.ui.util.CommonUtils;

/**
 * Loads a html page from the local file system and shows it in the editor pane.
 * 
 * @author chetan_bh
 */
public class NewWelcomePanel extends Cab2bPanel
{
	
	JEditorPane welcomePageEditorPane;
	public static MainFrame mainFrame;
	
	public NewWelcomePanel(MainFrame newMainFrame)
	{
		this.mainFrame = newMainFrame;
		initGUI();
	}
	
	private void initGUI()
	{
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(Color.WHITE);
		welcomePageEditorPane = createEditorPane();
		welcomePageEditorPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
		welcomePageEditorPane.setFont(Cab2bStandardFonts.ARIAL_PLAIN_12);
		welcomePageEditorPane.addHyperlinkListener(new HyperlinkListener()
		{
			public void hyperlinkUpdate(HyperlinkEvent e)
			{
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
				{
					String className = e.getDescription();
					if (className.equalsIgnoreCase("MainSearchPanel"))
					{
										
						GlobalNavigationPanel.mainSearchPanel = new MainSearchPanel();
						Dimension dimension = MainFrame.mainframeScreenDimesion;						
						//edu.wustl.cab2b.client.ui.util.CommonUtils.FrameReference = mainFrame;
						
						//reset DataList Save status
						 SaveDatalistPanel.isDataListSaved = false;
						 
						WindowUtilities.showInDialog(mainFrame, GlobalNavigationPanel.mainSearchPanel, "Search Data",
								new Dimension((int) (dimension.width * 0.90),
										(int) (dimension.height * 0.85)), true, true);					 
					}
					else
					{
						JOptionPane.showMessageDialog(mainFrame, "Yet to Implement");
					}
				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(welcomePageEditorPane);
		scrollPane.setBorder(null);
		this.add(scrollPane);
	}
	
	private JEditorPane createEditorPane()
	{
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);
		
		java.net.URL helpURL = null;
		
		try{
            helpURL = this.getClass().getClassLoader().getResource("welcomepage.html");
			editorPane.setPage(helpURL);
		}catch(MalformedURLException malExp)
		{
			CommonUtils.handleException(malExp, mainFrame, true, true, false, false);
		}catch (IOException e)
		{
			CommonUtils.handleException(e, mainFrame, true, true, false, false);
		}
		
		return editorPane;
	}
	
	public static void main(String[] args)
	{
		NewWelcomePanel welcomePanel = new NewWelcomePanel(new MainFrame());
		
		WindowUtilities.showInFrame(welcomePanel, "Welcome Panel");
	}
	
}

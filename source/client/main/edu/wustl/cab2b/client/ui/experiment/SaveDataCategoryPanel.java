/**
 * 
 */
package edu.wustl.cab2b.client.ui.experiment;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTextField;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;

/**
 * The panel to show dialog for saving a subset of the datalist as a category
 * @author Juber Patel
 *
 */
public class SaveDataCategoryPanel extends Cab2bPanel
{
	private ExperimentDataCategoryGridPanel gridPanel;
	
	private Cab2bLabel titleLabel;
	private Cab2bTextField titleField;
	private Cab2bButton cancelButton;
	private Cab2bButton saveButton;
	
	private JDialog dialog;
	
	/**
	 * 
	 * @param gridPanel the grid panel for which this dialog is shown
	 */
	public SaveDataCategoryPanel(ExperimentDataCategoryGridPanel gridPanel)
	{
		this.gridPanel = gridPanel; 
		initGUI();
		initEventModel();
		showAsDialog();
	}
	
	private void initGUI()
	{
		titleLabel = new Cab2bLabel("Title");
		titleField = new Cab2bTextField();
		cancelButton = new Cab2bButton("Cancel");
		saveButton = new Cab2bButton("Save");
		
		add(titleLabel);
		add("tab hfill",titleField);
		add("br right",cancelButton);
		add(saveButton);
		
	}
	
	private void initEventModel()
	{
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				dialog.dispose();
			}
			
		});
		
		saveButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				String title = titleField.getText();
				
				if(!CommonUtils.isNameValid(title))
				{
					//TO DO
					//invalid title
				}
                           				
				gridPanel.saveDataCategory(title);
				dialog.dispose();
			}
			
		});
		
	}
	
	
	private JDialog showAsDialog()
	{
        Dimension mainDimension = MainFrame.mainframeScreenDimesion;
        Dimension dialogDimension = new Dimension((int)(mainDimension.width * 0.35), (int)(mainDimension.height * 0.15)); 
        
        dialog = WindowUtilities.setInDialog(NewWelcomePanel.mainFrame, this, "Save As Data Category", dialogDimension, true, false);
        dialog.setVisible(true);
        
        return dialog;
    }
	
	
	
	

}

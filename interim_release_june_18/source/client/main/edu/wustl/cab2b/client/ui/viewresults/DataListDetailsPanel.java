package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.table.TableModel;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bFileFilter;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.client.ui.controls.CustomizableBorder;


public class DataListDetailsPanel extends Cab2bPanel implements ActionListener
{
    
    /**
     * Table to show the data.
     */
	Cab2bTable table;
    Cab2bButton m_exportButton;
    /**
     * Default constructor.
     */
    public DataListDetailsPanel()
    {
    }
    
    /**
     * Default constructor.
     */
    public DataListDetailsPanel(Object[] attributes, Object[][] dataRows)
    {
        initGUI(attributes, dataRows);
    }
    
    /**
     * Initializes the UI.
     */
    public void initGUI(Object[] attributes, Object[][] dataRows)
    {
        this.setLayout(new RiverLayout());
        table = new Cab2bTable(true, dataRows, attributes);
        this.add("br hfill vfill",new JScrollPane(table));
        m_exportButton = new Cab2bButton("Export");
        this.add("br", this.m_exportButton);
        m_exportButton.addActionListener(this);
        this.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));
        
    }
    /**
     * Method to perform Button click events
     */
	public void actionPerformed(ActionEvent event) 
	{
		if(event.getSource() == m_exportButton)
		{
			// Perform export operation
			performExportOperation();
		}
	}
	/**
	 * Method to perform export operation on data list
	 * It prompt user for specifying file name and
	 * then saves selected rows into it in the from of .csv
	 */
	private void performExportOperation()
	{
		int[] selectedIndexes = table.getSelectedRows();
		if(0 == selectedIndexes.length)
		{
			JOptionPane.showMessageDialog(this, "No data set is selected for export", "Data List Export Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		boolean done = false;
		do{
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new Cab2bFileFilter(new String[]{"csv"}));
			int status = fileChooser.showSaveDialog(this);
			File selFile  = fileChooser.getSelectedFile();
			String fileName = selFile.getAbsolutePath();
			if(JFileChooser.APPROVE_OPTION  == status)
			{
				if(true == selFile.exists())
				{
					// Prompt user to confirm if he wants to override the value 
					int confirmationValue = JOptionPane.showConfirmDialog(fileChooser, "The file " + selFile.getName() 
						+ " already exists.\nDo you want to replace existing file?", "caB2B Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if(confirmationValue == JOptionPane.NO_OPTION)
						continue;
				}
				else
				{
					if(false == fileName.endsWith(".csv"))
					{
						fileName = fileName + ".csv";
					}
				}
				try 
				{
					// Save data to selected file
					TableModel model = table.getModel();
					int totalColumns = table.getColumnCount();
					FileWriter fstream = new FileWriter(fileName);
					BufferedWriter out = new BufferedWriter(fstream);
					StringBuffer sb = new StringBuffer();
					for(int j=1; j<totalColumns; j++)
					{
						if(j != 1)
						{
							sb.append(",");
						}
						// If special character in the column name
						// put it into double quotes
						String text = model.getColumnName(j);
						if(-1 != text.indexOf(","))
						{
							text = "\"" + text + "\"";
						}
						sb.append(text);
					}
					sb.append("\n");
					out.write(sb.toString());
					sb.setLength(0);
					/**
					 * Write the actual column values to file
					 */
					for(int i=0; i<selectedIndexes.length; i++)
					{
						for(int j=1; j<totalColumns; j++)
						{
							Object object = table.getValueAt(selectedIndexes[i], j);
							if(j != 1)
							{
								sb.append(",");
							}
							if(object == null)
							{
								sb.append("");
							}
							else
							{
								// If special character in the column value
								// put it into double quotes
								String text = object.toString();
								if(-1 != text.indexOf(","))
								{
									text = "\"" + text + "\"";
								}
								sb.append(text);
							}
						}
						sb.append("\n");
						out.write(sb.toString());
						sb.setLength(0);
					}
					out.close();
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			done = true;
		}while(!done);

	}
}
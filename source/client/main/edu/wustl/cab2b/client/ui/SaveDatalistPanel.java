/**

 * 

 */

package edu.wustl.cab2b.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTextField;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.CustomSwingWorker;
import edu.wustl.cab2b.common.datalist.DataListBusinessInterface;
import edu.wustl.cab2b.common.datalist.DataListHome;
import edu.wustl.cab2b.common.domain.DataListMetadata;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author deepak_shingan
 *
 */

public class SaveDatalistPanel extends Cab2bPanel
{

	private Cab2bLabel titleLabel;
	private Cab2bLabel descLabel;
	private Cab2bTextField txtTitle;
	private JTextArea txtDesc;
	private Cab2bButton cancelButton;
	private Cab2bButton saveButton;
	private Cab2bPanel centerPanel;
	private Cab2bPanel bottomPanel;

	private JScrollPane m_srollPane;

	JDialog dialog;
	MainSearchPanel mainSearchPanel;
	
	//flag used to indicate status of data list  
	public static boolean isDataListSaved = false;

	SaveDatalistPanel(MainSearchPanel mainSearchpanel)
	{
		this.mainSearchPanel = mainSearchpanel;
		initGUIWithGBL();
	}

	private void initGUIWithGBL()
	{
		titleLabel = new Cab2bLabel("Title");
		descLabel = new Cab2bLabel("Description");
		txtTitle = new Cab2bTextField();

		txtDesc = new JTextArea();
		m_srollPane = new JScrollPane(txtDesc);

		cancelButton = new Cab2bButton("Cancel");
		cancelButton.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent arg0)
			{
				Logger.out.info("disposing save data list dialog... ");
				dialog.dispose();
			}
		});

		saveButton = new Cab2bButton("Save");
		saveButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				isDataListSaved = true;
				//dialog.dispose();

				String dataListName = txtTitle.getText();
				if (dataListName == null || dataListName.equals(""))
				{
					dataListName = "" + System.currentTimeMillis();
				}
				String dataListDesc = txtDesc.getText();

				DataListMetadata dataListAnnotation = new DataListMetadata();
				dataListAnnotation.setName(dataListName);
				dataListAnnotation.setDescription(dataListDesc);
				dataListAnnotation.setCreatedOn(new Date());
				dataListAnnotation.setLastUpdatedOn(new Date());

				MainSearchPanel.getDataList().setDataListAnnotation(dataListAnnotation);

				
				CustomSwingWorker sw = new CustomSwingWorker(SaveDatalistPanel.this)
				{
					Long id;
					
					@Override
					protected void doNonUILogic() throws RuntimeException
					{
						DataListBusinessInterface dataListBI = (DataListBusinessInterface) CommonUtils
						.getBusinessInterface(EjbNamesConstants.DATALIST_BEAN, DataListHome.class,
								SaveDatalistPanel.this);
						
						try
						{
							id = dataListBI.saveDataList(MainSearchPanel.getDataList());
							
							MainSearchPanel.savedDataListMetadata = dataListBI.retrieveDataListMetadata(id);
							
							Logger.out.info("data list saved successfully (in entity with id) : "+id);
							
						}
						catch (RemoteException e)
						{
							CommonUtils
									.handleException(e, SaveDatalistPanel.this, true, true, false, false);
						}
						finally
						{
							dialog.dispose();
						}
					}

					@Override
					protected void doUIUpdateLogic() throws RuntimeException
					{
						dialog.dispose();
						if(id > 0)
							JOptionPane.showMessageDialog(mainSearchPanel, "Data List saved successfully !");
						else
							Logger.out.debug("data list not saved ! "+id);
						Logger.out.info("datalist id : "+MainSearchPanel.getDataList().getDataListAnnotation().getId());
						Logger.out.info("entity id for dl : "+MainSearchPanel.getDataList().getDataListAnnotation().getEntityId());
					}
					
				};
				sw.start();

				// ---------- Remove this code (Used to serialize datalist)-------
				//try{
				//	File dataListFile = new File("dataList.ser");
				//	FileOutputStream fos = new FileOutputStream(dataListFile);
				//	ObjectOutputStream oos = new ObjectOutputStream(fos);
				//	// serialize the data list.
				//	oos.writeObject(MainSearchPanel.dataList);
				//}catch(Exception exp)
				//{
				//	exp.printStackTrace();
				//}
				// --------------------------------------------------------------- 
			}

		});

		GridBagLayout gbl = new GridBagLayout();

		centerPanel = new Cab2bPanel();
		centerPanel.setLayout(gbl);

		this.setLayout(new BorderLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.insets = new Insets(5, 5, 5, 5);
		centerPanel.add(titleLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 4;
		gbc.gridheight = 1;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		centerPanel.add(txtTitle, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;

		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.NONE;
		centerPanel.add(descLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 4;
		gbc.gridheight = 3;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		centerPanel.add(m_srollPane, gbc);

		bottomPanel = new Cab2bPanel();
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		flowLayout.setHgap(10);
		bottomPanel.setLayout(flowLayout);
		bottomPanel.add(cancelButton);
		bottomPanel.add(saveButton);

		this.add(centerPanel, BorderLayout.CENTER);
		this.add(bottomPanel, BorderLayout.SOUTH);
	}

	public JDialog showInDialog()
	{
		Dimension dimension = MainFrame.mainframeScreenDimesion;
		dialog = WindowUtilities.setInDialog(NewWelcomePanel.mainFrame, this, "Save Data List",
				new Dimension((int) (dimension.width * 0.35), (int) (dimension.height * 0.30)),
				true, false);

		Logger.out.info("dialog initialized ########## " + dialog);
		dialog.setVisible(true);
		return dialog;
	}

	public static void main(String[] args)
	{
		SaveDatalistPanel obj = new SaveDatalistPanel(null);

		JFrame frame = new JFrame("Experiment");
		frame.setSize(550, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(obj);
		frame.setVisible(true);
	}
}

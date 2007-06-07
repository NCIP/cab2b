
package edu.wustl.cab2b.client.ui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.VerticalLayout;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;

/**
 * A panel which lists all the available LookAndFeel and highlights the
 * current LookAndFeel. By selecting any one of the availabel L&F the user 
 * can change the L&F of the current application instantly.
 * 
 * @author chetan_bh
 */

public class LookAndFeelSelectionPanel extends Cab2bTitledPanel
{

	/**
	 * Frame whose look and feel needs to be changed.
	 */
	public static JFrame frame;

	public static String className;

	/**
	 * List of standard Look and Feels. 
	 */
	String[] lookAndFeels = {"com.sun.java.swing.plaf.gtk.GTKLookAndFeel",
			"javax.swing.plaf.metal.MetalLookAndFeel",
			"com.sun.java.swing.plaf.windows.WindowsLookAndFeel",
			"com.sun.java.swing.plaf.motif.MotifLookAndFeel",
			"com.sun.java.swing.plaf.mac.MacLookAndFeel"};

	public LookAndFeelSelectionPanel(JFrame newFrame)
	{
		super("Available Look & Feel");
		frame = newFrame;
		className = UIManager.getLookAndFeel().getClass().getName();
		initGUI();
	}

	/**
	 * Function to find and construct a available L&F's List.
	 *
	 */
	private void initGUI()
	{
		JXPanel parentPanel = new Cab2bPanel();
		parentPanel.setLayout(new VerticalLayout());
		this.add(parentPanel);

		LookAndFeelInfo[] lAndF = UIManager.getInstalledLookAndFeels();
		//Logger.out.debug("Installed L&F ::");
		ButtonGroup group = new ButtonGroup();

		for (int i = 0; i < lAndF.length; i++)
		{
			String lAndFName = lAndF[i].getName();
			String lAndFClassName = lAndF[i].getClassName();
			//Logger.out.debug(lAndFName+" fullClassName "+lAndF[i].getClassName());
			JRadioButton button = new JRadioButton(lAndFName);
			if (className.equals(lAndFClassName))
			{
				button.setSelected(true);
			}
			button.setActionCommand(lAndFClassName);
			button.addItemListener(new ItemListener()
			{

				public void itemStateChanged(ItemEvent arg0)
				{
					if (arg0.getStateChange() == 1)
					{
						String className = ((JRadioButton) arg0.getSource()).getActionCommand();
						//Logger.out.debug("className "+className);
						try
						{
							UIManager.setLookAndFeel(className);
							updateUITree();
						}
						catch (ClassNotFoundException e)
						{
							e.printStackTrace();
						}
						catch (InstantiationException e)
						{
							e.printStackTrace();
						}
						catch (IllegalAccessException e)
						{
							e.printStackTrace();
						}
						catch (UnsupportedLookAndFeelException e)
						{
							e.printStackTrace();
						}
					}
				}
			});
			parentPanel.add(button);
			group.add(button);
		}
	}

	/**
	 * update the component tree of the frame, after changing the L&F.
	 */
	private void updateUITree()
	{
		SwingUtilities.updateComponentTreeUI(frame);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		//JOptionPane.showOptionDialog(null,lAndFSelctionPanel,"Look and Feel Selection",1,2,null,null,null);
		JFrame frame = new JFrame("Look and Feel Frame");
		LookAndFeelSelectionPanel lAndFSelctionPanel = new LookAndFeelSelectionPanel(frame);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.getContentPane().add(lAndFSelctionPanel);
		frame.setVisible(true);

	}

}

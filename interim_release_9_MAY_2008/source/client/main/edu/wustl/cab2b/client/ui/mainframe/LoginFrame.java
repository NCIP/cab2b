package edu.wustl.cab2b.client.ui.mainframe;

import static edu.wustl.cab2b.client.ui.util.ClientConstants.APPLICATION_RESOURCES_FILE_NAME;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.CAB2B_LOGO_IMAGE;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.ERROR_CODE_FILE_NAME;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.MissingResourceException;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.border.Border;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.painter.MattePainter;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bCheckBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bComboBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTextField;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.CustomSwingWorker;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeHandler;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * @author Chandrakant Talele
 * @author Hrishikesh Rajpathak
 */
public class LoginFrame extends JXFrame {

	private static final long serialVersionUID = 1L;

	private static final Border border = BorderFactory
			.createLineBorder(new Color(100, 200, 220));

	private Cab2bHyperlink forgotPass;

	private Cab2bTextField serverText;

	private Cab2bTextField port;

	private Cab2bComboBox idProvider;

	private Cab2bTextField usrNameText;

	private JPasswordField passText;

	private Cab2bCheckBox remember;

	private Cab2bButton login;

	public LoginFrame selfReference = this;

	public LoginFrame() {
		super("Login - ca Bench To Bedside (B2B)");
		initUI();
	}

	private Font getTextFont() {
		Cab2bLabel label = new Cab2bLabel(":");
		return new Font(label.getFont().getName(), Font.BOLD, label.getFont()
				.getSize() - 1);
	}

	public void initUI() {
		Font font = getTextFont();

		Cab2bTitledPanel titledPanel = new Cab2bTitledPanel(
				"ca Bench To Bedside (B2B)");
		titledPanel.setTitleFont(new Font(font.getName(), Font.BOLD, font
				.getSize() + 6));
		titledPanel.setTitleForeground(Color.WHITE);
		this.add(titledPanel);

		Color color1 = new Color(60, 164, 217);
		Color color2 = new Color(60, 99, 152);
		MattePainter painter = new MattePainter(new GradientPaint(0f, 0f,
				color1, 0f, 25f, color2));
		titledPanel.setTitlePainter(painter);

		usrNameText = getTextField("");
		usrNameText.setPreferredSize(new Dimension(160, 20));

		passText = new JPasswordField();
		passText.setEchoChar('*');
		passText.setBorder(border);
		passText.setPreferredSize(new Dimension(160, 20));

		forgotPass = new Cab2bHyperlink(true);
		forgotPass.setText("Forgot Password?");
		forgotPass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});

		forgotPass.setEnabled(false);

		// currently no need
		forgotPass.setVisible(false);

		serverText = getTextField("localhost");
		remember = new Cab2bCheckBox();

		idProvider = new Cab2bComboBox();
		idProvider.setBorder(border);
		idProvider.addItem("Training");
		idProvider.addItem("Production");

		port = getTextField("1099");

		login = new Cab2bButton("Login");
		login.addActionListener(new LoginButtonListener());

		Cab2bPanel panel = new Cab2bPanel();
		panel.setLayout(new RiverLayout(10, 5));

		Cab2bLabel userNameLabel = new Cab2bLabel("User Name :", font);
		addInRow(panel, userNameLabel, usrNameText);

		Cab2bLabel passWordLabel = new Cab2bLabel("Password :", font);
		addInRow(panel, passWordLabel, passText);

		panel.add("tab ", forgotPass);

		Cab2bLabel serverLabel = new Cab2bLabel("caB2B Server :", font);
		// addInRow(panel, serverLabel, serverText);

		// Cab2bLabel portLabel = new Cab2bLabel("Port:", font);
		// panel.add(portLabel);
		// panel.add(port);

		Cab2bLabel idProviderLabel = new Cab2bLabel("ID Provider :", font);
		addInRow(panel, idProviderLabel, idProvider);

		// addInRow(panel, new JLabel(), remember);
		// panel.add(new JLabel("Remember Me"));

		addInRow(panel, new JLabel(), login);

		titledPanel.add(panel);
		titledPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		URL url = this.getClass().getClassLoader()
				.getResource(CAB2B_LOGO_IMAGE);
		Image im = Toolkit.getDefaultToolkit().getImage(url);
		this.setIconImage(im);

		Dimension labelSize = new Dimension(390, 225);
		this.setMinimumSize(labelSize);
		this.setPreferredSize(labelSize);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screenSize.width / 2 - (labelSize.width / 2),
				screenSize.height / 2 - (labelSize.height / 2));
		this.setSize(380, 220);

		this.setUndecorated(true);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JXFrame.EXIT_ON_CLOSE);
	}

	private void addInRow(Cab2bPanel panel, JLabel label, JComponent field) {
		panel.add("br tab", label);
		panel.add("tab", field);
	}

	private Cab2bTextField getTextField(String text) {
		Cab2bTextField textField = new Cab2bTextField();
		textField.setBorder(border);
		textField.setText(text);
		return textField;
	}

	public static void main(String[] args) {
		Logger.configure();
		setHome();
		Logger.configure(); // pick config from log4j.properties
		initializeResources(); // Initialize all Resources

		LoginFrame loginFrame = new LoginFrame();
		loginFrame.setVisible(true);
	}

	public static void setHome() {
		String userHome = System.getProperty("user.home");

		File cab2bHome = new File(userHome, "cab2b");
		System.setProperty("cab2b.home", cab2bHome.getAbsolutePath());
	}

	protected static void initializeResources() {
		try {
			ErrorCodeHandler.initBundle(ERROR_CODE_FILE_NAME);
			ApplicationProperties.initBundle(APPLICATION_RESOURCES_FILE_NAME);
		} catch (MissingResourceException mre) {
			CheckedException checkedException = new CheckedException(mre
					.getMessage(), mre, ErrorCodeConstants.IO_0002);
			CommonUtils.handleException(checkedException, null, true, true,
					false, true);
		}
	}

	/**
	 * This method checks if the user trying to login is a valid grid user or
	 * not
	 * 
	 * @param userName
	 * @param password
	 * @param idProvider
	 * @return boolean stating is the user is a valid grid user or not
	 * @throws MalformedURIException
	 * @throws RemoteException
	 * @throws AuthenticationProviderFault
	 * @throws InsufficientAttributeFault
	 * @throws InvalidCredentialFault
	 */
	final private boolean validateCredentials(String userName, String password,
			String idProvider) {
		return UserValidator.validateUser(userName, password, idProvider);
	}

	private class LoginButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			CustomSwingWorker swingWorker = new CustomSwingWorker(
					LoginFrame.this) {

				@Override
				protected void doNonUILogic() {
					String serverIP = serverText.getText();
					String jndiPort = port.getText();
					final String userName = usrNameText.getText();
					char[] passwordArray = passText.getPassword();
					String password = new String(passwordArray);
					String IDProvider = idProvider.getSelectedItem().toString();

					String url = "jnp://" + serverIP + ":" + jndiPort;
					System.setProperty("java.naming.provider.url", url);
					System.setProperty("java.naming.factory.initial",
							"org.jnp.interfaces.NamingContextFactory");
					System.setProperty("java.naming.factory.url.pkgs",
							"org.jboss.naming:org.jnp.interfaces");

					if (validateCredentials(userName, password, IDProvider)) {
						Thread mainThread = new Thread() {
							public void run() {
								MainFrame.main(new String[]{userName});
							}
						};
						mainThread.setPriority(Thread.NORM_PRIORITY);
						mainThread.start();
					} else {
						showError();
					}

					selfReference.dispose();
				}

				protected void showError() {
					JOptionPane.showMessageDialog(LoginFrame.this,
							"Please check the credentials!");
				}

				@Override
				protected void doUIUpdateLogic() throws Exception {
				}
			};
			swingWorker.start();
		}
	}
}
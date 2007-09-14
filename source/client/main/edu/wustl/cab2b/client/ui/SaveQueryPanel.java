/**
 * 
 */
package edu.wustl.cab2b.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTextField;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.CustomSwingWorker;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineHome;
import edu.wustl.cab2b.common.queryengine.ICab2bParameterizedQuery;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.common.util.logger.Logger;

/**
 * @author chetan_patil
 *
 */
public class SaveQueryPanel extends Cab2bPanel {
    private static final long serialVersionUID = 1L;

    private Cab2bTextField queryNameText;

    private JTextArea queryDescriptionText;

    private Cab2bButton cancelButton;

    private Cab2bButton saveButton;

    private JDialog dialog;

    private MainSearchPanel mainSearchPanel;

    public SaveQueryPanel(MainSearchPanel mainSearchpanel) {
        this.mainSearchPanel = mainSearchpanel;
        initializeGUI();
    }

    private void initializeGUI() {
        Cab2bLabel queryNameLabel = new Cab2bLabel("Query Name:");
        queryNameText = new Cab2bTextField();
        queryNameText.setColumns(32);

        Cab2bLabel queryDescriptionLabel = new Cab2bLabel("Description:");
        queryDescriptionText = new JTextArea(10, 32);
        JScrollPane jScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.getViewport().add(queryDescriptionText);

        cancelButton = new Cab2bButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Logger.out.debug("disposing save data list dialog... ");
                dialog.dispose();
            }
        });

        saveButton = new Cab2bButton("Save");
        saveButton.addActionListener(new SaveButtonListener());

        Cab2bPanel centerPanel = new Cab2bPanel(new RiverLayout(5, 5));
        centerPanel.add(queryNameLabel);
        centerPanel.add("tab ", queryNameText);
        centerPanel.add("br ", queryDescriptionLabel);
        centerPanel.add("tab ", jScrollPane);

        Cab2bPanel bottomPanel = new Cab2bPanel();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        flowLayout.setHgap(10);
        bottomPanel.setLayout(flowLayout);
        bottomPanel.add(saveButton);
        bottomPanel.add(cancelButton);

        this.setLayout(new BorderLayout());
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    public JDialog showInDialog() {
        Dimension dimension = MainFrame.mainframeScreenDimesion;

        dialog = WindowUtilities.setInDialog(NewWelcomePanel.mainFrame, this, "Save Query", new Dimension(
                (int) (dimension.width * 0.35), (int) (dimension.height * 0.30)), true, false);
        dialog.setVisible(true);

        return dialog;
    }

    private class SaveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            CustomSwingWorker customSwingWorker = new CustomSwingWorker(SaveQueryPanel.this) {
                boolean isQuerySaved = false;

                @Override
                protected void doNonUILogic() throws RuntimeException {
                    String queryName = queryNameText.getText();
                    if (queryName == null || queryName.equals("")) {
                        queryName = "" + new Date();
                    }
                    String queryDescription = queryDescriptionText.getText();

                    final IClientQueryBuilderInterface clientQueryBuilder = mainSearchPanel.getQueryObject();
                    ICab2bParameterizedQuery cab2bQuery = (ICab2bParameterizedQuery) clientQueryBuilder.getQuery();
                    cab2bQuery.setName(queryName);
                    cab2bQuery.setDescription(queryDescription);

                    QueryEngineBusinessInterface queryEngineBusinessInterface = (QueryEngineBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                                                EjbNamesConstants.QUERY_ENGINE_BEAN,
                                                                                                                                                QueryEngineHome.class,
                                                                                                                                                SaveQueryPanel.this);
                    try {
                        queryEngineBusinessInterface.saveQuery(cab2bQuery);
                        isQuerySaved = true;
                    } catch (Exception exception) {
                        CommonUtils.handleException(exception, MainFrame.newWelcomePanel, true, true, true, false);
                    } finally {
                        dialog.dispose();
                    }
                }

                @Override
                protected void doUIUpdateLogic() throws RuntimeException {
                    if (isQuerySaved) {
                        MainFrame.updateMySeachQueryBox();
                        SearchNavigationPanel.messageLabel.setText("Query saved successfully.");
                        updateUI();
                    }
                }
            };
            customSwingWorker.start();
        }
    }
}

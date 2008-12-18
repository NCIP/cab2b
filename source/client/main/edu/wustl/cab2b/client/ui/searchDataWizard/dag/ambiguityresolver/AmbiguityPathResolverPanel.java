package edu.wustl.cab2b.client.ui.searchDataWizard.dag.ambiguityresolver;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JSplitPane;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXPanel;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.cache.ClientSideCache;
import edu.wustl.cab2b.client.metadatasearch.MetadataSearch;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.RiverLayout;
import edu.wustl.cab2b.common.beans.MatchedClass;
import edu.wustl.cab2b.common.cache.IEntityCache;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.util.Utility;

/**
 * A searchPanel which displays list of available paths for the current source, target entity, 
 * and a diagrammatic preview searchPanel which displays the diagrammatic view of the query 
 * being constructed.
 * 
 * @author chetan_bh
 */
public class AmbiguityPathResolverPanel extends Cab2bPanel {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(AmbiguityPathResolverPanel.class);
    private static final long serialVersionUID = 1L;

    /**
     * A vertical split pane, where upper part is for available paths,
     * and lower part to show the diagramatic preview of the query.
     */
    private JSplitPane splitPanel = null;

    /**
     * @see AvailablePathsPanel
     */
    private AvailablePathsPanel availablePathsPanel;

    /**
     * @see DiagrammaticPreviewPanel
     */
    private DiagrammaticPreviewPanel diagrammaticPreviewPanel;

    private Cab2bButton doneButton;

    private Cab2bButton cancelButton;

    /**
     * Collections of source-target entity interfaces, where there are multiple paths,
     * which needs to manually resolved by the user. 
     */
    public AmbiguityPathResolverPanel(Vector<Vector<EntityInterface>> sourceTargetCollection) {
        initGUI();
    }

    private void initGUI() {
        this.setLayout(new RiverLayout());
        splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPanel.setOneTouchExpandable(false);
        splitPanel.setDividerLocation(400);
        splitPanel.setDividerSize(4);
        availablePathsPanel = new AvailablePathsPanel(null);
        diagrammaticPreviewPanel = new DiagrammaticPreviewPanel();
        splitPanel.add(availablePathsPanel);
        splitPanel.add(diagrammaticPreviewPanel);

        this.add("hfill vfill", splitPanel);

        // TODO this buttons may not be needed becase a standard dialog in which this
        // searchPanel is put will be having "Ok" and "Cancel" buttons.
        doneButton = new Cab2bButton("Done");
        cancelButton = new Cab2bButton("Cancel");

        JXPanel buttonsPanel = new Cab2bPanel(new RiverLayout(5, 5));
        buttonsPanel.add(doneButton);
        buttonsPanel.add(cancelButton);

        this.add("br", buttonsPanel);
    }

    public static String getFullPathNames(IPath path) {
        StringBuffer fullPathNames = new StringBuffer();

        List<IAssociation> associationList = path.getIntermediateAssociations();
        boolean isFirstAssociation = false;
        for (IAssociation association : associationList) {
            if (!isFirstAssociation) {
                isFirstAssociation = true;

                EntityInterface srourceEntity = association.getSourceEntity();
                String srourceEntityName = Utility.parseClassName(srourceEntity.getName());

                EntityInterface targetEntity = association.getTargetEntity();
                String targetEntityName = Utility.parseClassName(targetEntity.getName());

                fullPathNames.append(srourceEntityName).append(">>").append(targetEntityName);
            } else {
                EntityInterface targetEntity = association.getTargetEntity();
                String targetEntityName = Utility.parseClassName(targetEntity.getName());

                fullPathNames.append(">>").append(targetEntityName);
            }
        }
        return fullPathNames.toString();
    }

    public static Vector<EntityInterface> getEntityInterfaceFor(String[] searchTerms) {
        logger.debug("searchTerms " + searchTerms);

        Vector<EntityInterface> returner = new Vector<EntityInterface>();
        int[] searchTarget = new int[1];
        searchTarget[0] = Constants.CLASS;

        for (int i = 0; i < searchTerms.length; i++) {
            String[] searchString = new String[1];
            searchString[0] = searchTerms[i];
            logger.debug("searchTerms ==>> " + searchTerms[i]);
            MatchedClass matchedClass = null;
            try {
                IEntityCache cache = ClientSideCache.getInstance();
                MetadataSearch metadataSearch = new MetadataSearch(cache);
                matchedClass = metadataSearch.search(searchTarget, searchString, Constants.BASED_ON_TEXT);
            } catch (CheckedException re) {
                re.printStackTrace();
            }

            logger.debug("matched Class entity collection size =>> "
                    + matchedClass.getEntityCollection().size());

            Set entityCollection = matchedClass.getEntityCollection();
            EntityInterface entityInter;
            if (entityCollection.size() > 0) {
                entityInter = (EntityInterface) entityCollection.iterator().next();
                logger.debug(entityInter.getName() + ", " + entityInter.getDescription() + ", "
                        + entityInter.getClass());
                returner.add(entityInter);
            }
        }
        return returner;
    }
}
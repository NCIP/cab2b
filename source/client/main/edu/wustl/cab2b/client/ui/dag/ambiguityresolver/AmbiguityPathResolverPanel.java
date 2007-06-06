package edu.wustl.cab2b.client.ui.dag.ambiguityresolver;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import org.jdesktop.swingx.JXPanel;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.cache.ClientSideCache;
import edu.wustl.cab2b.client.metadatasearch.MetadataSearch;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.common.beans.MatchedClass;
import edu.wustl.cab2b.common.cache.IEntityCache;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * A panel which displays list of available paths for the current source, target entity, 
 * and a diagrammatic preview panel which displays the diagrammatic view of the query 
 * being constructed.
 * 
 * @author chetan_bh
 */
public class AmbiguityPathResolverPanel extends Cab2bPanel {

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
        availablePathsPanel = new AvailablePathsPanel(null, null);
        diagrammaticPreviewPanel = new DiagrammaticPreviewPanel();
        splitPanel.add(availablePathsPanel);
        splitPanel.add(diagrammaticPreviewPanel);

        this.add("hfill vfill", splitPanel);

        // TODO this buttons may not be needed becase a standard dialog in which this
        // panel is put will be having "Ok" and "Cancel" buttons.
        doneButton = new Cab2bButton("Done");
        cancelButton = new Cab2bButton("Cencel");

        JXPanel buttonsPanel = new Cab2bPanel(new RiverLayout(5, 5));
        buttonsPanel.add(doneButton);
        buttonsPanel.add(cancelButton);

        this.add("br", buttonsPanel);
    }

    public static String getFullPathNames(IPath path) {
        String returner = "";

        List<IAssociation> assoList = path.getIntermediateAssociations();
        Iterator<IAssociation> listIterator = assoList.listIterator();

        boolean firstAssoOver = false;
        while (listIterator.hasNext()) {
            IAssociation asso = listIterator.next();
            if (!firstAssoOver) {
                EntityInterface srcEntity = asso.getSourceEntity();
                EntityInterface tarEntity = asso.getTargetEntity();
                //System.out.println("src " + srcEntity.getName());
                //System.out.println(tarEntity.getName());
                String srcEntityName = Utility.parseClassName(srcEntity.getName());
                String tarEntityName = Utility.parseClassName(tarEntity.getName());

                firstAssoOver = true;

                returner += srcEntityName + "->" + tarEntityName;
            } else {
                EntityInterface tarEntity = asso.getTargetEntity();
                String tarEntityName = Utility.parseClassName(tarEntity.getName());

                returner += "->" + tarEntityName;
            }
        }
        return returner;
    }

    public static Vector<EntityInterface> getEntityInterfaceFor(String[] searchTerms) {
        Logger.out.debug("searchTerms " + searchTerms);

        Vector<EntityInterface> returner = new Vector<EntityInterface>();
        int[] searchTarget = new int[1];
        searchTarget[0] = Constants.CLASS;

        for (int i = 0; i < searchTerms.length; i++) {
            String[] searchString = new String[1];
            searchString[0] = searchTerms[i];
            Logger.out.debug("searchTerms ==>> " + searchTerms[i]);
            MatchedClass matchedClass = null;
            try {
                IEntityCache cache = ClientSideCache.getInstance();
                MetadataSearch metadataSearch = new MetadataSearch(cache);
                matchedClass = metadataSearch.search(searchTarget, searchString, Constants.BASED_ON_TEXT);
            } catch (CheckedException re) {
                re.printStackTrace();
            }

            Logger.out.info("matched Class entity collection size =>> "
                    + matchedClass.getEntityCollection().size());

            Set entityCollection = matchedClass.getEntityCollection();
            EntityInterface entityInter;
            if (entityCollection.size() > 0) {
                entityInter = (EntityInterface) entityCollection.iterator().next();
                Logger.out.info(entityInter.getName() + ", " + entityInter.getDescription() + ", "
                        + entityInter.getClass());
                returner.add(entityInter);
            }
        }
        return returner;
    }

    public Map getUserSelectedpaths() {
        //return availablePathsPanel.getUserSelectedpaths();
        return null;
    }

    public static void main(String[] args) {
        Logger.configure("log4j.properties");

        String[] searchTerms = { "ProbeSet", "MicroArray", "OMIM", "Gene" };
        Vector<EntityInterface> entIntVector = getEntityInterfaceFor(searchTerms);

        Vector<Vector<EntityInterface>> srcTsrColl = new Vector<Vector<EntityInterface>>();

        Vector<EntityInterface> srcTar1 = new Vector<EntityInterface>();
        srcTar1.add(entIntVector.get(0));
        srcTar1.add(entIntVector.get(2));

        Vector<EntityInterface> srcTar2 = new Vector<EntityInterface>();
        srcTar2.add(entIntVector.get(1));
        srcTar2.add(entIntVector.get(3));

        srcTsrColl.add(srcTar2);
        srcTsrColl.add(srcTar1);

        AmbiguityPathResolverPanel ambiguityPathResolverPanel = new AmbiguityPathResolverPanel(srcTsrColl);
        JOptionPane.showMessageDialog(null, ambiguityPathResolverPanel);
        Logger.out.info("user selected ambigious paths #####>>>>> "
                + ambiguityPathResolverPanel.getUserSelectedpaths());

    }

}

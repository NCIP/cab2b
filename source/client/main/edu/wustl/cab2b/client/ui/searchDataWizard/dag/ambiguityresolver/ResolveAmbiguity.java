package edu.wustl.cab2b.client.ui.searchDataWizard.dag.ambiguityresolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.query.IPathFinder;
import edu.wustl.cab2b.client.ui.util.WindowUtilities;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.common.querysuite.metadata.path.ICuratedPath;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.util.logger.Logger;

/**
 * Class to resolve mabiguity between two categories
 * 
 * @author Administrator
 * 
 */
public class ResolveAmbiguity {

    /**
     * Ambiguity objects available
     */
    private Vector<AmbiguityObject> ambiguityObjectsVector;

    /**
     * Map of ambiguty objects to paths
     */
    private Map<AmbiguityObject, List<IPath>> ambiguityObjectToPathsMap = new HashMap<AmbiguityObject, List<IPath>>();

    /**
     * pathfinder object
     */
    private IPathFinder iPathFinder;

    /**
     * Constructor
     * @param ambiguityObjects
     * @param pathFinder
     */
    public ResolveAmbiguity(Vector<AmbiguityObject> ambiguityObjects, IPathFinder pathFinder) {
        ambiguityObjectsVector = ambiguityObjects;
        iPathFinder = pathFinder;
    }

    /**
     * Constructor
     * @param ambiguityObject
     * @param pathFinder
     */
    public ResolveAmbiguity(AmbiguityObject ambiguityObject, IPathFinder pathFinder) {
        ambiguityObjectsVector = new Vector<AmbiguityObject>();
        ambiguityObjectsVector.add(ambiguityObject);
        iPathFinder = pathFinder;
    }

    /**
     * Returns map of of ambiguty objects to its available paths
     * @return Map<AmbiguityObject, List<IPath>>
     */
    public Map<AmbiguityObject, List<IPath>> getPathsForAllAmbiguities() {
        for (int i = 0; i < ambiguityObjectsVector.size(); i++) {
            AmbiguityObject ambiguityObject = ambiguityObjectsVector.get(i);
            Map<String, List<IPath>> allPathMap = getPaths(ambiguityObject.getSourceEntity(),
                                                           ambiguityObject.getTargetEntity());

            List<IPath> selectedPathList = allPathMap.get(Constants.SELECTED_PATH);
            List<IPath> curratedPathList = allPathMap.get(Constants.CURATED_PATH);
            List<IPath> generalPathList = allPathMap.get(Constants.GENERAL_PATH);

            if (!selectedPathList.isEmpty()) {
                ambiguityObjectToPathsMap.put(ambiguityObject, selectedPathList);
            } else if (curratedPathList.size() == 1) {
                ambiguityObjectToPathsMap.put(ambiguityObject, curratedPathList);
            } else if (generalPathList.size() == 1) {
                ambiguityObjectToPathsMap.put(ambiguityObject, generalPathList);
            } else {
                List<IPath> selectedPaths = null;
                if (!curratedPathList.isEmpty() || !generalPathList.isEmpty()) {
                    selectedPaths = showAmbiguityResolverUI(allPathMap);
                }
                ambiguityObjectToPathsMap.put(ambiguityObject, selectedPaths);
            }
        }
        return ambiguityObjectToPathsMap;
    }

    /**
     * Method to show ambiguity resolver UI
     * 
     */
    private List<IPath> showAmbiguityResolverUI(Map<String, List<IPath>> allPathMap) {
        AvailablePathsPanel availablePathsPanel = new AvailablePathsPanel(allPathMap);
        availablePathsPanel.parentWindow = WindowUtilities.showInDialog(NewWelcomePanel.getMainFrame(),
                                                                        availablePathsPanel,
                                                                        "Path Ambiguity Resolver",
                                                                        Constants.WIZARD_SIZE2_DIMENSION, true,
                                                                        false);

        return availablePathsPanel.getUserSelectedPaths();
    }

    /**
     * Method to get all possible paths for given source and destination entity
     * 
     * @param sourceEntity
     * @param destinationEntity
     * @return
     */
    private Map<String, List<IPath>> getPaths(EntityInterface sourceEntity, EntityInterface destinationEntity) {
        Set<ICuratedPath> allCuratedPaths = iPathFinder.getCuratedPaths(sourceEntity, destinationEntity);
        Logger.out.debug("  getCuratedPaths() executed : " + allCuratedPaths.size());

        List<IPath> selectedPaths = new ArrayList<IPath>();
        List<IPath> curatedPaths = new ArrayList<IPath>();
        for (ICuratedPath iCuratedPaths : allCuratedPaths) {
            Set<IPath> iPathSet = iCuratedPaths.getPaths();
            if (iPathSet != null && !iPathSet.isEmpty()) {
                for (IPath iPath : iPathSet) {
                    if (iCuratedPaths.isSelected()) {
                        selectedPaths.add(iPath);
                    } else {
                        curatedPaths.add(iPath);
                    }
                }
            }
        }

        List<IPath> generalPaths = iPathFinder.getAllPossiblePaths(sourceEntity, destinationEntity);

        Map<String, List<IPath>> allPathMap = new HashMap<String, List<IPath>>(3);
        allPathMap.put(Constants.SELECTED_PATH, selectedPaths);
        allPathMap.put(Constants.CURATED_PATH, curatedPaths);
        allPathMap.put(Constants.GENERAL_PATH, generalPaths);

        return allPathMap;
    }
}

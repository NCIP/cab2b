/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.client.ui.visualization.charts;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import edu.wustl.cab2b.client.ui.util.ClientConstants;
import edu.wustl.cab2b.common.util.PropertyLoader;
import edu.wustl.cab2b.common.util.Utility;

/**
 * @author chetan_patil
 *
 */
public final class ModelMap {
    private static ModelMap selfReference;

    private Map<String, Vector<String>> refreshModelMap = new HashMap<String, Vector<String>>();

    private ModelMap() {
        loadRefreshModelMap();
    }

    public static final ModelMap getInstance() {
        if (selfReference == null) {
            selfReference = new ModelMap();
        }
        return selfReference;
    }

    public final Vector<String> getTargetModelNames(String sourceModelName) {
        return refreshModelMap.get(sourceModelName);
    }

    private final void loadRefreshModelMap() {
        Properties properties = Utility.getPropertiesFromFile(ClientConstants.MODEL_MAP_PROPERTY_FILE);
        Enumeration<?> propertyKeys = properties.propertyNames();

        while (propertyKeys.hasMoreElements()) {
            String sourceModel = (String) propertyKeys.nextElement();
            String targetModels = properties.getProperty(sourceModel).trim();

            if (targetModels != null && targetModels.length() > 0) {
                String destinationModels[] = targetModels.split(",");

                Vector<String> destinationModelList = new Vector<String>();
                for (String destinationModel : destinationModels) {
                    destinationModelList.add(destinationModel.trim());
                }

                if (!destinationModelList.isEmpty()) {
                    refreshModelMap.put(sourceModel, destinationModelList);
                }
            }
        }
    }

}

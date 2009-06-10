/**
 * 
 */
package edu.wustl.cab2b.client.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import edu.wustl.cab2b.client.ui.ModelMap;

/**
 * @author chetan_patil
 *
 */
public class Mediator implements Observer {

    private Map<String, AbstractModel> abstractModelMap = new HashMap<String, AbstractModel>();

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable observable, Object object) {
        AbstractModel sourceModel = (AbstractModel) observable;
        String sourceModelName = sourceModel.getClass().getName();
        Vector<String> targetModelNames = ModelMap.getInstance().getTargetModelNames(sourceModelName);
        for (String targetModelName : targetModelNames) {
            AbstractModel abstractModel = abstractModelMap.get(targetModelName);
            abstractModel.refresh(sourceModel.getSignal());
        }
    }

    public AbstractModel addModel(AbstractModel abstractModel) {
        return abstractModelMap.put(abstractModel.getClass().getName(), abstractModel);
    }

    public AbstractModel removeModel(String modelName) {
        return abstractModelMap.remove(modelName);
    }

}

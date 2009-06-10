/**
 * 
 */
package edu.wustl.cab2b.client.ui.visualization.charts;

import java.util.Observable;
import java.util.Observer;

/**
 * @author chetan_patil
 *
 */
public abstract class AbstractViewer implements Observer {

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public abstract void update(Observable observable, Object object);
    
}

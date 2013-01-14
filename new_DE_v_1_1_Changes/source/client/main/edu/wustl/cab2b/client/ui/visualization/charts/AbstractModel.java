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

import java.util.Observable;

import edu.wustl.cab2b.client.ui.searchDataWizard.Signal;

/**
 * @author chetan_patil
 *
 */
public abstract class AbstractModel extends Observable {

    /**
     * 
     */
    private Signal signal;

    /**
     * @return the signal
     */
    public Signal getSignal() {
        return signal;
    }

    /**
     * @param signal the signal to set
     */
    public void setSignal(Signal signal) {
        this.signal = signal;
    }

    /**
     * @param signal
     * @return
     */
    protected boolean isProcessed(Signal signal) {
        return this.signal.equals(signal);
    }

    /**
     * @param signal
     */
    public void refresh(Signal signal) {
        if (isProcessed(signal)) {
            return;
        }
    }
}

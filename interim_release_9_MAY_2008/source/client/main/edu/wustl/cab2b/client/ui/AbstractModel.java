/**
 * 
 */
package edu.wustl.cab2b.client.ui;

import java.util.Observable;

/**
 * @author chetan_patil
 *
 */
public abstract class AbstractModel extends Observable {

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

    protected boolean isProcessed(Signal signal) {
        return this.signal.equals(signal);
    }

    public void refresh(Signal signal) {
        if (isProcessed(signal)) {
            return;
        }
    }

}

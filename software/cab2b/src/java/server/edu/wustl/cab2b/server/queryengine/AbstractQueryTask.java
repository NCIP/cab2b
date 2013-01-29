/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.queryengine;

/**
 * @author gaurav_mehta
 */
public abstract class AbstractQueryTask implements Runnable, Comparable<AbstractQueryTask> {
    /**
     * 
     */
    protected float minPriority;

    /**
     * 
     */
    protected float maxPriority;

    /**
     * @param minPriority
     * @param maxPriority
     */
    public AbstractQueryTask(float minPriority, float maxPriority) {
        this.minPriority = minPriority;
        this.maxPriority = maxPriority;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(AbstractQueryTask o) {
        if (maxPriority < o.maxPriority) {
            return 1;
        }
        if (maxPriority > o.maxPriority) {
            return -1;
        }
        return 0;
    }
}
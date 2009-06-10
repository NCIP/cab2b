package edu.wustl.cab2b.admin.flex;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * This Class represents basic DAG Object
 * 
 * @author lalit_chand  
 */

public abstract class DAGObject implements Externalizable {
    public long nodeId;

    public String nodeName;

    public DAGObject() {

    }

    /**
     * 
     * @return id
     */
    public long getNodeId() {
        return nodeId;
    }

    /**
     * 
     * @return name
     */
    public String getNodeName() {
        return nodeName;
    }

    public abstract String getToolTip();

    /**
     * 
     * @param objectInput
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

    }

    /**
     * 
     * @param newVal
     */
    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * 
     * @param newVal
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * 
     * @param objectOutput
     */
    public void writeExternal(ObjectOutput objectOutput) throws IOException {

    }

}
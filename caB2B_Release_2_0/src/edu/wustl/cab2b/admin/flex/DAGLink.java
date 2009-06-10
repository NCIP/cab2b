package edu.wustl.cab2b.admin.flex;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * This class represtents DAGPath from Flex.
 * 
 * @author lalit_chand
 */
public class DAGLink extends DAGObject {
    private static final long serialVersionUID = 1L;

    private String toolTip = null;

    private boolean isSelected = false;

    private long sourceNodeId = 0;

    private long destinationNodeId = 0;

    private String pathId;

    public String getToolTip() {
        return toolTip;
    }

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // TODO Auto-generated method stub
        toolTip = in.readUTF();
        pathId = in.readUTF();
        isSelected = in.readBoolean();
        sourceNodeId = in.readInt();
        destinationNodeId = in.readInt();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        // TODO Auto-generated method stub
        out.writeUTF(toolTip);
        out.writeUTF(pathId);
        out.writeBoolean(isSelected);
        out.writeInt((int) sourceNodeId);
        out.writeInt((int) destinationNodeId);
    }

    public String getPathId() {
        return pathId;
    }

    public void setPathId(String pathId) {
        this.pathId = pathId;
    }

    public long getSourceNodeId() {
        return sourceNodeId;
    }

    public void setSourceNodeId(long sourceExpId) {
        this.sourceNodeId = sourceExpId;
    }

    public long getDestinatioNodeId() {
        return destinationNodeId;
    }

    public void setDestinationNodeId(long destinationExpId) {
        this.destinationNodeId = destinationExpId;
    }

}

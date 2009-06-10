package edu.wustl.cab2b.admin.flex;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.wustl.cab2b.admin.util.AdminConstants;

/**
 * @author lalit_chand
 */
public class DAGNode extends DAGObject {
    private static final long serialVersionUID = -4803914964658460609L;

    private HttpSession session = flex.messaging.FlexContext.getHttpRequest().getSession(false);

    private int xCordinate;

    private int yCordinate;

    private String toolTip = "";

    private String operatorBetweenAttrAndAssociation = "";

    private String nodeType = "";

    public List<DAGNode> associationList = new ArrayList<DAGNode>();

    public List<String> operatorList = new ArrayList<String>();

    public List<DAGLink> dagpathList = new ArrayList<DAGLink>();

    private String errorMsg = "";

    private long entityId;

    private List<String> attributesList;

    /**
     * @return xCordinate
     */
    public int getXCordinate() {
        return xCordinate;
    }

    /**
     * @return yCordinate
     */
    public int getYCordinate() {
        return yCordinate;
    }

    /**
     * @return toolTip
     */
    public String getToolTip() {
        return toolTip;
    }

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public List<String> getAttributeList() {
        return attributesList;
    }

    public void setAttribute(List attributeList) {
        this.attributesList = attributeList;
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        nodeName = in.readUTF();
        toolTip = in.readUTF();
        nodeId = in.readInt();
        operatorBetweenAttrAndAssociation = in.readUTF();
        nodeType = in.readUTF();
        associationList = (List<DAGNode>) in.readObject();
        operatorList = (List<String>) in.readObject();
        dagpathList = (List<DAGLink>) in.readObject();
        errorMsg = in.readUTF();
        xCordinate = in.readInt();
        yCordinate = in.readInt();
        entityId = in.readInt();
        attributesList = (List<String>) in.readObject();

        CategoryDagPanel categoryDagPanel = (CategoryDagPanel) session.getAttribute(AdminConstants.CATEGORY_INSTANCE);
        for (DAGNode dagNode : categoryDagPanel.getDagNodeSet()) {
            if (dagNode.getNodeId() == nodeId) {
                dagNode.xCordinate = xCordinate;
                dagNode.yCordinate = yCordinate;
            }
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(nodeName);
        out.writeUTF(toolTip);
        out.writeInt((int) nodeId);
        out.writeUTF(operatorBetweenAttrAndAssociation);
        out.writeUTF(nodeType);
        out.writeObject(associationList);
        out.writeObject(operatorList);
        out.writeObject(dagpathList);
        out.writeUTF(errorMsg);
        out.writeInt(xCordinate);
        out.writeInt(yCordinate);
        out.writeInt((int) entityId);
        out.writeObject(attributesList);
    }

    /**
    * Equals on getNodeId().
    */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof DAGNode)) {
            return false;
        }

        DAGNode o = (DAGNode) obj;
        return getNodeId() == o.getNodeId();
    }

    /**
     * Hash on getNodeId().
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getNodeId()).toHashCode();
    }

}

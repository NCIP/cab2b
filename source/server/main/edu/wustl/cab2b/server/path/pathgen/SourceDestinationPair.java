package edu.wustl.cab2b.server.path.pathgen;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 
 * @author rahul_ner
 *
 */
public class SourceDestinationPair {
    private Node srcNode;

    private Node destNode;

    private int hashCode;

    /**
     * Constructor initializes source and destination node.
     * @param srcNode
     * @param destNode
     */
    public SourceDestinationPair(Node srcNode, Node destNode) {
        this.srcNode = srcNode;
        this.destNode = destNode;
    }

    /**
     * Returns destination node 
     * @return
     */
    public Node getDestNode() {
        return destNode;
    }

    /**
     * Returns source node
     * @return
     */
    public Node getSrcNode() {
        return srcNode;
    }

    /**
     * Checks whether this object is equal to obj
     * @return
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SourceDestinationPair)) {
            return false;
        }
        SourceDestinationPair other = (SourceDestinationPair) obj;
        return srcNode.equals(other.getSrcNode())
                && destNode.equals(other.getDestNode());
    }

    /**
     * Returns hashCode
     * @return hash code
     */
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = new HashCodeBuilder().append(srcNode).append(destNode).toHashCode();
        }
        return hashCode;
    }
}

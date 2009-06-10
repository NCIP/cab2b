package edu.wustl.cab2b.server.path.pathgen;

import org.apache.commons.lang.builder.HashCodeBuilder;

public class SourceDestinationPair {
    private Node srcNode;

    private Node destNode;

    private int hashCode;

    public SourceDestinationPair(Node srcNode, Node destNode) {
        this.srcNode = srcNode;
        this.destNode = destNode;
    }

    public Node getDestNode() {
        return destNode;
    }

    public Node getSrcNode() {
        return srcNode;
    }

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

    public int hashCode() {
        if (hashCode == 0) {
            hashCode = new HashCodeBuilder().append(srcNode).append(destNode).toHashCode();
        }
        return hashCode;
    }
}

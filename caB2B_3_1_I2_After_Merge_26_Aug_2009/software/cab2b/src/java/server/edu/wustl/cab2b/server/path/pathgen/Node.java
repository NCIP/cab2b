package edu.wustl.cab2b.server.path.pathgen;

/**
 * Immutable wrapper around integer representing a vertex in a
 * {@link edu.wustl.cab2b.server.path.pathgen.Graph}.
 * @author srinath_k
 */
public class Node {
    private int id;

    Node(int id) {
        this.id = id;
    }

    /**
     * Checks whether this object is equal to given object
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Node) {
            Node node = (Node) obj;
            if (node.id == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns hashCode
     * @return hash code
     */
    public int hashCode() {
        return id;
    }

    /**
     * Returns Id.
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Returns Id in string form
     * @return
     */
    public String getIdAsString() {
        return String.valueOf(getId());
    }

    /**
     * Returns object in string form
     * @return This Object string
     */
    public String toString() {
        return String.valueOf(id);
    }
}

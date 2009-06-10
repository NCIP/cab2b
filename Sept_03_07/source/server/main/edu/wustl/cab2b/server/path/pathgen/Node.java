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

    public int hashCode() {
        return id;
    }

    public int getId() {
        return id;
    }

    public String getIdAsString() {
        return String.valueOf(getId());
    }

    public String toString() {
        return String.valueOf(id);
    }
}

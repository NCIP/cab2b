package edu.wustl.cab2b.server.path.pathgen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An adjacency list representation of a (directd) graph. Each vertex of the
 * graph is a {@link edu.wustl.cab2b.server.path.pathgen.Node}.
 * @see edu.wustl.cab2b.server.path.pathgen.Node
 * @author srinath_k
 */
public class Graph {
    private Map<Node, Set<Node>> graph;

    public Graph() {
        graph = new HashMap<Node, Set<Node>>();
    }

    /**
     * Creates the graph from an adjacency matrix. The {@link Node}s have id's
     * corresponding to the row/column index in the matrix.
     * @param adjacencyMatrix
     *            adjacency matrix representation of a graph; (i,j) = true if
     *            and only if a directed edge i -> j is present.
     * @throws IllegalArgumentException
     *             if the adjacency matrix is null, or if it is not a square
     *             matrix.
     */
    public Graph(boolean[][] adjacencyMatrix) {
        if (adjacencyMatrix == null) {
            throw new IllegalArgumentException(
                    "Adjacency matrix cannot be null.");
        }
        if (adjacencyMatrix.length == 0) {
            graph = new HashMap<Node, Set<Node>>();
            return;
        }
        if (adjacencyMatrix.length != adjacencyMatrix[0].length) {
            throw new IllegalArgumentException(
                    "Adjacency matrix must be square.");
        }
        graph = new HashMap<Node, Set<Node>>();
        Node[] nodes = new Node[adjacencyMatrix.length];
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            nodes[i] = new Node(i);
            addNode(nodes[i]);
        }
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                if (adjacencyMatrix[i][j]) {
                    addAdjacentNode(nodes[i], nodes[j]);
                }
            }
        }
    }

    public void addAdjacentNode(Node fromNode, Node toNode) {
        if (!containsNode(fromNode)) {
            addNode(fromNode);
        }
        getAdjacentNodes(fromNode).add(toNode);
    }

    public void addNode(Node node) {
        graph.put(node, new HashSet<Node>());
    }

    public boolean removeNode(Node node) {
        if (!containsNode(node)) {
            return false;
        }
        graph.remove(node);
        for (Node fromNode : graph.keySet()) {
            removeEdge(fromNode, node);
        }
        return true;
    }

    public boolean removeEdge(Node fromNode, Node toNode) {
        if (!containsNode(fromNode)) {
            return false;
        }
        return getAdjacentNodes(fromNode).remove(toNode);
    }

    /**
     * Node y is adjacent to node x iff edge (x,y) exists.
     * @return set of adjacent nodes.
     */
    public Set<Node> getAdjacentNodes(Node node) {
        return graph.get(node);
    }

    public boolean containsNode(Node node) {
        return graph.containsKey(node);
    }

    public Graph clone() {
        Graph clone = new Graph();

        for (Map.Entry<Node, Set<Node>> entry : graph.entrySet()) {
            Node fromNode = entry.getKey();
            clone.addNode(fromNode);
            for (Node toNode : entry.getValue()) {
                clone.addAdjacentNode(fromNode, toNode);
            }
        }
        return clone;
    }

    public int numberOfNodes() {
        return graph.size();
    }

    public Set<Node> allNodes() {
        return graph.keySet();
    }

    public void clear() {
        graph.clear();
    }

    public boolean isEdgePresent(Node fromNode, Node toNode) {
        if (!containsNode(fromNode)) {
            return false;
        }
        return getAdjacentNodes(fromNode).contains(toNode);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Graph)) {
            return false;
        }
        Graph otherGraph = (Graph) obj;
        return graph.equals(otherGraph.graph);
    }

    public int hashCode() {
        return graph.hashCode();
    }
}

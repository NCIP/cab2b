package edu.wustl.common.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO
// path related methods check when source == target

/**
 * A weighted, directed graph that allows self-loops. This class can be used to
 * superimpose a graph structure on any set of objects that logically represent
 * vertices and weights. For example, if an application has a class called
 * <tt>City</tt>, then the <tt>Graph</tt> class can be used to create a
 * route map across all cities by specifying vertices that are of type
 * <tt>City</tt> and weights that are of type <tt>Long</tt> (indicating
 * distance).<br>
 * Note that <tt>null</tt> vertices are not allowed. Thus, all methods throw a
 * <tt>NullPointerException</tt> if a specified vertex parameter is
 * <tt>null</tt>. <br>
 * Many methods in this class throw an <tt>IllegalArgumentException</tt> if a
 * specified vertex is not present in the graph; please refer the methods' docs.
 * <br>
 * Note that modifications to the collections returned by the methods DO NOT
 * affect the graph.<br>
 * 
 * @param <V> the type of vertices
 * @param <W> the type of weights on edges
 * @see Tree
 */

public class Graph<V, W> implements Serializable, Cloneable {

    private static final long serialVersionUID = 2744129191470144562L;

    private transient HashMap<V, Map<V, W>> incomingEdgeMap;

    private HashMap<V, Map<V, W>> outgoingEdgeMap;

    private int numEdges = 0;

    public Graph() {
        initMaps();
    }

    /**
     * Constructs a new graph containing the same vertices and edges as that of
     * the specified graph (copy constructor).
     * 
     * @param graph the graph whose contents are to be copied
     * @throws NullPointerException if the specified graph is <tt>null</tt>
     */
    public Graph(Graph<? extends V, ? extends W> graph) {
        assign(graph);
    }

    private void initMaps() {
        incomingEdgeMap = new HashMap<V, Map<V, W>>();
        outgoingEdgeMap = new HashMap<V, Map<V, W>>();
    }

    /* DATA-RELATED METHODS */
    // ACCESSORS
    /**
     * @return set of all vertices present in graph.
     */
    public Set<V> getVertices() {
        return copy(outgoingEdgeMap.keySet());
    }

    /**
     * @return set of all the weights in this graph.
     */
    public Set<W> getAllWeights() {
        Set<W> res = new HashSet<W>();
        for (Map<V, W> val : incomingEdgeMap.values()) {
            res.addAll(val.values());
        }
        return res;
    }

    /**
     * Returns the outgoing edges from the specified vertex.
     * 
     * @param source the vertex whose outgoing edges are required
     * @return <tt>map</tt> with key as the adjacent vertex and value as the
     *         weight on the outgoing edge.
     * @throws IllegalArgumentException if the specified vertex is not present
     *             in this graph
     */
    public Map<V, W> getOutgoingEdges(V source) {
        validateVertex(source);
        return copy(outgoingEdgeMap.get(source));
    }

    /**
     * Returns the incoming edges to the specified vertex.
     * 
     * @param target the vertex whose incoming edges are required
     * @return <tt>map</tt> with key as the adjacent vertex and value as the
     *         weight on the incoming edge.
     * @throws IllegalArgumentException if the specified vertex is not present
     *             in this graph
     */
    public Map<V, W> getIncomingEdges(V target) {
        validateVertex(target);
        return copy(incomingEdgeMap.get(target));
    }

    /**
     * Checks if the specified vertex is present in this graph.
     * 
     * @param vertex the vertex whose presence is to be checked.
     * @return <tt>true</tt> if this vertex is present in this graph;
     *         <tt>false</tt> otherwise.
     */
    public boolean containsVertex(V vertex) {
        return outgoingEdgeMap.containsKey(vertex);
    }

    /**
     * Checks if there is an edge from the specified source vertex to the
     * specified target vertex.
     * 
     * @return <tt>true</tt> if the specified edge exists; <tt>false</tt>
     *         otherwise
     * @throws IllegalArgumentException if either of the specified vertices is
     *             not present in this graph
     */
    public boolean containsEdge(V source, V target) {
        validateVertex(source);
        validateVertex(target);
        return outgoingEdgeMap.get(source).containsKey(target);
    }

    /**
     * Returns the weight on the edge specified by the vertices.
     * 
     * @return the weight if the specified edge exists, or <tt>null</tt> if
     *         the specified edge does not exist.
     * @throws IllegalArgumentException if either of the specified vertices is
     *             not present in this graph
     */
    public W getEdge(V source, V target) {
        validateVertex(source);
        validateVertex(target);
        return outgoingEdgeMap.get(source).get(target);
    }

    /**
     * @return the number of edges in this graph.
     */
    public int numEdges() {
        return numEdges;
    }

    /**
     * @return the number of vertices in this graph.
     */
    public int numVertices() {
        return outgoingEdgeMap.size();
    }

    // MODIFIERS
    /**
     * Removes the specified edge from this graph.
     * 
     * @param source the source vertex of the edge
     * @param target the target vertex of the edge
     * @return the weight on the removed edge, or <tt>null</tt> if the
     *         specified edge did not exist.
     * @throws IllegalArgumentException if either of the specified vertices is
     *             not present in this graph
     */
    public W removeEdge(V source, V target) {
        if (containsEdge(source, target)) {
            numEdges--;
            outgoingEdgeMap.get(source).remove(target);
            return incomingEdgeMap.get(target).remove(source);
        }
        return null;
    }

    /**
     * Adds the specified edge to this graph. If this graph previously contained
     * the specified edge, then the old weight is replaced by the specified one.
     * 
     * @param source the source vertex of the edge
     * @param target the target vertex of the edge
     * @param weight the (possibly <tt>null</tt>) weight on the edge
     * @return the old weight on this edge, or <tt>null</tt> if this edge did
     *         not already exist.
     */
    public W putEdge(V source, V target, W weight) {
        addVertex(source);
        addVertex(target);
        if (!containsEdge(source, target)) {
            numEdges++;
        }
        outgoingEdgeMap.get(source).put(target, weight);
        return incomingEdgeMap.get(target).put(source, weight);
    }

    /**
     * Adds the specified vertex to this graph.
     * 
     * @param vertex the vertex to add
     * @return <tt>false</tt> if this vertex already existed.
     */
    public boolean addVertex(V vertex) {
        checkNull(vertex);
        if (containsVertex(vertex))
            return false;
        else {
            incomingEdgeMap.put(vertex, new HashMap<V, W>());
            outgoingEdgeMap.put(vertex, new HashMap<V, W>());
            return true;
        }
    }

    /**
     * Adds the specified vertices to this graph.
     * 
     * @param vertices the set of vertices to add
     * @return <tt>true</tt> if atleast one new vertex was added as a result
     *         of this call.
     */
    public boolean addVertices(Set<? extends V> vertices) {
        if (vertices == null) {
            return false;
        }
        boolean modified = false;
        for (V v : vertices) {
            if (addVertex(v))
                modified = true;
        }
        return modified;
    }

    /**
     * Removes the specified vertex and associated edges from this graph.
     * 
     * @param vertex the vertex to be removed.
     * @return <tt>true</tt> if the specified vertex existed in this graph,
     *         <tt>false</tt> otherwise.
     */
    public boolean removeVertex(V vertex) {
        if (!containsVertex(vertex)) {
            return false;
        }
        for (V source : getInNeighbours(vertex)) {
            removeEdge(source, vertex);
        }
        for (V target : getOutNeighbours(vertex)) {
            removeEdge(vertex, target);
        }
        incomingEdgeMap.remove(vertex);
        outgoingEdgeMap.remove(vertex);
        return true;
    }

    /**
     * Removes all vertices and edges from this graph.
     */
    public void clear() {
        incomingEdgeMap.clear();
        outgoingEdgeMap.clear();
    }

    // HELPERS
    private void checkNull(V vertex) {
        if (vertex == null) {
            throw new NullPointerException("null vertex.");
        }
    }

    private void validateVertex(V vertex) {
        checkNull(vertex);
        if (!containsVertex(vertex)) {
            throw new IllegalArgumentException("specified vertex is not present in graph.");
        }
    }

    private static <E> Set<E> copy(Set<E> set) {
        return new HashSet<E>(set);
    }

    private static <K, V> Map<K, V> copy(Map<K, V> map) {
        return new HashMap<K, V>(map);
    }

    // ////////////////////////////////////////////////////////////////////////////////
    /* GRAPH STRUCTURE BASED OPERATIONS */
    /**
     * @return the set of vertices that are adjacent to and reachable from the
     *         specified vertex.
     * @throws IllegalArgumentException if the specified vertex is not present
     *             in this graph
     */
    public Set<V> getOutNeighbours(V v) {
        return getOutgoingEdges(v).keySet();
    }

    /**
     * @return the set of vertices that are adjacent to the specified vertex and
     *         from which the specified vertex is reachable.
     * @throws IllegalArgumentException if the specified vertex is not present
     *             in this graph
     */
    public Set<V> getInNeighbours(V v) {
        return getIncomingEdges(v).keySet();
    }

    /**
     * Returns the number of outgoing edges from the specified vertex.
     * 
     * @param v the vertex whose out-degree is required.
     * @return the number of outgoing edges from the specified vertex.
     * @throws IllegalArgumentException if the specified vertex is not present
     *             in this graph
     */
    public int outDegree(V v) {
        return getOutgoingEdges(v).size();
    }

    /**
     * Returns the number of incoming edges to the specified vertex.
     * 
     * @param v the vertex whose in-degree is required.
     * @return the number of incoming edges to the specified vertex.
     * @throws IllegalArgumentException if the specified vertex is not present
     *             in this graph
     */
    public int inDegree(V v) {
        return getIncomingEdges(v).size();
    }

    /**
     * Returns the set of vertices that cannot be reached from any other vertex
     * in this graph. This implies that if a vertex contains a self-loop and no
     * other edges, then that vertex is <tt>unreachable</tt>.
     * 
     * @return set of vertices that cannot be reached from any other vertex in
     *         this graph.
     */
    public Set<V> getUnreachableVertices() {
        Set<V> res = new HashSet<V>();
        for (Map.Entry<V, Map<V, W>> entry : incomingEdgeMap.entrySet()) {
            if (entry.getValue().isEmpty() || Collections.singleton(entry.getKey()).equals(entry.getValue().keySet())) {
                res.add(entry.getKey());
            }
        }
        return res;
    }

    /**
     * Checks if there exists a path from the specified source vertex to the
     * specified target vertex. Every vertex is reachable from itself; thus, if
     * <tt>source.equals(target)</tt> then this method returns <tt>true</tt>.
     * 
     * @param source the source vertex
     * @param target the target vertex
     * @return <tt>true</tt> is there exists a path from the specified source
     *         vertex to the specified target vertex; <tt>false</tt>
     *         otherwise.
     * @throws IllegalArgumentException if either of the specified verices is
     *             not present in this graph.
     */
    public boolean isReachable(V source, V target) {
        validateVertex(source);
        validateVertex(target);
        return isReachableValidVertices(source, target);
    }

    private boolean isReachableValidVertices(V source, V target) {
        if (source.equals(target))
            return true; // finally reached from source to target!!!
        for (V v : getOutNeighbours(source)) {
            if (!v.equals(source) && isReachableValidVertices(v, target))
                return true;
        }
        return false;
    }

    /**
     * All possible (acyclic) vertex-paths between two vertices. If
     * <tt>source.equals(target)</tt>, then no paths are returned. A
     * <tt>vertex-path</tt> is a list of vertices on the path, source and
     * target vertices inclusive. Thus, for each path, the first vertex is the
     * source vertex and the last vertex is the target vertex.
     * 
     * @param source the source vertex.
     * @param target the target vertex.
     * @return list of paths from specified source vertex to specified target
     *         vertex.
     * @throws IllegalArgumentException if either of the specified verices is
     *             not present in this graph.
     */
    public Set<List<V>> getVertexPaths(V source, V target) {
        validateVertex(source);
        validateVertex(target);
        if (source.equals(target)) {
            return new HashSet<List<V>>();
        }
        return getVertexPaths(source, target, new HashSet<V>());
    }

    // TODO check source == target
    private Set<List<V>> getVertexPaths(V source, V target, Set<V> verticesToIgnore) {
        Set<List<V>> res = new HashSet<List<V>>();
        verticesToIgnore.add(target);
        for (Map.Entry<V, W> entry : incomingEdgeMap.get(target).entrySet()) {
            V srcSrc = entry.getKey();
            if (verticesToIgnore.contains(srcSrc)) {
                continue;
            }
            if (source.equals(srcSrc)) {
                List<V> path = new ArrayList<V>();
                path.add(source);
                path.add(target);
                res.add(path);
                continue;
            }
            Set<List<V>> thePaths = getVertexPaths(source, srcSrc, verticesToIgnore);

            for (List<V> thePath : thePaths) {
                thePath.add(target);
            }
            res.addAll(thePaths);
        }
        verticesToIgnore.remove(target);
        return res;
    }

    /**
     * All possible (acyclic) edge-paths between two vertices. If
     * <tt>source.equals(target)</tt>, then no paths are returned. An
     * <tt>edge-path</tt> is a list of weights on the edges on the path.
     * 
     * @param source the source vertex.
     * @param target the target vertex.
     * @return list of paths from specified source vertex to specified target
     *         vertex.
     * @throws IllegalArgumentException if either of the specified verices is
     *             not present in this graph.
     */
    public Set<List<W>> getEdgePaths(V source, V target) {
        Set<List<W>> edgePaths = new HashSet<List<W>>();
        Set<List<V>> verticesPaths = getVertexPaths(source, target);
        for (List<V> thePath : verticesPaths) {
            List<W> theEdgePath = new ArrayList<W>();
            for (int j = 1; j < thePath.size(); j++) {
                theEdgePath.add(getEdge(thePath.get(j - 1), thePath.get(j)));
            }
            edgePaths.add(theEdgePath);
        }
        return edgePaths;
    }

    // ///////////////////////////////////////////////////////////
    /* STRUCTURAL CONSTRAINT CHECKERS */
    /**
     * Checks if the graph is weakly connected. Weakly connected digraph
     * (definition from MathWorld): A directed graph in which it is possible to
     * reach any node starting from any other node by traversing edges in some
     * direction (i.e., not necessarily in the direction they point).<br>
     * Note that the null graph (no vertices) and the singleton graph (one
     * vertex) are weakly connected.
     * 
     * @return <tt>true</tt> if graph is weakly connected; <tt>false</tt>otherwise.
     */
    public boolean isConnected() {
        Set<V> vertices = getVertices();
        if (!vertices.isEmpty()) {
            dfs(vertices.iterator().next(), vertices);
        }
        return vertices.isEmpty();
    }

    /*
     * Method to traverse using Depth First algorithm. It removes the vertex
     * from allVertexSet while visiting each vertex. dfs of connected graph
     * should result into the allVetrexSet empty.
     * 
     * @param vertex The vertex to be visited. @param allVertexSet Set of all
     * nodes not visited yet.
     */
    private void dfs(V vertex, Set<V> allVertexSet) {
        allVertexSet.remove(vertex);
        Set<V> adjacentVertices = new HashSet<V>();
        adjacentVertices.addAll(outgoingEdgeMap.get(vertex).keySet());
        adjacentVertices.addAll(incomingEdgeMap.get(vertex).keySet());
        // insure against self-edge
        adjacentVertices.remove(vertex);
        for (V adjacentVertex : adjacentVertices) {
            if (allVertexSet.contains(adjacentVertex))
                dfs(adjacentVertex, allVertexSet);
        }
    }

    /**
     * Checks if this graph is a tree.
     * 
     * @return <tt>true</tt> if this graph is a tree
     */
    public boolean isTree() {
        if (getVertices().isEmpty()) {
            return true;
        }
        Set<V> roots = getUnreachableVertices();
        if (roots.size() != 1) {
            return false;
        }
        V root = roots.iterator().next();
        // no self-loop at root
        if (inDegree(root) != 0) {
            return false;
        }
        Set<V> visitedVertices = new HashSet<V>();
        visitedVertices.add(root);
        List<V> currVertices = new ArrayList<V>();
        currVertices.addAll(getOutNeighbours(root));

        // bfs and checks that you visit each vertex only once
        while (!currVertices.isEmpty()) {
            List<V> nextVertices = new ArrayList<V>();
            for (V currV : currVertices) {
                if (visitedVertices.contains(currV)) {
                    return false;
                } else {
                    if (inDegree(currV) != 1 || containsEdge(currV, currV)) {
                        return false;
                    }
                    visitedVertices.add(currV);
                }
                nextVertices.addAll(getOutNeighbours(currV));
            }
            currVertices = nextVertices;
        }
        return visitedVertices.equals(getVertices());
    }

    /**
     * Checks if adding specified edge will cause a new cycle in the graph. Note
     * that if the specified edge already exists, then this method returns
     * <tt>false</tt>, since this edge cannot cause a <b>new</b> cycle.
     * 
     * @param source the source vertex
     * @param target the target vertex
     * @return <tt>true</tt> if adding the specified edge will cause a new
     *         cycle in this graph.
     */
    public boolean willCauseNewCycle(V source, V target) {
        if (!(containsVertex(source) && containsVertex(target))) {
            return source.equals(target);
        }

        return !containsEdge(source, target) && isReachableValidVertices(target, source);
    }

    /**
     * Returns a shallow copy of this <tt>Graph</tt> instance: the vertices
     * and edges themselves are not cloned.
     * 
     * @return a shallow copy of this graph.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Graph<V, W> clone() {
        Graph<V, W> res = null;
        try {
            res = (Graph<V, W>) super.clone();
        } catch (CloneNotSupportedException e) {
            // can't occur
        }
        res.assign(this);
        return res;
    }

    /**
     * Performs a reverse clone i.e. initializes this graph instance to be the
     * same as the specified graph.
     * 
     * @param graph the graph instance to be reverse-cloned.
     * @throws NullPointerException if the specified graph is <tt>null</tt>
     */
    public void assign(Graph<? extends V, ? extends W> graph) {
        if (graph == null) {
            throw new NullPointerException();
        }
        assign2(graph);
    }

    private <V1 extends V, E1 extends W> void assign2(Graph<V1, E1> graph) {
        initMaps();
        for (V1 source : graph.getVertices()) {
            addVertex(source);
            for (Map.Entry<V1, E1> entry : graph.getOutgoingEdges(source).entrySet()) {
                putEdge(source, entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Returns a string representation of this graph. The string representation
     * consists of the set of outgoing edges for each vertex.
     * 
     * @return a string representation of this graph.
     */
    @Override
    public String toString() {
        return outgoingEdgeMap.toString();
    }

    /**
     * Compares the specified object with this graph for equality. Returns
     * <tt>true</tt> if the given object is also a graph and the two graphs
     * represent the same set of vertices and weighted edges. More formally, two
     * graph instances <tt>t1</tt> and <tt>t2</tt> represent the same graph
     * if <tt>t1.getVertices().equals(t2.getVertices())</tt> and for every
     * vertex <tt>v</tt> in <tt>t1.getVertices()</tt>,
     * <tt> (t1.getOutgoingEdges(v).equals(t2.getOutgoingEdges(v))) </tt>.
     * 
     * @param o object to be compared for equality with this graph.
     * @return <tt>true</tt> if the specified object is equal to this graph.
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Graph)) {
            return false;
        }
        Graph<V, W> other = (Graph<V, W>) obj;
        return outgoingEdgeMap.equals(other.outgoingEdgeMap);
    }

    @Override
    public int hashCode() {
        return outgoingEdgeMap.hashCode();
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        incomingEdgeMap = new HashMap<V, Map<V, W>>();
        for (V v : outgoingEdgeMap.keySet()) {
            incomingEdgeMap.put(v, new HashMap<V, W>());
        }
        for (Map.Entry<V, Map<V, W>> outgoingEdgesEntry : outgoingEdgeMap.entrySet()) {
            V source = outgoingEdgesEntry.getKey();
            for (Map.Entry<V, W> outgoingEdge : outgoingEdgesEntry.getValue().entrySet()) {
                V target = outgoingEdge.getKey();
                W edge = outgoingEdge.getValue();
                incomingEdgeMap.get(target).put(source, edge);
            }
        }
    }
}
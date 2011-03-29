package edu.wustl.cab2b.server.path.pathgen;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
/**
 * Computes all possible paths present in a directed graph. No path returned
 * should contain a cycle. Suppose the graph is (V, E) where V is the set of
 * vertices and E is the set of edges. A source-dest pair is represented as
 * <code>i->j</code>.<br>
 * The algorithm is as follows : <br>
 * For each pair of nodes <code>{i, j : i &isin; V, j &isin; V, i &ne; j}</code>
 * in the graph, call <code>getPaths(i->j, {})</code>. Self-edges (a
 * self-edge is a path of the form <code>i->i</code>) are then added to the
 * resulting set of paths.<br>
 * <code>getPaths()</code> is the method where the core of the algorithm
 * resides. Suppose<code>
 * P(i->j, N) </code>is the set of paths about to be
 * returned from <code>getPaths()</code>.<br>
 * Following is what happens on a call <code>getPaths(i->j, N)</code>, where
 * N is the ignoredNodesSet : <br>
 * <ol>
 * <li>Let <code>X =
 * {@link edu.wustl.cab2b.server.path.pathgen.GraphPathFinderCache#getPathsOnIgnoringNodes(SourceDestinationPair, Set)}
 * with (i->j, N)</code>
 * as parameters; if <code>X != null</code>, then <code>P(i->j, N) =
 * X</code>;
 * return <code>P(i->j, N)</code>. Else continue.
 * <li>If <code>i->j &isin; E</code> then add a path <code>i->j</code> to
 * <code>P(i->j, N)</code>.</li>
 * <li>Let
 * <code>K = {k : k &isin; V, k &ne; i, k &ne; j, k &isin; N, i->k &isin; E)</code>.
 * For each <code>k &isin; K</code>, do the following : <br>
 * <ol>
 * <li>Call <code>getPaths (k->j, N &cup; {i})</code>. Suppose the returned
 * set of paths is R.</li>
 * <li>For each path <code>Rx (0 < x < |R|) in R</code>, add the path
 * <code>i->Rx to P(i->j, N)</code>.</li>
 * </ol>
 * <li>Add <code>P(i->j, N)</code> to the cache.
 * <li>Return <code>P(i->j, N)</code></li>
 * </ol>
 * @author srinath_k
 */
public class GraphPathFinder {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(GraphPathFinder.class);

    /** Constant for memory cache */
    public static boolean MEM_CACHE = true;

    private Graph inputGraph;

    private GraphPathFinderCache cache;

    private void wrapup() {
        this.inputGraph = null;
        this.cache.cleanup();
        this.cache = null;
    }

    private GraphPathFinderCache getCache() {
        return cache;
    }

    /**
     * 
     * @param adjacencyMatrix
     * @param replicationNodes
     * @param conn
     * @return
     */
    public Set<Path> getAllPaths(boolean[][] adjacencyMatrix, Map<Integer, Set<Integer>> replicationNodes,
                                 Connection conn) {
        return getAllPaths(adjacencyMatrix, replicationNodes, conn, Integer.MAX_VALUE);
    }


    /**
     * Returns all paths
     * @param adjacencyMatrix
     * @param replicationNodes
     * @param conn
     * @param maxLength
     * @return
     * @throws IllegalArgumentException if <tt>maxLength < 2</tt>
     */
    public Set<Path> getAllPaths(boolean[][] adjacencyMatrix, Map<Integer, Set<Integer>> replicationNodes,
                                 Connection conn, int maxLength) {
        logger.info("Entered GraphPathFinder...");
        if (maxLength < 2) {
            throw new IllegalArgumentException("maxLength should be atleast 2.");
        }
        long startTime = System.currentTimeMillis();
        // init
        this.inputGraph = new Graph(adjacencyMatrix);
        if (MEM_CACHE) {
            this.cache = new MemoryCache();
        } else {
            this.cache = new DatabaseCache(conn);
        }
        // end init
        Node[] allNodes = this.inputGraph.allNodes().toArray(new Node[0]);
        int numPaths = 0;
        for (Node srcNode : allNodes) {
            logger.debug("Processing " + srcNode);
            for (Node destNode : allNodes) {
                if (srcNode.equals(destNode)) {
                    // don't process self-edges now...
                    continue;
                }
                SourceDestinationPair sdp = new SourceDestinationPair(srcNode, destNode);
                //                logger.info("Processing " + srcNode + " to " + destNode);

                Set<Path> srcDestPaths = getPaths(sdp, new HashSet<Node>(), maxLength);
                numPaths += srcDestPaths.size();
                // if (KEEP_WRITING) {
                // PathToFileWriter.APPEND = true;
                // PathToFileWriter.writePathsToFile(srcDestPaths, null);
                // }
            }
        }
        Set<Path> result = new HashSet<Path>();
        // process self edges
        for (Node node : allNodes) {
            if (isEdgePresent(node, node)) {
                result.add(new Path(node, node));
                ++numPaths;
            }
        }
        // add other paths
        result.addAll(getCache().getAllPaths());
        wrapup();
        result = PathReplicationUtil.replicatePaths(result, replicationNodes);
        long endTime = System.currentTimeMillis();
        logger.info("Time taken GraphPathFinder : " + (endTime - startTime));
        logger.info("Exiting GraphPathFinder.");
        return result;
    }

    private Set<Path> getPaths(SourceDestinationPair sdp, Set<Node> nodesToIgnore, final int maxLength) {
        Node srcNode = sdp.getSrcNode();
        Node destNode = sdp.getDestNode();
        Set<Path> res = new HashSet<Path>();
        // see if there are paths calculated already...
        Set<Path> cachedPaths = getCache().getPathsOnIgnoringNodes(sdp, nodesToIgnore);
        if (cachedPaths != null) {
            res.addAll(cachedPaths);
            return res;
        }
        Set<Node> interNodes = new HashSet<Node>(this.inputGraph.allNodes());
        interNodes.remove(srcNode);
        interNodes.remove(destNode);
        interNodes.removeAll(nodesToIgnore);

        if (isEdgePresent(srcNode, destNode)) {
            res.add(new Path(srcNode, destNode));
        }
        Set<Node> nodesToIgnoreNext = new HashSet<Node>(nodesToIgnore);
        nodesToIgnoreNext.add(srcNode);
        for (Node interNode : interNodes) {
            if (isEdgePresent(srcNode, interNode)) {
                Set<Path> pathsFromInterToDest = getPaths(new SourceDestinationPair(interNode, destNode),
                                                          nodesToIgnoreNext, maxLength);

                for (Path pathFromInterToDest : pathsFromInterToDest) {
                    if (pathFromInterToDest.numNodes() == maxLength) {
                        continue;
                    }
                    List<Node> intermediateNodes = new ArrayList<Node>();

                    intermediateNodes.add(interNode);
                    intermediateNodes.addAll(pathFromInterToDest.getIntermediateNodes());
                    Path resPath = new Path(srcNode, destNode, intermediateNodes);
                    res.add(resPath);
                }

            }
        }
        addEntryToCache(sdp, nodesToIgnore, res);
        //  
        return res;
    }

    private void addEntryToCache(SourceDestinationPair sdp, Set<Node> nodesToIgnore, Set<Path> res) {
        getCache().addEntry(sdp, nodesToIgnore, res);
    }

    private boolean isEdgePresent(Node srcNode, Node destNode) {
        return this.inputGraph.isEdgePresent(srcNode, destNode);
    }
}
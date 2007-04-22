package edu.wustl.cab2b.server.path.pathgen;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.common.util.logger.Logger;

/**
 * Computes all possible paths present in a directed graph. No path returned
 * should contain a cycle. Suppose the graph is (V, E) where V is the set of
 * vertices and E is the set of edges. A source-dest pair is represented as
 * i->j.<br>
 * The algorithm is as follows : <br>
 * For each pair of nodes {i, j : i &epsilon; V, j &epsilon; V, i!= j} in the
 * graph, call getPaths(i->j, {}). Self-edges (a self-edge is a path of the form
 * i->i) are then added to the resulting set of paths.<br>
 * getPaths() is the method where the core of the algorithm resides. Suppose
 * P(i->j, N) is the set of paths about to be returned from getPaths().<br>
 * Following is what happens on a call getPaths(i->j, N) where N is the
 * ignoredNodesSet : <br>
 * <ol>
 * <li>If an entry (i->j, N) exists in the cache, return its corresponding set
 * of paths; else continue.
 * <li>If i->j &epsilon; E then add a path i->j to P(i->j, N).</li>
 * <li>Let K = {k : k &epsilon; V, k !&epsilon; N, k->j &epsilon E). For each k
 * &epsilon; K, do the following : <br>
 * <ol>
 * <li>Call getPaths (k->j, N &cup; {i}). Suppose the returned set of paths is
 * R.</li>
 * <li>For each path Rx (0 < x < |R|), add the path i->Rx to P(i->j, N).</li>
 * </ol>
 * <li>Add P(i->j, N) to the cache.
 * <li>Return P(i->j, N)</li>
 * </ol>
 * @author srinath_k
 */
public class GraphPathFinder {
    // public static boolean KEEP_WRITING = false;

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

    public Set<Path> getAllPaths(boolean[][] adjacencyMatrix,
                                 Map<Integer, Set<Integer>> replicationNodes,
                                 Connection conn) {
        Logger.out.info("Entered GraphPathFinder...");
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
            for (Node destNode : allNodes) {
                if (srcNode.equals(destNode)) {
                    // don't process self-edges now...
                    continue;
                }
                SourceDestinationPair sdp = new SourceDestinationPair(srcNode,
                        destNode);
                Logger.out.info("Processing " + srcNode + " to " + destNode);

                Set<Path> srcDestPaths = getPaths(sdp, new HashSet<Node>());
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
        Logger.out.info("Time taken GraphPathFinder : " + (endTime - startTime));
        Logger.out.info("Exiting GraphPathFinder.");
        return result;
    }

    private Set<Path> getPaths(SourceDestinationPair sdp,
                               Set<Node> nodesToIgnore) {
        Node srcNode = sdp.getSrcNode();
        Node destNode = sdp.getDestNode();
        Set<Path> res = new HashSet<Path>();
        // see if there are paths calculated already...
        Set<Path> cachedPaths = getCache().getPathsOnIgnoringNodes(sdp,
                                                                   nodesToIgnore);
        if (cachedPaths != null) {
            res.addAll(cachedPaths);
            return res;
        }
        Set<Node> interNodes = new HashSet<Node>(this.inputGraph.allNodes());
        interNodes.remove(srcNode);
        interNodes.remove(destNode);
        interNodes.removeAll(nodesToIgnore);

        if (isEdgePresent(srcNode, destNode)) {
            // the edge isn't forbidden, or we wouldn't be here...
            res.add(new Path(srcNode, destNode));
        }
        Set<Node> nodesToIgnoreNext = new HashSet<Node>(nodesToIgnore);
        nodesToIgnoreNext.add(srcNode);
        for (Node interNode : interNodes) {
            if (isEdgePresent(srcNode, interNode)) {
                Set<Path> pathsFromInterToDest = getPaths(
                                                          new SourceDestinationPair(
                                                                  interNode,
                                                                  destNode),
                                                          nodesToIgnoreNext);

                for (Path pathFromInterToDest : pathsFromInterToDest) {
                    List<Node> intermediateNodes = new ArrayList<Node>();

                    intermediateNodes.add(interNode);
                    intermediateNodes.addAll(pathFromInterToDest.getIntermediateNodes());
                    Path resPath = new Path(srcNode, destNode,
                            intermediateNodes);
                    res.add(resPath);
                }

            }
        }
        addEntryToCache(sdp, nodesToIgnore, res);
        // System.out.println(res);
        return res;
    }

    private void addEntryToCache(SourceDestinationPair sdp,
                                 Set<Node> nodesToIgnore, Set<Path> res) {
        getCache().addEntry(sdp, nodesToIgnore, res);
    }

    private boolean isEdgePresent(Node srcNode, Node destNode) {
        return this.inputGraph.isEdgePresent(srcNode, destNode);
    }
}
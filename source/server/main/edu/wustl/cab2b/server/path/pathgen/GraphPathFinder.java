package edu.wustl.cab2b.server.path.pathgen;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.common.util.logger.Logger;

/**
 * @see edu.wustl.cab2b.server.path.pathgen.GraphPathFinderInput
 * @author srinath_k
 */
public class GraphPathFinder {
    public static boolean KEEP_WRITING = false;

    public static boolean MEM_CACHE = true;

    private Graph inputGraph;

    private GraphPathFinderCache cache;

    private void wrapup() {
        getCache().cleanup();
        this.inputGraph = null;
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
                // result.addAll(srcDestPaths);
                if (KEEP_WRITING) {
                    PathToFileWriter.APPEND = true;
                    PathToFileWriter.writePathsToFile(srcDestPaths, null);
                }
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
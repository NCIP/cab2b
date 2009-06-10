package edu.wustl.cab2b.server.path.pathgen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * A concrete cache impl that uses in-memory data structures. Use this cache if
 * the estimated no. of paths is less, and high speed in desired. Typically,
 * this cache should be the first choice; you consider using an alternate cache
 * if and only if you encounter an {@link java.lang.OutOfMemoryError} using this
 * cache.
 * @author srinath_k
 */
class MemoryCache extends GraphPathFinderCache {
    private class PathsOnIgnoringNodes {

        Map<Set<Node>, Set<Path>> ignoredNodesToPaths = new HashMap<Set<Node>, Set<Path>>();

        /**
         * Adds entry
         * @param ignoredNodes
         * @param paths
         */
        public void addEntry(Set<Node> ignoredNodes, Set<Path> paths) {
            ignoredNodesToPaths.put(ignoredNodes, paths);
        }

        /**
         * Returns path on ignored nodes
         * @param ignoredNodes
         * @return
         */
        public Set<Path> getPathsOnIgnoringNodes(Set<Node> ignoredNodes) {
            return ignoredNodesToPaths.get(ignoredNodes);
        }

        /**
         * Checks whether path is on ignored nodes or not.
         * @param ignoredNodes
         * @return
         */
        public boolean containsPathOnIgnoringNodes(Set<Node> ignoredNodes) {
            return ignoredNodesToPaths.containsKey(ignoredNodes);
        }

        /**
         * Returns ignored nodes to paths.
         * @return
         */
        public Map<Set<Node>, Set<Path>> getAllEntries() {
            return ignoredNodesToPaths;
        }
    }

    private Map<SourceDestinationPair, PathsOnIgnoringNodes> calculatedPaths;

    /**
     * Default constructor for MemoryCache
     */
    public MemoryCache() {
        super();
        this.calculatedPaths = new HashMap<SourceDestinationPair, PathsOnIgnoringNodes>();
    }

    /**
     * Adds the specified entry to cache, and removes all entries from the cache
     * whose ignoredNodesSet is a superset of the specified ignoredNodes. e.g
     * Suppose that entries <code>P(i->j, Ni)</code> (for some i's) existed in
     * the cache. If addEntry() is called with <code>P(i->j, M)</code>, then
     * each entry <code>P(i->j, Ni)</code> where <code>Ni &sube; M</code> is
     * removed from the cache; as mentioned in contract for
     * {@link GraphPathFinderCache#addEntry(SourceDestinationPair, Set, Set)},
     * <code>P(i->j, Ni)</code> can and will be computed from
     * <code>P(i->j, M)</code> if needed.<br>
     * This is to prevent memory usage from exploding. The corresponding adverse
     * on performance is relatively minor.
     * @see edu.wustl.cab2b.server.path.pathgen.GraphPathFinderCache#addEntry(edu.wustl.cab2b.server.path.pathgen.SourceDestinationPair,
     *      java.util.Set, java.util.Set)
     */
    public void addEntry(SourceDestinationPair sdp, Set<Node> ignoredNodes,
                         Set<Path> paths) {
        checkAlive();
        PathsOnIgnoringNodes pathsOnIgnoringNodes;
        if (this.calculatedPaths.containsKey(sdp)) {
            pathsOnIgnoringNodes = this.calculatedPaths.get(sdp);
        } else {
            pathsOnIgnoringNodes = new PathsOnIgnoringNodes();
            this.calculatedPaths.put(sdp, pathsOnIgnoringNodes);
        }

        Iterator<Set<Node>> keySetIter = pathsOnIgnoringNodes.getAllEntries().keySet().iterator();
        while (keySetIter.hasNext()) {
            Set<Node> key = keySetIter.next();
            if (key.containsAll(ignoredNodes)) {
                keySetIter.remove();
            }
        }
        pathsOnIgnoringNodes.addEntry(ignoredNodes, paths);
    }

    /**
     * returns path on ignoring nodes.
     * @param sdp
     * @param ignoredNodes
     * @return
     * @see edu.wustl.cab2b.server.path.pathgen.GraphPathFinderCache#getPathsOnIgnoringNodes(edu.wustl.cab2b.server.path.pathgen.SourceDestinationPair,
     *      java.util.Set)
     */
    public Set<Path> getPathsOnIgnoringNodes(SourceDestinationPair sdp,
                                             Set<Node> ignoredNodes) {
        checkAlive();
        if (haveWeSeenThisSDPBefore(sdp)) {
            PathsOnIgnoringNodes pathsOnIgnoringNodes = getCalculatedPaths(sdp);
            if (pathsOnIgnoringNodes.containsPathOnIgnoringNodes(ignoredNodes)) {
                return pathsOnIgnoringNodes.getPathsOnIgnoringNodes(ignoredNodes);
            }
            Map<Set<Node>, Set<Path>> allEntries = pathsOnIgnoringNodes.getAllEntries();
            boolean foundSubset = false;
            Set<Node> subset = null;
            for (Set<Node> key : allEntries.keySet()) {
                if (ignoredNodes.containsAll(key)) {
                    foundSubset = true;
                    subset = key;
                    break;
                }
            }
            if (foundSubset) {
                Set<Path> res = new HashSet<Path>();
                for (Path path : allEntries.get(subset)) {
                    Set<Node> pathNodes = new HashSet<Node>(
                            path.getIntermediateNodes());
                    pathNodes.retainAll(ignoredNodes);
                    if (pathNodes.isEmpty()) {
                        res.add(path);
                    }
                }
                return res;
            }
        }
        return null;
    }

    private boolean haveWeSeenThisSDPBefore(SourceDestinationPair sdp) {
        return this.calculatedPaths.containsKey(sdp);
    }

    private PathsOnIgnoringNodes getCalculatedPaths(SourceDestinationPair sdp) {
        return this.calculatedPaths.get(sdp);
    }

    /**
     * Cleans up this cache, and marks it dead.
     */
    public void cleanup() {
        checkAlive();
        this.calculatedPaths = null;
        super.cleanup();
    }

    /**
     * Returns all the entries in the cache.
     * @see edu.wustl.cab2b.server.path.pathgen.GraphPathFinderCache#getAllPaths()
     */
    Set<Path> getAllPaths() {
        Set<Path> res = new HashSet<Path>();
        Iterator<Entry<SourceDestinationPair, PathsOnIgnoringNodes>> calcPathsIter = this.calculatedPaths.entrySet().iterator();
        while (calcPathsIter.hasNext()) {
            Map.Entry<SourceDestinationPair, PathsOnIgnoringNodes> entry = calcPathsIter.next();
            for (Set<Path> paths : entry.getValue().getAllEntries().values()) {
                res.addAll(paths);
            }
            // calcPathsIter.remove();
        }
        return res;
    }
}

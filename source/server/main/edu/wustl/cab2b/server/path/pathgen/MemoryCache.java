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
 * cache.<br>
 * @author srinath_k
 */
class MemoryCache extends GraphPathFinderCache {
    private class PathsOnIgnoringNodes {

        Map<Set<Node>, Set<Path>> ignoredNodesToPaths = new HashMap<Set<Node>, Set<Path>>();

        public void addEntry(Set<Node> ignoredNodes, Set<Path> paths) {
            ignoredNodesToPaths.put(ignoredNodes, paths);
        }

        public Set<Path> getPathsOnIgnoringNodes(Set<Node> ignoredNodes) {
            return ignoredNodesToPaths.get(ignoredNodes);
        }

        public boolean containsPathOnIgnoringNodes(Set<Node> ignoredNodes) {
            return ignoredNodesToPaths.containsKey(ignoredNodes);
        }

        public Map<Set<Node>, Set<Path>> getAllEntries() {
            return ignoredNodesToPaths;
        }
    }

    private Map<SourceDestinationPair, PathsOnIgnoringNodes> calculatedPaths;

    public MemoryCache() {
        super();
        this.calculatedPaths = new HashMap<SourceDestinationPair, PathsOnIgnoringNodes>();
    }

    /**
     * Adds the specified entry to cache, and removes all entries from the cache
     * whose ignoredNodesSet is a superset of the specified ignoredNodes.<br>
     * i.e. Suppose that entries P[i->j, Ni] (for some i's) existed in the
     * cache. If addEntry() is called with P[i->j, M], then each entry P[i->j,
     * Ni] where Ni &sub; M is removed from the cache (see
     * {@link GraphPathFinderCache}).<br>
     * We can remove such entries, since, P[i->j, N1] can be computed trivially
     * from P[i->j, N2] if N1 &sub; N2, by ignoring the paths that contain the
     * nodes in {N2 - N1}.<br>
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

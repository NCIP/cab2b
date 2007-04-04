package edu.wustl.cab2b.server.path.pathgen;

import java.util.Set;

abstract class GraphPathFinderCache {
    private boolean alive;

    GraphPathFinderCache() {
        this.alive = true;
    }

    /**
     * Adds this entry to the cache.
     * @param sdp
     * @param ignoredNodes
     * @param paths
     */
    abstract void addEntry(SourceDestinationPair sdp, Set<Node> ignoredNodes,
                           Set<Path> paths);

    /**
     * If there is an entry with given keys, the paths are returned. Otherwise
     * an entry is found whose ignoredNodes is a subset of the specified
     * ignoredNodes. The paths thus found are pruned to remove any nodes that
     * may need to be ignored.
     * @param sdp sdp
     * @param ignoredNodes ignored nodes 
     * @return the set of paths
     */
    abstract Set<Path> getPathsOnIgnoringNodes(SourceDestinationPair sdp,
                                               Set<Node> ignoredNodes);

    /**
     * @return ALL the paths in the cache.
     */
    abstract Set<Path> getAllPaths();

    void cleanup() {
        this.alive = false;
    }

    void checkAlive() {
        if (!alive) {
            throw new UnsupportedOperationException(
                    "Don't ask dead people to help you...");
        }
    }
}

package edu.wustl.cab2b.server.path.pathgen;

import java.util.Set;

/**
 * Defines the operations that are to be supported by any cache that can be used
 * by {@link edu.wustl.cab2b.server.path.pathgen.GraphPathFinder} and contains
 * implementation to check that the cache is alive.<br>
 * The cache concrete implementations of this class should always call
 * checkAlive() first in each method to check that the cache is still usable.<br>
 * The following notation is used to represent an entry in this cache <br>:
 * P(i->j, N) represents the set of paths from i to j obtained by ignoring the
 * nodes in N, i.e. none of paths in P(i->j, N) will contain any of the nodes in
 * N.<br>
 * Note that for a given source-dest pair (sdp), P(i->j, N1) can be computed
 * trivially from P(i->j, N2) if N1 &sub; N2, by ignoring the paths that contain
 * the nodes in {N2 - N1}.<br>
 * @author srinath_k
 */
abstract class GraphPathFinderCache {
    /**
     * Indicates that the cache is alive.
     */
    private boolean alive;

    GraphPathFinderCache() {
        this.alive = true;
    }

    /**
     * Adds this entry to the cache.
     * @param sdp
     *            source-dest pair
     * @param ignoredNodes
     *            the nodes that were ignored
     * @param paths
     *            the paths computed.
     */
    abstract void addEntry(SourceDestinationPair sdp, Set<Node> ignoredNodes,
                           Set<Path> paths);

    /**
     * If there is an entry with given keys, the paths are returned. Otherwise
     * an entry is found whose ignoredNodes is a subset of the specified
     * ignoredNodes. The paths thus found are pruned to remove any nodes that
     * may need to be ignored.
     * @param sdp
     *            source-dest pait.
     * @param ignoredNodes
     *            ignored nodes
     * @return the set of paths; if no corresponding entry is present in the
     *         cache, then <code>null</code> is returned. Note that an empty
     *         set will be returned if P[sdp, ignoredNodes] is in fact empty
     *         i.e. there are no paths for the given src-dest pair on ignoring
     *         the specified nodes.
     */
    abstract Set<Path> getPathsOnIgnoringNodes(SourceDestinationPair sdp,
                                               Set<Node> ignoredNodes);

    /**
     * @return ALL the paths in the cache.
     */
    abstract Set<Path> getAllPaths();

    /**
     * Cleans up this cache, and marks it dead.
     */
    void cleanup() {
        this.alive = false;
    }

    /**
     * @throws IllegalStateException
     *             if the cache is not alive (i.e. cleanup() has been called on
     *             this cache).
     */
    void checkAlive() {
        if (!alive) {
            throw new IllegalStateException(
                    "Don't ask dead people to help you...");
        }
    }
}

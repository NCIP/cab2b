package edu.wustl.cab2b.server.path.pathgen;

import java.util.Set;

/**
 * Defines the operations that are to be supported by any cache that can be used
 * by {@link edu.wustl.cab2b.server.path.pathgen.GraphPathFinder} and contains
 * implementation to check that the cache is alive.<br>
 * The concrete implementations of this class should always call checkAlive()
 * first in each method to check that the cache is still usable.<br>
 * The following notation is used to represent an entry in this cache : <br>
 * <code>P(i->j, N)</code> represents the set of paths from i to j obtained by
 * ignoring the nodes in N, i.e. none of paths in <code>P(i->j, N)</code> will
 * contain any of the nodes in N.
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
     * Returns the set of paths for given src-dest pair and ignored nodes.
     * Denote the src-dest pair by <code>i->j</code>, and ignoredNodes by N.<br>
     * Let <code>n(p)</code> denote the nodes in a path p. Then, given that
     * <code>N1 &sube; N2</code>, we can compute <code>P(i->j, N1</code>
     * from <code>P(i->j, N2)</code> using the following formula<br>
     * <code>
     * P(i->j, N1) = {p : p &isin; P(i->j, N2), n(p) &cap; N1 = {} }.</code><br>
     * Thus this method is expected to do the following : <br>
     * <ol>
     * <li>If there is an entry in the cache <code>P(i->j, N)</code>, return
     * it, else continue.</li>
     * <li>If there exists an entry in the cache <code>P(i->j, M)</code> such
     * that <code>M &sube;
     * N</code> then compute <code>P(i->j, N)</code>
     * using above formula and return it, else continue</li>
     * <li>Return null</li>
     * </ol>
     * Note that if an empty set of paths is returned, it means that it has been
     * computed already that there are no paths present, i.e.
     * <code>P(i->j, N) = {}</code>.
     * @param sdp
     *            source-dest pair.
     * @param ignoredNodes
     *            ignored nodes.
     * @return the set of paths as found using the steps mentioned above.
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

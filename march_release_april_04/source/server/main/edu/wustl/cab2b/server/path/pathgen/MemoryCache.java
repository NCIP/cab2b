package edu.wustl.cab2b.server.path.pathgen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

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

    public void addEntry(SourceDestinationPair sdp, Set<Node> ignoredNodes, Set<Path> paths) {
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

    public Set<Path> getPathsOnIgnoringNodes(SourceDestinationPair sdp, Set<Node> ignoredNodes) {
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
                    Set<Node> pathNodes = new HashSet<Node>(path.getIntermediateNodes());
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
        this.calculatedPaths = null;
        super.cleanup();
    }

    Set<Path> getAllPaths() {
        Set<Path> res = new HashSet<Path>();
        Iterator<Entry<SourceDestinationPair, PathsOnIgnoringNodes>> calcPathsIter = this.calculatedPaths.entrySet().iterator();
        while (calcPathsIter.hasNext()) {
            Map.Entry<SourceDestinationPair, PathsOnIgnoringNodes> entry = calcPathsIter.next();
            for (Set<Path> paths : entry.getValue().getAllEntries().values()) {
                res.addAll(paths);
            }
            calcPathsIter.remove();
        }
        return res;
    }
}

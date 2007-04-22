package edu.wustl.cab2b.server.path.pathgen;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.wustl.common.util.logger.Logger;

/**
 * Replicates paths through a node for a given set of equivalent nodes. <br>
 * e.g. if replicationNodes has an entry [i, {n1, n2}], then each path that
 * contains the node i is replicated to pass through the nodes n1, n2 instead of
 * i; for instance a path a->b->i->c will be replicated to form the paths
 * a->b->n1->c, a->b->n2->c<br>
 * A special case is that of a self-edge. If a path i->i existed, it would be
 * replicated to form paths {s->d : s &epsilon; {i, n1, n2}, d &epsilon; {i, n1,
 * n2}}.
 * @author srinath_k
 */
class PathReplicationUtil {
    public static Set<Path> replicatePaths(
                                           Set<Path> inputPaths,
                                           Map<Integer, Set<Integer>> replicationNodes) {
        Logger.out.info("Entered PathReplicationUtil...");
        Logger.out.info("No. of input paths : " + inputPaths.size());
        Set<Path> res = new HashSet<Path>(inputPaths);
        for (Map.Entry<Integer, Set<Integer>> entry : replicationNodes.entrySet()) {

            Node origNode = createNode(entry.getKey());
            Set<Integer> repNodes = entry.getValue();
            Logger.out.info("Replicating paths for origNode " + origNode);

            Set<Path> tempRes = new HashSet<Path>(res);
            for (Path path : tempRes) {
                if (path.containsNode(origNode)) {
                    if (path.isSelfEdge()) {
                        res.addAll(processSelfEdge(origNode, repNodes));
                    } else {
                        for (Integer repNodeInt : repNodes) {
                            Node repNode = createNode(repNodeInt);
                            Path newPath = path.replace(origNode, repNode);
                            if (!newPath.isCyclePresent()) {
                                res.add(newPath);
                            }
                        }
                    }
                }
            }
        }
        Logger.out.info("No. of paths post-replication : " + res.size());
        Logger.out.info("Exiting PathReplicationUtil.");
        return res;
    }

    private static Node createNode(int id) {
        return new Node(id);
    }

    private static Set<Path> processSelfEdge(Node origNode,
                                             Set<Integer> repNodes) {
        Set<Node> allNodes = new HashSet<Node>(repNodes.size() + 1);
        allNodes.add(origNode);
        for (Integer repNode : repNodes) {
            allNodes.add(createNode(repNode));
        }

        Set<Path> res = new HashSet<Path>();
        for (Node node1 : allNodes) {
            for (Node node2 : allNodes) {
                res.add(new Path(node1, node2));
            }
        }
        return res;
    }
}

package edu.wustl.cab2b.server.path.pathgen;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
/**
 * Replicates paths through a node for a given set of equivalent nodes. <br>
 * e.g. if replicationNodes has an entry <code>[i, {n1, n2}]</code>, then
 * each path that contains the node i is replicated to pass through the nodes
 * n1, n2 instead of i; for instance a path <code>a->b->i->c</code> will be
 * replicated to form the paths <code>a->b->n1->c, a->b->n2->c.</code><br>
 * <p>
 * Note that if there is another entry <code>[n1, {x, y, ...}]</code> the
 * paths through i are also replicated for {x, y, ...}. Thus the replicated
 * paths are propagated along the replication nodes.
 * <p>
 * A special case is that of a self-edge. If a path <code>i->i</code> existed,
 * it would be replicated to form paths
 * <code>{s->d : s &isin; {i, n1, n2}, d &isin; {i, n1,
 * n2}}</code>.
 * 
 * @author srinath_k
 */
class PathReplicationUtil {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(PathReplicationUtil.class);
    private static void replicateAndPropagate(Map<Integer, Set<Integer>> replicationNodes,
                                              Set<Path> replicatedPaths, Integer key) {
        if (!replicationNodes.containsKey(key)) {
            return;
        }
        Node origNode = createNode(key);
        Set<Integer> repNodes = replicationNodes.get(key);
        logger.info("Replicating paths for origNode " + origNode);

        Set<Path> relevantPaths = new HashSet<Path>();
        Set<Path> origPaths = new HashSet<Path>(replicatedPaths);
        for (Path path : origPaths) {
            if (path.containsNode(origNode)) {
                relevantPaths.add(path);
            }
        }
        for (Integer repNodeInt : repNodes) {
            Node repNode = createNode(repNodeInt);
            Set<Path> repPathsForRepNode = new HashSet<Path>();
            for (Path path : relevantPaths) {
                if (path.isSelfEdge()) {
                    Set<Path> newPaths = processSelfEdge(origNode, repNodes);
                    repPathsForRepNode.addAll(newPaths);
                } else {
                    Path newPath = path.replace(origNode, repNode);
                    if (!newPath.isCyclePresent()) {
                        repPathsForRepNode.add(newPath);
                    }
                }

            }
            replicateAndPropagate(replicationNodes, repPathsForRepNode, repNodeInt);
            replicatedPaths.addAll(repPathsForRepNode);
        }
    }

    public static Set<Path> replicatePaths(Set<Path> inputPaths, Map<Integer, Set<Integer>> replicationNodes) {
        logger.info("Entered PathReplicationUtil...");
        logger.info("No. of input paths : " + inputPaths.size());

        Set<Path> res = new HashSet<Path>(inputPaths);
        for (Integer key : replicationNodes.keySet()) {
            Set<Path> replicatedPaths = new HashSet<Path>(res);
            replicateAndPropagate(replicationNodes, replicatedPaths, key);
            res.addAll(replicatedPaths);
        }
        logger.info("No. of paths post-replication : " + res.size());
        logger.info("Exiting PathReplicationUtil.");
        return res;
    }

    private static Node createNode(int id) {
        return new Node(id);
    }

    private static Set<Path> processSelfEdge(Node origNode, Set<Integer> repNodes) {
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

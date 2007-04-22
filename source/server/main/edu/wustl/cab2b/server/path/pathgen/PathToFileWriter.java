package edu.wustl.cab2b.server.path.pathgen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

/**
 * Util to write a set of paths to a file. Each path is written as one line. The
 * line is of the following form :<br>
 * srcNodeId###interNodeid1_interNodeid2_..._interNodeid2n###destNodeId<br>
 * e.g. 1###6_3_9_7###23; here 1 is srcNodeId, 23 is destNodeId and {6,3,9,7}
 * are the intermediate nodes' ids.<br>
 * In above example, ### is referred to as END_NODES_DELIMITER, and _ as
 * INTERMEDIATE_NODES_DELIMITER. These two properties (and the output file name)
 * can be customized.<br>
 * Default values : <br>
 * <code>
 * END_NODES_DELIMITER = "###"<br>
 * INTERMEDIATE_NODES_DELIMITER = "_"<br>
 * FILE_NAME = "(userhome)/Paths.txt"<br>
 * </code><br>
 * The caller can specify an array of "nodeTags"; a node tag is something that
 * the caller want written instead of the nodeId. e.g. if each node represents a
 * city in the original graph, then corresponding city names can be present as
 * the nodeTag.
 * @author srinath_k
 */
public class PathToFileWriter {
    private static final String DEFAULT_END_NODES_DELIMITER = "###";

    private static final String DEFAULT_INTERMEDIATE_NODES_DELIMITER = "_";

    private static final String DEFAULT_FILE_NAME = System.getProperty("user.home")
            + File.separator + "Paths.txt";

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static String END_NODES_DELIMITER = DEFAULT_END_NODES_DELIMITER;

    public static String INTERMEDIATE_NODES_DELIMITER = DEFAULT_INTERMEDIATE_NODES_DELIMITER;

    public static String FILE_NAME = DEFAULT_FILE_NAME;

    /**
     * If true, the specified file is opened in append mode.
     * @see FileWriter#FileWriter(java.io.File, boolean)
     */
    public static boolean APPEND = true;

    private static String getEndNodesDelimiter() {
        if (END_NODES_DELIMITER == null) {
            return DEFAULT_END_NODES_DELIMITER;
        } else {
            return END_NODES_DELIMITER;
        }
    }

    private static String getIntermediateNodesDelimiter() {
        if (INTERMEDIATE_NODES_DELIMITER == null) {
            return DEFAULT_INTERMEDIATE_NODES_DELIMITER;
        } else {
            return INTERMEDIATE_NODES_DELIMITER;
        }
    }

    private static String getFileName() {
        if (FILE_NAME == null) {
            return DEFAULT_FILE_NAME;
        } else {
            return FILE_NAME;
        }
    }

    public static void writePathsToFile(Set<Path> paths, Object[] nodeTags) {
        try {
            FileWriter writer = new FileWriter(new File(getFileName()), APPEND);
            String endNodesDelimiter = getEndNodesDelimiter();
            String intermediateNodesDelimiter = getIntermediateNodesDelimiter();

            for (Path path : paths) {
                Node srcNode = path.fromNode();
                Node destNode = path.toNode();
                StringBuffer temp = new StringBuffer();

                if (nodeTags != null) {
                    temp.append(nodeTags[srcNode.getId()]);
                } else {
                    temp.append(srcNode.getId());
                }
                temp.append(endNodesDelimiter);

                boolean isFirst = true;
                for (Node interNode : path.getIntermediateNodes()) {
                    if (!isFirst) {
                        temp.append(intermediateNodesDelimiter);
                    }
                    if (nodeTags != null) {
                        temp.append(nodeTags[interNode.getId()]);
                    } else {
                        temp.append(interNode.getId());
                    }
                    isFirst = false;
                }
                temp.append(endNodesDelimiter);
                if (nodeTags != null) {
                    temp.append(nodeTags[destNode.getId()]);
                } else {
                    temp.append(destNode.getId());
                }
                temp.append(LINE_SEPARATOR);
                writer.write(temp.toString());
                writer.flush();
            }
            writer.close();
        } catch (IOException ioex) {
            System.out.println("Failed to create output file in current directory"
                    + ioex.getMessage());
        }
    }
}

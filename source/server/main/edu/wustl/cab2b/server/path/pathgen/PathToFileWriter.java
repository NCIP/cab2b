package edu.wustl.cab2b.server.path.pathgen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class PathToFileWriter {
    public static String END_NODES_DELIMITER = "###";

    public static String LINE_SEPARATOR = System.getProperty("line.separator");

    public static String INTERMEDIATE_NODES_DELIMITER = "_";

    public static String FILE_NAME = System.getProperty("user.home")
            + File.separator + "Paths.txt";

    public static boolean APPEND = true;

    public static void writePathsToFile(Set<Path> paths, Object[] nodeTags) {
        try {
            FileWriter writer = new FileWriter(new File(FILE_NAME), APPEND);

            for (Path path : paths) {
                Node srcNode = path.fromNode();
                Node destNode = path.toNode();
                StringBuffer temp = new StringBuffer();

                if (nodeTags != null) {
                    temp.append(nodeTags[srcNode.getId()]);
                } else {
                    temp.append(srcNode.getId());
                }
                temp.append(END_NODES_DELIMITER);

                boolean isFirst = true;
                for (Node interNode : path.getIntermediateNodes()) {
                    if (!isFirst) {
                        temp.append(INTERMEDIATE_NODES_DELIMITER);
                    }
                    if (nodeTags != null) {
                        temp.append(nodeTags[interNode.getId()]);
                    } else {
                        temp.append(interNode.getId());
                    }
                    isFirst = false;
                }
                temp.append(END_NODES_DELIMITER);
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

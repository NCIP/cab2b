/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2bwebapp.bizlogic.executequery;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2bwebapp.bizlogic.UserBackgroundQueries;
import edu.wustl.cab2bwebapp.constants.Constants;

/**
 * Class for doing CSV related operations.
 * @author deepak_shingan
 *
 */
public class CsvWriter {
    private List<AttributeInterface> headers = null;

    private BufferedWriter out = null;

    public CsvWriter(List<AttributeInterface> headers, String fileName) {
        this.headers = headers;
        try {
            FileWriter fstream = new FileWriter(UserBackgroundQueries.EXPORT_CSV_DIR + File.separator + fileName);
            out = new BufferedWriter(fstream);
            writeHeaders();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while creating file.");
        }

    }

    /**
     * Writes headers to file.
     * @throws IOException
     */
    private void writeHeaders() throws IOException {
        if (headers != null) {
            for (AttributeInterface attribute : headers) {
                out.append(attribute.getName());
                out.append(',');
            }

            out.append(Constants.MODEL_NAME);
            out.append(',');
            out.append(Constants.HOSTING_CANCER_RESEARCH_CENTER);
            out.append(',');
            out.append(Constants.POINT_OF_CONTACT);
            out.append(',');
            out.append(Constants.CONTACT_EMAIL);
            out.append(',');
            out.append(Constants.HOSTING_INSTITUTION);
            out.append(',');
            out.append('\n');
            out.flush();
        }
    }

    /**
     * Writes records/date to file.
     * @param recordMap
     * @param serviceUrlMetadata
     * @throws IOException
     */
    public void writeData(List<Map<AttributeInterface, Object>> recordList, ServiceURLInterface serviceUrlMetadata)
            throws IOException {
        String modelName =
                Utility.createModelName(serviceUrlMetadata.getDomainModel(), serviceUrlMetadata.getVersion());
        String centerName = Utility.getHostingInstitutionName(serviceUrlMetadata);

        if (recordList != null) {
            for (Map<AttributeInterface, Object> recordMap : recordList) {
                for (AttributeInterface attribute : headers) {
                    Object val = recordMap.get(attribute);
                    append(getEscapedValue(val));
                }
                append(modelName);
                append(serviceUrlMetadata.getHostingCenter());
                append(serviceUrlMetadata.getContactName());
                append(serviceUrlMetadata.getContactMailId());
                append(centerName);
                out.append('\n');
                out.flush();
            }
        }
    }

    /**
     * Returns CSV safe string by handling special character strings.   
     * @param val
     * @return
     */
    private String getEscapedValue(Object val) {
        String value = val == null ? "" : val.toString();
        value = value.replace("\"", "\"\"");
        if (value.contains(",") || value.contains("\n")) {
            value = '"' + value + '"';
        }
        return value;
    }

    /**
     * @param value
     * @throws IOException
     */
    private void append(Object value) throws IOException {
        out.append(value == null ? "" : value.toString());
        out.append(',');
    }

    /**
     * Close the file stream.
     * @throws IOException
     */
    public void closeFile() {
        if (out != null) {
            try {
                out.close();
                out = null;
            } catch (IOException e) {
                //Do nothing
                e.printStackTrace();
            }
        }
    }

    /**
     * This method adds Query metadata like Query title, conditions, description, user in the exported result sheet.  
     * @param qStatus
     */
    public static void addMetadataToFile(QueryStatus qStatus) {
        ICab2bQuery query = qStatus.getQuery();
        String queryTitle = "Query Title: " + query.getName() + "\n";
        String querydescription =
                '"' + "Query Description: " + query.getDescription().replaceAll("\"", "\"\"") + '"' + "\n";
        String executionDate = "Execution Date: " + qStatus.getQueryStartTime() + "\n";
        String executedBy = qStatus.getUser().getUserName();
        executedBy =
                "Executed By: " + executedBy.substring(executedBy.lastIndexOf("=") + 1, executedBy.length())
                        + "\n\n";
        String queryConditions = "Conditions\n";
        String pattern = "(.*)\\((.*)\\)(.*)";
        String values[] = qStatus.getQueryConditions().split(";");
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        queryConditions += "Parameter,Condition,Value\n";
        for (int j = 0; j < values.length; j++) {
            Matcher m = p.matcher(values[j]);
            m.find();
            queryConditions +=
                    '"'
                            + m.group(1).replaceAll("\"", "\"\"")
                            + '"'
                            + ","
                            + '"'
                            + edu.wustl.cab2b.common.util.Utility.getFormattedString(m.group(2))
                                .replaceAll("\"", "\"\"")
                            + '"'
                            + ","
                            + '"'
                            + (m.group(3) == null || m.group(3).equals("null") ? "" : m.group(3)
                                .replaceAll("\"", "\"\"")) + '"' + "\n";
        }
        queryConditions += "\n";
        File file = new File(UserBackgroundQueries.EXPORT_CSV_DIR + File.separator + qStatus.getFileName());
        byte fileContents[] = new byte[(int) file.length()];
        String updatedFileContents = queryTitle + querydescription + executionDate + executedBy; // + queryConditions;

        try {
            RandomAccessFile rafin = new RandomAccessFile(file, "r");
            rafin.readFully(fileContents);
            rafin.close();
            updatedFileContents += new String(fileContents);
	    updatedFileContents += "\n";
	    updatedFileContents += queryConditions;
            RandomAccessFile rafout = new RandomAccessFile(file, "rw");
            rafout.setLength(updatedFileContents.length());
            rafout.writeBytes(updatedFileContents);
            rafout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        closeFile();
    }
}

/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.queryengine.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.jmock.MockObjectTestCase;

public abstract class BaseQueryEngineTestUtil extends MockObjectTestCase {
   
    protected String testText(String file) {
        return testText(this, file);
    }

    // protected String testText(String file, String defaultString) {
    // return testText(this, file, defaultString);
    // }

    protected String testText(Object obj, String file) {
        try {
            InputStream stream = obj.getClass().getResourceAsStream(file);
            if (stream == null)
                // return defaultString;
                return "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    stream));
            try {
                StringBuffer buffer = new StringBuffer();
                int ch = reader.read();
                while (ch >= 0) {
                    buffer.append((char) ch);
                    ch = reader.read();
                }
                return buffer.toString().replaceAll(
                                                    System.getProperty("line.separator"),
                                                    "\n");
            } finally {
                if (reader != null)
                    reader.close();
            }
        } catch (IOException ex) { // convert to unchecked
            throw new RuntimeException(ex);
        }
    }

    protected void writeText(String existingFile, String newFile,
                             String textToWrite) {
        writeText(this, existingFile, newFile, textToWrite);
    }

    protected void writeText(Object obj, String existingFile, String newFile,
                             String textToWrite) {
        writeText(obj.getClass(), existingFile, newFile, textToWrite);
    }

    protected void writeText(Class c, String existingFile, String newFile,
                             String textToWrite) {
        try {
            URL existingResource = c.getResource(existingFile);
            URI newResource = new URI(
                    new URL(existingResource, newFile).toString());
            FileWriter tgtFile = new FileWriter(new File(newResource));
            try {
                tgtFile.write(textToWrite);
            } finally {
                tgtFile.close();
            }
        } catch (MalformedURLException ex) { // convert to unchecked
            ex.printStackTrace();
        } catch (URISyntaxException ex) { // convert to unchecked
            ex.printStackTrace();
        } catch (IOException ex) { // convert to unchecked
            ex.printStackTrace();
        }
    }

    protected String rationalizeWhitespace(String input) {
        return input.replaceAll("\\s+", " ");
    }
}

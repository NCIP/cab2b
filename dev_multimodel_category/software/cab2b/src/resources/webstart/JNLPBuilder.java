/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author Juber Patel
 *
 */
public class JNLPBuilder {

    private Document templateDoc;

    private File destDir;

    private Document clientJNLP;

    public JNLPBuilder(File templateFile, File clientJNLP, File destDir) throws Exception {
        this.destDir = destDir;

        //create Document for the client JNLP file and for the template file
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        this.templateDoc = builder.parse(templateFile);
        this.clientJNLP = builder.parse(clientJNLP);
        
        //remove all the 'extension' elements
        Element resources = (Element) this.clientJNLP.getElementsByTagName("resources").item(0);
        NodeList extensions = this.clientJNLP.getElementsByTagName("extension");
        int nodes = extensions.getLength();
        //getLength returns different values 
        while(extensions.getLength()>0) {
            resources.removeChild(extensions.item(0));
        }
        
        //save();

    }

    /**
     * cretae the JNLP file for the given jar file name and add an entry to the client JNLP file
     * @param jarFile the jar file name, along with .jar extension
     */
    public void createJNLP(String jarFile) throws Exception {

        String name = jarFile.trim().substring(0, jarFile.trim().lastIndexOf("."));
        File jnlpFile = new File(destDir, name + ".jnlp");

        NodeList list = templateDoc.getElementsByTagName("jnlp");
        Element jnlp = (Element) (list.item(0));
        jnlp.setAttribute("href", "cab2b/webpage/jnlp/" + name + ".jnlp");

        list = templateDoc.getElementsByTagName("jar");
        Element jar = (Element) (list.item(0));
        jar.setAttribute("href", "cab2b/webpage/client/" + name + ".jar");

        //write the document using a "do nothing" transformer 
        //as there is no other way to save the document to a new file
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.transform(new DOMSource(templateDoc), new StreamResult(new FileOutputStream(jnlpFile)));

        //make an 'extension' entry into the client JNLP 
        Element extension = clientJNLP.createElement("extension");
        extension.setAttribute("name", name);
        extension.setAttribute("href", "cab2b/webpage/jnlp/" + name + ".jnlp");
        Element resources = (Element) clientJNLP.getElementsByTagName("resources").item(0);
        resources.appendChild(extension);

    }

    /**
     * save the client jnlp Document to the client jnlp file
     * @throws Exception
     */
    public void saveClientJNLP() throws Exception {

        //write the document using a "do nothing" transformer 
        //as there is no other way to save the document to a new file
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.transform(new DOMSource(clientJNLP), new StreamResult(new FileOutputStream(new File(destDir,
                "cab2b_client_webstart.jnlp"))));

    }

    /**
     * @param args
     *  args[0] location of the template file
     *  args[1] location of client JNLP's template 
     *  args[2] directory that holds the jars for which to create jnlp's
     *  args[3] destination directory for jnlp's
     */
    public static void main(String[] args) throws Exception{

        File templateFile = new File(args[0]);
        File clientJNLP = new File(args[1]);
        File jarDir = new File(args[2]);
        File destDir = new File(args[3]);
        JNLPBuilder jnlpBuilder = new JNLPBuilder(templateFile, clientJNLP, destDir);

        String[] files = jarDir.list();

        for (String jarFile : files) {
            jnlpBuilder.createJNLP(jarFile);
        }

       jnlpBuilder.saveClientJNLP();

    }

}

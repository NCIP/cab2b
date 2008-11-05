package edu.wustl.cab2b.client.ui.controls;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * A class extension of JFileChooser having some more properties, provides a simple mechanism for the user to choose a file.
 * @author chetan_bh
 *
 */
public class Cab2bFileFilter extends FileFilter {

    /**
     * Array of extension
     */
    private String[] extensionFilter;

    /**
     * Constructs a FileChooser pointing to the user's default directory having extensions provided in extensionArray
     * @param extensionArray
     */
    public Cab2bFileFilter(String[] extensionArray) {
        extensionFilter = extensionArray;
    }

    /* (non-Javadoc)
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    /**
     * Function checks whether the file is a directory or if that file type is allowed or not
     * @param file which needs to be checked 
     */
    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        int extnIndex = file.getName().lastIndexOf(".");
        if (extnIndex != -1) {
            String actualExt = file.getName().substring(extnIndex + 1);
            for (int i = 0; i < extensionFilter.length; i++) {
                if (extensionFilter[i].compareToIgnoreCase(actualExt) == 0)
                    return true;
            }
        }
        return false;
    }

    /** gets the Description a file
     * @see javax.swing.filechooser.FileFilter#getDescription()
     * @return String
     */
    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        StringBuffer description = new StringBuffer();
        for (int i = 0; i < extensionFilter.length; i++) {
            if (i > 0) {
                description.append(" | ");
            }
            description.append("*.").append(extensionFilter[i]);
        }
        return description.toString();
    }

}

/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;
import java.awt.Toolkit;
import java.text.Format;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * Class for returning text field with specific properties depending on parameters provided. 
 * @author chetan_bh
 */
public class Cab2bFormattedTextField extends JFormattedTextField {
    private static final long serialVersionUID = 1L;

    /**
     * For getting native method implementation
     */
    private static final Toolkit toolkit = Toolkit.getDefaultToolkit();

    /** For plain fields*/
    public static final int PLAIN_FIELD = 0;

    /** For positive integers */
    public static final int WHOLE_NUMBER_FIELD = 1;

    /** For positive and negative integers */
    public static final int INTEGER_NUMBER_FIELD = 2;

    /** For decimals */
    public static final int FLOAT_NUMBER_FIELD = 3;

    /** For dates */
    public static final int DATE_FILED = 4;

    /** For alpha-numeric fields, with no special characters */
    public static final int ALPHA_NUMERIC_FIELD = 5;

    /**
     * Flag for setting command
     */
    private boolean isCommaAllowed = false;
    
    /**
     * Class for setting text field documents 
     */
    private DocumentFactory documentFactory = new DocumentFactory();

    /** Default fieldType is Float */
    private int fieldType = 2;
    /**
     * Method to set if comma is allowed in the formatted string
     * This is required when user specified condition is In
     * and this text field is going to contain comma separated values
     * @param commaAllowed
     */
    public void setCommaAllowed(boolean commaAllowed) {
        isCommaAllowed = commaAllowed;
    }

    /**
     * @param columns
     * @param fieldType
     */
    public Cab2bFormattedTextField(int columns, int fieldType) {
        this.fieldType = fieldType;
        setDocument(documentFactory.getDocument(fieldType));
        initializeFormatter();
        this.setColumns(columns);
        //setBorder(ClientConstants.border);
        setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));
    }

    /**
     * 
     */
    private void initializeFormatter() {
        //dateFormatter = DateFormat.getInstance();
    }

    /**
     * Constructor
     * @param value
     */
    public Cab2bFormattedTextField(Object value) {
        super(value);
    }

    /**
     * Constructor
     * @param format
     */
    public Cab2bFormattedTextField(Format format) {
        super(format);
    }

    /**
     * Constructor
     * @param formatter
     * @param fieldType
     */
    public Cab2bFormattedTextField(AbstractFormatter formatter, int fieldType) {
        super(formatter);
        this.fieldType = fieldType;
        setDocument(documentFactory.getDocument(fieldType));
        initializeFormatter();
        //this.setFormatter(formatter);
    }

    /**
     * Constructor
     * @param factory
     */
    public Cab2bFormattedTextField(AbstractFormatterFactory factory) {
        super(factory);
    }

    /**
     * Constructor
     * @param factory
     * @param currentValue
     */
    public Cab2bFormattedTextField(AbstractFormatterFactory factory, Object currentValue) {
        super(factory, currentValue);
    }

    /** 
     * This method calls a factory method to get the correct document 
     * depending on the fieldType input parameter 
     * @return Document
     * @see javax.swing.JTextField#createDefaultModel()
     */
    protected Document createDefaultModel() {
        if (documentFactory == null) {
            documentFactory = new DocumentFactory();
        }
        return documentFactory.getDocument(fieldType);
    }

    /**
     * Utility method to find the number of occurrence of a particular character in the String 
     * @param str
     * @param character
     * @return
     */
    private int countCharacterIn(String str, char character) {
        int count = 0;

        char[] chars = str.toCharArray();

        for (char characterInStr : chars) {
            if (characterInStr == character) {
                count++;
            }
        }
        return count;
    }

    /**
     * Changed focus view of textfield
     */
    public void changeFocus() {
        Border newBorder = new BevelBorder(1, Color.RED, Color.RED);
        this.setBorder(newBorder);
        this.updateUI();
    }

    /**
     * @author chetan_bh
     */
    protected class WholeNumberDocument extends PlainDocument {
        private static final long serialVersionUID = 1L;

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            char[] source = str.toCharArray();
            char[] result = new char[source.length];
            int j = 0;

            for (int i = 0; i < result.length; i++) {
                if (Character.isDigit(source[i])) {
                    result[j++] = source[i];
                } else if ((isCommaAllowed == true) && (source[i] == ',')) {
                    result[j++] = source[i];
                } else {
                    toolkit.beep();
                }
            }
            super.insertString(offs, new String(result, 0, j), a);
        }
    }

    /**
     * @author chetan_bh
     */

    protected class DecimalNumberDocument extends PlainDocument {
        private static final long serialVersionUID = 1L;

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            char[] source = str.toCharArray();
            char[] result = new char[source.length];
            int j = 0;

            String text = this.getText(0, this.getLength());

            int charCount = countCharacterIn(text, '.');

            boolean singleOccurenceOfDeciPoint = (charCount == 1) ? true : false;
            for (int i = 0; i < result.length; i++) {
                if (Character.isDigit(source[i]) || (source[i] == '-' && this.getLength() == 0) || (source[i] == '.')
                        && !singleOccurenceOfDeciPoint) {
                    result[j++] = source[i];
                } else if ((isCommaAllowed == true) && (source[i] == ',')) {
                    result[j++] = source[i];
                } else {
                    toolkit.beep();
                }
            }
            super.insertString(offs, new String(result, 0, j), a);
        }
    }

    /**
     * @author chetan_bh
     */
    protected class IntegerNumberDocument extends PlainDocument {
        private static final long serialVersionUID = 1L;

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            char[] source = str.toCharArray();
            char[] result = new char[source.length];
            int j = 0;
            for (int i = 0; i < result.length; i++) {
                if (Character.isDigit(source[i]) || (source[i] == '-') && this.getLength() == 0) {
                    result[j++] = source[i];
                } else if ((isCommaAllowed == true) && (source[i] == ',')) {
                    result[j++] = source[i];
                } else {
                    toolkit.beep();
                }
            }
            super.insertString(offs, new String(result, 0, j), a);
        }
    }

    /**
     * @author chetan_bh
     */
    protected class AlphaNumericDocument extends PlainDocument {
        private static final long serialVersionUID = 1L;

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            char[] source = str.toCharArray();
            char[] result = new char[source.length];
            int j = 0;
            for (int i = 0; i < result.length; i++) {
                if (Character.isDigit(source[i]) || Character.isLetter(source[i])) {
                    result[j++] = source[i];
                } else {
                    toolkit.beep();
                    //changeFocus();s                    
                }
            }
            super.insertString(offs, new String(result, 0, j), a);
        }
    }

    /**
     * @author chetan_bh
     */
    /* TODO : This is not functional yet */
    protected class DateDocument extends PlainDocument {
        private static final long serialVersionUID = 1L;

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        }
    }

    /**
     * @author chetan_bh
     */
    protected class DocumentFactory {

        /**
         * @param fieldType
         * @return
         */
        public PlainDocument getDocument(int fieldType) {
            //TODO 	if(fieldType == DATE_FILED)	{		}
            PlainDocument document = null;
            switch (fieldType) {
                case WHOLE_NUMBER_FIELD:
                    document = new WholeNumberDocument();
                    break;
                case INTEGER_NUMBER_FIELD:
                    document = new IntegerNumberDocument();
                    break;
                case FLOAT_NUMBER_FIELD:
                    document = new DecimalNumberDocument();
                    break;
                case DATE_FILED:
                    document = new DateDocument();
                    break;
                case ALPHA_NUMERIC_FIELD:
                    document = new AlphaNumericDocument();
                    break;
                case PLAIN_FIELD:
                    document = new PlainDocument();
                    break;
                default:
                    throw new IllegalArgumentException("Field type is unknown");
            }
            return document;
        }
    }
}
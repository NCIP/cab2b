package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;
import java.awt.Toolkit;
import java.text.Format;

import javax.swing.JFormattedTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import edu.wustl.cab2b.client.ui.util.ClientConstants;

/**
 * @author chetan_bh
 */
public class Cab2bFormattedTextField extends JFormattedTextField {
    private static final long serialVersionUID = 1L;

    private static final Toolkit toolkit = Toolkit.getDefaultToolkit();

    //private DateFormat dateFormatter;

    private DocumentFactory documentFactory = new DocumentFactory();

    /* Default fieldType is Float */
    int fieldType = 2;

    /**/
    public static final int PLAIN_FIELD = 0;

    /* For positive integers */
    public static final int WHOLE_NUMBER_FIELD = 1;

    /* For positive and negative integers */
    public static final int INTEGER_NUMBER_FIELD = 2;

    /* For decimals */
    public static final int FLOAT_NUMBER_FIELD = 3;

    /* For dates */
    public static final int DATE_FILED = 4;

    /* For alpha-numeric fields, with no special characters */
    public static final int ALPHA_NUMERIC_FIELD = 5;

    private boolean m_commaAllowed = false;

    /**
     * Method to set if comma is allowed in the formmated string
     * This is required when user specified condition is In
     * and this text field is going to contain comma seperated values
     * @param commaAllowed
     */
    public void setCommaAllowed(boolean commaAllowed) {
        m_commaAllowed = commaAllowed;
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
        setBorder(ClientConstants.border);
    }

    private void initializeFormatter() {
        //dateFormatter = DateFormat.getInstance();
    }

    public Cab2bFormattedTextField(Object value) {
        super(value);
    }

    public Cab2bFormattedTextField(Format format) {
        super(format);
    }

    public Cab2bFormattedTextField(AbstractFormatter formatter, int fieldType) {
        super(formatter);
        this.fieldType = fieldType;
        setDocument(documentFactory.getDocument(fieldType));
        initializeFormatter();
        //this.setFormatter(formatter);
    }

    public Cab2bFormattedTextField(AbstractFormatterFactory factory) {
        super(factory);
    }

    public Cab2bFormattedTextField(AbstractFormatterFactory factory, Object currentValue) {
        super(factory, currentValue);
    }

    /* This method calls a factory method to get the correct doument 
     * depending on the fieldType input parameter */
    protected Document createDefaultModel() {
        if (documentFactory == null)
            documentFactory = new DocumentFactory();
        return documentFactory.getDocument(fieldType);
    }

    /* Utility method to find the number of occurrence of a particular character in the String */
    private int countCharacterIn(String str, char character) {
        int count = 0;

        char[] chars = str.toCharArray();

        for (char characterInStr : chars) {
            if (characterInStr == character)
                count++;
        }
        return count;
    }

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
                } else if ((m_commaAllowed == true) && (source[i] == ',')) {
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
                if (Character.isDigit(source[i]) || (source[i] == '-' && this.getLength() == 0)
                        || (source[i] == '.') && !singleOccurenceOfDeciPoint)
                    result[j++] = source[i];
                else if ((m_commaAllowed == true) && (source[i] == ',')) {
                    result[j++] = source[i];
                } else {
                    toolkit.beep();
                    System.err.println("insertString: " + source[i]);
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
                } else if ((m_commaAllowed == true) && (source[i] == ',')) {
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
                if (Character.isDigit(source[i]) || Character.isLetter(source[i]))
                    result[j++] = source[i];
                else {
                    toolkit.beep();
                    //changeFocus();
                    System.err.println("insertString: " + source[i]);
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
            //			String text = getText(0, this.getLength());
            //			try
            //			{
            //				Date userEnteredDate = dateFormatter.parse(text);
            //			}
            //			catch (ParseException e)
            //			{
            //				System.err.println("date format not correct !");
            //			}
            //			super.insertString(offs, str, a);
        }
    }

    /**
     * @author chetan_bh
     */
    protected class DocumentFactory {

        public PlainDocument getDocument(int fieldType) {
            //TODO 	if(fieldType == DATE_FILED)	{		}
            PlainDocument document = null;
            if (fieldType == WHOLE_NUMBER_FIELD)
                document = new WholeNumberDocument();
            else if (fieldType == INTEGER_NUMBER_FIELD)
                document = new IntegerNumberDocument();
            else if (fieldType == FLOAT_NUMBER_FIELD)
                document = new DecimalNumberDocument();
            else if (fieldType == DATE_FILED)
                document = new DateDocument();
            else if (fieldType == ALPHA_NUMERIC_FIELD)
                document = new AlphaNumericDocument();
            else if (fieldType == PLAIN_FIELD)
                document = new PlainDocument();
            return document;
        }
    }
}
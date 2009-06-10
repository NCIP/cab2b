package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.MaskFormatter;
import javax.swing.text.PlainDocument;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.WindowUtilities;

/**
 * 
 * @author chetan_bh
 */
public class Cab2bFormattedTextField extends JFormattedTextField
{
	private static final Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	private NumberFormat numberFormatter;
	private DateFormat dateFormatter;
	
	public DocumentFactory documentFactory = new DocumentFactory();
	
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
	public void setCommaAllowed(boolean commaAllowed)
	{
		m_commaAllowed = commaAllowed;
	}
	public Cab2bFormattedTextField(int columns, int fieldType)
	{
		this.fieldType = fieldType;
		setDocument(documentFactory.getDocument(fieldType));
		initializeFormatter();
		this.setColumns(columns);
	}
	
	private void initializeFormatter()
	{
		numberFormatter = NumberFormat.getInstance();
		dateFormatter = DateFormat.getInstance();
	}
	
	public Cab2bFormattedTextField(Object value)
	{
		super(value);
	}

	public Cab2bFormattedTextField(Format format)
	{
		super(format);
	}

	public Cab2bFormattedTextField(AbstractFormatter formatter,int fieldType)
	{
		super(formatter);
		this.fieldType = fieldType;
		setDocument(documentFactory.getDocument(fieldType));
		initializeFormatter();
		//this.setFormatter(formatter);
	}

	public Cab2bFormattedTextField(AbstractFormatterFactory factory)
	{
		super(factory);
	}

	public Cab2bFormattedTextField(AbstractFormatterFactory factory, Object currentValue)
	{
		super(factory, currentValue);
	}
	
	/* This method calls a factory method to get the correct doument 
	 * depending on the fieldType input parameter */
	protected Document createDefaultModel()
	{
		if(documentFactory == null)
			documentFactory = new DocumentFactory();
		return documentFactory.getDocument(fieldType);
	}
	
	/* Utility method to find the number of occurrence of a particular character in the String */
	private int countCharacterIn(String str, char character)
	{
		int count = 0;
		
		char[] chars = str.toCharArray();
		
		for(char characterInStr : chars)
		{
			if(characterInStr == character)
				count++;
		}
		return count;
	}
	
	public void changeFocus()
	{
		Border newBorder = new BevelBorder( 1, Color.RED,Color.RED);
		this.setBorder(newBorder);
		this.updateUI();
	}
	
	public static void main(String[] args)
	{
		JPanel panel = new JPanel(new RiverLayout());
		
		// 1
		final Cab2bFormattedTextField cab2bFTF0 = new Cab2bFormattedTextField(10, Cab2bFormattedTextField.PLAIN_FIELD);
		panel.add("p", new JLabel("Plain Text : "));
		panel.add("tab", cab2bFTF0);
		
		// 2
		final Cab2bFormattedTextField cab2bFTF1 = new Cab2bFormattedTextField(10, Cab2bFormattedTextField.WHOLE_NUMBER_FIELD);
		panel.add("p",new JLabel("Counting Number : "));
		panel.add("tab",cab2bFTF1);
		
		// 3
		final Cab2bFormattedTextField cab2bFTF2 = new Cab2bFormattedTextField(10, Cab2bFormattedTextField.INTEGER_NUMBER_FIELD);
		panel.add("p",new JLabel("Integer Number : "));
		panel.add("tab",cab2bFTF2);
		
		// 4
		final Cab2bFormattedTextField cab2bFTF3 = new Cab2bFormattedTextField(10, Cab2bFormattedTextField.FLOAT_NUMBER_FIELD);
		panel.add("p",new JLabel("Floating Number : "));
		panel.add("tab",cab2bFTF3);
		
		
		MaskFormatter maskFormatter = null;
		MaskFormatter maskFormatter1 = null;
		try{
			maskFormatter = new MaskFormatter("##/##/####");
			maskFormatter1 = new MaskFormatter("##/ULL/####");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		// 5
		final Cab2bFormattedTextField cab2bFTF4 = new Cab2bFormattedTextField(maskFormatter, Cab2bFormattedTextField.DATE_FILED);
		cab2bFTF4.setColumns(10);
		panel.add("p",new JLabel("Date : "));
		panel.add("tab", cab2bFTF4);
		
		// 6
		final Cab2bFormattedTextField cab2bFTF5 = new Cab2bFormattedTextField(maskFormatter1, Cab2bFormattedTextField.DATE_FILED);
		cab2bFTF5.setColumns(10);
		panel.add("p", new JLabel("Date (with month name) : "));
		panel.add("tab", cab2bFTF5);
		
		final Cab2bFormattedTextField cab2bFTF6 = new Cab2bFormattedTextField(10, Cab2bFormattedTextField.ALPHA_NUMERIC_FIELD);
		
		panel.add("p",new JLabel("Alpha Numeric Field : "));
		panel.add("tab", cab2bFTF6);
		
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					System.out.println("cab2bFTF0 "+cab2bFTF0.getValue()+", "+cab2bFTF0.getText());
					System.out.println("cab2bFTF1 "+cab2bFTF1.getValue()+", "+cab2bFTF1.getText());
					System.out.println("cab2bFTF2 "+cab2bFTF2.getValue()+", "+cab2bFTF2.getText());
					System.out.println("cab2bFTF3 "+cab2bFTF3.getValue()+", "+cab2bFTF3.getText());
					System.out.println("cab2bFTF4 "+cab2bFTF4.getValue()+", "+cab2bFTF4.getText());
					System.out.println("cab2bFTF5 "+cab2bFTF5.getValue()+", "+cab2bFTF5.getText());
					System.out.println("cab2bFTF6 "+cab2bFTF6.getValue()+", "+cab2bFTF6.getText());
					
					System.out.println("Thread in AL "+Thread.currentThread().getName());
					
				}
			}
		);
		
		panel.add("p",okButton);
		
		WindowUtilities.showInFrame(panel, "Cab2bFormattedTextField");
	}
	
	/* */
	protected class WholeNumberDocument extends PlainDocument
	{
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
		{
			char[] source = str.toCharArray();
			char[] result = new char[source.length];
			int j = 0;

			for (int i = 0; i < result.length; i++)
			{
				if (Character.isDigit(source[i]))
				{
					result[j++] = source[i];
				}
				else if ((m_commaAllowed == true)&& (source[i] == ','))
				{
					result[j++] = source[i];
				}
				else
				{
					toolkit.beep();
				}
			}
			super.insertString(offs, new String(result, 0, j), a);
		}
	}
	
	/* */
	protected class DecimalNumberDocument extends PlainDocument
	{
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
		{
			char[] source = str.toCharArray();
			char[] result = new char[source.length];
			int j = 0;
			
			String text = this.getText(0, this.getLength());
			
			int charCount = countCharacterIn(text, '.');
			
			boolean singleOccurenceOfDeciPoint = (charCount == 1)? true : false;
			for (int i = 0; i < result.length; i++)
			{
				if (Character.isDigit(source[i]) || 
						(source[i] == '-' && this.getLength() == 0) ||
						(source[i] == '.') && !singleOccurenceOfDeciPoint)
					result[j++] = source[i];
				else if ((m_commaAllowed == true)&& (source[i] == ','))
				{
					result[j++] = source[i];
				}
				else
				{
					toolkit.beep();
					System.err.println("insertString: " + source[i]);
				}
			}
			super.insertString(offs, new String(result, 0, j), a);
		}
	}
	
	/* */
	protected class IntegerNumberDocument extends PlainDocument
	{
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
		{
			char[] source = str.toCharArray();
			char[] result = new char[source.length];
			int j = 0;
			for (int i = 0; i < result.length; i++)
			{
				if (Character.isDigit(source[i]) || (source[i] == '-') && this.getLength() == 0)
				{
					result[j++] = source[i];
				}
				else if ((m_commaAllowed == true)&& (source[i] == ','))
				{
					result[j++] = source[i];
				}
				else
				{
					toolkit.beep();
				}
			}
			super.insertString(offs, new String(result, 0, j), a);
		}
	}
	
	/*  */
	protected class AlphaNumericDocument extends PlainDocument
	{
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
		{
			char[] source = str.toCharArray();
			char[] result = new char[source.length];
			int j = 0;
			for (int i = 0; i < result.length; i++)
			{
				if (Character.isDigit(source[i]) || Character.isLetter(source[i]))
					result[j++] = source[i];
				else
				{
					toolkit.beep();
					//changeFocus();
					System.err.println("insertString: " + source[i]);
				}
			}
			super.insertString(offs, new String(result, 0, j), a);
		}
	}
	
	/* TODO : This is not functional yet */
	protected class DateDocument extends PlainDocument
	{
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
		{
			String text = getText(0, this.getLength());
			try
			{
				Date userEnteredDate = dateFormatter.parse(text);
			}
			catch (ParseException e)
			{
				System.err.println("date format not correct !");
			}
			super.insertString(offs, str, a);
		}
	}
	
	protected class DocumentFactory
	{
		
		public PlainDocument getDocument(int fieldType)
		{
			//System.out.println("fieldType "+fieldType+", "+DATE_FILED);
			if(fieldType == DATE_FILED)
			{
				;
			}
			PlainDocument document = null;
			if(fieldType == WHOLE_NUMBER_FIELD)
				document = new WholeNumberDocument();
			else if(fieldType == INTEGER_NUMBER_FIELD)
				document = new IntegerNumberDocument();
			else if(fieldType == FLOAT_NUMBER_FIELD)
				document = new DecimalNumberDocument();
			else if(fieldType == DATE_FILED)
				document = new DateDocument();
			else if(fieldType == ALPHA_NUMERIC_FIELD)
				document = new AlphaNumericDocument(); 
			else if(fieldType == PLAIN_FIELD)
				document = new PlainDocument();
			
			return document;
		}
	}
	
}

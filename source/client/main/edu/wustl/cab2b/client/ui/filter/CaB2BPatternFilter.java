package edu.wustl.cab2b.client.ui.filter;

import org.jdesktop.swingx.decorator.PatternFilter;

/**
 * This class is CaB2B implementation of Pattern Filter
 * 
 * @author hrishikesh_rajpathak
 * 
 */
public class CaB2BPatternFilter extends PatternFilter implements
		CaB2BFilterInterface {
	String pattern;
	String columnName;
	public CaB2BPatternFilter(String regularExpr, int matchFlags, int col, String colName) {
		super(regularExpr, matchFlags, col);
		this.columnName=colName;
		pattern=regularExpr;
	}

	/**
	 * For creating a copy of current filter
	 * 
	 */
	public CaB2BFilterInterface copy() {
		return new CaB2BPatternFilter(this.getPattern().toString(), 0, this
				.getColumnIndex(),this.columnName);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("\"");
		stringBuffer.append(columnName);
		stringBuffer.append("\"");
		stringBuffer.append(" like ");
		stringBuffer.append("*");
		stringBuffer.append(pattern);
		stringBuffer.append("*");
		return (stringBuffer.toString());
	}

}

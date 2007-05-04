/**
 * <p>Title: CaB2BPatternFilter Class>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Hrishikesh Rajpathak
 * @version 1.0
 */
package edu.wustl.cab2b.client.ui.filter;

import org.jdesktop.swingx.decorator.PatternFilter;

/**
 * This class is CaB2B implementation of Pattern Filter
 *
 */
public class CaB2BPatternFilter extends PatternFilter implements CaB2BFilterInterface {

	
	
	public CaB2BPatternFilter(String regularExpr, int matchFlags, int col) {
		super(regularExpr,matchFlags,col);
	}
	/**
	 * For creating a copy of current filter
	 *
	 */
	public CaB2BFilterInterface copy() {
		return new CaB2BPatternFilter(this.getPattern().toString(),0,this.getColumnIndex());
	}

}

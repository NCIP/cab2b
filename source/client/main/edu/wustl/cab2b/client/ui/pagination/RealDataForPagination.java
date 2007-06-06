package edu.wustl.cab2b.client.ui.pagination;

import java.util.Vector;

import edu.wustl.common.util.logger.Logger;

/**
 *
 * @author chetan_bh
 */
public class RealDataForPagination {
	// Display Name, Description, Link Url, Image Location
	public static String[][] realData = { {"Participant","Desc","",""},
										  {"Bio Specimen","Biological specimen collected from the participant","",""},
										  {"Address","Postal adsress of the participant","",""},
										  {"Cell Specimen","Desc","",""},
										  {"Medical Identifier","Desc","",""},
										  {"Institution","Desc","",""},
										  {"Site","Desc","",""},
										  {"Cancer","A Complex Disease","",""},
										  {"Diabetes","Desc","",""},
										  {"Tissue","Tissue is a collection of cell of same typs","",""},
										  {"Storage Container","Desc","",""},
										  {"Microarray","Desc","",""},
										  {"RNA","Ribo Nucliec Acid a Biochemical molecule","",""},
										  {"Reagent","Desc","",""},
										  {"Tissue Sample","Desc","",""},
										  {"Collection Protocol","Protocol for collecting specimen from a participant","",""},
										  {"Distribution","Desc","",""},
										  {"Temperature","Desc","",""},
										  {"SNP","Single Nucleotide Polymosrhism","",""},
										  {"Nucleotide","Desc","",""},
										  {"DNA","Deoxy Ribo Nucliec Acid","",""},
										  {"mRNA","messenger RNA","",""},
										  {"Sequence","Biological sequence","",""},
										  {"Protein","Biochemical molecule","",""},
										  {"tRNA","transfer RNA","",""},
										  {"Rack","storage type","",""},
										  {"Shelf","storage type","",""},
										  {"Tube","storage type","",""},
										  {"Freezer","storage type","",""},
										  {"Investigator","Person who investigates a research","",""},
										  {"Technician","Person handling technical things","",""},
										  {"Pathway Metabolic","Biological pathways","",""},
										  {"Pathway BioCemical","Biological pathways","",""},
										  {"Pathway Protein-Protein Interaction","Biological pathways","",""},
										  {"Pathway Singnaling","Biological pathways","",""},
										  {"Process","Biological process","",""}
										};

	public static String[][] personsData = { {"Smith Mary A","Female, Age:31, Collection Protocol Registration Date: 26-Feb-2004","",""},
											 {"Doe Jane S","Female, Age:37, Collection Protocol Registration Date: 26-Sep-2004","",""},
											 {"Ana Williams","Female, Age:32, Collection Protocol Registration Date: 09-Aug-2002","",""},
											 {"Maria Fernihough","Female, Age:31, Collection Protocol Registration Date: 26-Sep-2004","",""},
											 {"Doe Jane S","Female, Age:25, Collection Protocol Registration Date: 26-Oct-2001","",""},
											 {"Ana Janes","Female, Age:36, Collection Protocol Registration Date: 26-Dec-2002","",""},
											 {"Doe Jolie","Female, Age:41, Collection Protocol Registration Date: 26-Sep-2004","",""},
											 {"Smith Jones","Female, Age:21, Collection Protocol Registration Date: 26-Sep-2003","",""},
											 {"Nicole Lame D","Female, Age:31, Collection Protocol Registration Date: 26-Jun-2004","",""},
											 {"Tara F Ried","Female, Age:51, Collection Protocol Registration Date: 26-Sep-2004","",""},
											 {"Lucy Jones","Female, Age:29, Collection Protocol Registration Date: 26-Sep-2002","",""},
											 {"Kelly Hu","Female, Age:31, Collection Protocol Registration Date: 26-Sep-2003","",""},
											 {"Estella Jenny","Female, Age:26, Collection Protocol Registration Date: 12-Jan-2005","",""}
										};

	public static Vector<PageElement> getRealData(String[][] realData)
	{
		Vector realDataVector = new Vector();

		for(int i = 0; i < realData.length; i++)
		{
			String[] element = realData[i];
			
			String displayName = element[0];
			String description = element[1];
			String linkURL     = element[2];
			String imageLocation = element[3];
			
			PageElement pageElement = new PageElementImpl();
			pageElement.setDisplayName(displayName);
			pageElement.setDescription(description);
			pageElement.setLinkURL(linkURL);
			pageElement.setImageLocation(imageLocation);
			
			realDataVector.add(pageElement);
		}

		return realDataVector;
	}
	
	public static void main(String[] args)
	{
		Logger.out.debug("realData size >> "+RealDataForPagination.getRealData(realData).size());
		Logger.out.info("real data from pagination >>> "+RealDataForPagination.getRealData(realData));
	}

}

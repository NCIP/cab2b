package edu.wustl.cab2b.common;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.HibernateException;

public class CustomDataCategoryModel implements Serializable {

	private List<IdName> dataListIdName;
	private Map<Long, List<IdName>> rootDlToLeafDlIdName;

	public CustomDataCategoryModel(List<IdName> dataListIdName,Map<Long, List<IdName>> rootDlToLeafDlIdName) throws HibernateException {
		this.dataListIdName = dataListIdName;
		this.rootDlToLeafDlIdName=rootDlToLeafDlIdName;
	}
	
	public List<IdName> getRooCategories(Long dataListId) {
		return rootDlToLeafDlIdName.get(dataListId);
	}
	
	public CustomDataCategoryModel(List<IdName> dataListIdName) throws HibernateException {
		this.dataListIdName = dataListIdName;
	}

	/**
	 * @return Returns the dataListIdName.
	 */
	public List<IdName> getDataListIdName() {
		return dataListIdName;
	}

	/**
	 * @return Returns the rootDataListToAssociatedDataListsIdName.
	 */
	public Map<Long, List<IdName>> getrootDlToLeafDlIdName() {
		return rootDlToLeafDlIdName;
	}

}

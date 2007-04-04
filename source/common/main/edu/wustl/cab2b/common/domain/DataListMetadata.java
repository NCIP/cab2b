package edu.wustl.cab2b.common.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * This is a DataList domain object. This will be mapped to "datalist" table.
 * 
 * @hibernate.joined-subclass table="datalist"
 * @hibernate.joined-subclass-key column="DL_ID"
 * 
 * @author chetan_bh
 */
public class DataListMetadata extends AdditionalMetadata implements Serializable
{
	private static final long serialVersionUID = 1234567890L;
}

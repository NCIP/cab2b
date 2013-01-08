/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.queryengine;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public class ServiceGroup implements Serializable {

	private static final long serialVersionUID = -897722669252090017L;
	private Long id;
	private String name;
	private Cab2bQuery query;
	private Collection<ServiceGroupItem> items;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Collection<ServiceGroupItem> getItems() {
		SortedSet<ServiceGroupItem> sortedItems = new TreeSet<ServiceGroupItem>(items);
		return sortedItems;
	}
	public void setItems(Collection<ServiceGroupItem> items) {
		this.items = items;
	}
	public Cab2bQuery getQuery() {
		return query;
	}
	public void setQuery(Cab2bQuery query) {
		this.query = query;
	}

}

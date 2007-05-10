package edu.wustl.cab2b.client.ui.experiment;

import edu.common.dynamicextensions.domaininterface.EntityInterface;

public class ExperimentEntity {
	private EntityInterface m_entity;

	public void setEntityInterface(EntityInterface entity) {
		m_entity = entity;
	}

	public EntityInterface getEntityInterface() {
		return m_entity;
	}

	public String toString() {
		String displyaName = edu.wustl.cab2b.common.util.Utility.getTaggedValue(
				m_entity.getTaggedValueCollection(),
				edu.wustl.cab2b.common.util.Constants.ENTITY_DISPLAY_NAME).getValue();
		return displyaName;

	}
}

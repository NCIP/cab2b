package edu.wustl.cab2b.client.ui.dag.ambiguityresolver;

import edu.common.dynamicextensions.domaininterface.EntityInterface;

public class AmbiguityObject {
	private EntityInterface m_sourceEntity;

	private EntityInterface m_targetEntity;

	public AmbiguityObject() {
	}

	public AmbiguityObject(EntityInterface sourceEntity,
			EntityInterface targetEntity) {
		m_sourceEntity = sourceEntity;
		m_targetEntity = targetEntity;
	}

	public void setSourceEntity(EntityInterface entity) {
		m_sourceEntity = entity;
	}

	public EntityInterface getSourceEntity() {
		return m_sourceEntity;
	}

	public void setTargetEntity(EntityInterface entity) {
		m_targetEntity = entity;
	}

	public EntityInterface getTargetEntity() {
		return m_targetEntity;
	}

}

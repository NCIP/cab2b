package edu.wustl.metadata.hibernate;

import org.hibernate.Hibernate;
import org.hibernate.type.NullableType;

import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.wustl.common.hibernate.CanonicalFormProvider;

public abstract class AbstractMetadataCanonicalFormProvider<T extends AbstractMetadataInterface>
        implements
            CanonicalFormProvider<T, Long> {

    public final boolean equals(T o1, T o2) {
        if (o1 == null && o2 == null) {
            return true;
        }
        // TODO check this
        return o1.equals(o2);
    }

    public final T nullSafeFromCanonicalForm(Long entityId) {
        return entityId != null ? getObjectFromEntityCache(entityId) : null;
    }

    public final Long nullSafeToCanonicalForm(T abstractMetadataObj) {
        return abstractMetadataObj != null ? abstractMetadataObj.getId() : null;
    }

    public final NullableType canonicalFormType() {
        return Hibernate.LONG;
    }

    public abstract Class<T> objectClass();

    protected abstract T getObjectFromEntityCache(Long id);
}

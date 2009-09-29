package edu.wustl.metadata.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;
import edu.wustl.common.util.ObjectCloner;

/**
 * Used for cloning objects that contain references to Dynamic Extensions (DE)
 * entity, association or attribute. This class customizes the serialization
 * process of <tt>ObjectCloner</tt> to only write out the id of the DE object.
 * It correspondingly customizes the deserialization process to obtain the DE
 * object, based on its id, from <tt>AbstractEntityCache</tt>.
 * 
 * @author srinath_k
 * 
 */
public class DyExtnObjectCloner extends ObjectCloner {
    @Override
    protected CloneInputStream createObjectInputStream() throws IOException {
        return new DyExtnResolveObjectInputStream(createInputStream());
    }

    @Override
    protected CloneOutputStream createObjectOutputStream() throws IOException {
        return new DyExtnReplaceObjectOutputStream(createOutputStream());
    }

    private static class DyExtnResolveObjectInputStream extends CloneInputStream {
        DyExtnResolveObjectInputStream(InputStream in) throws IOException {
            super(in);
            enableResolveObject(true);
        }

        @Override
        protected Object resolveObject(Object obj) throws IOException {
            if (obj instanceof Replacement) {
                return ((Replacement) obj).getOrigObject();
            }
            return super.resolveObject(obj);
        }
    }

    private static class DyExtnReplaceObjectOutputStream extends CloneOutputStream {
        public DyExtnReplaceObjectOutputStream(OutputStream out) throws IOException {
            super(out);
            enableReplaceObject(true);
        }

        @Override
        protected Object replaceObject(Object obj) throws IOException {
            if (obj instanceof EntityInterface) {
                return new ReplacementForEntity((EntityInterface) obj);
            }
            if (obj instanceof AttributeInterface) {
                return new ReplacementForAttribute((AttributeInterface) obj);
            }
            if (obj instanceof AssociationInterface) {
                return new ReplacementForAssociation((AssociationInterface) obj);
            }
            return super.replaceObject(obj);
        }
    }

    private static interface Replacement<D extends AbstractMetadataInterface> {
        Long getId();

        D getOrigObject();
    }

    private static class ReplacementForEntity implements Serializable, Replacement<EntityInterface> {
        private static final long serialVersionUID = -1324920205387970975L;

        private final Long id;

        ReplacementForEntity(EntityInterface entity) {
            this.id = entity.getId();
        }

        public Long getId() {
            return id;
        }

        public EntityInterface getOrigObject() {
            return AbstractEntityCache.getCache().getEntityById(id);
        }
    };

    private static class ReplacementForAttribute implements Serializable, Replacement<AttributeInterface> {
        private static final long serialVersionUID = 9194062618296956803L;

        private final Long id;

        ReplacementForAttribute(AttributeInterface attr) {
            this.id = attr.getId();
        }

        public Long getId() {
            return id;
        }

        public AttributeInterface getOrigObject() {
            return AbstractEntityCache.getCache().getAttributeById(id);
        }

    };

    private static class ReplacementForAssociation implements Serializable, Replacement<AssociationInterface> {
        private static final long serialVersionUID = 6784553723374640799L;

        private final Long id;

        ReplacementForAssociation(AssociationInterface assoc) {
            this.id = assoc.getId();
        }

        public Long getId() {
            return id;
        }

        public AssociationInterface getOrigObject() {
            return AbstractEntityCache.getCache().getAssociationById(id);
        }
    };

}

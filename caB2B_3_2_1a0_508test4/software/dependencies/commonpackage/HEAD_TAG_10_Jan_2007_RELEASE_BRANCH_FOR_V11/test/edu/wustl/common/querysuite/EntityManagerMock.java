package edu.wustl.common.querysuite;

/**
 * @author Mandar Shidhore
 * @version 1.0
 * @created 17-Oct-2006 16:32:04 PM
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domain.Association;
import edu.common.dynamicextensions.domain.Attribute;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.Role;
import edu.common.dynamicextensions.domain.databaseproperties.ConstraintProperties;
import edu.common.dynamicextensions.domain.databaseproperties.TableProperties;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.global.Constants;
import edu.common.dynamicextensions.util.global.Constants.AssociationDirection;
import edu.common.dynamicextensions.util.global.Constants.InheritanceStrategy;

public class EntityManagerMock extends EntityManager {
    DomainObjectFactory factory = new DomainObjectFactory() {
        @Override
        public EntityInterface createEntity() {
            EntityInterface entity = super.createEntity();
            entity.setEntityGroup(entityGroup);
            entityGroup.addEntity(entity);
            return entity;
        }

        @Override
        public AttributeInterface createStringAttribute() {
            return (setAttrId(super.createStringAttribute()));
        }

        @Override
        public AttributeInterface createDoubleAttribute() {
            return (setAttrId(super.createDoubleAttribute()));
        }

        @Override
        public AttributeInterface createDateAttribute() {
            return (setAttrId(super.createDateAttribute()));
        }

        @Override
        public AttributeInterface createLongAttribute() {
            return (setAttrId(super.createLongAttribute()));
        }

        private AttributeInterface setAttrId(AttributeInterface attr) {
            attr.setId(attrId++);
            return attr;
        }
    };

    public EntityManagerMock() {
        entityGroup = factory.createEntityGroup();
    }

    public final EntityGroupInterface entityGroup;

    private long attrId = 1;

    public List<EntityInterface> entityList = new ArrayList<EntityInterface>();

    public static String PARTICIPANT_NAME = "edu.wustl.catissuecore.domain.Participant";

    public static String PARTICIPANT_MEDICAL_ID_NAME = "edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier";

    public static String COLLECTION_PROTOCOL_NAME = "edu.wustl.catissuecore.domain.CollectionProtocol";

    public static String COLLECTION_PROTOCOL_REGISTRATION_NAME = "edu.wustl.catissuecore.domain.CollectionProtocolRegistration";

    public static String SPECIMEN_PROTOCOL_NAME = "edu.wustl.catissuecore.domain.SpecimenProtocol";

    public static String SPECIMEN_COLLECTION_GROUP_NAME = "edu.wustl.catissuecore.domain.SpecimenCollectionGroup";

    public static String COLLECTION_PROTOCOL_EVT_NAME = "edu.wustl.catissuecore.domain.CollectionProtocolEvent";

    public static String SPECIMEN_NAME = "edu.wustl.catissuecore.domain.Specimen";

    public static String SPECIMEN_CHARACTERISTIC_NAME = "edu.wustl.catissuecore.domain.SpecimenCharacteristics";

    public static String SPECIMEN_EVT_NAME = "edu.wustl.catissuecore.domain.SpecimenEventParameters";

    public static String CHKIN_CHKOUT_EVT_NAME = "edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter";

    public static String FROZEN_EVT_NAME = "edu.wustl.catissuecore.domain.FrozenEventParameters";

    public static String COLL_EVT_NAME = "edu.wustl.catissuecore.domain.CollectionEventParameters";

    public static String PROCEDURE_EVT_NAME = "edu.wustl.catissuecore.domain.ProcedureEventParameters";

    public static String RECEIVED_EVT_NAME = "edu.wustl.catissuecore.domain.ReceivedEventParameters";

    public static String CELL_SPE_REVIEW_EVT_NAME = "edu.wustl.catissuecore.domain.CellSpecimenReviewParameters";

    public static String REVIEW_EVT_PARAM_NAME = "edu.wustl.catissuecore.domain.ReviewEventParameters";

    public static String SITE_NAME = "edu.wustl.catissuecore.domain.Site";

    public static String MOLECULAR_SPECIMEN_NAME = "edu.wustl.catissuecore.domain.MolecularSpecimen";

    public static String TISSUE_SPECIMEN_NAME = "edu.wustl.catissuecore.domain.TissueSpecimen";

    public static String FLUID_SPECIMEN_NAME = "edu.wustl.catissuecore.domain.FluidSpecimen";

    public static String BIOHAZARD_NAME = "edu.wustl.catissuecore.domain.Biohazard";

    public static String DE_LEVEL1_INHERITANCE = "Level1DE";

    public static String DE_LEVEL2_INHERITANCE = "Level2DE";

    public static String DE_LEVEL3_INHERITANCE = "Level3DE";

    public static String DE_LEVEL4_INHERITANCE = "Level4DE";

    public static Long PARTICIPANT_ID = new Long(1);

    public static Long PARTICIPANT_MEDICAL_ID = new Long(2);

    public static Long COLLECTION_PROTOCOL_ID = new Long(4);

    public static Long COLLECTION_PROTOCOL_REGISTRATION_ID = new Long(3);

    public static Long SPECIMEN_PROTOCOL_ID = new Long(5);

    public static Long SPECIMEN_COLLECTION_GROUP_ID = new Long(7);

    public static Long COLLECTION_PROTOCOL_EVT_ID = new Long(6);

    public static Long SPECIMEN_ID = new Long(8);

    public static Long SPECIMEN_EVT_ID = new Long(9);

    public static Long CHKIN_CHKOUT_EVT_ID = new Long(10);

    public static Long FROZEN_EVT_ID = new Long(11);

    public static Long PROCEDURE_EVT_ID = new Long(12);

    public static Long RECEIVED_EVT_ID = new Long(13);

    public static Long SITE_ID = new Long(14);

    public static Long SPECIMEN_CHARACTERISTIC_ID = new Long(15);

    public static Long MOLECULAR_SPECIMEN_ID = new Long(16);

    public static Long TISSUE_SPECIMEN_ID = new Long(17);

    public static Long CELL_SPE_REVIEW_EVT_ID = new Long(18);

    public static Long REVIEW_EVT_PARAM_ID = new Long(19);

    public static Long FLUID_SPECIMEN_ID = new Long(20);

    public static Long BIOHAZARD_ID = new Long(21);

    public static Long DE_LEVEL1_INHERITANCE_ID = new Long(22);

    public static Long DE_LEVEL2_INHERITANCE_ID = new Long(23);

    public static Long DE_LEVEL3_INHERITANCE_ID = new Long(24);

    public static Long DE_LEVEL4_INHERITANCE_ID = new Long(25);

    public static Long COLL_EVT_ID = new Long(26);

    public static Set<String> specimenClasses = new HashSet<String>();

    public static Set<String> eventClasses = new HashSet<String>();
    static {
        specimenClasses.add(SPECIMEN_NAME);
        specimenClasses.add(MOLECULAR_SPECIMEN_NAME);
        specimenClasses.add(TISSUE_SPECIMEN_NAME);
        specimenClasses.add(FLUID_SPECIMEN_NAME);

        eventClasses.add(SPECIMEN_EVT_NAME);
        eventClasses.add(FROZEN_EVT_NAME);
        eventClasses.add(COLL_EVT_NAME);
        eventClasses.add(RECEIVED_EVT_NAME);
        eventClasses.add(REVIEW_EVT_PARAM_NAME);
        eventClasses.add(CELL_SPE_REVIEW_EVT_NAME);
    }

    static long identifier = 0L;

    /*    *//**
             * @see edu.common.dynamicextensions.entitymanager.EntityManager#findEntity(edu.common.dynamicextensions.domaininterface.EntityInterface)
             */
    /*
     * @Override public Collection findEntity(EntityInterface arg0) { // TODO
     * Auto-generated method stub return super.findEntity(arg0); }
     */

    /**
     * @see edu.common.dynamicextensions.entitymanager.EntityManager#getAllEntities()
     */
    @Override
    public Collection<EntityInterface> getAllEntities() throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException {
        entityList.add((EntityInterface) getEntityByName(PARTICIPANT_NAME));
        entityList.add((EntityInterface) getEntityByName(PARTICIPANT_MEDICAL_ID_NAME));
        entityList.add((EntityInterface) getEntityByName(COLLECTION_PROTOCOL_REGISTRATION_NAME));
        entityList.add((EntityInterface) getEntityByName(COLLECTION_PROTOCOL_NAME));
        entityList.add((EntityInterface) getEntityByName(COLLECTION_PROTOCOL_EVT_NAME));
        entityList.add((EntityInterface) getEntityByName(CHKIN_CHKOUT_EVT_NAME));
        entityList.add((EntityInterface) getEntityByName(SITE_NAME));
        return entityList;
    }

    /**
     * @see edu.common.dynamicextensions.entitymanager.EntityManager#getAssociation(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public Collection<AssociationInterface> getAssociation(String sourceEntityName, String sourceRoleName)
            throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException {
        Collection<AssociationInterface> associations = new ArrayList<AssociationInterface>();
        if (sourceEntityName.equals(PARTICIPANT_NAME) && sourceRoleName.equals("participant")) {
            AssociationInterface association = createAssociation(PARTICIPANT_NAME, PARTICIPANT_MEDICAL_ID_NAME,
                    AssociationDirection.BI_DIRECTIONAL, "participant", "participantMedicalIdentifierCollection", null,
                    "PARTICIPANT_ID");
            associations.add(association);

            // another Association.
            association = createAssociation(PARTICIPANT_NAME, COLLECTION_PROTOCOL_REGISTRATION_NAME,
                    AssociationDirection.BI_DIRECTIONAL, "participant", "collectionProtocolRegistrationCollection",
                    null, "PARTICIPANT_ID");
            associations.add(association);
        } else if (sourceEntityName.equals(PARTICIPANT_NAME) && sourceRoleName.equals("annotation")) {
            AssociationInterface association = createAssociation(PARTICIPANT_NAME, DE_LEVEL4_INHERITANCE,
                    AssociationDirection.SRC_DESTINATION, "annotation", "participant", null, "PARTICIPANT_ID");
            associations.add(association);
        } else if (sourceEntityName.equals(PARTICIPANT_MEDICAL_ID_NAME)
                && sourceRoleName.equals("ParticipantMedicalIdentifier")) {
            AssociationInterface association = createAssociation(PARTICIPANT_MEDICAL_ID_NAME, SITE_NAME,
                    AssociationDirection.SRC_DESTINATION, "ParticipantMedicalIdentifier", "Site", "SITE_ID", null);
            associations.add(association);
        } else if (sourceEntityName.equals(PARTICIPANT_MEDICAL_ID_NAME)
                && sourceRoleName.equals("participantMedicalIdentifierCollection")) {
            AssociationInterface association = createAssociation(PARTICIPANT_MEDICAL_ID_NAME, PARTICIPANT_NAME,
                    AssociationDirection.BI_DIRECTIONAL, "participantMedicalIdentifierCollection", "participant",
                    "PARTICIPANT_ID", null);
            associations.add(association);
        } else if (sourceEntityName.equals(SPECIMEN_COLLECTION_GROUP_NAME)
                && sourceRoleName.equals("specimenCollectionGroup")) {
            AssociationInterface association = createAssociation(SPECIMEN_COLLECTION_GROUP_NAME, SPECIMEN_NAME,
                    AssociationDirection.BI_DIRECTIONAL, "specimenCollectionGroup", "specimenCollection", null,
                    "SPECIMEN_COLLECTION_GROUP_ID");
            associations.add(association);

            for (String specimenClass : specimenClasses) {
                association = createAssociation(sourceEntityName, specimenClass, AssociationDirection.BI_DIRECTIONAL,
                        "specimenCollectionGroup", "specimenCollection", null, "SPECIMEN_COLLECTION_GROUP_ID");
                associations.add(association);
            }

            association = createAssociation(SPECIMEN_COLLECTION_GROUP_NAME, COLLECTION_PROTOCOL_EVT_NAME,
                    AssociationDirection.SRC_DESTINATION, "specimenCollectionGroup", "collectionProtocolEvent",
                    "COLLECTION_PROTOCOL_EVT_ID", null);
            associations.add(association);

            association = createAssociation(SPECIMEN_COLLECTION_GROUP_NAME, COLLECTION_PROTOCOL_REGISTRATION_NAME,
                    AssociationDirection.BI_DIRECTIONAL, "specimenCollectionGroup", "collectionProtocolRegistration",
                    "COLLECTION_PROTOCOL_REG_ID", null);
            associations.add(association);

            association = createAssociation(SPECIMEN_COLLECTION_GROUP_NAME, SITE_NAME,
                    AssociationDirection.SRC_DESTINATION, "specimenCollectionGroup", "site", "SITE_ID", null);
            associations.add(association);

        } else if (specimenClasses.contains(sourceEntityName) && sourceRoleName.equals("childrenSpecimen")) {
            AssociationInterface association = null;

            for (String specimenClass : specimenClasses) {
                association = createAssociation(sourceEntityName, specimenClass, AssociationDirection.BI_DIRECTIONAL,
                        "childrenSpecimen", "collectionProtocolEvent", null, "PARENT_SPECIMEN_ID");
                associations.add(association);
            }
        } else if (specimenClasses.contains(sourceEntityName) && sourceRoleName.equals("specimenCollection")) {
            AssociationInterface association = null;

            for (String specimenClass : specimenClasses) {
                association = createAssociation(specimenClass, BIOHAZARD_NAME, AssociationDirection.BI_DIRECTIONAL,
                        "specimenCollection", "biohazardCollection", "SPECIMEN_ID", "BIOHAZARD_ID");
                association.getConstraintProperties().setName("CATISSUE_SPECIMEN_BIOHZ_REL");
                associations.add(association);
            }
        } else if (sourceEntityName.equals(BIOHAZARD_NAME) && sourceRoleName.equals("biohazardCollection")) {
            AssociationInterface association = null;

            for (String specimenClass : specimenClasses) {
                association = createAssociation(sourceEntityName, specimenClass, AssociationDirection.BI_DIRECTIONAL,
                        "biohazardCollection", "specimenCollection", "BIOHAZARD_ID", "SPECIMEN_ID");
                association.getConstraintProperties().setName("CATISSUE_SPECIMEN_BIOHZ_REL");
                associations.add(association);
            }
        } else if (specimenClasses.contains(sourceEntityName) && sourceRoleName.equals("")) {
            AssociationInterface association = null;

            association = createAssociation(sourceEntityName, SPECIMEN_CHARACTERISTIC_NAME,
                    AssociationDirection.SRC_DESTINATION, "", "specimenCharacteristics", "SPECIMEN_CHARACTERISTICS_ID",
                    null);
            associations.add(association);
        } else if (specimenClasses.contains(sourceEntityName) && sourceRoleName.equals("specimen")) {
            for (String event : eventClasses) {
                AssociationInterface association = createAssociation(sourceEntityName, event,
                        AssociationDirection.BI_DIRECTIONAL, "specimen", "specimenEventCollection", null, "SPECIMEN_ID");
                associations.add(association);
            }
        } else if (sourceEntityName.equals(COLLECTION_PROTOCOL_REGISTRATION_NAME)
                && sourceRoleName.equals("collectionProtocolRegistration")) {
            AssociationInterface association = createAssociation(COLLECTION_PROTOCOL_REGISTRATION_NAME,
                    SPECIMEN_COLLECTION_GROUP_NAME, AssociationDirection.BI_DIRECTIONAL,
                    "collectionProtocolRegistration", "SpecimenCollectionGroupCollection", null,
                    "COLLECTION_PROTOCOL_REG_ID");
            associations.add(association);
        } else if (sourceEntityName.equals(COLLECTION_PROTOCOL_REGISTRATION_NAME) && sourceRoleName.equals("")) {
            AssociationInterface association = createAssociation(COLLECTION_PROTOCOL_REGISTRATION_NAME,
                    COLLECTION_PROTOCOL_NAME, AssociationDirection.SRC_DESTINATION, "", "collectionProtocol",
                    "COLLECTION_PROTOCOL_ID", null);
            associations.add(association);
        } else if (sourceEntityName.equals(COLLECTION_PROTOCOL_NAME) && sourceRoleName.equals("collectionProtocol")) {
            AssociationInterface association = createAssociation(COLLECTION_PROTOCOL_NAME,
                    COLLECTION_PROTOCOL_EVT_NAME, AssociationDirection.BI_DIRECTIONAL, "collectionProtocol",
                    "collectionProtocolEventCollection", null, "COLLECTION_PROTOCOL_ID");
            associations.add(association);
        }
        return associations;
    }

    /**
     * @return
     * @throws DynamicExtensionsSystemException
     */
    public AssociationInterface createAssociation(String source, String target, AssociationDirection direction,
            String sourceRoleName, String targetRoleName, String sourceKey, String targetKey)
            throws DynamicExtensionsSystemException {
        AssociationInterface association = factory.createAssociation();

        EntityInterface sourceEntity = getEntityByName(source);
        EntityInterface targetEntity = getEntityByName(target);

        association.setEntity(sourceEntity);
        sourceEntity.addAssociation(association);
        association.setTargetEntity(targetEntity);
        association.setAssociationDirection(direction);
        association.setId(identifier++);

        Role sourceRole = new Role();
        sourceRole.setName(sourceRoleName);
        sourceRole.setId(1L);
        // TODO check association Type for linking:
        // sourceRole.setAssociationType("linking");
        sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
        sourceRole.setMaxCardinality(1);
        sourceRole.setMinCardinality(1);
        association.setSourceRole(sourceRole);

        Role targetRole = new Role();
        targetRole.setName(targetRoleName);
        targetRole.setId(2L);
        // TODO check association Type for linking:
        // targetRole.setAssociationType("linking");
        targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);

        targetRole.setMaxCardinality(10);
        targetRole.setMinCardinality(1);
        association.setTargetRole(targetRole);

        ConstraintProperties constraintProperties = new ConstraintProperties();
        constraintProperties.setSourceEntityKey(sourceKey);
        constraintProperties.setTargetEntityKey(targetKey);
        ((Association) association).setConstraintProperties(constraintProperties);
        return association;
    }

    /**
     * @see edu.common.dynamicextensions.entitymanager.EntityManager#getAssociations(java.lang.Long,
     *      java.lang.Long)
     */
    @Override
    public Collection<AssociationInterface> getAssociations(Long sourceEntityId, Long targetEntityId)
            throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException {
        Collection<AssociationInterface> associationsCollection = new ArrayList<AssociationInterface>();

        if (sourceEntityId.equals(PARTICIPANT_ID) && targetEntityId.equals(PARTICIPANT_MEDICAL_ID)) {
            AssociationInterface currentAssociation = factory.createAssociation();

            EntityInterface sourceEntity = getEntityByName(PARTICIPANT_NAME);
            EntityInterface targetEntity = getEntityByName(PARTICIPANT_MEDICAL_ID_NAME);

            currentAssociation.setEntity(sourceEntity);
            currentAssociation.setTargetEntity(targetEntity);
            currentAssociation.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);

            Role sourceRole = new Role();
            sourceRole.setName("participant");
            sourceRole.setId(1L);
            sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
            sourceRole.setMaxCardinality(1);
            sourceRole.setMinCardinality(1);
            currentAssociation.setSourceRole(sourceRole);

            Role targetRole = new Role();
            targetRole.setName("participantMedicalIdentifierCollection");
            targetRole.setId(2L);
            targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
            targetRole.setMaxCardinality(10);
            targetRole.setMinCardinality(0);
            currentAssociation.setTargetRole(targetRole);

            ConstraintProperties constraintProperties = new ConstraintProperties();
            constraintProperties.setSourceEntityKey(null);
            constraintProperties.setTargetEntityKey("PARTICIPANT_ID");
            ((Association) currentAssociation).setConstraintProperties(constraintProperties);

            associationsCollection.add(currentAssociation);
        } else if (sourceEntityId.equals(PARTICIPANT_ID) && targetEntityId.equals(COLLECTION_PROTOCOL_REGISTRATION_ID)) {
            AssociationInterface currentAssociation = factory.createAssociation();

            EntityInterface sourceEntity = getEntityByName(PARTICIPANT_NAME);
            EntityInterface targetEntity = getEntityByName(COLLECTION_PROTOCOL_REGISTRATION_NAME);

            currentAssociation.setEntity(sourceEntity);
            currentAssociation.setTargetEntity(targetEntity);
            currentAssociation.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);

            Role sourceRole = new Role();
            sourceRole.setName("participant");
            sourceRole.setId(1L);
            // TODO check association Type for linking:
            // sourceRole.setAssociationType("linking");
            sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
            sourceRole.setMaxCardinality(1);
            sourceRole.setMinCardinality(1);
            currentAssociation.setSourceRole(sourceRole);

            Role targetRole = new Role();
            targetRole.setName("collectionProtocolRegistrationCollection");
            targetRole.setId(2L);
            // TODO check association Type for linking:
            // targetRole.setAssociationsType("linking");
            targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
            targetRole.setMaxCardinality(10);
            targetRole.setMinCardinality(0);
            currentAssociation.setTargetRole(targetRole);

            associationsCollection.add(currentAssociation);
        } else if (sourceEntityId.equals(COLLECTION_PROTOCOL_REGISTRATION_ID)
                && targetEntityId.equals(COLLECTION_PROTOCOL_ID)) {
            AssociationInterface currentAssociation = factory.createAssociation();

            EntityInterface sourceEntity = getEntityByName(COLLECTION_PROTOCOL_REGISTRATION_NAME);
            EntityInterface targetEntity = getEntityByName(COLLECTION_PROTOCOL_NAME);

            currentAssociation.setEntity(sourceEntity);
            currentAssociation.setTargetEntity(targetEntity);
            // TODO check Direction for:
            // currentAssociation.setDirection("Destination -> Source");
            currentAssociation.setAssociationDirection(Constants.AssociationDirection.SRC_DESTINATION);

            Role sourceRole = new Role();
            sourceRole.setName("collectionProtocol");
            sourceRole.setId(1L);
            // TODO check association Type for linking:
            // sourceRole.setAssociationType("linking");
            sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
            sourceRole.setMaxCardinality(1);
            sourceRole.setMinCardinality(1);
            currentAssociation.setSourceRole(sourceRole);

            Role targetRole = new Role();
            targetRole.setName("collectionProtocolRegistrationCollection");
            targetRole.setId(2L);
            // TODO check association Type for linking:
            // targetRole.setAssociationType("linking");
            targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
            targetRole.setMaxCardinality(10);
            targetRole.setMinCardinality(0);
            currentAssociation.setTargetRole(targetRole);

            associationsCollection.add(currentAssociation);
        } else if (sourceEntityId.equals(COLLECTION_PROTOCOL_REGISTRATION_ID)
                && targetEntityId.equals(SPECIMEN_COLLECTION_GROUP_ID)) {
            AssociationInterface currentAssociation = factory.createAssociation();

            EntityInterface sourceEntity = getEntityByName(COLLECTION_PROTOCOL_REGISTRATION_NAME);
            EntityInterface targetEntity = getEntityByName(SPECIMEN_COLLECTION_GROUP_NAME);

            currentAssociation.setEntity(sourceEntity);
            currentAssociation.setTargetEntity(targetEntity);
            currentAssociation.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);

            Role sourceRole = new Role();
            sourceRole.setName("collectionProtocolRegistration");
            sourceRole.setId(1L);
            // TODO check association Type for linking:
            // sourceRole.setAssociationType("linking");
            sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
            sourceRole.setMaxCardinality(1);
            sourceRole.setMinCardinality(1);
            currentAssociation.setSourceRole(sourceRole);

            Role targetRole = new Role();
            targetRole.setName("SpecimenCollectionGroupCollection");
            targetRole.setId(2L);
            // TODO check association Type for linking:
            // targetRole.setAssociationType("linking");
            targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);

            targetRole.setMaxCardinality(10);
            targetRole.setMinCardinality(0);
            currentAssociation.setTargetRole(targetRole);

            associationsCollection.add(currentAssociation);
        } else if (sourceEntityId.equals(SPECIMEN_COLLECTION_GROUP_ID)
                && targetEntityId.equals(COLLECTION_PROTOCOL_EVT_ID)) {
            AssociationInterface currentAssociation = factory.createAssociation();

            EntityInterface sourceEntity = getEntityByName(SPECIMEN_COLLECTION_GROUP_NAME);
            EntityInterface targetEntity = getEntityByName(COLLECTION_PROTOCOL_EVT_NAME);

            currentAssociation.setEntity(sourceEntity);
            currentAssociation.setTargetEntity(targetEntity);
            currentAssociation.setAssociationDirection(Constants.AssociationDirection.SRC_DESTINATION);
            // currentAssociation.setDirection("Source -> Destination");

            Role sourceRole = new Role();
            sourceRole.setName("specimenCollectionGroup");
            sourceRole.setId(1L);
            // TODO check association Type for linking:
            // sourceRole.setAssociationType("linking");
            sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
            sourceRole.setMaxCardinality(10);
            sourceRole.setMinCardinality(0);
            currentAssociation.setSourceRole(sourceRole);

            Role targetRole = new Role();
            targetRole.setName("collectionProtocolEvent");
            targetRole.setId(2L);
            // TODO check association Type for linking:
            // targetRole.setAssociationType("linking");
            targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);

            targetRole.setMaxCardinality(1);
            targetRole.setMinCardinality(1);
            currentAssociation.setTargetRole(targetRole);

            associationsCollection.add(currentAssociation);
        } else if (sourceEntityId.equals(COLLECTION_PROTOCOL_ID) && targetEntityId.equals(COLLECTION_PROTOCOL_EVT_ID)) {
            AssociationInterface currentAssociation = factory.createAssociation();

            EntityInterface sourceEntity = getEntityByName(COLLECTION_PROTOCOL_NAME);
            EntityInterface targetEntity = getEntityByName(COLLECTION_PROTOCOL_EVT_NAME);

            currentAssociation.setEntity(sourceEntity);
            currentAssociation.setTargetEntity(targetEntity);
            currentAssociation.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);

            Role sourceRole = new Role();
            sourceRole.setName("collectionProtocol");
            sourceRole.setId(1L);
            // TODO check association Type for linking:
            // sourceRole.setAssociationType("linking");
            sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
            sourceRole.setMaxCardinality(1);
            sourceRole.setMinCardinality(1);
            currentAssociation.setSourceRole(sourceRole);

            Role targetRole = new Role();
            targetRole.setName("collectionProtocolEventCollection");
            targetRole.setId(2L);
            // TODO check association Type for linking:
            // targetRole.setAssociationType("linking");
            targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);

            targetRole.setMaxCardinality(10);
            targetRole.setMinCardinality(1);
            currentAssociation.setTargetRole(targetRole);

            associationsCollection.add(currentAssociation);
        } else if (sourceEntityId.equals(SPECIMEN_COLLECTION_GROUP_ID) && targetEntityId.equals(SPECIMEN_ID)) {
            AssociationInterface currentAssociation = factory.createAssociation();

            EntityInterface sourceEntity = getEntityByName(SPECIMEN_COLLECTION_GROUP_NAME);
            EntityInterface targetEntity = getEntityByName(SPECIMEN_NAME);

            currentAssociation.setEntity(sourceEntity);
            currentAssociation.setTargetEntity(targetEntity);
            currentAssociation.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);

            Role sourceRole = new Role();
            sourceRole.setName("specimenCollectionGroup");
            sourceRole.setId(1L);
            // TODO check association Type for linking:
            // sourceRole.setAssociationType("linking");
            sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
            sourceRole.setMaxCardinality(1);
            sourceRole.setMinCardinality(1);
            currentAssociation.setSourceRole(sourceRole);

            Role targetRole = new Role();
            targetRole.setName("specimenCollection");
            targetRole.setId(2L);
            // TODO check association Type for linking:
            // targetRole.setAssociationType("linking");
            targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);

            targetRole.setMaxCardinality(10);
            targetRole.setMinCardinality(1);
            currentAssociation.setTargetRole(targetRole);

            associationsCollection.add(currentAssociation);

            ConstraintProperties constraintProperties = new ConstraintProperties();
            constraintProperties.setSourceEntityKey(null);
            constraintProperties.setTargetEntityKey("SPECIMEN_COLLECTION_GROUP_ID");
            ((Association) currentAssociation).setConstraintProperties(constraintProperties);
        } else if (sourceEntityId.equals(SPECIMEN_ID) && targetEntityId.equals(SPECIMEN_ID)) {
            AssociationInterface currentAssociation = factory.createAssociation();

            EntityInterface sourceEntity = getEntityByName(SPECIMEN_NAME);
            EntityInterface targetEntity = getEntityByName(SPECIMEN_NAME);

            currentAssociation.setEntity(sourceEntity);
            currentAssociation.setTargetEntity(targetEntity);
            currentAssociation.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);

            Role sourceRole = new Role();
            sourceRole.setName("childrenSpecimen");
            sourceRole.setId(1L);
            // TODO check association Type for linking:
            // sourceRole.setAssociationType("linking");
            sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
            sourceRole.setMaxCardinality(10);
            sourceRole.setMinCardinality(0);
            currentAssociation.setSourceRole(sourceRole);

            Role targetRole = new Role();
            targetRole.setName("parentSpecimen");
            targetRole.setId(2L);
            // TODO check association Type for linking:
            // targetRole.setAssociationType("linking");
            targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);

            targetRole.setMaxCardinality(1);
            targetRole.setMinCardinality(0);
            currentAssociation.setTargetRole(targetRole);

            associationsCollection.add(currentAssociation);

            ConstraintProperties constraintProperties = new ConstraintProperties();
            constraintProperties.setSourceEntityKey(null);
            constraintProperties.setTargetEntityKey("PARENT_SPECIMEN_ID");
            ((Association) currentAssociation).setConstraintProperties(constraintProperties);
        } else if (sourceEntityId.equals(SPECIMEN_COLLECTION_GROUP_ID) && targetEntityId.equals(SITE_ID)) {
            AssociationInterface currentAssociation = factory.createAssociation();

            EntityInterface sourceEntity = getEntityByName(SPECIMEN_COLLECTION_GROUP_NAME);
            EntityInterface targetEntity = getEntityByName(SITE_NAME);

            currentAssociation.setEntity(sourceEntity);
            currentAssociation.setTargetEntity(targetEntity);
            currentAssociation.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);

            Role sourceRole = new Role();
            sourceRole.setName("specimenCollectionGroup");
            sourceRole.setId(1L);
            sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
            sourceRole.setMaxCardinality(1);
            sourceRole.setMinCardinality(1);
            currentAssociation.setSourceRole(sourceRole);

            Role targetRole = new Role();
            targetRole.setName("site");
            targetRole.setId(2L);
            targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
            targetRole.setMaxCardinality(10);
            targetRole.setMinCardinality(0);
            currentAssociation.setTargetRole(targetRole);

            ConstraintProperties constraintProperties = new ConstraintProperties();
            constraintProperties.setSourceEntityKey("SITE_ID");
            constraintProperties.setTargetEntityKey(null);
            ((Association) currentAssociation).setConstraintProperties(constraintProperties);

            associationsCollection.add(currentAssociation);
        } else if (sourceEntityId.equals(SPECIMEN_ID) && targetEntityId.equals(SPECIMEN_CHARACTERISTIC_ID)) {
            AssociationInterface currentAssociation = factory.createAssociation();

            EntityInterface sourceEntity = getEntityByName(SPECIMEN_NAME);
            EntityInterface targetEntity = getEntityByName(SPECIMEN_CHARACTERISTIC_NAME);

            currentAssociation.setEntity(sourceEntity);
            currentAssociation.setTargetEntity(targetEntity);
            currentAssociation.setAssociationDirection(Constants.AssociationDirection.SRC_DESTINATION);

            Role sourceRole = new Role();
            sourceRole.setName("");
            sourceRole.setId(1L);
            // TODO check association Type for linking:
            // sourceRole.setAssociationType("linking");
            sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
            sourceRole.setMaxCardinality(10);
            sourceRole.setMinCardinality(0);
            currentAssociation.setSourceRole(sourceRole);

            Role targetRole = new Role();
            targetRole.setName("specimenCharacteristics");
            targetRole.setId(2L);
            // TODO check association Type for linking:
            // targetRole.setAssociationType("linking");
            targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);

            targetRole.setMaxCardinality(1);
            targetRole.setMinCardinality(1);
            currentAssociation.setTargetRole(targetRole);

            ConstraintProperties constraintProperties = new ConstraintProperties();
            constraintProperties.setSourceEntityKey("SPECIMEN_CHARACTERISTICS_ID");
            constraintProperties.setTargetEntityKey(null);
            ((Association) currentAssociation).setConstraintProperties(constraintProperties);

            associationsCollection.add(currentAssociation);
        } else {
            System.out.println("There is no association between these two entities");
        }
        for (AssociationInterface association : associationsCollection) {
            association.setId(identifier++);
        }
        return associationsCollection;
    }

    /**
     * @see edu.common.dynamicextensions.entitymanager.EntityManager#getAttribute(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public AttributeInterface getAttribute(String entityName, String attributeName)
            throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException {
        EntityInterface entity = getEntityByName(entityName);

        if (entity != null) {
            return getSpecificAttribute(entity.getAttributeCollection(), attributeName);
        }
        return null;
    }

    /*    *//**
             * @see edu.common.dynamicextensions.entitymanager.EntityManager#getEntitiesByAttributeConceptCode(java.lang.String)
             */
    /*
     * @Override public Collection getEntitiesByAttributeConceptCode(String
     * arg0) throws DynamicExtensionsSystemException,
     * DynamicExtensionsApplicationException { return
     * super.getEntitiesByAttributeConceptCode(arg0); }
     * 
     *//**
         * @see edu.common.dynamicextensions.entitymanager.EntityManager#getEntitiesByAttributeConceptName(java.lang.String)
         */
    /*
     * @Override public Collection getEntitiesByAttributeConceptName(String
     * arg0) throws DynamicExtensionsSystemException,
     * DynamicExtensionsApplicationException { return
     * super.getEntitiesByAttributeConceptName(arg0); }
     * 
     *//**
         * @see edu.common.dynamicextensions.entitymanager.EntityManager#getEntitiesByAttributeDescription(java.lang.String)
         */
    /*
     * @Override public Collection getEntitiesByAttributeDescription(String
     * arg0) throws DynamicExtensionsSystemException,
     * DynamicExtensionsApplicationException { return
     * super.getEntitiesByAttributeDescription(arg0); }
     */

   /* *//**
     * @see edu.common.dynamicextensions.entitymanager.EntityManager#getEntitiesByAttributeName(java.lang.String)
     *//*
    @Override
    public Collection getEntitiesByAttributeName(String arg0) throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException {
        return super.getEntitiesByAttributeName(arg0);
    }*/

    /**
     * @see edu.common.dynamicextensions.entitymanager.EntityManager#getEntitiesByConceptCode(java.lang.String)
     */
    @Override
    public Collection getEntitiesByConceptCode(String arg0) throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException {
        return super.getEntitiesByConceptCode(arg0);
    }

    /*    *//**
             * @see edu.common.dynamicextensions.entitymanager.EntityManager#getEntitiesByConceptName(java.lang.String)
             */
    /*
     * @Override public Collection getEntitiesByConceptName(String arg0) throws
     * DynamicExtensionsSystemException, DynamicExtensionsApplicationException {
     * return super.getEntitiesByConceptName(arg0); }
     * 
     *//**
         * @see edu.common.dynamicextensions.entitymanager.EntityManager#getEntityByDescription(java.lang.String)
         */
    /*
     * @Override public Collection<EntityInterface>
     * getEntityByDescription(String arg0) throws
     * DynamicExtensionsSystemException, DynamicExtensionsApplicationException {
     * return super.getEntityByDescription(arg0); }
     */

    /**
     * @see edu.common.dynamicextensions.entitymanager.EntityManager#getEntityByName(java.lang.String)
     */
    @Override
    public EntityInterface getEntityByName(String name) throws DynamicExtensionsSystemException {
        if (name.equalsIgnoreCase(PARTICIPANT_NAME)) {
            return createParticipantEntity(name);
        } else if (name.equalsIgnoreCase(PARTICIPANT_MEDICAL_ID_NAME)) {
            return createParticipantMedicalIdentifierEntity(name);
        } else if (name.equalsIgnoreCase(COLLECTION_PROTOCOL_REGISTRATION_NAME)) {
            return createCollectionProtocolRegistrationEntity(name);
        } else if (name.equalsIgnoreCase(COLLECTION_PROTOCOL_NAME)) {
            return createCollectionProtocolEntity(name);
        } else if (name.equalsIgnoreCase(SPECIMEN_PROTOCOL_NAME)) {
            return createSpecimenProtocolEntity(name);
        } else if (name.equalsIgnoreCase(COLLECTION_PROTOCOL_EVT_NAME)) {
            return createCollectionProtocolEventEntity(name);
        } else if (name.equalsIgnoreCase(SPECIMEN_COLLECTION_GROUP_NAME)) {
            return createSpecimenCollectionGroupEntity(name);
        } else if (name.equalsIgnoreCase(SPECIMEN_NAME)) {
            return createSpecimenEntity(name);
        } else if (name.equalsIgnoreCase(MOLECULAR_SPECIMEN_NAME)) {
            return createMolecularSpecimenEntity(name);
        } else if (name.equalsIgnoreCase(TISSUE_SPECIMEN_NAME)) {
            return createTissueSpecimenEntity(name);
        } else if (name.equalsIgnoreCase(FLUID_SPECIMEN_NAME)) {
            return createFluidSpecimenEntity(name);
        } else if (name.equalsIgnoreCase(SPECIMEN_CHARACTERISTIC_NAME)) {
            return createSpecimenCharacteristicEntity(name);
        } else if (name.equalsIgnoreCase(SPECIMEN_EVT_NAME)) {
            return createSpecimenEventParametersEntity(name);
        } else if (name.equalsIgnoreCase(CHKIN_CHKOUT_EVT_NAME)) {
            return createCheckInCheckOutEventParameterEntity(name);
        } else if (name.equalsIgnoreCase(FROZEN_EVT_NAME)) {
            return createFrozenEventParametersEntity(name);
        } else if (name.equalsIgnoreCase(COLL_EVT_NAME)) {
            return createCollectionEventParamsEntity(name);
        } else if (name.equalsIgnoreCase(PROCEDURE_EVT_NAME)) {
            return createProcedureEventParametersEntity(name);
        } else if (name.equalsIgnoreCase(RECEIVED_EVT_NAME)) {
            return createReceivedEventParametersEntity(name);
        } else if (name.equalsIgnoreCase(REVIEW_EVT_PARAM_NAME)) {
            return createReviewEventParametersEntity(name);
        } else if (name.equalsIgnoreCase(SITE_NAME)) {
            return createSiteEntity(name);
        } else if (name.equals(CELL_SPE_REVIEW_EVT_NAME)) {
            return createCellSpecimenReviewEventParametersEntity(name);
        } else if (name.equals(BIOHAZARD_NAME)) {
            return createBiohazardEntity(name);
        } else if (name.equals(DE_LEVEL4_INHERITANCE)) {
            return createDE4LevelEntity(name);
        } else if (name.equals(DE_LEVEL3_INHERITANCE)) {
            return createDE3LevelEntity(name);
        } else if (name.equals(DE_LEVEL2_INHERITANCE)) {
            return createDE2LevelEntity(name);
        } else if (name.equals(DE_LEVEL1_INHERITANCE)) {
            return createDE1LevelEntity(name);
        }

        return null;
    }

    /*
     * @param name Creates a Site entity, sets the attributes collection and
     * table properties for the entity.
     */
    private EntityInterface createSiteEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(SITE_NAME);
        e.setCreatedDate(new Date());
        e.setDescription("This is a Site entity");
        e.setId(SITE_ID);
        e.setLastUpdated(new Date());

        ((Entity) e).setAbstractAttributeCollection(getSiteAttributes(e));

        TableProperties siteTableProperties = new TableProperties();
        siteTableProperties.setName("catissue_site");
        siteTableProperties.setId(SITE_ID);
        ((Entity) e).setTableProperties(siteTableProperties);

        return e;
    }

    /*
     * @param name Creates a participant entity, sets the attributes collection
     * and table properties for the entity.
     */
    private EntityInterface createParticipantEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(PARTICIPANT_NAME);
        e.setCreatedDate(new Date());
        e.setDescription("This is a participant entity");
        e.setId(PARTICIPANT_ID);
        e.setLastUpdated(new Date());

        ((Entity) e).setAbstractAttributeCollection(getParticipantAttributes(e));

        TableProperties participantTableProperties = new TableProperties();
        participantTableProperties.setName("catissue_participant");
        participantTableProperties.setId(PARTICIPANT_ID);
        ((Entity) e).setTableProperties(participantTableProperties);

        return e;
    }

    private void setAttributeEntityReference(EntityInterface entity, ArrayList<AttributeInterface> attributes) {
        for (AttributeInterface attribute : attributes) {
            attribute.setEntity(entity);
        }
    }

    /*
     * @param name Creates a participant medical identifier entity, sets
     * attributes collection and table properties for the entity.
     */
    private EntityInterface createParticipantMedicalIdentifierEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(PARTICIPANT_MEDICAL_ID_NAME);
        e.setCreatedDate(new Date());
        e.setDescription("This is a participant medical identifier entity");
        e.setId(PARTICIPANT_MEDICAL_ID);
        e.setLastUpdated(new Date());

        ((Entity) e).setAbstractAttributeCollection(getParticipantMedicalIdentifierAttributes(e));

        TableProperties participantMedicalIdentifierTableProperties = new TableProperties();
        participantMedicalIdentifierTableProperties.setName("catissue_part_medical_id");
        participantMedicalIdentifierTableProperties.setId(PARTICIPANT_MEDICAL_ID);
        ((Entity) e).setTableProperties(participantMedicalIdentifierTableProperties);
        return e;
    }

    /*
     * @param name Creates a collection protocol registration entity, sets
     * attributes collection and table properties for the entity.
     */
    private EntityInterface createCollectionProtocolRegistrationEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(COLLECTION_PROTOCOL_REGISTRATION_NAME);
        e.setCreatedDate(new Date());
        e.setDescription("This is a collection protocol registration entity");
        e.setId(COLLECTION_PROTOCOL_REGISTRATION_ID);
        e.setLastUpdated(new Date());

        ((Entity) e).setAbstractAttributeCollection(getCollectionProtocolRegistrationAttributes(e));

        TableProperties collectionProtocolRegistrationTableProperties = new TableProperties();
        collectionProtocolRegistrationTableProperties.setName("catissue_coll_prot_reg");
        collectionProtocolRegistrationTableProperties.setId(COLLECTION_PROTOCOL_REGISTRATION_ID);
        ((Entity) e).setTableProperties(collectionProtocolRegistrationTableProperties);
        return e;
    }

    /*
     * @param name Creates a collection protocol entity, sets attributes
     * collection and table properties for the entity.
     */
    private EntityInterface createCollectionProtocolEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(COLLECTION_PROTOCOL_NAME);
        e.setCreatedDate(new Date());
        e.setDescription("This is a collection protocol entity");
        e.setId(COLLECTION_PROTOCOL_ID);
        e.setLastUpdated(new Date());

        ((Entity) e).setAbstractAttributeCollection(getCollectionProtocolAttributes(e));

        TableProperties collectionProtocolTableProperties = new TableProperties();
        collectionProtocolTableProperties.setName("catissue_collection_protocol");
        collectionProtocolTableProperties.setId(COLLECTION_PROTOCOL_ID);
        ((Entity) e).setTableProperties(collectionProtocolTableProperties);

        EntityInterface parent = createSpecimenProtocolEntity(SPECIMEN_PROTOCOL_NAME);
        e.setParentEntity(parent);
        e.setInheritanceStrategy(InheritanceStrategy.TABLE_PER_SUB_CLASS);
        e.getAbstractAttributeCollection().addAll(getSpecimenProtocolAttributes(e));
        return e;
    }

    /*
     * @param name Creates a specimen protocol entity, sets attributes
     * collection and table properties for the entity.
     */
    private EntityInterface createSpecimenProtocolEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(SPECIMEN_PROTOCOL_NAME);
        e.setCreatedDate(new Date());
        e.setDescription("This is a specimen protocol entity");
        e.setId(SPECIMEN_PROTOCOL_ID);
        e.setLastUpdated(new Date());

        ((Entity) e).setAbstractAttributeCollection(getSpecimenProtocolAttributes(e));

        TableProperties specimenProtocolTableProperties = new TableProperties();
        specimenProtocolTableProperties.setName("catissue_specimen_protocol");
        specimenProtocolTableProperties.setId(SPECIMEN_PROTOCOL_ID);
        ((Entity) e).setTableProperties(specimenProtocolTableProperties);
        return e;
    }

    /*
     * @param name Creates a collection protocol event entity, sets attributes
     * collection and table properties for the entity.
     */
    private EntityInterface createCollectionProtocolEventEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(COLLECTION_PROTOCOL_EVT_NAME);
        e.setCreatedDate(new Date());
        e.setDescription("This is a collection protocol event entity");
        e.setId(COLLECTION_PROTOCOL_EVT_ID);
        e.setLastUpdated(new Date());

        ((Entity) e).setAbstractAttributeCollection(getCollectionProtocolEventAttributes(e));

        TableProperties collectionProtocolEventTableProperties = new TableProperties();
        collectionProtocolEventTableProperties.setName("catissue_coll_prot_event");
        collectionProtocolEventTableProperties.setId(COLLECTION_PROTOCOL_EVT_ID);
        ((Entity) e).setTableProperties(collectionProtocolEventTableProperties);
        return e;
    }

    /*
     * @param name Creates a specimen collection group entity, sets attributes
     * collection and table properties for the entity.
     */
    private EntityInterface createSpecimenCollectionGroupEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(SPECIMEN_COLLECTION_GROUP_NAME);
        e.setCreatedDate(new Date());
        e.setDescription("This is a specimen collection group entity");
        e.setId(SPECIMEN_COLLECTION_GROUP_ID);
        e.setLastUpdated(new Date());

        ((Entity) e).setAbstractAttributeCollection(getSpecimenCollectionGroupAttributes(e));

        TableProperties specimenCollectionGroupTableProperties = new TableProperties();
        specimenCollectionGroupTableProperties.setName("catissue_specimen_coll_group");
        specimenCollectionGroupTableProperties.setId(SPECIMEN_COLLECTION_GROUP_ID);
        ((Entity) e).setTableProperties(specimenCollectionGroupTableProperties);
        return e;
    }

    /*
     * @param name Creates a specimen entity, sets attributes collection and
     * table properties for the entity.
     */
    private EntityInterface createSpecimenEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(SPECIMEN_NAME);
        e.setCreatedDate(new Date());
        e.setDescription("This is a specimen entity");
        e.setId(SPECIMEN_ID);
        e.setLastUpdated(new Date());

        ((Entity) e).setAbstractAttributeCollection(getSpecimenAttributes(e));

        TableProperties specimenTableProperties = new TableProperties();
        specimenTableProperties.setName("catissue_specimen");
        specimenTableProperties.setId(SPECIMEN_ID);
        ((Entity) e).setTableProperties(specimenTableProperties);
        return e;
    }

    /*
     * @param name Creates a specimen entity, sets attributes collection and
     * table properties for the entity.
     */
    private EntityInterface createBiohazardEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(BIOHAZARD_NAME);
        e.setCreatedDate(new Date());
        e.setDescription("This is a specimen entity");
        e.setId(BIOHAZARD_ID);
        e.setLastUpdated(new Date());

        ((Entity) e).setAbstractAttributeCollection(getBioHazardAttributes(e));

        TableProperties biohazardTableProperties = new TableProperties();
        biohazardTableProperties.setName("CATISSUE_BIOHAZARD");
        biohazardTableProperties.setId(BIOHAZARD_ID);
        ((Entity) e).setTableProperties(biohazardTableProperties);
        return e;
    }

    private EntityInterface createDE1LevelEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(DE_LEVEL1_INHERITANCE);
        e.setCreatedDate(new Date());
        e.setDescription("This is a DE Annotation");
        e.setId(DE_LEVEL1_INHERITANCE_ID);
        e.setLastUpdated(new Date());

        ((Entity) e).setAbstractAttributeCollection(getDE1LevelAttributes(e));

        TableProperties tableProperties = new TableProperties();
        tableProperties.setName("DE_LEVEL1");
        tableProperties.setId(DE_LEVEL1_INHERITANCE_ID);
        ((Entity) e).setTableProperties(tableProperties);
        return e;
    }

    private EntityInterface createDE2LevelEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(DE_LEVEL2_INHERITANCE);
        e.setCreatedDate(new Date());
        e.setDescription("This is a DE Annotation");
        e.setId(DE_LEVEL2_INHERITANCE_ID);
        e.setLastUpdated(new Date());

        ((Entity) e).setAbstractAttributeCollection(getDE2LevelAttributes(e));

        TableProperties tableProperties = new TableProperties();
        tableProperties.setName("DE_LEVEL2");
        tableProperties.setId(DE_LEVEL2_INHERITANCE_ID);
        ((Entity) e).setTableProperties(tableProperties);

        EntityInterface parent = createDE1LevelEntity(DE_LEVEL1_INHERITANCE);
        e.setParentEntity(parent);
        e.setInheritanceStrategy(InheritanceStrategy.TABLE_PER_SUB_CLASS);
        e.getAbstractAttributeCollection().addAll(getDE1LevelAttributes(e));
        return e;
    }

    private EntityInterface createDE3LevelEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(DE_LEVEL3_INHERITANCE);
        e.setCreatedDate(new Date());
        e.setDescription("This is a DE Annotation");
        e.setId(DE_LEVEL3_INHERITANCE_ID);
        e.setLastUpdated(new Date());

        ((Entity) e).setAbstractAttributeCollection(getDE3LevelAttributes(e));

        TableProperties tableProperties = new TableProperties();
        tableProperties.setName("DE_LEVEL3");
        tableProperties.setId(DE_LEVEL3_INHERITANCE_ID);
        ((Entity) e).setTableProperties(tableProperties);

        EntityInterface parent = createDE2LevelEntity(DE_LEVEL2_INHERITANCE);
        e.setParentEntity(parent);
        e.setInheritanceStrategy(InheritanceStrategy.TABLE_PER_SUB_CLASS);
        e.getAbstractAttributeCollection().addAll(getDE2LevelAttributes(e));
        e.getAbstractAttributeCollection().addAll(getDE1LevelAttributes(e));

        return e;
    }

    private EntityInterface createDE4LevelEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(DE_LEVEL4_INHERITANCE);
        e.setCreatedDate(new Date());
        e.setDescription("This is a DE Annotation");
        e.setId(DE_LEVEL4_INHERITANCE_ID);
        e.setLastUpdated(new Date());

        ((Entity) e).setAbstractAttributeCollection(getDE4LevelAttributes(e));

        TableProperties tableProperties = new TableProperties();
        tableProperties.setName("DE_LEVEL4");
        tableProperties.setId(DE_LEVEL4_INHERITANCE_ID);
        ((Entity) e).setTableProperties(tableProperties);

        EntityInterface parent = createDE3LevelEntity(DE_LEVEL3_INHERITANCE);
        e.setParentEntity(parent);
        e.setInheritanceStrategy(InheritanceStrategy.TABLE_PER_SUB_CLASS);
        e.getAbstractAttributeCollection().addAll(getDE3LevelAttributes(e));
        e.getAbstractAttributeCollection().addAll(getDE2LevelAttributes(e));
        e.getAbstractAttributeCollection().addAll(getDE1LevelAttributes(e));
        return e;
    }

    /*
     * @param name Creates a Molecular specimen entity, sets attributes
     * collection and table properties for the entity.
     */
    private EntityInterface createMolecularSpecimenEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(MOLECULAR_SPECIMEN_NAME);
        e.setCreatedDate(new Date());
        e.setDescription("This is a Molecular specimen entity");
        e.setId(MOLECULAR_SPECIMEN_ID);
        e.setLastUpdated(new Date());

        ArrayList attributes = new ArrayList<AttributeInterface>();

        AttributeInterface att1 = factory.createDoubleAttribute();
        att1.setName("concentrationInMicrogramPerMicroliter");
        ColumnPropertiesInterface c1 = factory.createColumnProperties();
        c1.setName("CONCENTRATION");
        ((Attribute) att1).setColumnProperties(c1);
        att1.setEntity(e);
        attributes.add(att1);
        ((Entity) e).setAbstractAttributeCollection(attributes);

        TableProperties specimenTableProperties = new TableProperties();
        specimenTableProperties.setName("catissue_specimen");
        specimenTableProperties.setId(MOLECULAR_SPECIMEN_ID);
        ((Entity) e).setTableProperties(specimenTableProperties);

        e.setParentEntity(createSpecimenEntity(SPECIMEN_NAME));
        e.setInheritanceStrategy(InheritanceStrategy.TABLE_PER_HEIRARCHY);
        e.setDiscriminatorValue("Molecular");
        e.setDiscriminatorColumn("SPECIMEN_CLASS");
        attributes.addAll(getSpecimenAttributes(e));

        return e;
    }

    /*
     * @param name Creates a Tissue specimen entity, sets attributes collection
     * and table properties for the entity.
     */
    private EntityInterface createTissueSpecimenEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(TISSUE_SPECIMEN_NAME);
        e.setCreatedDate(new Date());
        e.setDescription("This is a Molecular specimen entity");
        e.setId(TISSUE_SPECIMEN_ID);
        e.setLastUpdated(new Date());

        TableProperties specimenTableProperties = new TableProperties();
        specimenTableProperties.setName("catissue_specimen");
        specimenTableProperties.setId(TISSUE_SPECIMEN_ID);
        ((Entity) e).setTableProperties(specimenTableProperties);

        e.setParentEntity(createSpecimenEntity(SPECIMEN_NAME));
        e.setInheritanceStrategy(InheritanceStrategy.TABLE_PER_HEIRARCHY);
        e.setDiscriminatorValue("Tissue");
        e.setDiscriminatorColumn("SPECIMEN_CLASS");
        e.getAbstractAttributeCollection().addAll(getSpecimenAttributes(e));
        return e;
    }

    /*
     * @param name Creates a Fluid specimen entity, sets attributes collection
     * and table properties for the entity.
     */
    private EntityInterface createFluidSpecimenEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(FLUID_SPECIMEN_NAME);
        e.setCreatedDate(new Date());
        e.setDescription("This is a Molecular specimen entity");
        e.setId(FLUID_SPECIMEN_ID);
        e.setLastUpdated(new Date());

        TableProperties specimenTableProperties = new TableProperties();
        specimenTableProperties.setName("catissue_specimen");
        specimenTableProperties.setId(FLUID_SPECIMEN_ID);
        ((Entity) e).setTableProperties(specimenTableProperties);

        e.setParentEntity(createSpecimenEntity(SPECIMEN_NAME));
        e.setInheritanceStrategy(InheritanceStrategy.TABLE_PER_HEIRARCHY);
        e.setDiscriminatorValue("Fluid");
        e.setDiscriminatorColumn("SPECIMEN_CLASS");
        e.getAbstractAttributeCollection().addAll(getSpecimenAttributes(e));

        return e;
    }

    /*
     * @param name Creates a SpecimenCharacteristic entity, sets attributes
     * collection and table properties for the entity.
     */
    private EntityInterface createSpecimenCharacteristicEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(SPECIMEN_CHARACTERISTIC_NAME);
        e.setCreatedDate(new Date());
        e.setDescription("This is a specimen Characteristic entity");
        e.setId(SPECIMEN_CHARACTERISTIC_ID);
        e.setLastUpdated(new Date());

        ((Entity) e).setAbstractAttributeCollection(getSpecimenCharacteristicAttributes(e));

        TableProperties specimenCharacteristicTableProperties = new TableProperties();
        specimenCharacteristicTableProperties.setName("catissue_specimen_char");
        specimenCharacteristicTableProperties.setId(SPECIMEN_CHARACTERISTIC_ID);
        ((Entity) e).setTableProperties(specimenCharacteristicTableProperties);
        return e;
    }

    /*
     * @param name Creates a specimen event parameters entity, sets attributes
     * collection and table properties for the entity.
     */
    private EntityInterface createSpecimenEventParametersEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(SPECIMEN_EVT_NAME);
        e.setCreatedDate(new Date());
        e.setDescription("This is a specimen event parameters entity");
        e.setId(SPECIMEN_EVT_ID);
        e.setLastUpdated(new Date());

        ((Entity) e).setAbstractAttributeCollection(getSpecimenEventParametersAttributes(e));

        TableProperties specimenEventParametersTableProperties = new TableProperties();
        specimenEventParametersTableProperties.setName("catissue_specimen_event_param");
        specimenEventParametersTableProperties.setId(SPECIMEN_EVT_ID);
        ((Entity) e).setTableProperties(specimenEventParametersTableProperties);
        return e;
    }

    /*
     * @param name Creates a check in check out event parameters entity, sets
     * attributes collection and table properties for the entity.
     */
    private EntityInterface createCheckInCheckOutEventParameterEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(CHKIN_CHKOUT_EVT_NAME);
        e.setCreatedDate(new Date());
        e.setDescription("This is a check in check out event parameters entity");
        e.setId(CHKIN_CHKOUT_EVT_ID);
        e.setLastUpdated(new Date());

        ((Entity) e).setAbstractAttributeCollection(getCheckInCheckOutEventParameterAttributes(e));

        TableProperties checkInCheckOutEventParameterTableProperties = new TableProperties();
        checkInCheckOutEventParameterTableProperties.setName("catissue_in_out_event_param");
        checkInCheckOutEventParameterTableProperties.setId(CHKIN_CHKOUT_EVT_ID);
        ((Entity) e).setTableProperties(checkInCheckOutEventParameterTableProperties);
        return e;
    }

    /*
     * @param name Creates a frozen event parameters entity, sets attributes
     * collection and table properties for the entity.
     */
    private EntityInterface createFrozenEventParametersEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(FROZEN_EVT_NAME);
        e.setCreatedDate(new Date());
        e.setDescription("This is a frozen event parameters entity");
        e.setId(FROZEN_EVT_ID);
        e.setLastUpdated(new Date());

        ((Entity) e).setAbstractAttributeCollection(getFrozenEventParameterAttributes(e));

        TableProperties frozenEventParameterTableProperties = new TableProperties();
        frozenEventParameterTableProperties.setName("catissue_frozen_event_param");
        frozenEventParameterTableProperties.setId(FROZEN_EVT_ID);
        ((Entity) e).setTableProperties(frozenEventParameterTableProperties);

        e.setParentEntity(createSpecimenEventParametersEntity(SPECIMEN_EVT_NAME));
        e.setInheritanceStrategy(InheritanceStrategy.TABLE_PER_SUB_CLASS);
        e.getAbstractAttributeCollection().addAll(getSpecimenEventParametersAttributes(e));
        return e;
    }

    private EntityInterface createCollectionEventParamsEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(COLL_EVT_NAME);
        e.setCreatedDate(new Date());
        e.setDescription("This is a frozen event parameters entity");
        e.setId(COLL_EVT_ID);
        e.setLastUpdated(new Date());

        ((Entity) e).setAbstractAttributeCollection(getCollectionEventParameterAttributes(e));

        TableProperties collEventParameterTableProperties = new TableProperties();
        collEventParameterTableProperties.setName("catissue_coll_event_param");
        collEventParameterTableProperties.setId(COLL_EVT_ID);
        ((Entity) e).setTableProperties(collEventParameterTableProperties);

        e.setParentEntity(createSpecimenEventParametersEntity(SPECIMEN_EVT_NAME));
        e.setInheritanceStrategy(InheritanceStrategy.TABLE_PER_SUB_CLASS);
        e.getAbstractAttributeCollection().addAll(getSpecimenEventParametersAttributes(e));
        return e;
    }

    /*
     * @param name Creates a procedure event parameters entity, sets attributes
     * collection and table properties for the entity.
     */
    private EntityInterface createProcedureEventParametersEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(PROCEDURE_EVT_NAME);
        e.setCreatedDate(new Date());
        e.setDescription("This is a procedure event parameters entity");
        e.setId(PROCEDURE_EVT_ID);
        e.setLastUpdated(new Date());

        ((Entity) e).setAbstractAttributeCollection(getProcedureEventParametersAttributes(e));

        TableProperties procedureEventParametersTableProperties = new TableProperties();
        procedureEventParametersTableProperties.setName("catissue_procedure_event_param");
        procedureEventParametersTableProperties.setId(PROCEDURE_EVT_ID);
        ((Entity) e).setTableProperties(procedureEventParametersTableProperties);

        e.setParentEntity(createSpecimenEventParametersEntity(SPECIMEN_EVT_NAME));
        e.setInheritanceStrategy(InheritanceStrategy.TABLE_PER_SUB_CLASS);
        e.getAbstractAttributeCollection().addAll(getSpecimenEventParametersAttributes(e));
        return e;
    }

    /*
     * @param name Creates a Review event parameters entity, sets attributes
     * collection and table properties for the entity.
     */
    private EntityInterface createReviewEventParametersEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(REVIEW_EVT_PARAM_NAME);
        e.setCreatedDate(new Date());
        e.setDescription("This is a review event parameters entity");
        e.setId(REVIEW_EVT_PARAM_ID);
        e.setLastUpdated(new Date());

        TableProperties reviewEventParametersTableProperties = new TableProperties();
        reviewEventParametersTableProperties.setName("catissue_event_param");
        reviewEventParametersTableProperties.setId(REVIEW_EVT_PARAM_ID);
        ((Entity) e).setTableProperties(reviewEventParametersTableProperties);

        e.setParentEntity(createSpecimenEventParametersEntity(SPECIMEN_EVT_NAME));
        e.setInheritanceStrategy(InheritanceStrategy.TABLE_PER_SUB_CLASS);
        e.getAbstractAttributeCollection().addAll(getSpecimenEventParametersAttributes(e));

        return e;
    }

    /*
     * @param name Creates a Cell Specimen Review event parameters entity, sets
     * attributes collection and table properties for the entity.
     */
    private EntityInterface createCellSpecimenReviewEventParametersEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(CELL_SPE_REVIEW_EVT_NAME);
        e.setCreatedDate(new Date());
        e.setDescription("This is a review event parameters entity");
        e.setId(CELL_SPE_REVIEW_EVT_ID);
        e.setLastUpdated(new Date());

        Collection<AbstractAttributeInterface> attributes = new ArrayList<AbstractAttributeInterface>();
        AttributeInterface att1 = factory.createDoubleAttribute();
        att1.setName("neoplasticCellularityPercentage");
        ColumnPropertiesInterface c1 = factory.createColumnProperties();
        c1.setName("NEOPLASTIC_CELLULARITY_PER");
        ((Attribute) att1).setColumnProperties(c1);
        att1.setEntity(e);
        attributes.add(att1);

        AttributeInterface att2 = factory.createDoubleAttribute();
        att2.setName("viableCellPercentage");
        // att3.setSize(50);
        ColumnPropertiesInterface c2 = factory.createColumnProperties();
        c2.setName("VIABLE_CELL_PERCENTAGE");
        ((Attribute) att2).setColumnProperties(c2);
        att2.setEntity(e);
        attributes.add(att2);

        ((Entity) e).setAbstractAttributeCollection(attributes);

        TableProperties reviewEventParametersTableProperties = new TableProperties();
        reviewEventParametersTableProperties.setName("CATISSUE_CELL_SPE_REVIEW_PARAM");
        reviewEventParametersTableProperties.setId(CELL_SPE_REVIEW_EVT_ID);
        ((Entity) e).setTableProperties(reviewEventParametersTableProperties);

        e.setParentEntity(createReviewEventParametersEntity(REVIEW_EVT_PARAM_NAME));
        e.setInheritanceStrategy(InheritanceStrategy.TABLE_PER_SUB_CLASS);

        e.getAbstractAttributeCollection().addAll(getSpecimenEventParametersAttributes(e));
        return e;
    }

    /*
     * @param name Creates a received event parameters entity, sets attributes
     * collection and table properties for the entity.
     */
    private EntityInterface createReceivedEventParametersEntity(String name) {
        EntityInterface e = factory.createEntity();
        e.setName(RECEIVED_EVT_NAME);
        e.setCreatedDate(new Date());
        e.setDescription("This is a received event parameters entity");
        e.setId(RECEIVED_EVT_ID);
        e.setLastUpdated(new Date());

        ((Entity) e).setAbstractAttributeCollection(getReceivedEventParametersAttributes(e));

        TableProperties receivedEventParametersTableProperties = new TableProperties();
        receivedEventParametersTableProperties.setName("catissue_received_event_param");
        receivedEventParametersTableProperties.setId(RECEIVED_EVT_ID);
        ((Entity) e).setTableProperties(receivedEventParametersTableProperties);
        return e;
    }

    private void setAttributeId(Collection<AttributeInterface> attributeCollection) {
        for (AttributeInterface attribute : attributeCollection) {
            attribute.setId(identifier++);
        }
    }

    /*
     * Creates attributes for site entity, creates and sets a column property
     * for each attribute and adds all the attributes to a collection.
     */
    private ArrayList getSiteAttributes(EntityInterface entity) {
        ArrayList<AttributeInterface> siteAttributes = new ArrayList<AttributeInterface>();

        AttributeInterface att1 = factory.createLongAttribute();
        // att7.setDefaultValue(20L);
        att1.setName("id");
        ColumnPropertiesInterface c1 = factory.createColumnProperties();
        c1.setName("IDENTIFIER");
        ((Attribute) att1).setColumnProperties(c1);
        (att1).setIsPrimaryKey(new Boolean(true));

        AttributeInterface att2 = factory.createStringAttribute();
        // att5.setDefaultValue("firstName");
        att2.setName("name");
        ColumnPropertiesInterface c2 = factory.createColumnProperties();
        c2.setName("NAME");
        ((Attribute) att2).setColumnProperties(c2);

        AttributeInterface att3 = factory.createStringAttribute();
        // att5.setDefaultValue("firstName");
        att3.setName("type");
        ColumnPropertiesInterface c3 = factory.createColumnProperties();
        c3.setName("TYPE");
        ((Attribute) att3).setColumnProperties(c3);

        AttributeInterface att4 = factory.createStringAttribute();
        // att5.setDefaultValue("firstName");
        att4.setName("emailAddress");
        ColumnPropertiesInterface c4 = factory.createColumnProperties();
        c4.setName("EMAIL_ADDRESS");
        ((Attribute) att4).setColumnProperties(c4);

        AttributeInterface att5 = factory.createStringAttribute();
        // att1.setDefaultValue("activityStatus");
        att5.setName("activityStatus");
        ColumnPropertiesInterface c5 = factory.createColumnProperties();
        c5.setName("ACTIVITY_STATUS");
        ((Attribute) att5).setColumnProperties(c5);

        siteAttributes.add(0, att1);
        siteAttributes.add(1, att2);
        siteAttributes.add(2, att3);
        siteAttributes.add(3, att4);
        siteAttributes.add(4, att5);

        setAttributeId(siteAttributes);
        setAttributeEntityReference(entity, siteAttributes);
        return siteAttributes;
    }

    /*
     * Creates attributes for participant entity, creates and sets a column
     * property for each attribute and adds all the attributes to a collection.
     */
    private ArrayList getParticipantAttributes(EntityInterface entity) {
        ArrayList<AttributeInterface> participantAttributes = new ArrayList<AttributeInterface>();

        AttributeInterface att1 = factory.createStringAttribute();
        // att1.setDefaultValue("activityStatus");
        att1.setName("activityStatus");
        ColumnPropertiesInterface c1 = factory.createColumnProperties();
        c1.setName("ACTIVITY_STATUS");
        ((Attribute) att1).setColumnProperties(c1);

        AttributeInterface att2 = factory.createDateAttribute();
        // att2.setDefaultValue(new Date(12 - 03 - 1995));
        att2.setName("birthDate");
        ColumnPropertiesInterface c2 = factory.createColumnProperties();
        c2.setName("BIRTH_DATE");
        ((Attribute) att2).setColumnProperties(c2);

        AttributeInterface att3 = factory.createDateAttribute();
        // att3.setDefaultValue(new Date(12 - 03 - 2005));
        att3.setName("deathDate");
        ColumnPropertiesInterface c3 = factory.createColumnProperties();
        c3.setName("DEATH_DATE");
        ((Attribute) att3).setColumnProperties(c3);

        AttributeInterface att4 = factory.createStringAttribute();
        // att4.setDefaultValue("ethnicity");
        att4.setName("ethnicity");
        ColumnPropertiesInterface c4 = factory.createColumnProperties();
        c4.setName("ETHNICITY");
        ((Attribute) att4).setColumnProperties(c4);

        AttributeInterface att5 = factory.createStringAttribute();
        // att5.setDefaultValue("firstName");
        att5.setName("firstName");
        ColumnPropertiesInterface c5 = factory.createColumnProperties();
        c5.setName("FIRST_NAME");
        ((Attribute) att5).setColumnProperties(c5);

        AttributeInterface att6 = factory.createStringAttribute();
        // att6.setDefaultValue("gender");
        att6.setName("gender");
        ColumnPropertiesInterface c6 = factory.createColumnProperties();
        c6.setName("GENDER");
        ((Attribute) att6).setColumnProperties(c6);

        AttributeInterface att7 = factory.createLongAttribute();
        // att7.setDefaultValue(20L);
        att7.setName("id");

        ColumnPropertiesInterface c7 = factory.createColumnProperties();
        c7.setName("IDENTIFIER");
        ((Attribute) att7).setColumnProperties(c7);
        (att7).setIsPrimaryKey(new Boolean(true));

        AttributeInterface att8 = factory.createStringAttribute();;
        // att8.setDefaultValue("lastName");
        att8.setName("lastName");
        ColumnPropertiesInterface c8 = factory.createColumnProperties();
        c8.setName("LAST_NAME");
        ((Attribute) att8).setColumnProperties(c8);

        AttributeInterface att9 = factory.createStringAttribute();
        // att9.setDefaultValue("middleName");
        att9.setName("middleName");
        ColumnPropertiesInterface c9 = factory.createColumnProperties();
        c9.setName("MIDDLE_NAME");
        ((Attribute) att9).setColumnProperties(c9);

        AttributeInterface att10 = factory.createStringAttribute();
        // att10.setDefaultValue("sexGenotype");
        att10.setName("sexGenotype");
        ColumnPropertiesInterface c10 = factory.createColumnProperties();
        c10.setName("GENOTYPE");
        ((Attribute) att10).setColumnProperties(c10);

        AttributeInterface att11 = factory.createStringAttribute();
        // att11.setDefaultValue("socialSecurityNumber");
        att11.setName("socialSecurityNumber");
        ColumnPropertiesInterface c11 = factory.createColumnProperties();
        c11.setName("SOCIAL_SECURITY_NUMBER");
        ((Attribute) att11).setColumnProperties(c11);

        AttributeInterface att12 = factory.createStringAttribute();
        // att12.setDefaultValue("vitalStatus");
        att12.setName("vitalStatus");
        ColumnPropertiesInterface c12 = factory.createColumnProperties();
        c12.setName("VITAL_STATUS");
        ((Attribute) att12).setColumnProperties(c12);

        participantAttributes.add(0, att1);
        participantAttributes.add(1, att2);
        participantAttributes.add(2, att3);
        participantAttributes.add(3, att4);
        participantAttributes.add(4, att5);
        participantAttributes.add(5, att6);
        participantAttributes.add(6, att7);
        participantAttributes.add(7, att8);
        participantAttributes.add(8, att9);
        participantAttributes.add(9, att10);
        participantAttributes.add(10, att11);
        participantAttributes.add(11, att12);

        setAttributeId(participantAttributes);
        setAttributeEntityReference(entity, participantAttributes);
        return participantAttributes;
    }

    /*
     * Creates attributes for participant medical identifier entity, creates and
     * sets a column property for each attribute and adds all the attributes to
     * a collection.
     */
    private ArrayList getParticipantMedicalIdentifierAttributes(EntityInterface entity) {
        ArrayList<AttributeInterface> participantMedicalIdentifierAttributes = new ArrayList<AttributeInterface>();

        AttributeInterface att1 = factory.createLongAttribute();
        att1.setName("id");
        ColumnPropertiesInterface c1 = factory.createColumnProperties();
        c1.setName("IDENTIFIER");
        ((Attribute) att1).setColumnProperties(c1);
        att1.setIsPrimaryKey(new Boolean(true));

        AttributeInterface att2 = factory.createStringAttribute();
        att2.setName("medicalRecordNumber");
        ColumnPropertiesInterface c2 = factory.createColumnProperties();
        c2.setName("MEDICAL_RECORD_NUMBER");
        ((Attribute) att2).setColumnProperties(c2);

        participantMedicalIdentifierAttributes.add(0, att1);
        participantMedicalIdentifierAttributes.add(1, att2);

        setAttributeId(participantMedicalIdentifierAttributes);
        setAttributeEntityReference(entity, participantMedicalIdentifierAttributes);
        return participantMedicalIdentifierAttributes;
    }

    /*
     * Creates attributes for collection protocol registration entity, creates
     * and sets a column property for each attribute and adds all the attributes
     * to a collection.
     */
    private ArrayList getCollectionProtocolRegistrationAttributes(EntityInterface entity) {
        ArrayList<AttributeInterface> collectionProtocolRegistrationAttributes = new ArrayList<AttributeInterface>();

        AttributeInterface att1 = factory.createStringAttribute();
        att1.setName("activityStatus");
        ColumnPropertiesInterface c1 = factory.createColumnProperties();
        c1.setName("ACTIVITY_STATUS");
        ((Attribute) att1).setColumnProperties(c1);

        AttributeInterface att2 = factory.createLongAttribute();
        att2.setName("id");
        ColumnPropertiesInterface c2 = factory.createColumnProperties();
        c2.setName("IDENTIFIER");
        ((Attribute) att2).setColumnProperties(c2);
        att2.setIsPrimaryKey(new Boolean(true));

        AttributeInterface att3 = factory.createStringAttribute();
        att3.setName("protocolParticipantIdentifier");
        ColumnPropertiesInterface c3 = factory.createColumnProperties();
        c3.setName("PROTOCOL_PARTICIPANT_ID");
        ((Attribute) att3).setColumnProperties(c3);

        AttributeInterface att4 = factory.createDateAttribute();
        att4.setName("registrationDate");
        ColumnPropertiesInterface c4 = factory.createColumnProperties();
        c4.setName("REGISTRATION_DATE");
        ((Attribute) att4).setColumnProperties(c4);

        collectionProtocolRegistrationAttributes.add(0, att1);
        collectionProtocolRegistrationAttributes.add(1, att2);
        collectionProtocolRegistrationAttributes.add(2, att3);
        collectionProtocolRegistrationAttributes.add(3, att4);

        setAttributeId(collectionProtocolRegistrationAttributes);
        setAttributeEntityReference(entity, collectionProtocolRegistrationAttributes);
        return collectionProtocolRegistrationAttributes;
    }

    /*
     * Creates attributes for collection protocol entity, creates and sets a
     * column property for each attribute and adds all the attributes to a
     * collection.
     */
    private ArrayList getCollectionProtocolAttributes(EntityInterface entity) {
        ArrayList<AttributeInterface> collectionProtocolAttributes = new ArrayList<AttributeInterface>();

        AttributeInterface att1 = factory.createBooleanAttribute();
        att1.setName("aliquotInSameContainer");
        ColumnPropertiesInterface c1 = factory.createColumnProperties();
        c1.setName("ALIQUOT_IN_SAME_CONTAINER");
        ((Attribute) att1).setColumnProperties(c1);

        collectionProtocolAttributes.add(0, att1);

        setAttributeId(collectionProtocolAttributes);
        setAttributeEntityReference(entity, collectionProtocolAttributes);
        return collectionProtocolAttributes;
    }

    /*
     * Creates attributes for specimen protocol entity, creates and sets a
     * column property for each attribute and adds all the attributes to a
     * collection.
     */
    private ArrayList getSpecimenProtocolAttributes(EntityInterface entity) {
        ArrayList<AttributeInterface> specimenProtocolAttributes = new ArrayList<AttributeInterface>();

        AttributeInterface att1 = factory.createStringAttribute();
        att1.setName("activityStatus");
        ColumnPropertiesInterface c1 = factory.createColumnProperties();
        c1.setName("ACTIVITY_STATUS");
        ((Attribute) att1).setColumnProperties(c1);

        AttributeInterface att2 = factory.createStringAttribute();
        att2.setName("descriptionURL");
        ColumnPropertiesInterface c2 = factory.createColumnProperties();
        c2.setName("DESCRIPTION_URL");
        ((Attribute) att2).setColumnProperties(c2);

        AttributeInterface att3 = factory.createDateAttribute();
        att3.setName("endDate");
        ColumnPropertiesInterface c3 = factory.createColumnProperties();
        c3.setName("END_DATE");
        ((Attribute) att3).setColumnProperties(c3);

        AttributeInterface att4 = factory.createIntegerAttribute();
        att4.setName("enrollment");
        ColumnPropertiesInterface c4 = factory.createColumnProperties();
        c4.setName("ENROLLMENT");
        ((Attribute) att4).setColumnProperties(c4);

        AttributeInterface att5 = factory.createLongAttribute();
        att5.setName("id");
        ColumnPropertiesInterface c5 = factory.createColumnProperties();
        c5.setName("IDENTIFIER");
        ((Attribute) att5).setColumnProperties(c5);
        att5.setIsPrimaryKey(new Boolean(true));

        AttributeInterface att6 = factory.createStringAttribute();
        att6.setName("irbIdentifier");
        ColumnPropertiesInterface c6 = factory.createColumnProperties();
        c6.setName("IRB_IDENTIFIER");
        ((Attribute) att6).setColumnProperties(c6);

        AttributeInterface att7 = factory.createStringAttribute();
        att7.setName("shortTitle");
        ColumnPropertiesInterface c7 = factory.createColumnProperties();
        c7.setName("SHORT_TITLE");
        ((Attribute) att7).setColumnProperties(c7);

        AttributeInterface att8 = factory.createDateAttribute();
        att8.setName("startDate");
        ColumnPropertiesInterface c8 = factory.createColumnProperties();
        c8.setName("START_DATE");
        ((Attribute) att8).setColumnProperties(c8);

        AttributeInterface att9 = factory.createStringAttribute();
        att9.setName("title");
        ColumnPropertiesInterface c9 = factory.createColumnProperties();
        c9.setName("TITLE");
        ((Attribute) att9).setColumnProperties(c9);

        specimenProtocolAttributes.add(0, att1);
        specimenProtocolAttributes.add(1, att2);
        specimenProtocolAttributes.add(2, att3);
        specimenProtocolAttributes.add(3, att4);
        specimenProtocolAttributes.add(4, att5);
        specimenProtocolAttributes.add(5, att6);
        specimenProtocolAttributes.add(6, att7);
        specimenProtocolAttributes.add(7, att8);
        specimenProtocolAttributes.add(8, att9);

        setAttributeId(specimenProtocolAttributes);
        setAttributeEntityReference(entity, specimenProtocolAttributes);
        return specimenProtocolAttributes;
    }

    /*
     * Creates attributes for collection protocol event entity, creates and sets
     * a column property for each attribute and adds all the attributes to a
     * collection.
     */
    private ArrayList getCollectionProtocolEventAttributes(EntityInterface entity) {
        ArrayList<AttributeInterface> collectionProtocolEventAttributes = new ArrayList<AttributeInterface>();

        AttributeInterface att1 = factory.createStringAttribute();
        att1.setName("clinicalStatus");
        ColumnPropertiesInterface c1 = factory.createColumnProperties();
        c1.setName("CLINICAL_STATUS");
        ((Attribute) att1).setColumnProperties(c1);

        AttributeInterface att2 = factory.createLongAttribute();
        att2.setName("id");
        ColumnPropertiesInterface c2 = factory.createColumnProperties();
        c2.setName("IDENTIFIER");
        ((Attribute) att2).setColumnProperties(c2);
        att2.setIsPrimaryKey(new Boolean(true));

        AttributeInterface att3 = factory.createDoubleAttribute();
        att3.setName("studyCalendarEventPoint");
        // att3.setSize(50);
        ColumnPropertiesInterface c3 = factory.createColumnProperties();
        c3.setName("STUDY_CALENDAR_EVENT_POINT");
        ((Attribute) att3).setColumnProperties(c3);

        collectionProtocolEventAttributes.add(0, att1);
        collectionProtocolEventAttributes.add(1, att2);
        collectionProtocolEventAttributes.add(2, att3);

        setAttributeId(collectionProtocolEventAttributes);
        setAttributeEntityReference(entity, collectionProtocolEventAttributes);
        return collectionProtocolEventAttributes;
    }

    /*
     * Creates attributes for specimen collection group entity, creates and sets
     * a column property for each attribute and adds all the attributes to a
     * collection.
     */
    private ArrayList getSpecimenCollectionGroupAttributes(EntityInterface entity) {
        ArrayList<AttributeInterface> specimenCollectionGroupAttributes = new ArrayList<AttributeInterface>();

        AttributeInterface att1 = factory.createLongAttribute();
        att1.setName("id");
        ColumnPropertiesInterface c1 = factory.createColumnProperties();
        c1.setName("IDENTIFIER");
        ((Attribute) att1).setColumnProperties(c1);
        att1.setIsPrimaryKey(new Boolean(true));

        AttributeInterface att2 = factory.createStringAttribute();
        att2.setName("name");
        ColumnPropertiesInterface c2 = factory.createColumnProperties();
        c2.setName("NAME");
        ((Attribute) att2).setColumnProperties(c2);

        AttributeInterface att3 = factory.createStringAttribute();
        att3.setName("clinicalDiagnosis");
        ColumnPropertiesInterface c3 = factory.createColumnProperties();
        c3.setName("CLINICAL_DIAGNOSIS");
        ((Attribute) att3).setColumnProperties(c3);

        AttributeInterface att4 = factory.createStringAttribute();
        att4.setName("clinicalStatus");
        ColumnPropertiesInterface c4 = factory.createColumnProperties();
        c4.setName("CLINICAL_STATUS");
        ((Attribute) att4).setColumnProperties(c4);

        AttributeInterface att5 = factory.createStringAttribute();
        att5.setName("activityStatus");
        ColumnPropertiesInterface c5 = factory.createColumnProperties();
        c5.setName("ACTIVITY_STATUS");
        ((Attribute) att5).setColumnProperties(c5);

        specimenCollectionGroupAttributes.add(0, att1);
        specimenCollectionGroupAttributes.add(1, att2);
        specimenCollectionGroupAttributes.add(2, att3);
        specimenCollectionGroupAttributes.add(3, att4);
        specimenCollectionGroupAttributes.add(4, att5);

        setAttributeId(specimenCollectionGroupAttributes);
        setAttributeEntityReference(entity, specimenCollectionGroupAttributes);
        return specimenCollectionGroupAttributes;
    }

    /*
     * Creates attributes for specimen entity, creates and sets a column
     * property for each attribute and adds all the attributes to a collection.
     */
    private ArrayList getSpecimenAttributes(EntityInterface entity) {
        ArrayList<AttributeInterface> specimenAttributes = new ArrayList<AttributeInterface>();

        AttributeInterface att1 = factory.createStringAttribute();
        att1.setName("activityStatus");
        ColumnPropertiesInterface c1 = factory.createColumnProperties();
        c1.setName("ACTIVITY_STATUS");
        ((Attribute) att1).setColumnProperties(c1);

        AttributeInterface att2 = factory.createBooleanAttribute();
        att2.setName("available");
        ColumnPropertiesInterface c2 = factory.createColumnProperties();
        c2.setName("AVAILABLE");
        ((Attribute) att2).setColumnProperties(c2);

        AttributeInterface att3 = factory.createStringAttribute();
        att3.setName("barcode");
        ColumnPropertiesInterface c3 = factory.createColumnProperties();
        c3.setName("BARCODE");
        ((Attribute) att3).setColumnProperties(c3);

        AttributeInterface att4 = factory.createStringAttribute();
        att4.setName("comment");
        ColumnPropertiesInterface c4 = factory.createColumnProperties();
        c4.setName("COMMENTS");
        ((Attribute) att4).setColumnProperties(c4);

        AttributeInterface att5 = factory.createLongAttribute();
        att5.setName("id");
        ColumnPropertiesInterface c5 = factory.createColumnProperties();
        c5.setName("IDENTIFIER");
        ((Attribute) att5).setColumnProperties(c5);
        att5.setIsPrimaryKey(new Boolean(true));

        AttributeInterface att6 = factory.createStringAttribute();
        att6.setName("label");
        ColumnPropertiesInterface c6 = factory.createColumnProperties();
        c6.setName("LABEL");
        ((Attribute) att6).setColumnProperties(c6);

        AttributeInterface att7 = factory.createStringAttribute();
        att7.setName("lineage");
        ColumnPropertiesInterface c7 = factory.createColumnProperties();
        c7.setName("LINEAGE");
        ((Attribute) att7).setColumnProperties(c7);

        AttributeInterface att8 = factory.createStringAttribute();
        att8.setName("pathologicalStatus");
        ColumnPropertiesInterface c8 = factory.createColumnProperties();
        c8.setName("PATHOLOGICAL_STATUS");
        ((Attribute) att8).setColumnProperties(c8);

        AttributeInterface att9 = factory.createIntegerAttribute();
        att9.setName("positionDimensionOne");
        ColumnPropertiesInterface c9 = factory.createColumnProperties();
        c9.setName("POSITION_DIMENSION_ONE");
        ((Attribute) att9).setColumnProperties(c9);

        AttributeInterface att10 = factory.createIntegerAttribute();
        att10.setName("positionDimensionTwo");
        ColumnPropertiesInterface c10 = factory.createColumnProperties();
        c10.setName("POSITION_DIMENSION_TWO");
        ((Attribute) att10).setColumnProperties(c10);

        AttributeInterface att11 = factory.createStringAttribute();
        att11.setName("type");
        ColumnPropertiesInterface c11 = factory.createColumnProperties();
        c11.setName("TYPE");
        ((Attribute) att11).setColumnProperties(c11);

        specimenAttributes.add(0, att1);
        specimenAttributes.add(1, att2);
        specimenAttributes.add(2, att3);
        specimenAttributes.add(3, att4);
        specimenAttributes.add(4, att5);
        specimenAttributes.add(5, att6);
        specimenAttributes.add(6, att7);
        specimenAttributes.add(7, att8);
        specimenAttributes.add(8, att9);
        specimenAttributes.add(9, att10);
        specimenAttributes.add(10, att11);

        setAttributeId(specimenAttributes);
        setAttributeEntityReference(entity, specimenAttributes);
        return specimenAttributes;
    }

    private ArrayList getDE4LevelAttributes(EntityInterface entity) {
        ArrayList<AttributeInterface> attributes = new ArrayList<AttributeInterface>();

        AttributeInterface att1 = factory.createStringAttribute();
        att1.setName("level4");
        ColumnPropertiesInterface c2 = factory.createColumnProperties();
        c2.setName("LEVEL4");
        ((Attribute) att1).setColumnProperties(c2);

        attributes.add(att1);

        setAttributeId(attributes);
        setAttributeEntityReference(entity, attributes);
        return attributes;
    }

    private ArrayList getDE3LevelAttributes(EntityInterface entity) {
        ArrayList<AttributeInterface> attributes = new ArrayList<AttributeInterface>();

        AttributeInterface att1 = factory.createStringAttribute();
        att1.setName("level3");
        ColumnPropertiesInterface c2 = factory.createColumnProperties();
        c2.setName("LEVEL3");
        ((Attribute) att1).setColumnProperties(c2);

        attributes.add(att1);

        setAttributeId(attributes);
        setAttributeEntityReference(entity, attributes);
        return attributes;
    }

    private ArrayList getDE2LevelAttributes(EntityInterface entity) {
        ArrayList<AttributeInterface> attributes = new ArrayList<AttributeInterface>();

        AttributeInterface att1 = factory.createStringAttribute();
        att1.setName("level2");
        ColumnPropertiesInterface c2 = factory.createColumnProperties();
        c2.setName("LEVEL2");
        ((Attribute) att1).setColumnProperties(c2);

        attributes.add(att1);

        setAttributeId(attributes);
        setAttributeEntityReference(entity, attributes);
        return attributes;
    }

    private ArrayList getDE1LevelAttributes(EntityInterface entity) {
        ArrayList<AttributeInterface> attributes = new ArrayList<AttributeInterface>();

        AttributeInterface att1 = factory.createLongAttribute();
        att1.setName("id");
        ColumnPropertiesInterface c1 = factory.createColumnProperties();
        c1.setName("IDENTIFIER");
        ((Attribute) att1).setColumnProperties(c1);
        att1.setIsPrimaryKey(new Boolean(true));

        AttributeInterface att2 = factory.createStringAttribute();
        att2.setName("level1");
        ColumnPropertiesInterface c2 = factory.createColumnProperties();
        c2.setName("LEVEL1");
        ((Attribute) att2).setColumnProperties(c2);

        attributes.add(att1);
        attributes.add(att2);

        setAttributeId(attributes);
        setAttributeEntityReference(entity, attributes);
        return attributes;
    }

    /*
     * Creates attributes for BioHazard entity, creates and sets a column
     * property for each attribute and adds all the attributes to a collection.
     */
    private ArrayList getBioHazardAttributes(EntityInterface entity) {
        ArrayList<AttributeInterface> bioHazardAttributes = new ArrayList<AttributeInterface>();

        AttributeInterface att1 = factory.createLongAttribute();
        att1.setName("id");
        ColumnPropertiesInterface c1 = factory.createColumnProperties();
        c1.setName("IDENTIFIER");
        ((Attribute) att1).setColumnProperties(c1);
        att1.setIsPrimaryKey(new Boolean(true));

        AttributeInterface att2 = factory.createStringAttribute();
        att2.setName("name");
        ColumnPropertiesInterface c2 = factory.createColumnProperties();
        c2.setName("NAME");
        ((Attribute) att2).setColumnProperties(c2);

        AttributeInterface att3 = factory.createStringAttribute();
        att3.setName("comment");
        ColumnPropertiesInterface c3 = factory.createColumnProperties();
        c3.setName("COMMENTS");
        ((Attribute) att3).setColumnProperties(c3);

        AttributeInterface att4 = factory.createStringAttribute();
        att4.setName("type");
        ColumnPropertiesInterface c4 = factory.createColumnProperties();
        c4.setName("TYPE");
        ((Attribute) att4).setColumnProperties(c4);

        bioHazardAttributes.add(0, att1);
        bioHazardAttributes.add(1, att2);
        bioHazardAttributes.add(2, att3);
        bioHazardAttributes.add(3, att4);

        setAttributeId(bioHazardAttributes);
        setAttributeEntityReference(entity, bioHazardAttributes);
        return bioHazardAttributes;
    }

    /*
     * Creates attributes for SpecimenCharacteristic entity, creates and sets a
     * column property for each attribute and adds all the attributes to a
     * collection.
     */
    private ArrayList getSpecimenCharacteristicAttributes(EntityInterface entity) {
        ArrayList<AttributeInterface> specimenCharacteristicAttributes = new ArrayList<AttributeInterface>();

        AttributeInterface att1 = factory.createLongAttribute();
        att1.setName("id");
        ColumnPropertiesInterface c1 = factory.createColumnProperties();
        c1.setName("IDENTIFIER");
        ((Attribute) att1).setColumnProperties(c1);
        att1.setIsPrimaryKey(new Boolean(true));

        AttributeInterface att2 = factory.createStringAttribute();
        att2.setName("tissueSite");
        ColumnPropertiesInterface c2 = factory.createColumnProperties();
        c2.setName("TISSUE_SITE");
        ((Attribute) att2).setColumnProperties(c2);

        AttributeInterface att3 = factory.createStringAttribute();
        att3.setName("tissueSide");
        ColumnPropertiesInterface c3 = factory.createColumnProperties();
        c3.setName("TISSUE_SIDE");
        ((Attribute) att3).setColumnProperties(c3);

        specimenCharacteristicAttributes.add(0, att1);
        specimenCharacteristicAttributes.add(1, att2);
        specimenCharacteristicAttributes.add(2, att3);

        setAttributeId(specimenCharacteristicAttributes);
        setAttributeEntityReference(entity, specimenCharacteristicAttributes);
        return specimenCharacteristicAttributes;
    }

    /*
     * Creates attributes for specimen event parameters entity, creates and sets
     * a column property for each attribute and adds all the attributes to a
     * collection.
     */
    private ArrayList getSpecimenEventParametersAttributes(EntityInterface entity) {
        ArrayList<AttributeInterface> specimenEventParametersAttributes = new ArrayList<AttributeInterface>();

        AttributeInterface att1 = factory.createLongAttribute();
        att1.setName("id");
        ColumnPropertiesInterface c1 = factory.createColumnProperties();
        c1.setName("IDENTIFIER");
        ((Attribute) att1).setColumnProperties(c1);

        AttributeInterface att2 = factory.createDateAttribute();
        att2.setName("timestamp");
        ColumnPropertiesInterface c2 = factory.createColumnProperties();
        c2.setName("EVENT_TIMESTAMP");
        ((Attribute) att2).setColumnProperties(c2);

        AttributeInterface att3 = factory.createStringAttribute();
        att3.setName("comments");
        ColumnPropertiesInterface c3 = factory.createColumnProperties();
        c3.setName("COMMENTS");
        ((Attribute) att3).setColumnProperties(c3);

        specimenEventParametersAttributes.add(0, att1);
        specimenEventParametersAttributes.add(1, att2);
        specimenEventParametersAttributes.add(2, att3);

        setAttributeId(specimenEventParametersAttributes);
        setAttributeEntityReference(entity, specimenEventParametersAttributes);
        return specimenEventParametersAttributes;
    }

    /*
     * Creates attributes for check in check out event parameters entity,
     * creates and sets a column property for each attribute and adds all the
     * attributes to a collection.
     */
    private ArrayList getCheckInCheckOutEventParameterAttributes(EntityInterface entity) {
        ArrayList<AttributeInterface> checkInCheckOutEventParameterAttributes = new ArrayList<AttributeInterface>();

        AttributeInterface att1 = factory.createStringAttribute();
        att1.setName("storageStatus");
        ColumnPropertiesInterface c1 = factory.createColumnProperties();
        c1.setName("STORAGE_STATUS");
        ((Attribute) att1).setColumnProperties(c1);

        checkInCheckOutEventParameterAttributes.add(0, att1);

        setAttributeId(checkInCheckOutEventParameterAttributes);
        setAttributeEntityReference(entity, checkInCheckOutEventParameterAttributes);
        return checkInCheckOutEventParameterAttributes;
    }

    /*
     * Creates attributes for frozen event parameters entity, creates and sets a
     * column property for each attribute and adds all the attributes to a
     * collection.
     */
    private ArrayList getFrozenEventParameterAttributes(EntityInterface entity) {
        ArrayList<AttributeInterface> frozenEventParameterAttributes = new ArrayList<AttributeInterface>();

        AttributeInterface att1 = factory.createStringAttribute();
        att1.setName("method");
        ColumnPropertiesInterface c1 = factory.createColumnProperties();
        c1.setName("METHOD");
        ((Attribute) att1).setColumnProperties(c1);

        frozenEventParameterAttributes.add(0, att1);

        setAttributeId(frozenEventParameterAttributes);
        setAttributeEntityReference(entity, frozenEventParameterAttributes);
        return frozenEventParameterAttributes;
    }

    private AttributeInterface createAttribute(String attrName, String colName) {
        AttributeInterface att1 = factory.createStringAttribute();
        att1.setName("attrName");
        ColumnPropertiesInterface c1 = factory.createColumnProperties();
        c1.setName("colName");
        ((Attribute) att1).setColumnProperties(c1);
        return att1;
    }

    /*
     * Creates attributes for frozen event parameters entity, creates and sets a
     * column property for each attribute and adds all the attributes to a
     * collection.
     */
    private ArrayList getCollectionEventParameterAttributes(EntityInterface entity) {
        ArrayList<AttributeInterface> collEventParameterAttributes = new ArrayList<AttributeInterface>();

        AttributeInterface att1 = factory.createStringAttribute();
        att1.setName("collectionProcedure");
        ColumnPropertiesInterface c1 = factory.createColumnProperties();
        c1.setName("COLL_PROC");
        ((Attribute) att1).setColumnProperties(c1);

        collEventParameterAttributes.add(createAttribute("collectionProcedure", "COLL_PROC"));
        collEventParameterAttributes.add(createAttribute("container", "CONTAINER"));

        setAttributeId(collEventParameterAttributes);
        setAttributeEntityReference(entity, collEventParameterAttributes);
        return collEventParameterAttributes;
    }

    /*
     * Creates attributes for procedure event parameters entity, creates and
     * sets a column property for each attribute and adds all the attributes to
     * a collection.
     */
    private ArrayList getProcedureEventParametersAttributes(EntityInterface entity) {
        ArrayList<AttributeInterface> procedureEventParameterAttributes = new ArrayList<AttributeInterface>();

        AttributeInterface att1 = factory.createStringAttribute();
        att1.setName("url");
        ColumnPropertiesInterface c1 = factory.createColumnProperties();
        c1.setName("URL");
        ((Attribute) att1).setColumnProperties(c1);

        AttributeInterface att2 = factory.createStringAttribute();
        att2.setName("name");
        ColumnPropertiesInterface c2 = factory.createColumnProperties();
        c2.setName("NAME");
        ((Attribute) att2).setColumnProperties(c2);

        procedureEventParameterAttributes.add(0, att1);
        procedureEventParameterAttributes.add(0, att2);

        setAttributeId(procedureEventParameterAttributes);
        setAttributeEntityReference(entity, procedureEventParameterAttributes);
        return procedureEventParameterAttributes;
    }

    /*
     * Creates attributes for received event parameters entity, creates and sets
     * a column property for each attribute and adds all the attributes to a
     * collection.
     */
    private ArrayList getReceivedEventParametersAttributes(EntityInterface entity) {
        ArrayList<AttributeInterface> receivedEventParameterAttributes = new ArrayList<AttributeInterface>();

        AttributeInterface att1 = factory.createStringAttribute();
        att1.setName("receivedQuality");
        ColumnPropertiesInterface c1 = factory.createColumnProperties();
        c1.setName("RECEIVED_QUALITY");
        ((Attribute) att1).setColumnProperties(c1);

        receivedEventParameterAttributes.add(0, att1);

        setAttributeId(receivedEventParameterAttributes);
        setAttributeEntityReference(entity, receivedEventParameterAttributes);
        return receivedEventParameterAttributes;
    }

    private AttributeInterface getSpecificAttribute(Collection<AttributeInterface> collection, String aName) {
        for (AttributeInterface attribute : collection) {
            if (attribute.getName().equalsIgnoreCase(aName)) {
                return attribute;
            }
        }

        return null;
    }

    public static void main(String[] args) {
        EntityManagerMock testMock = new EntityManagerMock();
        try {
            System.out
                    .println(testMock.getEntityByName(SPECIMEN_PROTOCOL_NAME).getAbstractAttributeCollection().size());
            System.out.println(testMock.getEntityByName(PARTICIPANT_NAME).getName());
            System.out.println(testMock.getEntityByName(COLLECTION_PROTOCOL_REGISTRATION_NAME).getDescription());
            System.out.println(testMock.getEntityByName(PARTICIPANT_MEDICAL_ID_NAME).getId());
            System.out.println("getAttribute(String, String) METHOD returns--> "
                    + testMock.getAttribute(SPECIMEN_NAME, "lineage").getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
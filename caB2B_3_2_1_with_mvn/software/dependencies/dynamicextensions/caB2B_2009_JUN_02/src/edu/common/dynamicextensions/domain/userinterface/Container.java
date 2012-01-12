
package edu.common.dynamicextensions.domain.userinterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.util.UserInterfaceiUtility;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.class table="DYEXTN_CONTAINER"
 * @hibernate.cache  usage="read-write"
 */
public class Container extends DynamicExtensionBaseDomainObject
        implements
            Serializable,
            ContainerInterface
{

    /**
     *
     */
    private static final long serialVersionUID = 8092366994778601914L;

    /**
     * @return
     * @hibernate.id name="id" column="IDENTIFIER" type="long"
     * length="30" unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="DYEXTN_CONTAINER_SEQ"
     */
    public Long getId()
    {
        return id;
    }

    /**
     * css for the buttons on the container.
     */
    protected String buttonCss;
    /**
     * Caption to be displayed on the container.
     */
    protected String caption;
    /**
     * css for the main table in the container.
     */
    protected String mainTableCss;
    /**
     * Specifies the indicator symbol that will be used to denote a required field.
     */
    protected String requiredFieldIndicatior;
    /**
     * Specifies the warning mesaage to be displayed in case required fields are not entered by the user.
     */
    protected String requiredFieldWarningMessage;
    /**
     * css of the title in the container.
     */
    protected String titleCss;
    /**
     * Collection of controls that are in this container.
     */
    protected Collection<ControlInterface> controlCollection = new HashSet<ControlInterface>();
    /**
     *
     */
    protected Map<AbstractAttributeInterface, Object> containerValueMap = new HashMap<AbstractAttributeInterface, Object>();
    /**
     * Entity to which this container is associated.
     */
    protected Entity entity;
    /**
     *
     */
    protected String mode = WebUIManagerConstants.EDIT_MODE;
    /**
     *
     */
    protected Boolean showAssociationControlsAsLink = false;

    /**
     * parent of this entity, null is no parent present.
     */
    protected ContainerInterface baseContainer = null;

    /**
     *
     */
    protected ContainerInterface incontextContainer = this;

    /**
     * @return
     */
    public String getMode()
    {
        return mode;
    }

    /**
     * @param mode
     */
    public void setMode(String mode)
    {
        this.mode = mode;
    }

    /**
     * Empty constructor
     */
    public Container()
    {
        super();
    }

    /**
     * @hibernate.property name="buttonCss" type="string" column="BUTTON_CSS"
     * @return Returns the buttonCss.
     */
    public String getButtonCss()
    {
        return buttonCss;
    }

    /**
     * @param buttonCss The buttonCss to set.
     */
    public void setButtonCss(String buttonCss)
    {
        this.buttonCss = buttonCss;
    }

    /**
     * @hibernate.property name="caption" type="string" column="CAPTION"
     * @return Returns the caption.
     */
    public String getCaption()
    {
        return caption;
    }

    /**
     * @param caption The caption to set.
     */
    public void setCaption(String caption)
    {
        this.caption = caption;
    }

    /**
     * @hibernate.set name="controlCollection" table="DYEXTN_CONTROL"
     * cascade="all-delete-orphan" inverse="false" lazy="false"
     * @hibernate.collection-key column="CONTAINER_ID"
     * @hibernate.cache  usage="read-write"
     * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.userinterface.Control"
     * @return Returns the controlCollection.
     */
    public Collection<ControlInterface> getControlCollection()
    {
        return controlCollection;
    }

    /**
     * @param controlCollection The controlCollection to set.
     */
    public void setControlCollection(Collection<ControlInterface> controlCollection)
    {
        this.controlCollection = controlCollection;
    }

    /**
     * @hibernate.many-to-one column ="ENTITY_ID" class="edu.common.dynamicextensions.domain.Entity"
     * cascade="none"
     * @return Returns the entity.
     */
    public EntityInterface getEntity()
    {
        return entity;
    }

    /**
     * @hibernate.property name="mainTableCss" type="string" column="MAIN_TABLE_CSS"
     * @return Returns the mainTableCss.
     */
    public String getMainTableCss()
    {
        return mainTableCss;
    }

    /**
     * @param mainTableCss The mainTableCss to set.
     */
    public void setMainTableCss(String mainTableCss)
    {
        this.mainTableCss = mainTableCss;
    }

    /**
     * @hibernate.property name="requiredFieldIndicatior" type="string" column="REQUIRED_FIELD_INDICATOR"
     * @return Returns the requiredFieldIndicatior.
     */
    public String getRequiredFieldIndicatior()
    {
        return requiredFieldIndicatior;
    }

    /**
     * @param requiredFieldIndicatior The requiredFieldIndicatior to set.
     */
    public void setRequiredFieldIndicatior(String requiredFieldIndicatior)
    {
        this.requiredFieldIndicatior = requiredFieldIndicatior;
    }

    /**
     * @hibernate.property name="requiredFieldWarningMessage" type="string" column="REQUIRED_FIELD_WARNING_MESSAGE"
     * @return Returns the requiredFieldWarningMessage.
     */
    public String getRequiredFieldWarningMessage()
    {
        return requiredFieldWarningMessage;
    }

    /**
     * @param requiredFieldWarningMessage The requiredFieldWarningMessage to set.
     */
    public void setRequiredFieldWarningMessage(String requiredFieldWarningMessage)
    {
        this.requiredFieldWarningMessage = requiredFieldWarningMessage;
    }

    /**
     * @hibernate.property name="titleCss" type="string" column="TITLE_CSS"
     * @return Returns the titleCss.
     */
    public String getTitleCss()
    {
        return titleCss;
    }

    /**
     * @param titleCss The titleCss to set.
     */
    public void setTitleCss(String titleCss)
    {
        this.titleCss = titleCss;
    }

    /**
     *
     */
    public void addControl(ControlInterface controlInterface)
    {
        if (controlCollection == null)
        {
            controlCollection = new HashSet<ControlInterface>();
        }
        controlCollection.add(controlInterface);
        controlInterface.setParentContainer(this);
    }

    /**
     *
     */
    public void setEntity(EntityInterface entityInterface)
    {
        entity = (Entity) entityInterface;
    }

    /**
     *
     * @param sequenceNumber
     * @return
     */
    public ControlInterface getControlInterfaceBySequenceNumber(String sequenceNumber)
    {
        Collection controlsCollection = this.getControlCollection();
        if (controlsCollection != null)
        {
            Iterator controlsIterator = controlsCollection.iterator();
            ControlInterface controlInterface;
            while (controlsIterator.hasNext())
            {
                controlInterface = (ControlInterface) controlsIterator.next();
                if (controlInterface.getSequenceNumber().equals(new Integer(sequenceNumber)))
                {
                    return controlInterface;
                }
            }
        }
        return null;
    }

    /**
     *
     */
    public void removeControl(ControlInterface controlInterface)
    {
        if ((controlInterface != null) && (controlCollection != null))
        {
            if (controlCollection.contains(controlInterface))
            {
                controlCollection.remove(controlInterface);
            }
        }
    }

    /**
     * remove all controls from the controls collection
     */
    public void removeAllControls()
    {
        if (controlCollection != null)
        {
            controlCollection.clear();
        }
    }

    /**
     * @see edu.common.dynamicextensions.domaininterface.EntityInterface#getAllAttributes()
     */
    public List<ControlInterface> getAllControls()
    {
        List<ControlInterface> controlsList = new ArrayList<ControlInterface>(this
                .getControlCollection());
        Collections.sort(controlsList);
        Collections.reverse(controlsList);

        List<ControlInterface> baseControlsList = new ArrayList<ControlInterface>();

        ContainerInterface baseContainer = this.baseContainer;
        while (baseContainer != null)
        {
            baseControlsList = new ArrayList(baseContainer.getControlCollection());
            Collections.sort(baseControlsList);
            Collections.reverse(baseControlsList);

            controlsList.addAll(baseControlsList);

            baseContainer.setIncontextContainer(this);
            baseContainer = baseContainer.getBaseContainer();

        }
        Collections.reverse(controlsList);
        return controlsList;
    }

    /**
     * @return return the HTML string for this type of a object
     * @throws DynamicExtensionsSystemException
     */
    public String generateContainerHTML() throws DynamicExtensionsSystemException
    {
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer
                .append("<table summary='' cellpadding='3' cellspacing='0'  align='center' width='100%'>");

        if (this.getMode() != null
                && this.getMode().equalsIgnoreCase(WebUIManagerConstants.EDIT_MODE))
        {
            stringBuffer.append("<tr>");
            stringBuffer.append("<td class='formMessage' colspan='3'>");
            stringBuffer.append(this.getRequiredFieldIndicatior() + "&nbsp;");
            stringBuffer.append(this.getRequiredFieldWarningMessage());
            stringBuffer.append("</td>");
            stringBuffer.append("</tr>");
        }
        else
        {
            if (this.baseContainer != null)
            {
                this.baseContainer.setMode(this.mode);
            }
        }
        stringBuffer.append(generateControlsHTML());
        stringBuffer.append("</table>");
        return stringBuffer.toString();
    }

    /**
     * @return return the HTML string for this type of a object
     * @throws DynamicExtensionsSystemException
     */
    public String generateControlsHTML() throws DynamicExtensionsSystemException
    {


        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("<tr>");
        stringBuffer.append("<td class='formTitle' colspan='3' align='left'>");
        stringBuffer.append(DynamicExtensionsUtility.getFormattedStringForCapitalization(this
				.getCaption()));
        stringBuffer.append("</td>");
        stringBuffer.append("</tr>");

        /*	List<ControlInterface> controlsList = new ArrayList<ControlInterface>(this
         .getControlCollection());
         */
        List<ControlInterface> controlsList = getAllControls();
        /*Collections.sort(controlsList);*/
        for (ControlInterface control : controlsList)
        {
            Object value = containerValueMap.get(control.getAbstractAttribute());
            control.setValue(value);
            if (this.showAssociationControlsAsLink
                    && control instanceof ContainmentAssociationControl)
            {
                ContainmentAssociationControl containmentAssociationControl = (ContainmentAssociationControl) control;
                String link = generateLink(containmentAssociationControl.getParentContainer());
                link = UserInterfaceiUtility.getControlHTMLAsARow(control, link);
                stringBuffer.append(link);
            }
            else
            {
                stringBuffer.append(control.generateHTML());
            }
        }
        this.showAssociationControlsAsLink = false;
        return stringBuffer.toString();
    }

    /**
     *
     * @return
     * @throws DynamicExtensionsSystemException
     */
    public String generateControlsHTMLAsGrid(
            List<Map<AbstractAttributeInterface, Object>> valueMapList)
            throws DynamicExtensionsSystemException
    {
        return UserInterfaceiUtility.generateHTMLforGrid(this, valueMapList);
    }

    /**
     * @return
     */
    public Map<AbstractAttributeInterface, Object> getContainerValueMap()
    {
        return containerValueMap;
    }

    /**
     * @param containerValueMap
     */
    public void setContainerValueMap(Map<AbstractAttributeInterface, Object> containerValueMap)
    {
        this.containerValueMap = containerValueMap;
    }

    /**
     *
     * @return
     */
    public Boolean getShowAssociationControlsAsLink()
    {
        return showAssociationControlsAsLink;
    }

    /**
     *
     * @param showAssociationControlsAsLink
     */
    public void setShowAssociationControlsAsLink(Boolean showAssociationControlsAsLink)
    {
        this.showAssociationControlsAsLink = showAssociationControlsAsLink;
    }

    /**
     * @see edu.common.dynamicextensions.domaininterface.userinterface.ContainmentAssociationControlInterface#generateLinkHTML()
     */
    public String generateLink(ContainerInterface containerInterface)
            throws DynamicExtensionsSystemException
    {
        String detailsString = "";
        boolean isDataPresent = UserInterfaceiUtility.isDataPresent(containerInterface
                .getContainerValueMap());
        if (isDataPresent)
        {
            if (mode.equals(WebUIManagerConstants.EDIT_MODE))
            {
                detailsString = ApplicationProperties.getValue("eav.att.EditDetails");
            }
            else if (mode.equals(WebUIManagerConstants.VIEW_MODE))
            {
                detailsString = ApplicationProperties.getValue("eav.att.ViewDetails");
            }
        }
        else
        {
            if (mode.equals(WebUIManagerConstants.EDIT_MODE))
            {
                detailsString = ApplicationProperties.getValue("eav.att.EnterDetails");
            }
            else if (mode.equals(WebUIManagerConstants.VIEW_MODE))
            {
                detailsString = ApplicationProperties.getValue("eav.att.NoDataToView");
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<a href='#' style='cursor:hand' ");
        stringBuffer.append("onclick='showChildContainerInsertDataPage(");
      //for bug 5864
        if(this.getId() == null)
        {
        	stringBuffer.append("\""
					+ DynamicExtensionsUtility.getFormattedStringForCapitalization(this
							.getCaption()) + "\"" + ",this");
        }
        else
        {
        	stringBuffer.append(this.getId() + ",this");
        }


        stringBuffer.append(")'>");
        stringBuffer.append(detailsString);
        stringBuffer.append("</a>");

        return stringBuffer.toString();
    }

    /**
     * @see edu.common.dynamicextensions.domaininterface.EntityInterface#getParentEntity()
     * @hibernate.many-to-one column="BASE_CONTAINER_ID" class="edu.common.dynamicextensions.domain.userinterface.Container" constrained="true"
     *                        cascade="none"
     */
    public ContainerInterface getBaseContainer()
    {
        return baseContainer;
    }

    /**
     *
     * @param baseContainer
     */
    public void setBaseContainer(ContainerInterface baseContainer)
    {
        this.baseContainer = baseContainer;
    }

    /**
     * @return the incontextContainer
     */
    public ContainerInterface getIncontextContainer()
    {
        return incontextContainer;
    }

    /**
     * @param incontextContainer the incontextContainer to set
     */
    public void setIncontextContainer(ContainerInterface incontextContainer)
    {
        this.incontextContainer = incontextContainer;
    }

}
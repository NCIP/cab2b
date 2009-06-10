package edu.wustl.cab2b.server.util;

import static edu.wustl.cab2b.common.util.Constants.TYPE_DERIVED;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

public class TestUtil {
    private final static String driver = "com.mysql.jdbc.Driver";

    private static Properties serverProperties;
    static {
        Logger.configure();
        String propertyfile = "server.properties";
        InputStream is = TestUtil.class.getClassLoader().getResourceAsStream(propertyfile);
        if (is == null) {
            Logger.out.error("Unable fo find property file : " + propertyfile
                    + "\n please put this file in classpath");
        }
        serverProperties = new Properties();
        try {
            serverProperties.load(is);
        } catch (IOException e) {
            Logger.out.error("Unable to load properties from : " + propertyfile);
            e.printStackTrace();
        }
    }

    private static DomainObjectFactory deFactory = DomainObjectFactory.getInstance();

    public static EntityInterface getEntity(String entityName, String attrName, EntityInterface parent) {
        AttributeInterface a = getAttribute(attrName);

        EntityInterface e1 = deFactory.createEntity();
        e1.setName(entityName);
        if (parent != null) {
            e1.setParentEntity(parent);
            markInherited(a);
        }
        e1.addAttribute(a);
        return e1;
    }

    public static EntityInterface getEntity(String entityName, String attrName) {
        AttributeInterface a = getAttribute(attrName);
        EntityInterface e1 = getEntity(entityName);
        e1.addAttribute(a);
        a.setEntity(e1);
        return e1;
    }

    public static EntityInterface getEntity(String entityName) {
        EntityInterface e1 = deFactory.createEntity();
        e1.setName(entityName);
        return e1;
    }

    public static EntityInterface getEntity(String entityName, long id) {
        EntityInterface e1 = deFactory.createEntity();
        e1.setId(id);
        e1.setName(entityName);
        return e1;
    }

    public static EntityGroupInterface getEntityGroup(String name, long id) {
        EntityGroupInterface e1 = deFactory.createEntityGroup();
        e1.setId(id);
        e1.setName(name);
        e1.setLongName(name);
        return e1;
    }

    public static void markInherited(AbstractMetadataInterface owner) {
        TaggedValueInterface taggedValue = deFactory.createTaggedValue();
        taggedValue.setKey(TYPE_DERIVED);
        taggedValue.setValue(TYPE_DERIVED);
        owner.addTaggedValue(taggedValue);
    }

    public static AssociationInterface getAssociation(String srcEntityName, String targetEntityName) {
        EntityInterface src = getEntity(srcEntityName);
        EntityInterface target = getEntity(targetEntityName);

        AssociationInterface association = deFactory.createAssociation();
        association.setName("AssociationName_" + src.getAssociationCollection().size() + 1);
        association.setEntity(src);
        src.addAssociation(association);
        association.setSourceRole(getRole("srcRole"));
        association.setTargetEntity(target);
        association.setTargetRole(getRole("tgtRole"));
        association.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
        return association;
    }

    private static RoleInterface getRole(String roleName) {
        RoleInterface role = deFactory.createRole();
        role.setAssociationsType(Constants.AssociationType.ASSOCIATION);
        role.setName(roleName);
        role.setMaximumCardinality(Constants.Cardinality.MANY);
        role.setMinimumCardinality(Constants.Cardinality.MANY);
        return role;
    }

    public static AttributeInterface getAttribute(String name) {
        AttributeInterface a = deFactory.createStringAttribute();
        a.setName(name);
        return a;
    }

    /**
     * Closes the connection.
     * @param connection to be closed.
     */
    public static void close(Connection connection) {

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                //DO NOTHING
                Logger.out.debug(e.toString());
            }
        }
    }

    public static Connection getConnection() {
        String ip = serverProperties.getProperty("database.server.ip");
        String port = serverProperties.getProperty("database.server.port");
        String name = serverProperties.getProperty("database.name");
        String userName = serverProperties.getProperty("database.username");
        String password = serverProperties.getProperty("database.password");
        String url = "jdbc:mysql://" + ip + ":" + port + "/" + name + "";

        Connection con = null;
        try {
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            Logger.out.error("Exception in getting connection");
            e.printStackTrace();
        }
        if (con == null)
            Logger.out.error("Got null connection");
        return con;
    }
}
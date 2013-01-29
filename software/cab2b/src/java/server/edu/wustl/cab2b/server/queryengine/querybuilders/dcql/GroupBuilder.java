/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.queryengine.querybuilders.dcql;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.cab2b.common.util.TreeNode;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.DcqlConstraint;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;

class GroupBuilder {
    /**
     * list of connectors.
     */
    private List<IConnector<LogicalOperator>> connectors;

    private List<? extends DcqlConstraint> constraints;

    GroupBuilder(List<IConnector<LogicalOperator>> connectors, List<? extends DcqlConstraint> constraints) {
        if (constraints.size() < 2) {
            throw new IllegalArgumentException("don't call me for such trivial jobs, dude!");
        }
        if (connectors.size() != constraints.size() - 1) {
            throw new IllegalArgumentException("No. of constraints should equal no. of operators minus one");
        }
        setConnectors(new ArrayList<IConnector<LogicalOperator>>(connectors));
        setConstraints(constraints);
    }

    public GroupBuilder(List<? extends DcqlConstraint> constraints, LogicalOperator logicalOperator) {
        this(createLogicalConnectors(constraints.size() - 1, logicalOperator), constraints);
    }

    /**
     * Create connectors with nesting 0 and given logical operator.
     * 
     * @param numConns no. of conns to create.
     * @param logicalOperator the logical operator
     * @return
     */
    private static List<IConnector<LogicalOperator>> createLogicalConnectors(int numConns, LogicalOperator logicalOperator) {
        List<IConnector<LogicalOperator>> conns = new ArrayList<IConnector<LogicalOperator>>(numConns);
        for (int i = 0; i < numConns; i++) {
            conns.add(QueryObjectFactory.createLogicalConnector(logicalOperator, 0));
        }

        return conns;
    }

    DcqlConstraint buildGroup() {
        int currentNesting = -1;
        TreeNode<Cab2bGroup> sentinelRootNode = new TreeNode<Cab2bGroup>(new Cab2bGroup());
        TreeNode<Cab2bGroup> currentNode = sentinelRootNode;

        // a sentinel connector.
        this.connectors.add(QueryObjectFactory.createLogicalConnector(LogicalOperator.And, -1));

        for (int i = 0; i < getConnectors().size(); i++) {
            IConnector<LogicalOperator> connector = getLogicalConnector(i);
            int nestingDiff = connector.getNestingNumber() - currentNesting;
            currentNesting = connector.getNestingNumber();
            if (nestingDiff > 0) {
                for (int j = 0; j < nestingDiff; j++) {
                    Cab2bGroup childGroup = currentNode.getValue().addGroup();
                    currentNode = currentNode.addChildValue(childGroup);
                }
            }
            currentNode.getValue().addConstraint(getConstraints().get(i));
            if (nestingDiff < 0) {
                for (int j = 0; j < 0 - nestingDiff; j++) {
                    currentNode = currentNode.getParent();
                    if (currentNode == null) {
                        throw new IllegalArgumentException("Seems parantheses are mismatched...");
                    }
                }
            }
            currentNode.getValue().setLogicRelation(connector.getOperator());

            // TODO maybe try to merge child group with parent group if operator
            // is same.... do only if nothing else to do.

        }

        assert currentNode == sentinelRootNode;
        return sentinelRootNode.getChildren().get(0).getValue().getDcqlConstraint();
    }

    private IConnector<LogicalOperator> getLogicalConnector(int i) {
        return getConnectors().get(i);
    }

    /**
     * @return the constraints.
     */
    List<? extends DcqlConstraint> getConstraints() {
        return constraints;
    }

    /**
     * @param constraints the constraints to set.
     */
    void setConstraints(List<? extends DcqlConstraint> constraints) {
        this.constraints = constraints;
    }

    /**
     * @return the expression.
     */
    List<IConnector<LogicalOperator>> getConnectors() {
        return connectors;
    }

    /**
     * @param expression the expression to set.
     */
    void setConnectors(List<IConnector<LogicalOperator>> expression) {
        this.connectors = expression;
    }
}

/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.queryengine.querybuilders.dcql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.DcqlConstraint;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;

public class ConstraintsBuilderResult {
    private Map<IExpression, DcqlConstraint> expressionToConstraintMap = new HashMap<IExpression, DcqlConstraint>();

    private Map<EntityInterface, List<DcqlConstraint>> classToDcqlConstraintsMap = new HashMap<EntityInterface, List<DcqlConstraint>>();

    /**
     * @return the expressionToConstraintMap.
     */
    public Map<IExpression, DcqlConstraint> getExpressionToConstraintMap() {
        return expressionToConstraintMap;
    }

    DcqlConstraint getConstraintForExpression(IExpression expression) {
        return getExpressionToConstraintMap().get(expression);
    }

    void putConstraintForExpression(IExpression expression,
                                    DcqlConstraint dcqlConstraint,
                                    boolean addConstraintToClass) {
        getExpressionToConstraintMap().put(expression, dcqlConstraint);
        if (addConstraintToClass) {
            addDcqlConstraintForEntity(
                                       expression.getQueryEntity().getDynamicExtensionsEntity(),
                                       dcqlConstraint);
        }
    }

    boolean containsExpression(IExpression expr) {
        return getExpressionToConstraintMap().containsKey(expr);
    }

    /**
     * @return the classToExpressionsMap.
     */
    public Map<EntityInterface, List<DcqlConstraint>> getClassToDcqlConstraintsMap() {
        return classToDcqlConstraintsMap;
    }

    private List<DcqlConstraint> getDcqlConstraintListForFunctionalClass(
                                                                         EntityInterface entity) {
        List<DcqlConstraint> dcqlConstraintsList;
        if (!getClassToDcqlConstraintsMap().containsKey(entity)) {
            dcqlConstraintsList = new ArrayList<DcqlConstraint>();
            getClassToDcqlConstraintsMap().put(entity, dcqlConstraintsList);
        } else {
            dcqlConstraintsList = getClassToDcqlConstraintsMap().get(entity);
        }
        return dcqlConstraintsList;
    }

    private void addDcqlConstraintForEntity(EntityInterface entity,
                                            DcqlConstraint dcqlConstraint) {
        getDcqlConstraintListForFunctionalClass(entity).add(dcqlConstraint);
    }

    public DcqlConstraint getDcqlConstraintForClass(
                                                              EntityInterface entity) {
        if (!classToDcqlConstraintsMap.containsKey(entity)) {
            return new DcqlConstraint();
        }
        List<DcqlConstraint> dcqlConstraints = getDcqlConstraintListForFunctionalClass(entity);
        if (dcqlConstraints.size() == 0) {
            return new DcqlConstraint();
        }
        if (dcqlConstraints.size() == 1) {
            return dcqlConstraints.get(0);
        }
        if (dcqlConstraints.size() > 1) {
            DcqlConstraint group = new GroupBuilder(dcqlConstraints,
                    LogicalOperator.Or).buildGroup();
            return group;
        }
        // impossible
        return null;
    }
}

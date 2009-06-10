package edu.wustl.cab2b.server.queryengine.utils;

import java.util.HashMap;
import java.util.Map;

import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import gov.nih.nci.cagrid.cqlquery.Predicate;

// todo impl
public final class TransformerUtil {
    private static Map<LogicalOperator, gov.nih.nci.cagrid.cqlquery.LogicalOperator> logicalOperatorMap;

    private static Map<RelationalOperator, Predicate> relationalOperatorMap;

    static {
        logicalOperatorMap = new HashMap<LogicalOperator, gov.nih.nci.cagrid.cqlquery.LogicalOperator>();
        logicalOperatorMap.put(LogicalOperator.And,
                               gov.nih.nci.cagrid.cqlquery.LogicalOperator.AND);
        logicalOperatorMap.put(LogicalOperator.Or,
                               gov.nih.nci.cagrid.cqlquery.LogicalOperator.OR);

        relationalOperatorMap = new HashMap<RelationalOperator, Predicate>();
        relationalOperatorMap.put(RelationalOperator.Equals, Predicate.EQUAL_TO);
        relationalOperatorMap.put(RelationalOperator.NotEquals,
                                  Predicate.NOT_EQUAL_TO);
        relationalOperatorMap.put(RelationalOperator.LessThan,
                                  Predicate.LESS_THAN);
        relationalOperatorMap.put(RelationalOperator.LessThanOrEquals,
                                  Predicate.LESS_THAN_EQUAL_TO);
        relationalOperatorMap.put(RelationalOperator.GreaterThan,
                                  Predicate.GREATER_THAN);
        relationalOperatorMap.put(RelationalOperator.GreaterThanOrEquals,
                                  Predicate.GREATER_THAN_EQUAL_TO);
        relationalOperatorMap.put(RelationalOperator.Contains, Predicate.LIKE);
        relationalOperatorMap.put(RelationalOperator.StartsWith, Predicate.LIKE);
        relationalOperatorMap.put(RelationalOperator.EndsWith, Predicate.LIKE);
        relationalOperatorMap.put(RelationalOperator.IsNull, Predicate.IS_NULL);
        relationalOperatorMap.put(RelationalOperator.IsNotNull,
                                  Predicate.IS_NOT_NULL);
        // TODO others
    }

    private TransformerUtil() {

    }

    public static gov.nih.nci.cagrid.cqlquery.LogicalOperator getCqlLogicalOperator(
                                                                                         LogicalOperator logicalOperator) {
        return logicalOperatorMap.get(logicalOperator);
    }

    public static Predicate getCqlPredicate(
                                                 RelationalOperator relationalOperator) {
        if (!relationalOperatorMap.containsKey(relationalOperator)) {
            throw new UnsupportedOperationException("Unknown operator...");
        }
        return relationalOperatorMap.get(relationalOperator);
    }
}

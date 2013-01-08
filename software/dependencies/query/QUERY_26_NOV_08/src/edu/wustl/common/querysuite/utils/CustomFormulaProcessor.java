/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.utils;

import static edu.wustl.common.querysuite.queryobject.RelationalOperator.Between;
import static edu.wustl.common.querysuite.queryobject.RelationalOperator.Equals;
import static edu.wustl.common.querysuite.queryobject.RelationalOperator.GreaterThanOrEquals;
import static edu.wustl.common.querysuite.queryobject.RelationalOperator.In;
import static edu.wustl.common.querysuite.queryobject.RelationalOperator.LessThanOrEquals;
import static edu.wustl.common.querysuite.queryobject.RelationalOperator.NotBetween;
import static edu.wustl.common.querysuite.queryobject.RelationalOperator.NotEquals;
import static edu.wustl.common.querysuite.queryobject.RelationalOperator.NotIn;
import static edu.wustl.common.querysuite.queryobject.RelationalOperator.getSQL;
import static edu.wustl.common.querysuite.queryobject.TermType.isDateTime;
import static edu.wustl.common.querysuite.queryobject.TermType.isInterval;

import java.util.EnumSet;
import java.util.List;

import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.IArithmeticOperand;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.ICustomFormula;
import edu.wustl.common.querysuite.queryobject.IDateOffset;
import edu.wustl.common.querysuite.queryobject.IDateOffsetLiteral;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.queryobject.TimeInterval;
import edu.wustl.common.querysuite.utils.TermProcessor.IAttributeAliasProvider;
import edu.wustl.common.util.ObjectCloner;
import edu.wustl.metadata.util.DyExtnObjectCloner;

/**
 * Provides the string representation of a custom formula. It uses
 * {@link TermProcessor} to obtain string representations of the LHS and RHS
 * terms and connects them based on the relational operator.
 * 
 * @author srinath_k
 * @see TermProcessor
 */
public class CustomFormulaProcessor {
    private TermProcessor termProcessor;

    private static final String SPACE = " ";

    /**
     * Uses the default {@link TermProcessor} to obtain string representation of
     * the terms.
     * 
     * @see TermProcessor#TermProcessor()
     */
    public CustomFormulaProcessor() {
        this.termProcessor = new TermProcessor();
    }

    /**
     * Uses a {@link TermProcessor} initialized with specified parameters to
     * obtain string representation of the terms.
     * 
     * @see TermProcessor#TermProcessor(IAttributeAliasProvider,
     *      DatabaseSQLSettings)
     */
    public CustomFormulaProcessor(IAttributeAliasProvider aliasProvider, DatabaseSQLSettings primitiveOperationProcessor, String queryType) {
        this.termProcessor = new TermProcessor(aliasProvider, primitiveOperationProcessor,queryType);
    }

    /**
     * @return the string representation of the specified formula.
     */
    public String asString(ICustomFormula formula) {
        if (!isValid(formula)) {
            throw new IllegalArgumentException("invalid custom formula.");
        }
        CFProc cfProc;
        if (isTemporal(formula)) {
            // temporal
            cfProc = new TemporalCFProc();
        } else {
            // non temporal
            cfProc = new DefaultCFProc();
        }
        return cfProc.getString(formula);
    }

    private boolean isTemporal(ICustomFormula formula) {
        TermType lhsType = formula.getLhs().getTermType();
        return isInterval(lhsType) || isDateTime(lhsType);
    }

    public boolean isValid(ICustomFormula customFormula) {
        // TODO check only numeric operators.
        if (customFormula == null) {
            return false;
        }
        RelationalOperator operator = customFormula.getOperator();
        if (operator == null) {
            return false;
        }

        int numRhs = customFormula.getAllRhs().size();
        if (operator == Between) {
            if (numRhs != 2) {
                return false;
            }
        }
        if (operator == In || operator == NotIn) {
            if (numRhs == 0) {
                return false;
            }
        } else {
            if (operator.numberOfValuesRequired() != numRhs) {
                return false;
            }
        }

        TermType lhsType = customFormula.getLhs().getTermType();
        for (ITerm rhs : customFormula.getAllRhs()) {
            if (!compatibleTypes(rhs.getTermType(), lhsType)) {
                return false;
            }
        }
        return true;
    }

    private boolean compatibleTypes(TermType type1, TermType type2) {
        return (isInterval(type1) && isInterval(type2)) || type1 == type2;
    }

    private interface CFProc {
        String getString(ICustomFormula formula);
    }

    private class BasicCFProc implements CFProc {
        public String getString(ICustomFormula formula) {
            String lhs = termString(formula.getLhs());
            RelationalOperator relationalOperator = formula.getOperator();
            if (relationalOperator.numberOfValuesRequired() == 0) {
                // is (not) null
                return lhs + SPACE + getSQL(relationalOperator);
            }
            List<ITerm> rhses = formula.getAllRhs();
            String rhs = termString(rhses.get(0));
            if (relationalOperator.numberOfValuesRequired() == 1) {
                // unary operators
                return lhs + SPACE + getSQL(relationalOperator) + SPACE + rhs;
            }
            if (relationalOperator == Between || relationalOperator == NotBetween) {
                String rhs2 = termString(rhses.get(1));
                boolean between = relationalOperator == Between;
                String logicOper = between ? " and " : " or ";
                String rel1 = between ? " >= " : " < ";
                String rel2 = between ? " < " : " >= ";
                // between :
                // (lhs >= rhs1 and lhs <= rhs2)
                // notBetween :
                // (lhs < rhs1 or lhs > rhs2)
                return brackets(lhs + rel1 + rhs) + logicOper + brackets(lhs + rel2 + rhs2);
            }
            // In : lhs = rhs1 or lhs = rhs2 or lhs = rhs3...
            // NotIn : lhs != rhs1 and lhs != rhs2 and lhs != rhs3...
            String sqlOper = relationalOperator == In ? getSQL(Equals) : getSQL(NotEquals);
            String logicOper = relationalOperator == In ? "or" : "and";
            sqlOper = SPACE + sqlOper + SPACE;
            logicOper = SPACE + logicOper + SPACE;
            String res = brackets(lhs + sqlOper + rhs);

            for (int i = 1, n = rhses.size(); i < n; i++) {
                rhs = termString(rhses.get(i));
                String next = logicOper + brackets(lhs + sqlOper + rhs);
                res += next;
            }
            return res;
        }
    }

    private class DefaultCFProc implements CFProc {
        public String getString(ICustomFormula formula) {
            CFProc basic = new BasicCFProc();
            RelationalOperator relationalOperator = formula.getOperator();

            // override between and notBetween so that order of rhses is
            // irrelevant.
            if (relationalOperator == Between || relationalOperator == NotBetween) {
                String res1 = basic.getString(formula);
                String res2 = basic.getString(swapRhses(formula));
                String logicOper = (relationalOperator == Between) ? " or " : " and ";
                // between :
                // (lhs between rhs1, rhs2) or (lhs between rhs2,rhs1)
                // notBetween :
                // (lhs notBetween rhs1, rhs2) and (lhs notBetween rhs2,rhs1)

                return brackets(res1) + logicOper + brackets(res2);
            } else {
                return basic.getString(formula);
            }
        }

    }

    private static ICustomFormula swapRhses(ICustomFormula formula) {
        formula = new DyExtnObjectCloner().clone(formula);
        List<ITerm> rhses = formula.getAllRhs();
        ITerm temp = rhses.get(0);
        rhses.set(0, rhses.get(1));
        rhses.set(1, temp);
        return formula;
    }

    private static EnumSet<RelationalOperator> temporalSpecials = EnumSet.of(Equals, NotEquals, Between,
             In, NotIn);

    private class TemporalCFProc implements CFProc {
        public String getString(ICustomFormula formula) {
            RelationalOperator relOper = formula.getOperator();
            if (!temporalSpecials.contains(relOper)) {
                return new DefaultCFProc().getString(formula);
            }
            ObjectCloner cloner = new DyExtnObjectCloner();
            formula = cloner.clone(formula);
            boolean intervalFormula = isInterval(formula.getLhs().getTermType());
            IArithmeticOperand offset = null;
            if (!intervalFormula) {
                offset = roundingOffset(formula.getLhs());
            }

            if (relOper.numberOfValuesRequired() == 1) {
                ITerm rhs = formula.getAllRhs().get(0);
                if (intervalFormula || offset == null) {
                    offset = roundingOffset(rhs);
                }
                if (relOper == Equals || relOper == NotEquals) {
                    formula.setOperator(relOper == Equals ? Between : NotBetween);
                    ITerm rhs2 = cloner.clone(rhs);
                    formula.addRhs(rhs2);

                    subtract(rhs, offset);
                    add(rhs2, offset);
                    return new BasicCFProc().getString(formula);
                }
//                if (relOper == LessThanOrEquals) {
//                	offset = offset((Integer.valueOf((IDateOffsetLiteral(offset)).getOffset()) - 1),findTimeInterval(rhs));
//                    add(rhs, offset);
//                    return new BasicCFProc().getString(formula);
//                }
//                if (relOper == GreaterThanOrEquals) {
//                	offset = offset((Integer.valueOf((IDateOffsetLiteral(offset)).getOffset()) - 1),findTimeInterval(rhs));
//                    subtract(rhs, offset);
//                    return new BasicCFProc().getString(formula);
//                }
                throw new RuntimeException("can't occur.");
            }
            if (relOper == Between) {
                return brackets(between(formula, offset)) + " or " + brackets(between(swapRhses(formula), offset));
            }

            // in, notIn remain
            RelationalOperator newOper;
            String logicOper;
            if (relOper == In) {
                newOper = Equals;
                logicOper = " or ";
            } else {
                // notIn
                newOper = NotEquals;
                logicOper = " and ";
            }

            String res = "";
            for (ITerm rhs : formula.getAllRhs()) {
                ICustomFormula newFormula = QueryObjectFactory.createCustomFormula();
                newFormula.setLhs(formula.getLhs());
                newFormula.setOperator(newOper);
                newFormula.addRhs(rhs);

                String s = new TemporalCFProc().getString(newFormula);
                res += brackets(s) + logicOper;
            }

            return res.substring(0, res.length() - logicOper.length());
        }

        private String between(ICustomFormula formula, IArithmeticOperand offset) {
            formula = new DyExtnObjectCloner().clone(formula);
            ITerm rhs = formula.getAllRhs().get(0);
            ITerm rhs2 = formula.getAllRhs().get(1);
            if (offset == null) {
                subtract(rhs, roundingOffset(rhs));
                add(rhs2, roundingOffset(rhs2));
            } else {
                subtract(rhs, offset);
                add(rhs2, offset);
            }
            return new BasicCFProc().getString(formula);
        }

        @SuppressWarnings("unchecked")
        private TimeInterval<?> findTimeInterval(ITerm term) {
            TimeInterval res = TimeInterval.Second;

            boolean found = false;
            for (IArithmeticOperand opnd : term) {
                if (opnd instanceof IDateOffset) {
                    TimeInterval timeInterval = ((IDateOffset) opnd).getTimeInterval();
                    if (timeInterval.compareTo(res) > 0) {
                        res = timeInterval;
                    }
                    found = true;
                }
            }
            if (!found) {
                return null;
            }
            return res;
        }

        private void subtract(ITerm term, IArithmeticOperand offset) {
            addOpndToTerm(term, offset, ArithmeticOperator.Minus);
        }

        private void add(ITerm term, IArithmeticOperand offset) {
            addOpndToTerm(term, offset, ArithmeticOperator.Plus);
        }

        private void addOpndToTerm(ITerm term, IArithmeticOperand opnd, ArithmeticOperator oper) {
          if (opnd != null) 
          {
                term.addParantheses();
                term.addOperand(conn(oper), opnd);
          }
        }

        private IConnector<ArithmeticOperator> conn(ArithmeticOperator oper) {
            return QueryObjectFactory.createArithmeticConnector(oper, 0);
        }

        private IArithmeticOperand roundingOffset(ITerm term) {
            TimeInterval<?> timeInterval = findTimeInterval(term);
            if (timeInterval == null) 
            {
                return null;
            }
            if (timeInterval.equals(TimeInterval.Second)) {
                return offset(0, TimeInterval.Second);
            }
            if (timeInterval.equals(TimeInterval.Minute)) {
                return offset(30, TimeInterval.Second);
            }
            if (timeInterval.equals(TimeInterval.Hour)) {
                return offset(30, TimeInterval.Minute);
            }
            if (timeInterval.equals(TimeInterval.Day)) {
                return offset(12, TimeInterval.Hour);
            }
            if (timeInterval.equals(TimeInterval.Week)) {
                return offset(4, TimeInterval.Day);
            }
            if (timeInterval.equals(TimeInterval.Month)) {
                return offset(15, TimeInterval.Day);
            }
            if (timeInterval.equals(TimeInterval.Quarter)) {
                return offset(46, TimeInterval.Day);
            }
            if (timeInterval.equals(TimeInterval.Year)) {
                return offset(183, TimeInterval.Day);
            }
            throw new RuntimeException("won't occur.");
        }

        private IDateOffsetLiteral offset(int offset, TimeInterval<?> timeInterval) {
            return QueryObjectFactory.createDateOffsetLiteral(String.valueOf(offset), timeInterval);
        }

    }

    private String termString(ITerm term) {
        return termProcessor.convertTerm(term).getString();
    }

    private String brackets(String s) {
        return "(" + s + ")";
    }
}

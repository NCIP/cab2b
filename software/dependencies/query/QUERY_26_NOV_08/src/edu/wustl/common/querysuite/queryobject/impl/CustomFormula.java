/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject.impl;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ICustomFormula;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.utils.CustomFormulaProcessor;

public class CustomFormula extends BaseQueryObject implements ICustomFormula {
    private static final long serialVersionUID = -8565812494510602507L;

    private ITerm lhs;

    private List<ITerm> rhs;

    private RelationalOperator operator;

    public ITerm getLhs() {
        if (lhs == null) {
            lhs = QueryObjectFactory.createTerm();
        }
        return lhs;
    }

    public void setLhs(ITerm lhs) {
        this.lhs = lhs;
    }

    public RelationalOperator getOperator() {
        return operator;
    }

    public void setOperator(RelationalOperator operator) {
        this.operator = operator;
    }

    public List<ITerm> getAllRhs() {
        if (rhs == null) {
            rhs = new ArrayList<ITerm>();
        }
        return rhs;
    }

    public void addRhs(ITerm rhs) {
        getAllRhs().add(rhs);
    }

    // for hibernate
    @SuppressWarnings("unused")
    private List<ITerm> getRhs() {
        return rhs;
    }

    @SuppressWarnings("unused")
    private void setRhs(List<ITerm> rhs) {
        this.rhs = rhs;
    }

    public boolean isValid() {
        return new CustomFormulaProcessor().isValid(this);
    }
}

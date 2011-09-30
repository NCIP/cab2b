package edu.wustl.common.querysuite.queryobject;

/**
 * Represents the binary arithmetic operators.
 * 
 * @author srinath_k
 * 
 */
public enum ArithmeticOperator implements IBinaryOperator {
    Plus("+"), Minus("-"), MultipliedBy("*"), DividedBy("/"), Unknown("");

    private ArithmeticOperator(String s) {
        this.mathString = s;
    }

    private String mathString;

    /**
     * @return the mathematical representation of this operator e.g.
     *         <tt>Plus.mathString() = "+"</tt>.
     */
    public String mathString() {
        return mathString;
    }
}

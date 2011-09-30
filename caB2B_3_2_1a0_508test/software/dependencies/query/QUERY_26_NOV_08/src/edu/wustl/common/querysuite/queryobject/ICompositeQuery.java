package edu.wustl.common.querysuite.queryobject;


/**
 * @author vijay_pande
 * Interface for Composite Query
 */
public interface ICompositeQuery extends IAbstractQuery
{
	/**
	 * Method to return operation associated with the Composite query
	 * @return operation Object of type IOperation
	 */
	public IOperation getOperation();
	
	/**
	 * Method to set operation associated with the Composite query
	 * @param operation Object of type IOperation
	 */
	public void setOperation(IOperation operation);
}

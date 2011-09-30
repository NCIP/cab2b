package titli.controller.interfaces;


/**
 * Interface that represents object metadata like tableName and identifier
 * @author pooja_deshpande
 *
 */
public interface ObjectMetadataInterface
{
	public String getTableName(Object obj);
	public String getUniqueIdentifier(Object obj);
}

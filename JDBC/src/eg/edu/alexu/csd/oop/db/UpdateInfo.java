package eg.edu.alexu.csd.oop.db;

public class UpdateInfo {

	private String tableName = null;
	private String columnSelectorName = null;
	private String opration = null;
	private String value = null;
	private int type;
	private String[] insertedColumns;
	private String[] insertedValues;
	private String[][] updateValues;
	
	public String[][] getUpdateValues() {
		return updateValues;
	}
	public void setUpdateValues(String[][] updateValues) {
		this.updateValues = updateValues;
	}
	
	
	public String[] getInsertedColumns() {
		return insertedColumns;
	}
	public void setInsertedColumns(String[] insertedColumns) {
		this.insertedColumns = insertedColumns;
	}
	public String[] getInsertedValues() {
		return insertedValues;
	}
	public void setInsertedValues(String[] insertedValues) {
		this.insertedValues = insertedValues;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColumnSelectorName() {
		return columnSelectorName;
	}
	public void setColumnSelectorName(String columnSelectorName) {
		this.columnSelectorName = columnSelectorName;
	}
	public String getOpration() {
		return opration;
	}
	public void setOpration(String opration) {
		this.opration = opration;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}

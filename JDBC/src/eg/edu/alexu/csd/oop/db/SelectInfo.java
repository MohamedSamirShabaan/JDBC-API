package eg.edu.alexu.csd.oop.db;

public class SelectInfo {

	private String tableName = null;
	private String[] columns = null;
	private String columnSelectorName = null;
	private String opration = null;
	private String value = null;
	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String[] getColumns() {
		return columns;
	}

	public void setColumns(String[] columns) {
		this.columns = columns;
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

}

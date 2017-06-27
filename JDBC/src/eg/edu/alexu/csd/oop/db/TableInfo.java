package eg.edu.alexu.csd.oop.db;

public class TableInfo {
	
	private String [][] columns = null;
	private String tableName = null ;
	
	public String[][] getColumns() {
		return columns;
	}
	public void setColumns(String[][] columns) {
		this.columns = columns;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
}

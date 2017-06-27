package eg.edu.alexu.csd.oop.db;

import java.sql.SQLException;

public class Parser {
	
	public boolean test(String s, String patten) {
		if (!s.matches("^[Cc][Rr][Ee][Aa][Tt][Ee].*")
				&& !s.matches("^[Dd][Rr][oO][Pp].*")
				&& !s.matches("^[dD][eE][lL][eE][tT][eE].*")
				&& !s.matches("^[uU][pP][dD][aA][tT][eE].*")
				&& !s.matches("^[Ss][Ee][Ll][Ee][Cc][Tt].*")) {
			if (!s.replace("\\s+|\\t", "").endsWith(")")) {
				return false;
			}
		}
		if (s.matches(patten)) {
			return true;
		}
		return false;
	}

	public SelectInfo selectParse(String query) {
		SelectInfo newSelect = new SelectInfo();
		if (test(query, Regex.SELECT)) {
			newSelect.setColumns(query.replaceAll(Regex.SELECT, "$1").toLowerCase()
					.replaceAll("\\s+", "").split("\\s*,\\s*"));
			newSelect.setTableName(query.replaceAll(Regex.SELECT, "$2").toLowerCase()
					.replaceAll("\\s+", ""));
			if (newSelect.getColumns()[0].matches("\\s*\\*\\s*")) {
				String where = query.replaceAll(Regex.SELECT, "$3");
				if (where.matches("^\\s*[Ww][Hh][Ee][Rr][Ee].*")) {
					newSelect.setColumnSelectorName(query
							.replaceAll(Regex.SELECT, "$4").toLowerCase()
							.replaceAll("\\s+", ""));
					newSelect.setOpration(query.replaceAll(Regex.SELECT, "$5")
							.toLowerCase().replaceAll("\\s+", ""));
					newSelect.setValue(query.replaceAll(Regex.SELECT, "$6")
							.toLowerCase().replaceAll("\\s+", ""));
					// SELECT * FROM tableName WHERE columName = value
					// type1
					newSelect.setType(1);
				} else {
					// SELECT * FROM tableName
					// type2
					newSelect.setType(2);
				}
			} else {
				String where = query.replaceAll(Regex.SELECT, "$3");
				if (where.matches("^\\s*[Ww][Hh][Ee][Rr][Ee].*")) {
					newSelect.setColumnSelectorName(query
							.replaceAll(Regex.SELECT, "$4").toLowerCase()
							.replaceAll("\\s+", ""));
					newSelect.setOpration(query.replaceAll(Regex.SELECT, "$5")
							.toLowerCase().replaceAll("\\s+", ""));
					newSelect.setValue(query.replaceAll(Regex.SELECT, "$6")
							.toLowerCase().replaceAll("\\s+", ""));
					// SELECT colName FROM tableName WHERE colSeNe F= value
					// type3
					newSelect.setType(3);
				} else {
					// SELECT colunmName FROM tableName
					// type4
					newSelect.setType(4);
				}
			}
		} else {
			throw new RuntimeException("Exception in Select");
		}
		return newSelect;
	}

	public TableInfo createTableParser(String query) throws SQLException{
		String tableName = query.replaceAll(Regex.EXTRACT_TABLE_VALUE, "$1")
				.toLowerCase();
		String values = query.replaceAll(Regex.EXTRACT_TABLE_VALUE, "$2").toLowerCase();
		String[] parts = values.split("\\s*,\\s*");
		String[][] finalValue = new String[parts.length][2];
		int u = 0;
		for (String n : parts) {
			finalValue[u] = n.split(" ");
			u++;
		}
		TableInfo newTable = new TableInfo();
		newTable.setColumns(finalValue);
		newTable.setTableName(tableName);
		return newTable;
	}

	public UpdateInfo delete(String query)throws SQLException{
		UpdateInfo newUpdateInfo = new UpdateInfo();
		newUpdateInfo.setTableName(query.replaceAll(Regex.DELETE, "$1").toLowerCase());
		if (newUpdateInfo.getTableName().matches("\\s*\\*\\s*")) {
			newUpdateInfo.setTableName(query.replaceAll(Regex.DELETE, "$2").toLowerCase());
			// DELETE * FROM Mohamed
			//type1
			newUpdateInfo.setType(1);
		} else {
			newUpdateInfo.setTableName(query.replaceAll(Regex.DELETE, "$2").toLowerCase());
			String where = query.replaceAll(Regex.DELETE, "$3");
			if (where.matches("^\\s*[Ww][Hh][Ee][Rr][Ee].*")) {
				newUpdateInfo.setColumnSelectorName(query.replaceAll(Regex.DELETE, "$4").toLowerCase());
				newUpdateInfo.setOpration(query.replaceAll(Regex.DELETE, "$5").toLowerCase());
				newUpdateInfo.setValue(query.replaceAll(Regex.DELETE, "$6").toLowerCase());
				// DELETE FROM mohamed WHERE selectedColumnName <=> value
				//type2
				newUpdateInfo.setType(2);
			} else {
				// DELETE FROM mohamed
				//type3
				newUpdateInfo.setType(3);
			}
		}
		return newUpdateInfo;
	}
	
	public UpdateInfo insert(String query) throws SQLException{
		UpdateInfo newUpdateInfo = new UpdateInfo();
		String values = query.replaceAll(Regex.INSERT, "$4").toLowerCase();
		if (values.matches(Regex.VALUE)) {
			newUpdateInfo.setTableName(query.replaceAll(Regex.INSERT, "$1").toLowerCase());
			String columns = query.replaceAll(Regex.INSERT, "$2").toLowerCase();
			columns = columns.replace("(", "").replace(")", "");
			newUpdateInfo.setInsertedColumns(columns.split("\\s*,\\s*"));
			newUpdateInfo.setInsertedValues(values.split("\\s*,\\s*"));
			if (newUpdateInfo.getInsertedColumns().length != newUpdateInfo.getInsertedValues().length && newUpdateInfo.getInsertedColumns().length != 1)
				throw new SQLException("Exception in Insert nubmer of columns diffrent");
			if (columns.equals("")) {
				newUpdateInfo.setInsertedColumns(null);
			}
		}
		return newUpdateInfo;
	}
	
	public UpdateInfo update(String query) throws SQLException{
		UpdateInfo newUpdateInfo = new UpdateInfo();
		if (test(query, Regex.UPDATE)) {
			// UPDATE
			newUpdateInfo.setTableName(query.replaceAll(Regex.UPDATE, "$1").toLowerCase());
			String namesAndValues = query.replaceAll(Regex.UPDATE, "$2")
					.toLowerCase();
			String[] parts = namesAndValues.split("\\s*,\\s*");
			String[][] finalValue = new String[parts.length][2];
			int u = 0;
			for (String n : parts) {
				String[] temp = n.split("\\s*=\\s*");
				if (temp.length == 2) {
					finalValue[u] = temp;
				} else if (temp.length > 2) {
					finalValue[u][0] = temp[0];
					finalValue[u][1] = "";
				}
				u++;
			}
			newUpdateInfo.setUpdateValues(finalValue);
			// UPDATE table_Name_1 SET column1=value1, column2=value2
			//type1
			newUpdateInfo.setType(1);
		} else if (test(query, Regex.UPDATE_WHERE)) {
			// UPDATE
			newUpdateInfo.setTableName(query.replaceAll(Regex.UPDATE_WHERE, "$1").toLowerCase());
			String namesAndValues = query.replaceAll(Regex.UPDATE_WHERE, "$2").toLowerCase();
			String[] parts = namesAndValues.split("\\s*,\\s*");
			String[][] finalValue = new String[parts.length][2];
			int u = 0;
			for (String n : parts) {
				String[] temp = n.split("\\s*=\\s*");
				if (temp.length == 2) {
					finalValue[u] = temp;
				} else if (temp.length > 2) {
					finalValue[u][0] = temp[0];
					finalValue[u][1] = "";
				}
				u++;
			}
			newUpdateInfo.setUpdateValues(finalValue);
			String where = query.replaceAll(Regex.UPDATE_WHERE, "$4");
			if (where.matches("^\\s*[Ww][Hh][Ee][Rr][Ee].*")) {
				newUpdateInfo.setColumnSelectorName(query.replaceAll(Regex.UPDATE_WHERE, "$5").toLowerCase());
				newUpdateInfo.setOpration(query.replaceAll(Regex.UPDATE_WHERE, "$6").toLowerCase());
				newUpdateInfo.setValue(query.replaceAll(Regex.UPDATE_WHERE, "$7").toLowerCase());
				// UPDATE table_Name_1 SET column1=value1, column2=value2 WHERE some_column = 15
				//type2
				newUpdateInfo.setType(2);
			}
		} else{
			throw new SQLException("Exception in update  query");
		}
		return newUpdateInfo;
	}
	public class Regex{
		
		public static final String DROP = "^[Dd][Rr][oO][Pp]\\s+([Tt][Aa][bB][Ll][Ee]|[Dd][Aa][Tt][Aa][bB][Aa][Ss][Ee])\\s+([a-zA-Z0-9_]+)\\s*";
		public static final String CREATE_DATABASE = "^[Cc][Rr][Ee][Aa][Tt][Ee]\\s+[Dd][Aa][Tt][Aa][bB][Aa][Ss][Ee]\\s+([a-zA-Z0-9_]+)\\s*";
		public static final String CREATE_TABLE = "^[Cc][Rr][Ee][Aa][Tt][Ee]\\s+[Tt][Aa][bB][Ll][Ee]\\s+[a-zA-Z0-9_]+\\s*\\((\\s*[a-zA-Z0-9_]+\\s+([iI][nN][tT]|[vV][aA][rR][cC][hH][aA][rR]){1}\\s*,?)+\\)\\s*";
		public static final String EXTRACT_TABLE_VALUE = "^[Cc][Rr][Ee][Aa][Tt][Ee]\\s+[Tt][Aa][bB][Ll][Ee]\\s+([a-zA-Z0-9_]+)\\s*\\((.*)\\)\\s*";
		public static final String DELETE = "^[dD][eE][lL][eE][tT][eE]\\s+(\\*\\s+)*[fF][rR][Oo][Mm]\\s+([a-zA-Z0-9_]+)(\\s+[Ww][Hh][Ee][Rr][Ee]\\s+([a-zA-Z0-9_]+)\\s*([<>=]?)\\s*([\'\'\"]*\\s*\\w+[\'\'\"]*\\s*))*\\s*";
		public static final String INSERT = "^[Ii][Nn][Ss][Ee][Rr][Tt]\\s+[Ii][Nn][Tt][Oo]\\s+([a-zA-Z0-9_]+)\\s*(\\((\\s*[a-zA-Z0-9_]+\\s*,?\\s*)+\\))*\\s*[vV][aA][lL][uU][eE][sS]\\s+\\((.*)\\)";
		public static final String SELECT = "^[Ss][Ee][Ll][Ee][Cc][Tt]\\s+([a-zA-Z0-9_,\\s*]+\\s+|\\*\\s+)[fF][rR][Oo][Mm]\\s+([a-zA-Z0-9_]+)(\\s+[Ww][Hh][Ee][Rr][Ee]\\s+([a-zA-Z0-9_]+)\\s*([<>=]?)\\s*(\\w+))*\\s*";
		public static final String UPDATE_WHERE = "^[uU][pP][dD][aA][tT][eE]\\s+([a-zA-Z0-9_]+)\\s+[Ss][Ee][Tt]\\s+((\\s*[a-zA-Z0-9_]+\\s*=+\\s*[\'\"]*\\s*\\w+\\s*[\'\"]*\\s*,?\\s*)+)(\\s+[Ww][Hh][Ee][Rr][Ee]\\s+([a-zA-Z0-9_]+)\\s*([<>=]?)\\s*([\'\'\"]*\\s*\\w+[\'\'\"]*\\s*))";
		public static final String UPDATE = "^[uU][pP][dD][aA][tT][eE]\\s+([a-zA-Z0-9_]+)\\s+[Ss][Ee][Tt]\\s+((\\s*[a-zA-Z0-9_]+\\s*=+\\s*[\'\"]*\\s*\\w+\\s*[\'\"]*\\s*,?\\s*)+)";
		public static final String VALUE = "(\\s*(\'|\")*\\s*\\w+\\s*(\'|\")*,)+\\s*(\'|\")*\\s*\\w+\\s*(\'|\")*";

	}
	
}

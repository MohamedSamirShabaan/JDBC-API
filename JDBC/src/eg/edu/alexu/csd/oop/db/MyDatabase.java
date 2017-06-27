package eg.edu.alexu.csd.oop.db;

import java.io.File;
import java.sql.SQLException;

import eg.edu.alexu.csd.oop.db.Parser.Regex;

public class MyDatabase implements Database {

	private String name = null;
	private String fullPath = null;
	private String path = null;
	private boolean falg = true;
	
	/**
	 * Singleton design pattern.
	 */
	private static MyDatabase instance;

	public static MyDatabase getInstance(String path) {
		try {
			if (instance == null) {
				instance = new MyDatabase(path);
			} else {
				instance.path = path;
			}
			return instance;
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("Error in singltone!!!");
		}
	}

	public static MyDatabase getInstance() {
		try {
			if (instance == null) {
				instance = new MyDatabase();
			}
			return instance;
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("Error in singltone!!!");
		}
	}

	public static void destoryInstance() {
		instance = null;
	}

	private MyDatabase() {
	}

	private MyDatabase(String path) {
		this.path = path;
	}

	@Override
	public String createDatabase(String databaseName, boolean dropIfExists) {
		// TODO Auto-generated method stub
		falg = true;
		name = databaseName.toLowerCase();
		File file = new File(path + File.separator + name);
		try {
			if (dropIfExists) {
				if (file.exists() && file.isDirectory()) {
					// DROP DATABASE databaseName
					executeStructureQuery("DROP DATABASE " + name);
					// CREATE DATABASE databaseName
					executeStructureQuery("CREATE DATABASE " + name);
				} else {
					// CREATE DATABASE databaseName
					executeStructureQuery("CREATE DATABASE " + name);
				}
			} else {
				if (file.exists() && file.isDirectory()) {
					// no thing
				} else {
					// CREATE DATABASE databaseName
					executeStructureQuery("CREATE DATABASE " + name);
				}
			}
			fullPath = file.getAbsolutePath();
			return fullPath;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean executeStructureQuery(String query) throws SQLException {
		// TODO Auto-generated method stub
		Parser parser = new Parser();
		AccessTables at = new MyAccessTables();
		boolean result = false;
		if (parser.test(query, Regex.CREATE_DATABASE)) {
			// CREATE DATABASE databaseName
			name = query.replaceAll(Regex.CREATE_DATABASE, "$1").toLowerCase();
			File file = new File(path + File.separator + name);
			result = file.mkdirs();
			fullPath = file.getAbsolutePath();
		} else if (parser.test(query, Regex.CREATE_TABLE)) {
			// CREATE TABLE tableName (name_1 type, name_2 type)
			TableInfo newTable = parser.createTableParser(query);
			String[][] finalValue = newTable.getColumns();
			String tableName = newTable.getTableName();
			File file = new File(fullPath + File.separator + tableName + ".xml");
			if (!file.exists()) {
				// to you MASRY you should use 2D array finalValues to detect
				// names and types of table's columns
				result = at.creatTable(fullPath, tableName, finalValue);
			} else {
				return false;
			}
		} else if (parser.test(query,
				"^[Dd][Rr][Oo][Pp]\\s+[Dd][Aa][Tt][Aa][bB][Aa][Ss][Ee].*")
				&& parser.test(query, Regex.DROP)) {
			// DROP DATABASE databaseName
			String nameDrop = query.replaceAll(Regex.DROP, "$2").toLowerCase();
			File file = new File(path + File.separator + nameDrop);
			result = deleteFolder(file);
		} else if (parser.test(query,
				"^[Dd][Rr][Oo][Pp]\\s+[Tt][Aa][Bb][Ll][Ee].*")
				&& parser.test(query, Regex.DROP)) {
			// DROP TABLE tableName
			String tableName = query.replaceAll(Regex.DROP, "$2")
					.toLowerCase();
			File file = new File(fullPath + File.separator + tableName + ".xml");
			result = deleteFolder(file);
		} else {
			throw new SQLException("Exception in Creat & Drop (Data or Table)");
		}
		if (falg) {
			return result;
		} else {
			return true;
		}

	}

	@Override
	public Object[][] executeQuery(String query) throws SQLException {
		// TODO Auto-generated method stub
		try {
			Parser parser = new Parser();
			SelectInfo newSelect = new SelectInfo();
			AccessTables at = new MyAccessTables();
			Object[][] selected = null;
			newSelect = parser.selectParse(query);
			String tableName = newSelect.getTableName();
			String[] columns = newSelect.getColumns();
			String columnSelectorName = newSelect.getColumnSelectorName();
			String opration = newSelect.getOpration();
			String value = newSelect.getValue();
			int type = newSelect.getType();
			if (type == 1) {
				selected = at.select(fullPath, tableName, columnSelectorName,
						opration, value);
			} else if (type == 2) {
				selected = at.select(fullPath, tableName);
			} else if (type == 3) {
				selected = at.select(fullPath, tableName, columns,
						columnSelectorName, opration, value);
			} else if (type == 4) {
				selected = at.select(fullPath, tableName, columns);
			}
			return selected;
		} catch (Exception e) {
			// TODO: handle exception
			throw new SQLException("Error in select");
		}
	}

	@Override
	public int executeUpdateQuery(String query) throws SQLException {
		// TODO Auto-generated method stub
		Parser parser = new Parser();
		AccessTables at = new MyAccessTables();
		int count = 0;
		String columnSelectorName = null;
		String opration = null;
		String value = null;
		String tableName = null;
		UpdateInfo newUpdateInfo;
		if (parser.test(query, "^[Dd][Ee][Ll][Ee][Tt][Ee].*")
				&& parser.test(query, Regex.DELETE)) {
			// DELETE
			newUpdateInfo = parser.delete(query);
			tableName = newUpdateInfo.getTableName();
			int type = newUpdateInfo.getType();
			if(type==1||type==3){
				count = at.delete(fullPath, tableName);
			} else if(type==2){
				columnSelectorName = newUpdateInfo.getColumnSelectorName(); 
				opration = newUpdateInfo.getOpration();
				value = newUpdateInfo.getValue();
				count = at.delete(fullPath, tableName, columnSelectorName,opration, value);
			}
		} else if (parser.test(query, "^[Ii][Nn][Ss][Ee][Rr][Tt].*")
				&& parser.test(query, Regex.INSERT)) {
			// INSERT
			newUpdateInfo = parser.insert(query);
			count = at.insert(fullPath, newUpdateInfo.getTableName(), newUpdateInfo.getInsertedColumns(), newUpdateInfo.getInsertedValues());
		} else if (parser.test(query, "^[Uu][Pp][Dd][Aa][Tt][Ee].*")) {
			// UPDATE
			newUpdateInfo = parser.update(query);
			tableName = newUpdateInfo.getTableName();
			int type = newUpdateInfo.getType();
			String[][] values = newUpdateInfo.getUpdateValues();
			if(type==1){
				count = at.update(fullPath, tableName, values);
			} else if(type==2){
				columnSelectorName = newUpdateInfo.getColumnSelectorName(); 
				opration = newUpdateInfo.getOpration();
				value = newUpdateInfo.getValue();
				count = at.update(fullPath, tableName, values, columnSelectorName, opration, value);
			}
		} else {
			throw new SQLException("Exception in Delete, Insert or Update");
		}
		return count;
	}

	private boolean deleteFolder(File file) {
		if (file.isDirectory()) {
			for (File sub : file.listFiles()) {
				deleteFolder(sub);
			}
		}
		return file.delete();
	}

}

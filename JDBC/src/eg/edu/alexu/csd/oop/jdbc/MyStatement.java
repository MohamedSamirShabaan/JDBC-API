package eg.edu.alexu.csd.oop.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.LinkedList;

import eg.edu.alexu.csd.oop.db.Database;
import eg.edu.alexu.csd.oop.db.MyDatabase;
import eg.edu.alexu.csd.oop.db.Parser;
import eg.edu.alexu.csd.oop.db.Parser.Regex;
import eg.edu.alexu.csd.oop.db.SelectInfo;
import eg.edu.alexu.csd.oop.db.TableInfo;

public class MyStatement implements java.sql.Statement {

	private String path = null;
	private int updatedCount = 0;
	private ResultSet resultSet = null;
	private Database db = null;
	private TableInfo newTableInfo;
	private LinkedList <String> batch = new LinkedList<String>();
	private Connection connect;

	public MyStatement(String path , Connection con) {
		// TODO Auto-generated constructor stub
		this.path = path;
		connect = con;
		db = MyDatabase.getInstance(path);
	}

	@Override
	public boolean execute(String sql) throws SQLException {
		// TODO Auto-generated method stub
		Parser parser = new Parser();
		if (path == null) {
			throw new SQLException("call execute on colse Statement");
		} else if (parser.test(sql, Regex.INSERT)
				|| parser.test(sql, Regex.DELETE)
				|| parser.test(sql, Regex.UPDATE)
				|| parser.test(sql, Regex.UPDATE_WHERE)) {
			int i = this.executeUpdate(sql);
			if (i != 0) {
				return true;
			} else {
				return false;
			}
		} else if (parser.test(sql, Regex.SELECT)) {
			resultSet = this.executeQuery(sql);
			if (!resultSet.next()) {
				return false;
			} else {
				return true;
			}
		}
		Parser paresr = new Parser();
		newTableInfo = paresr.createTableParser(sql);
		return db.executeStructureQuery(sql);
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		// TODO Auto-generated method stub
		if (path == null) {
			throw new SQLException("call executeQuery on colse Statement");
		} else {
			Parser parser = new Parser();
			SelectInfo newSelect = null;
			if (sql.matches(".*\\*.*") && sql.contains("*")) {
				newSelect = parser.selectParse(sql);
				if (newTableInfo.getTableName()
						.equals(newSelect.getTableName())) {
					String[][] tableStructure = newTableInfo.getColumns();
					String[] columns = new String[tableStructure.length];
					for (int i = 0; i < tableStructure.length; i++) {
						columns[i] = tableStructure[i][0];
					}
					newSelect.setColumns(columns);
				}
			} else if (!sql.contains("*")) {
				newSelect = parser.selectParse(sql);
			}
			Object[][] values = db.executeQuery(sql);
			resultSet = new MyResultSet(values, newSelect, this);
			return getResultSet();
		}
	}

	public int executeUpdate(String sql) throws SQLException {
		// TODO Auto-generated method stub
		if (path == null || db == null) {
			throw new SQLException("call executeUpdate on close Statement");
		} else {
			updatedCount = db.executeUpdateQuery(sql);
			return getUpdateCount();
		}
	}

	@Override
	public void close() throws SQLException {
		// TODO Auto-generated method stub
		try {
			path = null;
		} catch (Exception e) {
			// TODO: handle exception
			throw new SQLException("Error in close Connection!!!");
		}
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		// TODO Auto-generated method stub
		try {
			if (path != null) {
				return resultSet;
			} else {
				throw new SQLException("getResult on a close Statement!!!");
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new SQLException("Error in getResultSet!!!");
		}
	}

	@Override
	public int getUpdateCount() throws SQLException {
		// TODO Auto-generated method stub
		try {
			if (path != null) {
				return updatedCount;
			} else {
				throw new SQLException("getUpdateCount on a close Statement!!!");
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new SQLException("Error in getUpdateCount!!!");
		}
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addBatch(String sql) throws SQLException {
		// TODO Auto-generated method stub
		batch.add(sql);

	}

	@Override
	public void clearBatch() throws SQLException {
		// TODO Auto-generated method stub
		batch.clear();
	}

	@Override
	public int[] executeBatch() throws SQLException {
		// TODO Auto-generated method stub
		int [] operation = new int [batch.size()];
		int counter = 0;
		for(String s: batch){
			operation[counter++] = executeUpdate(s);
		}
		batch.clear();
		return operation;
	}

	@Override
	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		return connect;
	}

	/*------------------------------------END-------------------------------------*/

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void setMaxFieldSize(int max) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public int getMaxRows() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void setMaxRows(int max) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void cancel() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void setCursorName(String name) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public int getFetchDirection() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public int getResultSetType() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

}

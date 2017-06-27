package eg.edu.alexu.csd.oop.jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class MyConnection implements java.sql.Connection {

	private String path = null;

	public MyConnection(String path) {
		this.path = path;
	}

	@Override
	public Statement createStatement() throws SQLException {
		// TODO Auto-generated method stub
		if(path==null){
			throw new SQLException("call createStatement on colse connection");
		}else{
			return new MyStatement(path , this);
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
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void commit() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void rollback() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void setCatalog(String catalog) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public String getCatalog() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
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
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void setSchema(String schema) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public String getSchema() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void abort(Executor executor) throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

}

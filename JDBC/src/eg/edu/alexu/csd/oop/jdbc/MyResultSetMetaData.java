package eg.edu.alexu.csd.oop.jdbc;

import java.sql.SQLException;

public class MyResultSetMetaData implements java.sql.ResultSetMetaData {

	private Object [][] resultSet;
	private String[] columnsNames;
	private String tableName;

	public MyResultSetMetaData(String tablename, String[] columnsNames, Object[][] resultSet) {
		this.columnsNames = columnsNames;
		this.tableName = tablename;
		this.resultSet = resultSet;
	}

	@Override
	public int getColumnCount() throws SQLException {
		// TODO Auto-generated method stub
		try{
		return resultSet[0].length;
		}catch(NullPointerException e){
			return 0;
		}
	}

	@Override
	public String getColumnLabel(int column) throws SQLException {
		// TODO Auto-generated method stub
		return getColumnName(column);
	}

	@Override
	public String getColumnName(int column) throws SQLException {
		// TODO Auto-generated method stub
		if(column > 0 && column <= getColumnCount())
		{
			return columnsNames[column-1];
		}
		else
		{ 
			throw new SQLException();
		}
	}

	@Override
	public int getColumnType(int column) throws SQLException {
		// TODO Auto-generated method stub
		if (column > 0 && column <= columnsNames.length) {
			try{
				@SuppressWarnings("unused")
				int i = (Integer)resultSet[0][column-1];
				return java.sql.Types.INTEGER;
			}catch(Exception e){
				return java.sql.Types.VARCHAR;
			}
		}
		return column;
	}
	
	@Override
	public String getTableName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return tableName;
	}
	
	/*************************************************************************************/
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isAutoIncrement(int column) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCaseSensitive(int column) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSearchable(int column) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCurrency(int column) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int isNullable(int column) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSigned(int column) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int getColumnDisplaySize(int column) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSchemaName(int column) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int getPrecision(int column) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int getScale(int column) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getCatalogName(int column) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getColumnTypeName(int column) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isReadOnly(int column) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isWritable(int column) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDefinitelyWritable(int column) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getColumnClassName(int column) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}

package eg.edu.alexu.csd.oop.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import eg.edu.alexu.csd.oop.db.SelectInfo;

public class MyResultSet implements java.sql.ResultSet {

	private int currentRow = -1;
	private Statement statement;
	private Object resultSet[][];
	private boolean isClosed = false;
	private SelectInfo newSelect = null;

	public MyResultSet(Object[][] values, SelectInfo newSelect, Statement stat) {
		this.statement = stat;
		this.newSelect = newSelect;
		this.resultSet = values;
	}
	
	/**
	 * Moves the cursor to the given row number in this ResultSet object.
	 * Returns: true if the cursor is moved to a position in this ResultSet
	 * object; false if the cursor is before the first row or after the last row
	 * Throws: SQLException - if a database access error occurs; this method is
	 * called on a closed result set or the result set type is TYPE_FORWARD_ONLY
	 */
	@Override
	public boolean absolute(int row) throws SQLException {
		// TODO Auto-generated method stub
		if(row == 0){
			currentRow = -1;
			return false;
		}else if(row > 0){
			currentRow = row-1;
		}else if(row < 0)
			currentRow = row + resultSet.length;
		return true;
	}

	/**
	 * Retrieves whether the cursor is before the first row in this ResultSet
	 * object.
	 */
	@Override
	public boolean isBeforeFirst() throws SQLException {
		return (currentRow == -1);
	}

	/**
	 * Retrieves whether the cursor is after the last row in this ResultSet
	 * object.
	 */
	@Override
	public boolean isAfterLast() throws SQLException {
		return (currentRow == resultSet.length);
	}

	/**
	 * Retrieves whether the cursor is on the first row of this ResultSet
	 * object.
	 */
	@Override
	public boolean isFirst() throws SQLException {
		return (currentRow == 0);
	}

	/**
	 * Retrieves whether the cursor is on the last row of this ResultSet object.
	 */
	@Override
	public boolean isLast() throws SQLException {
		return (currentRow == resultSet.length - 1);
	}

	/**
	 * Moves the cursor to the front of this ResultSet object, just before the
	 * first row.
	 */
	@Override
	public void beforeFirst() throws SQLException {
		currentRow = -1;
	}

	/**
	 * Moves the cursor to the end of this ResultSet object, just after the last
	 * row.
	 */
	@Override
	public void afterLast() throws SQLException {
		currentRow = resultSet.length;
	}

	/** Moves the cursor to the first row in this ResultSet object */
	@Override
	public boolean first() throws SQLException {
		if (resultSet.length == 0)
			return false;
		currentRow = 0;
		return true;
	}

	/** Moves the cursor to the last row in this ResultSet object. */
	@Override
	public boolean last() throws SQLException {
		if (resultSet.length == 0)
			return false;
		currentRow = resultSet.length - 1;
		return true;
	}

	/** Moves the cursor forward one row from its current position. */
	@Override
	public boolean next() throws SQLException{
		if(currentRow >= resultSet.length-1 || resultSet.length == 0)
			return false;
		else{
			currentRow++;
			return true;
		}
	}

	/** Moves the cursor to the previous row in this ResultSet object. */
	@Override
	public boolean previous() throws SQLException {
		if (currentRow >= 0) {
			currentRow--;
			return true;
		} else
			return false;
	}

	/**
	 * Releases this ResultSet object's database and JDBC resources immediately
	 * instead of waiting for this to happen when it is automatically closed.
	 */
	@Override
	public void close() throws SQLException {
		isClosed = true;
	}

	/** Retrieves whether this ResultSet object has been closed. */
	@Override
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return isClosed;
	}

	@Override
	public Statement getStatement() throws SQLException {
		// TODO Auto-generated method stub
		return this.statement;
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * ResultSet object as an int in the Java programming language.
	 */
	@Override
	public int getInt(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		if (isClosed()) {
			throw new SQLException();
		}
		Integer counter = 0;
		if (columnIndex > 0 && columnIndex <= resultSet[0].length
				&& currentRow >= 0 && currentRow < resultSet.length) {
			try {
				counter = (Integer) resultSet[currentRow][columnIndex -1];
				return counter;
			} catch (Exception e) {
				throw new SQLException();
			}
		}
		return counter;
	}

	@Override
	public int getInt(String ColumnName) throws SQLException {
		// TODO Auto-generated method stub
		return (Integer)getInt(findColumn(ColumnName));
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * ResultSet object as a String in the Java programming language.
	 */
	@Override
	public String getString(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		if (isClosed()) {
			throw new SQLException();
		}
		if (columnIndex > 0 && columnIndex <= resultSet[0].length
				&& currentRow >= 0 && currentRow < resultSet.length) {
			try {
				String str = (String) resultSet[currentRow][columnIndex-1].toString();
				return str;
			} catch (Exception e) {
				throw new SQLException();
			}
		}
		return null;
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * ResultSet object as a String in the Java programming language.
	 */
	@Override
	public String getString(String ColumnName) throws SQLException {
		return getString(findColumn(ColumnName)).toString();
	}

	/**
	 * Gets the value of the designated column in the current row of this
	 * ResultSet object as an Object in the Java programming language.
	 */
	@Override
	public Object getObject(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		try{
			return getInt(columnIndex);
		}catch(SQLException e){
			return getString(columnIndex);
		}
		
	}

	/** Maps the given ResultSet column label to its ResultSet column index. */
	@Override
	public int findColumn(String ColumnName) throws SQLException {
		// TODO Auto-generated method stub
		if (isClosed()) {
			throw new SQLException();
		}
		for (int i = 0; i < newSelect.getColumns().length ; i++) {
			if (((String) newSelect.getColumns()[i])
					.equalsIgnoreCase(ColumnName)) {
				return i+1;
			}
		}
		throw new SQLException();
	}

	/**
	 * Retrieves the number, types and properties of this ResultSet object's
	 * columns.
	 */
	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		// TODO Auto-generated method stub
		return new MyResultSetMetaData(newSelect.getTableName(),
				newSelect.getColumns(), resultSet);
	}

	/*************************************************************************************************************/
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
	public boolean wasNull() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getBoolean(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public byte getByte(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public short getShort(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public long getLong(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public float getFloat(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public double getDouble(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex, int scale)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public byte[] getBytes(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Date getDate(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Time getTime(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getBoolean(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public byte getByte(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public short getShort(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public long getLong(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public float getFloat(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public double getDouble(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel, int scale)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public byte[] getBytes(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Date getDate(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Time getTime(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public InputStream getUnicodeStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getCursorName() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getObject(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Reader getCharacterStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int getRow() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean relative(int rows) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int getFetchDirection() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int getType() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int getConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean rowUpdated() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean rowInserted() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean rowDeleted() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNull(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateByte(int columnIndex, byte x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateShort(int columnIndex, short x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateInt(int columnIndex, int x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateLong(int columnIndex, long x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateFloat(int columnIndex, float x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateDouble(int columnIndex, double x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBigDecimal(int columnIndex, BigDecimal x)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateString(int columnIndex, String x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateDate(int columnIndex, Date x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateTime(int columnIndex, Time x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateTimestamp(int columnIndex, Timestamp x)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, int length)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateObject(int columnIndex, Object x, int scaleOrLength)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateObject(int columnIndex, Object x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNull(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBoolean(String columnLabel, boolean x)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateByte(String columnLabel, byte x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateShort(String columnLabel, short x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateInt(String columnLabel, int x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateLong(String columnLabel, long x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateFloat(String columnLabel, float x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateDouble(String columnLabel, double x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBigDecimal(String columnLabel, BigDecimal x)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateString(String columnLabel, String x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBytes(String columnLabel, byte[] x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateDate(String columnLabel, Date x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateTime(String columnLabel, Time x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateTimestamp(String columnLabel, Timestamp x)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader,
			int length) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateObject(String columnLabel, Object x, int scaleOrLength)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateObject(String columnLabel, Object x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void insertRow() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateRow() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteRow() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void refreshRow() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void cancelRowUpdates() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void moveToInsertRow() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void moveToCurrentRow() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getObject(int columnIndex, Map<String, Class<?>> map)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Ref getRef(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Blob getBlob(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Clob getClob(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Array getArray(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getObject(String columnLabel, Map<String, Class<?>> map)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Ref getRef(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Blob getBlob(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Clob getClob(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Array getArray(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Timestamp getTimestamp(int columnIndex, Calendar cal)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Timestamp getTimestamp(String columnLabel, Calendar cal)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public URL getURL(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public URL getURL(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateRef(String columnLabel, Ref x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBlob(String columnLabel, Blob x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateClob(String columnLabel, Clob x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateArray(int columnIndex, Array x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateArray(String columnLabel, Array x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public RowId getRowId(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public RowId getRowId(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNString(int columnIndex, String nString)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNString(String columnLabel, String nString)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNClob(String columnLabel, NClob nClob)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public NClob getNClob(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public NClob getNClob(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateSQLXML(int columnIndex, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateSQLXML(String columnLabel, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getNString(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getNString(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateClob(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T getObject(String columnLabel, Class<T> type)
			throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}

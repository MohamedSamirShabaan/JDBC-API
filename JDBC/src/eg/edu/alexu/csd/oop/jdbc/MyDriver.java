package eg.edu.alexu.csd.oop.jdbc;

import java.sql.Connection;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

public class MyDriver implements java.sql.Driver{

	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		// TODO Auto-generated method stub
		String path = "" + info.get("path");
		Connection connection = new MyConnection(path);
		return connection;
	}

	@Override
	public boolean acceptsURL(String url) throws SQLException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
			throws SQLException {
		// TODO Auto-generated method stub
		DriverPropertyInfo [] information = new DriverPropertyInfo[info.size()];
		DriverPropertyInfo temp = null;
		Enumeration<?> e = info.propertyNames();
		int counter = 0;
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			temp = new DriverPropertyInfo(key , null);
			temp.value = info.getProperty(key);
			temp.required = true;
			information[counter++] = temp;
		}
		return information;
	}

	/*------------------------------------END----------------------------------------*/
	
	@Override
	public int getMajorVersion() {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public int getMinorVersion() {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean jdbcCompliant() {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

}

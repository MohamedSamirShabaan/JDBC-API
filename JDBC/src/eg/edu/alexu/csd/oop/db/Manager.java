package eg.edu.alexu.csd.oop.db;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import eg.edu.alexu.csd.oop.jdbc.MyDriver;

public class Manager {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Driver driver = new MyDriver();
		Properties info = new Properties();
		File dbDir = new File("/debug/db/test/sample");
		info.put("path", dbDir.getAbsoluteFile());
		Connection connection = null;
		try {
			connection = driver.connect("jdbc:xmldb://localhost", info);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			statement.execute("DROP DATABASE SaMpLe");
//		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		try {
			statement.execute("DROP DATABASE SaMpLe");
			statement.execute("CREATE DATABASE SaMpLe");
			statement.execute("CREATE TABLE Heeba (id int, name varchar)");
			statement.execute("INSERT INTO Heeba VALUES (1, Ahmed)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

package eg.edu.alexu.csd.oop.db.test;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import eg.edu.alexu.csd.oop.jdbc.MyDriver;

public class Testat {

	@Test
	public void testDatabase() throws SQLException {
		Driver driver = new MyDriver();
		Properties info = new Properties();
		File dbDir = new File("");
		info.put("path", dbDir.getAbsoluteFile());
		Connection connection = driver.connect("jdbc:xmldb://localhost", info);		
		{
			Statement statement = connection.createStatement();
			statement.execute("DROP DATABASE SaMpLe");
			statement.execute("CREATE DATABASE SaMpLe");
			statement.execute("DROP TABLE table_name1");
			statement.execute("CREATE TABLE table_name1(column_name1 varchar, column_name2 int, column_name3 varchar)");
			statement.execute("CREATE TABLE table_name1(column_name1 varchar, column_name2 int, column_name3 varchar)");
			statement.execute("CREATE   TABLE   table_name1(column_name1 varchar , column_name2    int,  column_name3 varchar)");
			statement.execute("Create TABLE table_name1(column_name1 varchar, column_name2 int, column_name3 varchar)");
			int i = statement.executeUpdate("INSERT INTO table_name1(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.assertEquals("raws count updated", 1, i);
			i = 0 ;
			i = statement.executeUpdate("INSERt INTO table_name1(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.assertEquals("raws count updated", 1, i);
			i = 0;
			i = statement.executeUpdate("INSERT INTO table_name1(column_name1, COLUMN_NAME3, column_NAME2) VAlUES ('value2', 'value4', 5)");
			Assert.assertEquals("raws count updated", 1, i);
			i = 0;
			i = statement.executeUpdate("UPDATE table_namE1 SET column_name1='11111111', COLUMN_NAME2=22222222, column_name3='333333333' WHERE coLUmn_NAME3='VALUE3'");
			Assert.assertEquals("raws count updated", 2, i);
			i = 0;
			statement.execute("CREATE TABLE table_name1(column_name1 varchar, column_name2 int, column_name3 varchar)");
			i = statement.executeUpdate("INSERT INTO table_name1(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 6)");
			Assert.assertEquals("raws count updated", 1, i);
			i = 0 ;
			i = statement.executeUpdate("INSERt INTO table_name1(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
			Assert.assertEquals("raws count updated", 1, i);
			i = 0;
			i = statement.executeUpdate("INSERT INTO table_name1(column_name1, COLUMN_NAME3, column_NAME2) VAlUES ('value2', 'value4adfshfjgkhlgj', 5)");
			Assert.assertEquals("raws count updated", 1, i);
			i = 0;
			i = statement.executeUpdate("INSERT INTO table_name1(column_name1, COLUMN_NAME3, column_NAME2) VAlUES ('value2', 'value4', 3)");
			Assert.assertEquals("raws count updated", 1, i);
			i = 0;
			ResultSet r =  statement.executeQuery("SELECT * FROM table_name1 WHERE coluMN_NAME2 = 5");
			r.first();
//			System.out.println(r.getMetaData().getColumnType(3));System.out.println(java.sql.Types.INTEGER);
			Assert.assertEquals("Error in selected data",(r.getObject(1)) , "'value2'");
			statement.close();
		}
		connection.close();
	}


}

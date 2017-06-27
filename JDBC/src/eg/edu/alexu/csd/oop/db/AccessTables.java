package eg.edu.alexu.csd.oop.db;

public interface AccessTables {

	/**
	 * this method creates tables -XML files- in the name given and the path
	 * given
	 * 
	 * @param path
	 *            (you should create the file into)
	 * @param name
	 *            (the name of the XML file)
	 * @param vaules
	 *            (2D array of String contain the names of columns and it's
	 *            type) example for 2D array
	 *            name 		type -------------- 
	 *            id 		int
	 *            names 	varchar 
	 *            degree 	int ----------------------
	 * @return true if it success and false if it failed
	 */
	public boolean creatTable(String path, String name, String[][] vaules)
			throws java.sql.SQLException;

	/**
	 * this method select from table there are 3 state method should do first,
	 * work with(SELECT * FROM Name_of_table) select all columns of the table
	 * for all rows
	 * 
	 * @param path
	 *            (you should create the file into)
	 * @param tableName
	 *            = Name_of_table
	 * @param columnsNames
	 *            = null
	 * @param columnSelectorName
	 *            = null
	 * @param opration
	 *            = null
	 * @param value
	 *            = null
	 *            -------------------------------------------------------------
	 *            second, work with(SELECT selected_column FROM Name_of_table)
	 *            select columnName only of the table for all rows
	 * @param path
	 *            (you should create the file into)
	 * @param tableName
	 *            = Name_of_table
	 * @param columnsNames
	 *            = String[] selected_columns contain the columns wanted
	 * @param columnSelectorName
	 *            = null
	 * @param opration
	 *            = null
	 * @param value
	 *            = null
	 *            --------------------------------------------------------
	 *            ----------- second, work with(SELECT selected_column(name)
	 *            FROM Name_of_table(ahmed) WHERE Name_of_selector(id) <>=
	 *            value(5)) select columnName only of the table for rows which
	 *            this condition (columnSelectorName <>= value)valid whit it
	 * @param path
	 *            (you should create the file into)
	 * @param tableName
	 *            = String Name_of_table
	 * @param columnsNames
	 *            = String[] selected_columns contain the columns wanted
	 * @param columnSelectorName
	 *            = String Name_of_selector
	 * @param opration
	 *            = < or > or = only
	 * @param value
	 *            = String represent integer number you should cast it to
	 *            Integer
	 *            --------------------------------------------------------
	 *            ---------------------
	 * @return 2D array of ((object)) each row of it represent a selected raw
	 *         and it's column represent a selected column -it's name
	 *         columnName-
	 *         ------------------------------------------------------
	 *         ------------------------- SELECT * FROM Name_of_table(ahmed)
	 *         WHERE Name_of_selector(id) <>= value(5)
	 * 
	 * 
	 *         if (type == 1) { selected = at.select(fullPath, tableName, null,
	 *         columnSelectorName, operation, value);
	 * 
	 *         } else if (type == 2) { selected = at.select(fullPath, tableName,
	 *         null, null, null, null); } else if (type == 3) { selected =
	 *         at.select(fullPath, tableName, columns, columnSelectorName,
	 *         operation, value); } else if (type == 4) { selected =
	 *         at.select(fullPath, tableName, columns, null, null, null); }
	 * 
	 * 
	 */

	public Object[][] select(String path, String tableName,
			String columnSelectorName, String opration, String value)
			throws java.sql.SQLException;

	public Object[][] select(String path, String tableName)
			throws java.sql.SQLException;

	public Object[][] select(String path, String tableName,
			String[] columnsNames, String columnSelectorName, String opration,
			String value) throws java.sql.SQLException;

	public Object[][] select(String path, String tableName,
			String[] columnsNames) throws java.sql.SQLException;

	/**
	 * this method deletes just entries form table -not the whole file- there
	 * are to state this method should work whit it first, (DELETE FROM
	 * name_of_table) delete all entries in the table but not properties (don't
	 * delete names of columns just delete the value saved in it)
	 * 
	 * @param path
	 *            (you should create the file into)
	 * @param tableName
	 *            = name_of_table
	 * @param columnSelectorName
	 *            = null
	 * @param opration
	 *            = null
	 * @param value
	 *            = null
	 *            --------------------------------------------------------
	 *            --------------- first, (DELETE FROM name_of_table WHERE
	 *            Name_of_selector <>= value) delete all rows in the table which
	 *            this condition (columnSelectorName <>= value)valid whit it
	 * @param path
	 *            (you should create the file into)
	 * @param tableName
	 *            = name_of_table
	 * @param columnSelectorName
	 *            = Name_of_selector
	 * @param opration
	 *            = < or > or = only
	 * @param value
	 *            = String represent integer number you should cast it to
	 *            Integer
	 *            --------------------------------------------------------
	 *            -----------------------
	 * @return the deleted rows count
	 */
	
	public int delete(String path, String tableName)throws java.sql.SQLException;
	
	public int delete(String path, String tableName, String columnSelectorName,
			String opration, String value) throws java.sql.SQLException;

	/**
	 * this method have only one state should work with it State : (UPDATE
	 * table_name SET column1=value1, column2=value2 WHERE some_column <=>
	 * some_value)
	 * 
	 * @param path
	 *            (you should create the file into)
	 * @param tableName
	 *            = table_name
	 * @param values
	 *            = like the 2D array mentioned in the description of
	 *            createTable method name value -------------- j=0 j=1 i=0
	 *            column1 value1 i=1 column2 value2 i=2 column3 value3
	 * @param columnSelectorName
	 *            = some_column
	 * @param opration
	 *            = < or > or = only
	 * @param value
	 *            = String represent integer number you should cast it to
	 *            Integer
	 *            --------------------------------------------------------
	 *            ---------------------------------------
	 * @return the updated rows count
	 */
	public int update(String path, String tableName, String[][] values) throws java.sql.SQLException;
	
	public int update(String path, String tableName, String[][] values,
			String columnSelectorName, String opration, String value)
			throws java.sql.SQLException;

	/**
	 * there are two states first ,(
	 * "INSERT INTO table_name (column1, column2, column3) VALUES (value1, value2, value3)"
	 * ) insert mean create new row contain the entries given in parameters
	 * 
	 * @param path
	 *            (you should create the file into)
	 * @param tableName
	 *            = table_name
	 * @param columns
	 *            = 1D array have the names of columns
	 *            {"column1","column2","column3"}
	 * @param values
	 *            = 1D array have the values of entries respectively
	 *            {value1,value2,value3}
	 *            ----------------------------------------
	 *            --------------------------------------------- second ,
	 *            ("INSERT INTO table_name VALUES (value1, value2, value3)") the
	 *            same thing but without the names of columns
	 * @param path
	 *            (you should create the file into)
	 * @param tableName
	 *            = table_name
	 * @param columns
	 *            = null
	 * @param values
	 *            = 1D array have the values of entries respectively
	 *            {value1,value2,value3}
	 *            ----------------------------------------
	 *            -------------------------------------------------
	 * @return the inserted rows count
	 */
	public int insert(String path, String tableName, String[] columns,
			String[] values) throws java.sql.SQLException;;

}
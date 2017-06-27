package eg.edu.alexu.csd.oop.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class MyAccessTables implements AccessTables {
	
	private void createValidatorFile(String path, String[][] elements)
			throws FileNotFoundException {
		String cols = "";
		for (int i = 0; i < elements.length; i++) {
			cols += elements[i][0]+ ",";
		}
		cols = cols.substring(0, cols.length() - 1);

		File dtdFile = new File(path + ".dtd");
		PrintWriter printWriter = new PrintWriter(dtdFile);
		printWriter.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		printWriter.println("<!ELEMENT table (columns,rows)>");
		printWriter.println("<!ELEMENT columns (" + cols + ")>");
		printWriter.println("<!ELEMENT row (" + cols + ")>");
		printWriter.println("<!ELEMENT rows (row*)>");
		for (int i = 0; i < elements.length; i++) {
			printWriter.println("<!ELEMENT " + elements[i][0] + " (#PCDATA)>");
		}
		printWriter.close();
	}

	@Override
	public boolean creatTable(String path, String name, String[][] vaules)
			throws SQLException {
		try {
			// DTD

			// ----------
			Document doc = getDocument(path, name).newDocument();
			Element table = doc.createElement("table");
			Element columns = doc.createElement("columns");
			Element rows = doc.createElement("rows");
			for (String[] strings : vaules) {
				Element col = doc.createElement(strings[0]);
				col.setTextContent(strings[1]);
				columns.appendChild(col);
			}
			table.appendChild(columns);
			table.appendChild(rows);
			doc.appendChild(table);

			// Saving XML File
			createValidatorFile((path+File.separator+name), vaules);
			saveXml(path, name, doc);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Object[][] select(String path, String tableName,
			String[] columnsNames, String columnSelectorName, String opration,
			String value) throws SQLException {
		try {
			Object[][] allTable = getAllRows(path, tableName);
			String[] allColNames;
			boolean[] isWantedColumn;
			Object[][] wantedColumns;
			allColNames = getColumnsNames(path, tableName);

			System.out.println("all Col Names : " + allColNames[0]);
			// Specific Columns
			// ------------------------------------------------
			isWantedColumn = getWantedColumns(allColNames, columnsNames);
			wantedColumns = new Object[allTable.length][columnsNames.length];
			if (!(columnsNames == null)) {
				int k = 0;
				for (int j = 0; j < columnsNames.length; j++) {
					for (int t = 0; t < isWantedColumn.length; t++)
						System.out.println(isWantedColumn[t]);
					while (!isWantedColumn[k])
						k++;
					for (int i = 0; i < allTable.length; i++) {
						wantedColumns[i][j] = allTable[i][k];
					}
				}
			}

			// Specific Rows
			// --------------------------------------------------
			String[] comparable = { columnSelectorName };
			boolean[] comparableColumn = getWantedColumns(allColNames,
					comparable);
			List<Object[]> wantedRows = new LinkedList<>();
			// wantedRows.add(allColNames);
			for (int i = 0; i < allTable.length; i++) {
				if (isWantedRow(comparableColumn, opration, allTable[i], value)) {
					wantedRows.add(wantedColumns[i]);
				}
			}
			printTable(get2DObjectArrayFromList(wantedRows));
			return get2DObjectArrayFromList(wantedRows);
		} catch (Exception e) {
		}
		return new Object[0][0];

	}

	@Override
	public int delete(String path, String tableName, String columnSelectorName,
			String opration, String value) throws SQLException {
		return update(true, null, path, tableName, columnSelectorName,
				opration, value);
	}

	@Override
	public int update(String path, String tableName, String[][] values,
			String columnSelectorName, String opration, String value)
			throws SQLException {
		File table = new File(path + File.separator + tableName + ".xml");
		if (!table.exists()) {
			throw new SQLException("file table not found");
		}
		return update(false, values, path, tableName, columnSelectorName,
				opration, value);
	}

	@Override
	public int insert(String path, String tableName, String[] columns,
			String[] values) throws SQLException {
		try {
			System.out
					.println("Path : " + path + " , TableName : " + tableName);
			Document doc = getDocument(path, tableName).parse(
					fullPath(path, tableName));

			Element row = doc.createElement("row");
			if (columns == null)
				columns = getColumnsNames(path, tableName);
			else {
				arrange(columns, values);
			}
			for (int i = 0; i < values.length; i++) {
				// int
				if (values[i] != null) {
					System.out.println(columns[i] + " : " + values[i]);
					Element e = doc.createElement(columns[i]);
					e.setTextContent(values[i]);
					row.appendChild(e);
				}
			}

			Element rows = (Element) doc.getElementsByTagName("rows").item(0);
			rows.appendChild(row);
			saveXml(path, tableName, doc);

			// i is number of columns
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// Helping Methods ----------------------------------------

	private DocumentBuilder getDocument(String path, String tableName)
			throws Exception {
		String xmlPath = path + File.separator + tableName + ".xml";
		File xmlFile = new File(xmlPath);
		xmlFile.getParentFile().mkdirs();
		if (!xmlFile.exists())
			xmlFile.createNewFile();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		validateTryToCatch(factory);
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder;
	}

	private void saveXml(String path, String tableName, Document doc)
			throws Exception {
		File xmlFile = new File(fullPath(path, tableName));
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		// DTD
		DocumentType dt = doc.getImplementation().createDocumentType(
				"validator", null, (tableName+".dtd"));
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
				dt.getSystemId());

		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(xmlFile);
		transformer.transform(source, result);
	}

	private String[] getColumnsNames(String path, String tableName)
			throws Exception {
		Document doc = getDocument(path, tableName).parse(
				fullPath(path, tableName));
		Element columns = (Element) doc.getElementsByTagName("columns").item(0);
		NodeList l = columns.getChildNodes();
		String[] a = cleanList(l, false);

		return a;

	}

	private Object[][] getAllRows(String path, String tableName)
			throws Exception {
		Document doc = getDocument(path, tableName).parse(
				fullPath(path, tableName));
		NodeList rows = doc.getElementsByTagName("row");
		int columnsCount = getColumnsCount(path, tableName);
		Object[][] elements = new Object[rows.getLength()][columnsCount];
		String[] types = new String[columnsCount];
		String[] columns = getColumnsNames(path, tableName);
		for (int i = 0; i < columnsCount; i++) {
			types[i] = getColumnType(path, tableName, columns[i]);
		}
		// elements[0] = getColumnsNames(path, tableName);
		for (int i = 0; i < rows.getLength(); i++) {
			Element row = (Element) rows.item(i);
			NodeList valuesC = row.getChildNodes();
			String[] valuesS = cleanList(valuesC, true);
			String[] columnsNames = cleanList(valuesC, false);
			boolean[] addIn = getWantedColumns(columns, columnsNames);
			Object[] rowElements = new Object[columnsCount];
			int k = 0;
			for (int j = 0; j < columnsCount; j++) {
				String val = null;
				if (addIn[j])
					val = valuesS[k++];
				else
					continue;
				if (types[j].equals("int")) {
					rowElements[j] = Integer.parseInt(val);
				} else {
					rowElements[j] = val;
				}
			}
			elements[i] = rowElements;
		}

		return elements;
	}

	private String fullPath(String path, String tableName) {
		return path + File.separator + tableName + ".xml";
	}

	private int getColumnsCount(String path, String tableName) throws Exception {
		Document doc = getDocument(path, tableName).parse(
				fullPath(path, tableName));
		Element columns = (Element) doc.getElementsByTagName("columns").item(0);
		NodeList l = columns.getChildNodes();
		int count = 0;
		for (int i = 0; i < l.getLength(); i++) {
			String item = l.item(i).getNodeName();
			if (item.contains("#"))
				continue;
			count++;
		}
		return count;
	}

	private String getColumnType(String path, String tableName,
			String columnName) throws Exception {
		Document doc = getDocument(path, tableName).parse(
				fullPath(path, tableName));
		Element columns = (Element) doc.getElementsByTagName("columns").item(0);
		NodeList l = columns.getChildNodes();
		String type = "";
		for (int i = 0; i < l.getLength(); i++) {
			String item = l.item(i).getNodeName();
			org.w3c.dom.Node n = l.item(i);
			type = n.getTextContent();
			if (item.equals(columnName))
				return type;
		}
		return null;
	}

	private String[] cleanList(NodeList list, boolean returnTextContent) {
		List<String> columnsString = new LinkedList<String>();
		for (int i = 0; i < list.getLength(); i++) {
			String item = list.item(i).getNodeName();
			if (item.contains("#"))
				continue;
			if (returnTextContent)
				columnsString.add(list.item(i).getTextContent());
			else
				columnsString.add(item);
		}
		String[] a = new String[columnsString.size()];
		for (int i = 0; i < a.length; i++) {
			a[i] = columnsString.get(i);
		}

		return a;
	}

	private boolean[] getWantedColumns(String[] allColNames,
			String[] columnsNames) {
		boolean[] wantedColumns = new boolean[allColNames.length];
		for (int i = 0; i < allColNames.length; i++) {
			for (int j = 0; j < columnsNames.length; j++) {
				if (allColNames[i].equals(columnsNames[j])) {
					wantedColumns[i] = true;
				}
			}
		}
		return wantedColumns;
	}

	private boolean isWantedRow(boolean[] isComparableColumn, String operation,
			Object[] comparable, String valueToComoareWith) {
		String o = "";
		if (valueToComoareWith == null)
			return true;
		for (int i = 0; i < comparable.length; i++) {
			if (isComparableColumn[i] == true) {
				try {
					Integer vtcw = Integer.parseInt(valueToComoareWith);
					Integer cmprb = Integer.parseInt(comparable[i].toString());
					int v = vtcw.compareTo((Integer) cmprb);
					if (v == 0)
						o = "=";
					else if (v == 1)
						o = "<";
					else
						o = ">";
				} catch (Exception r) {
					// r.printStackTrace();
					int v = valueToComoareWith.compareTo(comparable[i]
							.toString());
					System.out.println(v);
					if (v == 0)
						o = "=";
					else if (v > 0)
						o = "<";
					else
						o = ">";
				}
				break;
			}
		}
		if (o.equals(operation))
			return true;
		return false;
	}

	private Object[][] get2DObjectArrayFromList(List<Object[]> list) {
		Object[][] array = new Object[list.size()][list.get(0).length];
		for (int i = 0; i < array.length; i++) {
			array[i] = list.get(i);
		}
		return array;
	}

	private void printTable(Object[][] table) {
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[0].length; j++) {
				System.out.print(table[i][j] + "  |  ");
			}
			System.out.println();
		}
	}

	private int update(boolean delete, String[][] valuesToUpdate, String path,
			String tableName, String columnSelectorName, String opration,
			String value) throws SQLException {
		Object[][] allTable = select(path, tableName);
		try {
			String[] allColNames = getColumnsNames(path, tableName);
			System.out.println("col Names : " + allColNames[0] + " | "
					+ allColNames[1] + " | " + allColNames[2]);
			String[] comparable = { columnSelectorName };
			boolean[] comparableColumn = getWantedColumns(allColNames,
					comparable);
			int count = 0;
			List<Object[]> wantedRows = new LinkedList<>();
			if (delete) {
				// DELETE
				for (int i = 0; i < allTable.length; i++) {
					count++;
					if (value == null) {

					} else if (!isWantedRow(comparableColumn, opration,
							allTable[i], value)) {
						wantedRows.add(allTable[i]);
						count--;
					}
				}
			} else {
				// UPDATE
				for (int i = 0; i < allTable.length; i++) {
					if (value == null) {

					}
					if (isWantedRow(comparableColumn, opration, allTable[i],
							value)) {
						// Change values
						count++;
						for (int j = 0; j < allColNames.length; j++) {
							for (int k = 0; k < valuesToUpdate.length; k++) {
								if (valuesToUpdate[k][0].equals(allColNames[j])) {
									allTable[i][j] = valuesToUpdate[k][1];
								}
							}
						}
					}
					wantedRows.add(allTable[i]);
				}

			}
			String[] columns = getColumnsNames(path, tableName);
			String[][] values = new String[columns.length][2];
			for (int i = 0; i < columns.length; i++) {
				String type = getColumnType(path, tableName, columns[i]);
				values[i][0] = columns[i];
				values[i][1] = type;
			}

			creatTable(path, tableName, values);
			// Insertion
			for (Object[] objects : wantedRows) {
				String[] vs = new String[objects.length];
				for (int i = 0; i < objects.length; i++) {
					if (objects[i] == null)
						vs[i] = null;
					else
						vs[i] = objects[i].toString();
				}
				insert(path, tableName, null, vs);
			}
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("in small update");
		}
	}

	private int updateAll(boolean delete, String[][] valuesToUpdate,
			String path, String tableName) throws SQLException {
		Object[][] allTable = select(path, tableName);
		try {
			String[] allColNames = getColumnsNames(path, tableName);
			System.out.println("col Names : " + allColNames[0] + " | "
					+ allColNames[1] + " | " + allColNames[2]);
			int count = 0;
			List<Object[]> wantedRows = new LinkedList<>();
			if (delete) {
				// DELETE
				for (int i = 0; i < allTable.length; i++) {
					count++;
				}
			} else {
				// UPDATE
				for (int i = 0; i < allTable.length; i++) {
					// Change values
					count++;
					for (int j = 0; j < allColNames.length; j++) {
						for (int k = 0; k < valuesToUpdate.length; k++) {
							if (valuesToUpdate[k][0].equals(allColNames[j])) {
								allTable[i][j] = valuesToUpdate[k][1];
							}
						}
					}
					wantedRows.add(allTable[i]);
				}

			}
			String[] columns = getColumnsNames(path, tableName);
			String[][] values = new String[columns.length][2];
			for (int i = 0; i < columns.length; i++) {
				String type = getColumnType(path, tableName, columns[i]);
				values[i][0] = columns[i];
				values[i][1] = type;
			}

			creatTable(path, tableName, values);
			// Insertion
			for (Object[] objects : wantedRows) {
				String[] vs = new String[objects.length];
				for (int i = 0; i < objects.length; i++) {
					if (objects[i] == null)
						vs[i] = null;
					else
						vs[i] = objects[i].toString();
				}
				insert(path, tableName, null, vs);
			}
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("in small update");
		}
	}

	private void arrange(String[] arrangeAccordingTo, String[] arrangAsWell) {
		for (int i = 0; i < arrangeAccordingTo.length; i++) {
			for (int j = i; j < arrangeAccordingTo.length; j++) {
				if (arrangeAccordingTo[i].compareTo(arrangeAccordingTo[j]) == 1) {
					String tmp = arrangeAccordingTo[i];
					arrangeAccordingTo[i] = arrangeAccordingTo[j];
					arrangeAccordingTo[j] = tmp;
					String asWell = arrangAsWell[i];
					arrangAsWell[i] = arrangAsWell[j];
					arrangAsWell[j] = asWell;
				}
			}
		}
	}

	public String[] getSelectedColumns(String path, String tableName,
			String[] columnsNames) throws Exception {
		String[] allCols = getColumnsNames(path, tableName);
		String[] cols = new String[columnsNames.length];
		int k = 0;
		for (int i = 0; i < allCols.length; i++) {
			for (int j = 0; j < columnsNames.length; j++) {
				if (allCols[i].equals(columnsNames[j])) {
					cols[k++] = allCols[i];
				}
			}
		}
		return cols;
	}

	private void validateTryToCatch(
			DocumentBuilderFactory documentBuilderFactory)
			throws ParserConfigurationException, SAXException, IOException {
		documentBuilderFactory.setValidating(true);
		DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();
		documentBuilder.setErrorHandler(new ErrorHandler() {
			@Override
			public void warning(SAXParseException exception)
					throws SAXException {
				throw exception;
			}

			@Override
			public void fatalError(SAXParseException exception)
					throws SAXException {
				throw exception;
			}

			@Override
			public void error(SAXParseException exception) throws SAXException {
				throw exception;
			}
		});
	}

	@Override
	public Object[][] select(String path, String tableName,
			String columnSelectorName, String opration, String value)
			throws SQLException {
		try {
			Object[][] allTable = getAllRows(path, tableName);
			String[] allColNames;
			allColNames = getColumnsNames(path, tableName);

			// Specific Rows
			// --------------------------------------------------
			String[] comparable = { columnSelectorName };
			boolean[] comparableColumn = getWantedColumns(allColNames,
					comparable);
			List<Object[]> wantedRows = new LinkedList<>();
			// wantedRows.add(allColNames);

			for (int i = 0; i < allTable.length; i++) {
				if (isWantedRow(comparableColumn, opration, allTable[i], value)) {
					wantedRows.add(allTable[i]);
				}
			}
			printTable(get2DObjectArrayFromList(wantedRows));
			return get2DObjectArrayFromList(wantedRows);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("Select type 1");
		}
	}

	@Override
	public Object[][] select(String path, String tableName) throws SQLException {
		try {
			Object[][] allTable = getAllRows(path, tableName);
			return allTable;
		} catch (Exception e) {
			throw new SQLException("Select type 2");
		}
	}

	@Override
	public Object[][] select(String path, String tableName,
			String[] columnsNames) throws SQLException {
		try {
			Object[][] allTable = getAllRows(path, tableName);
			String[] allColNames;
			boolean[] isWantedColumn;
			Object[][] wantedColumns;
			allColNames = getColumnsNames(path, tableName);

			// Specific Columns
			// ------------------------------------------------
			isWantedColumn = getWantedColumns(allColNames, columnsNames);
			wantedColumns = new Object[allTable.length][columnsNames.length];
			if (!(columnsNames == null)) {
				int k = 0;
				for (int j = 0; j < columnsNames.length; j++) {
					for (int t = 0; t < isWantedColumn.length; t++)
						System.out.println(isWantedColumn[t]);
					while (!isWantedColumn[k])
						k++;
					for (int i = 0; i < allTable.length; i++) {
						wantedColumns[i][j] = allTable[i][k];
					}
				}
			}
			return wantedColumns;
		} catch (Exception e) {
			throw new SQLException();
		}
	}

	@Override
	public int delete(String path, String tableName) throws SQLException {
		return updateAll(true, null, path, tableName);
	}

	@Override
	public int update(String path, String tableName, String[][] values)
			throws SQLException {
		return updateAll(false, values, path, tableName);
	}
}
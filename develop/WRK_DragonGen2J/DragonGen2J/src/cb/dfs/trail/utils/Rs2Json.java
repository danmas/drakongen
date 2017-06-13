package cb.dfs.trail.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Rs2Json {

	public static String select2json(Connection conn, String select_sql)
			throws SQLException {
		Statement stmt = null;
		String ret = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(select_sql);

			ret = rs2json(rs);

			rs.close();
			stmt.close();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
		}
		return ret;
	}

	public static String rs2json(ResultSet rs) throws SQLException {
//		if (rs.first() == false) {
//			return "[]";
//		} else {
//			rs.beforeFirst();
//		} // empty rs
		StringBuilder sb = new StringBuilder();
		Object item;
		String value;
		java.sql.ResultSetMetaData rsmd = rs.getMetaData();
		int numColumns = rsmd.getColumnCount();

		sb.append("[{");
		while (rs.next()) {

			for (int i = 1; i < numColumns + 1; i++) {
				String column_name = rsmd.getColumnName(i);
				item = rs.getObject(i);
				if (item != null) {
					value = item.toString();
					value = value.replace('"', '\'');
				} else {
					value = "null";
				}
				sb.append("\"" + column_name + "\":\"" + value + "\",");

			} // end For = end record

			sb.setCharAt(sb.length() - 1, '}'); // replace last comma with curly
												// bracket
			sb.append("\n,{");
		} // end While = end resultset

		sb.delete(sb.length() - 3, sb.length()); // delete last two chars
		sb.append("}]\n");

		return sb.toString();
	}
}

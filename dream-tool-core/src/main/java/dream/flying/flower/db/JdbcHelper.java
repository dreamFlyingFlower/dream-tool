package dream.flying.flower.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * JDBC工具类
 *
 * @author 飞花梦影
 * @date 2024-07-16 17:49:03
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class JdbcHelper {

	public static Connection connect(String url, String user, String pwd, String driverClass) {
		Connection conn = null;
		try {
			Class.forName(driverClass);
			conn = java.sql.DriverManager.getConnection(url, user, pwd);
			return conn;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void release(Connection conn) {
		if (null != conn) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("SQLException");
			}
		}
	}

	public static void release(Connection conn, Statement stmt, ResultSet rs) throws SQLException {
		if (rs != null) {
			rs.close();
			rs = null;
		}
		if (stmt != null) {
			stmt.close();
			stmt = null;
		}
		if (conn != null) {
			conn.close();
			conn = null;
		}
	}

	public static void release(Connection conn, Statement stmt, PreparedStatement pstmt, ResultSet rs)
			throws SQLException {
		if (rs != null) {
			rs.close();
			rs = null;
		}
		if (stmt != null) {
			stmt.close();
			stmt = null;
		}
		if (pstmt != null) {
			pstmt.close();
			pstmt = null;
		}
		if (conn != null) {
			conn.close();
			conn = null;
		}
	}

	public static TableMetaData getMetaData(ResultSet rs) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		TableMetaData meta = new TableMetaData(metaData.getTableName(1));
		int count = metaData.getColumnCount();
		for (int i = 1; i <= count; i++) {
			TableColumn column = new TableColumn(metaData.getColumnName(i).toLowerCase(), metaData.getColumnTypeName(i),
					metaData.getPrecision(i), metaData.getScale(i));
			meta.getColumns().add(column);
			meta.getColumnDetail().put(column.getColumn(), column);
		}
		return meta;
	}
}
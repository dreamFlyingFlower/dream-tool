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

	public static void release(Connection conn, Statement stmt, ResultSet rs) {
		if (rs != null)
			try {
				rs.close();
				rs = null;
			} catch (SQLException e) {
				System.out.println("SQLException");
			}
		if (stmt != null)
			try {
				stmt.close();
				stmt = null;
			} catch (SQLException e) {
				System.out.println("SQLException");
			}
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				System.out.println("SQLException");
			}
		}
	}

	public static void release(Connection conn, Statement stmt, PreparedStatement pstmt, ResultSet rs) {
		if (rs != null)
			try {
				rs.close();
				rs = null;
			} catch (SQLException e) {
				System.out.println("ResultSet Close Exception");
			}
		if (stmt != null)
			try {
				stmt.close();
				stmt = null;
			} catch (SQLException e) {
				System.out.println("Statement Close Exception");
			}
		if (pstmt != null)
			try {
				pstmt.close();
				pstmt = null;
			} catch (SQLException e) {
				System.out.println("PreparedStatement Close Exception");
			}
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				System.out.println("Connection Close Exception");
			}
		}
	}

	public static TableMetaData getMetaData(ResultSet rs) {
		try {
			ResultSetMetaData metaData = rs.getMetaData();
			TableMetaData meta = new TableMetaData(metaData.getTableName(1));
			int count = metaData.getColumnCount();
			for (int i = 1; i <= count; i++) {
				TableColumn column = new TableColumn(metaData.getColumnName(i).toLowerCase(),
						metaData.getColumnTypeName(i), metaData.getPrecision(i), metaData.getScale(i));
				meta.getColumns().add(column);
				meta.getColumnDetail().put(column.getColumn(), column);
			}
			return meta;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
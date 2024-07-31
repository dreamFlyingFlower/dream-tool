package dream.flying.flower.db.enums;

public enum DriverType {

	DM("dm", "dm.jdbc.driver.DmDriver"),
	MYSQL("mysql", "com.mysql.jdbc.Driver"),
	ORACLE("oracle", "oracle.jdbc.driver.OracleDriver"),
	SQLSERVER("sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
	SQLITE("sqlite", "org.sqlite.JDBC"),
	POSTGRESQL("postgresql", "org.postgresql.Driver"),
	DB2("db2", "com.ibm.db2.jdbc.app.DB2Driver");

	private String name;

	private String driverClass;

	DriverType(String name, String driverClass) {
		this.name = name;
		this.driverClass = driverClass;
	}

	public String getDriverClass() {
		return this.driverClass;
	}

	/**
	 * 根据数据库url拿到数据库匹配的驱动
	 * 
	 * @param url 数据库地址
	 * @return 数据库驱动
	 */
	public static String getDriverClass(String url) {
		DriverType[] values = DriverType.values();
		for (DriverType value : values) {
			if (url.indexOf(value.name) != -1) {
				return value.driverClass;
			}
		}
		return null;
	}
}
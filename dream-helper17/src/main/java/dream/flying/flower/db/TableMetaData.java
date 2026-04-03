package dream.flying.flower.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表元数据
 *
 * @author 飞花梦影
 * @date 2024-07-16 17:50:33
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class TableMetaData {

	/**
	 * 表名
	 */
	private String tableName;

	/**
	 * 表字段列表
	 */
	private List<TableColumn> columns;

	/**
	 * 表字段名与字段详情映射
	 */
	private Map<String, TableColumn> columnDetail;

	public TableMetaData(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<TableColumn> getColumns() {
		if (null == this.columns) {
			columns = new ArrayList<>();
		}
		return columns;
	}

	public void setColumns(List<TableColumn> columns) {
		this.columns = columns;
	}

	public Map<String, TableColumn> getColumnDetail() {
		if (null == this.columnDetail) {
			this.columnDetail = new HashMap<>();
		}
		return columnDetail;
	}

	public void setColumnDetail(Map<String, TableColumn> columnDetail) {
		this.columnDetail = columnDetail;
	}
}
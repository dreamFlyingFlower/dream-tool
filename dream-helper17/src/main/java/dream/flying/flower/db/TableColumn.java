package dream.flying.flower.db;

/**
 * 表字段
 *
 * @author 飞花梦影
 * @date 2024-07-16 17:50:45
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class TableColumn {

	/**
	 * 字段名
	 */
	private String column;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 长度
	 */
	private Integer precision;

	/**
	 * 精度
	 */
	private Integer scale;

	public TableColumn(String column, String type, int precision, int scale) {
		super();
		this.column = column;
		this.type = type;
		this.precision = precision;
		this.scale = scale;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public Integer getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}
}
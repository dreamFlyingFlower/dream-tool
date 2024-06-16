package dream.flying.flower.db.enums;

/**
 * 查询类型
 *
 * @author 飞花梦影
 * @date 2024-03-29 20:07:12
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public enum QueryType {
	/** = */
	EQ,
	/** != */
	NE,
	/** > */
	GT,
	/** >= */
	GE,
	/** < */
	LT,
	/** <= */
	LE,
	/** 区间 */
	BETWEEN,
	/** 区间之外 */
	NOTBETWEEN,
	/** 模糊匹配 */
	LIKE,
	/** 模糊不匹配 */
	NOTLIKE,
	/** %data */
	LIKELEFT,
	/** data% */
	LIKERIGHT,
	/** in */
	IN,
	/** not in */
	NOTIN,
	/** group by */
	GROUPBY,
}
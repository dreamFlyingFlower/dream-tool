package dream.flying.flower;

/**
 * 分页相关参数
 *
 * @author 飞花梦影
 * @date 2022-05-15 14:57:44
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public interface ConstPager {

	/** 分页参数,默认当前页数字段 */
	String PAGE_INDEX = "pageIndex";

	/** 分页参数,默认每页数据量字段 */
	String PAGE_SIZE = "pageSize";

	/** 分页参数pageSize,默认每页数据量 */
	int PAGE_SIZE_NUM = 10;

	/** 若查询全部数据,每次最大查询1000条 */
	int PAGE_MAX = 1000;

	/** 分页参数,默认总页数字段 */
	String TOTAL_PAGE = "totalPage";

	/** 分页参数,默认总数字段 */
	String TOTAL = "total";
}
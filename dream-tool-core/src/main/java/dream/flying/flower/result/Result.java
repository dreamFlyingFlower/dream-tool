package dream.flying.flower.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dream.flying.flower.ConstI18n;
import dream.flying.flower.collection.MapHelper;
import dream.flying.flower.common.CodeMsg;
import dream.flying.flower.common.Internation;
import dream.flying.flower.enums.TipEnum;
import dream.flying.flower.lang.StrHelper;

/**
 * result工具类,code为1时成功,其他全部失败
 * 
 * @author ParadiseWY
 * @date 2020-02-20 14:47:42
 * @git {@link https://github.com/mygodness100}
 */
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 成功或失败的code,或其他类型code
	 */
	private int code;

	/**
	 * 成功或失败的消息
	 */
	private String msg;

	/**
	 * 数据
	 */
	private T data;

	/**
	 * 是否分页
	 */
	private Boolean page;

	/**
	 * 分页时当前页数
	 */
	private Long pageIndex;

	/**
	 * 分页时每页条数
	 */
	private Long pageSize;

	/**
	 * 分页时数据的总条数
	 */
	private Long total;

	/**
	 * 分页时的总页数
	 */
	private Long totalPage;

	public Result() {
		super();
	}

	public Result(int code, String msg, T data, Boolean page, Long pageIndex, Long pageSize, Long total,
			Long totalPage) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
		this.page = page;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.total = total;
		this.totalPage = totalPage;
	}

	public static <T> Result<T> ok() {
		return ok(null);
	}

	public static <T> Result<T> ok(T t) {
		return ok(Internation.getStr(ConstI18n.MSG_SUCCESS), t);
	}

	public static <T> Result<T> ok(String msg, T t) {
		return result(1, msg, t);
	}

	public static <T> Result<T> ok(int code, T t) {
		return result(code, Internation.getStr(ConstI18n.MSG_SUCCESS), t);
	}

	public static <T> Result<T> error() {
		return error(Internation.getStr(ConstI18n.MSG_FAIL));
	}

	public static <T> Result<T> error(String msg) {
		return error(0, msg);
	}

	public static <T> Result<T> error(CodeMsg tipCode) {
		return error(tipCode.getValue(), tipCode.getMsg());
	}

	public static <T> Result<T> error(int code, String msg) {
		return error(code, msg, null);
	}

	public static <T> Result<T> error(int code, T t) {
		return error(code, null, t);
	}

	public static <T> Result<T> error(int code, String msg, T t) {
		return result(code, msg, t);
	}

	public static <T> Result<T> result(boolean flag) {
		return flag ? ok() : error();
	}

	public static <T> Result<T> result(T t) {
		return Objects.isNull(t) ? error() : ok(t);
	}

	public static <T> Result<T> result(TipEnum tip) {
		return result(tip, null);
	}

	public static <T> Result<T> result(TipEnum tip, T t) {
		return result(tip.getValue(), tip.getMsg(), t);
	}

	public static <T> Result<T> result(int code, String msg, T t) {
		return Result.<T>builder()
				.code(code)
				.msg(StrHelper.isBlank(msg)
						? (code == 1 ? Internation.getStr(ConstI18n.MSG_SUCCESS)
								: Internation.getStr(ConstI18n.MSG_FAIL))
						: msg)
				.data(t)
				.page(false)
				.build();
	}

	public static <T> Result<T> page(T t, long pageIndex, long pageSize, long total) {
		return page(1, null, t, pageIndex, pageSize, total);
	}

	public static <T> Result<T> page(T t) {
		return page(1, null, t, 0, 0, 0);
	}

	public static <T> Result<T> page(int code, String msg, T t, long pageIndex, long pageSize, long total) {
		return Result.<T>builder()
				.code(code)
				.msg(StrHelper.isBlank(msg)
						? (code == 1 ? Internation.getStr(ConstI18n.MSG_SUCCESS)
								: Internation.getStr(ConstI18n.MSG_FAIL))
						: msg)
				.data(t)
				.page(true)
				.pageIndex(pageIndex)
				.pageSize(pageSize)
				.total(total)
				.totalPage(pageSize == 0 ? 0 : (long) (Math.ceil((double) total / pageSize)))
				.build();
	}

	/**
	 * 将从数据库自定义sql语句取出的结果集的key转换为驼峰形式
	 * 
	 * @param data 下划线形式的结果集
	 * @return 驼峰形式的结果集
	 */
	public static Map<String, Object> snake2Hump(Map<String, Object> data) {
		if (MapHelper.isEmpty(data)) {
			return null;
		}
		Map<String, Object> result = new HashMap<>();
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			result.put(StrHelper.underline2Hump(entry.getKey()), entry.getValue());
		}
		data = null;
		return result;
	}

	/**
	 * 将从数据库自定义sql语句取出的结果集的key转换为驼峰形式
	 * 
	 * @param datas 下划线形式的结果集
	 * @return 驼峰形式的结果集
	 */
	public static List<Map<String, Object>> snake2Hump(List<Map<String, Object>> datas) {
		if (datas == null || datas.isEmpty()) {
			return null;
		}
		List<Map<String, Object>> result = new ArrayList<>();
		for (Map<String, Object> data : datas) {
			Map<String, Object> res = snake2Hump(data);
			result.add(res);
		}
		datas = null;
		return result;
	}

	public static <T> ResultBuilder<T> builder() {
		return new ResultBuilder<T>();
	}

	public static <T> ResultBuilder<T> builder(T data) {
		ResultBuilder<T> resultBuilder = new ResultBuilder<T>();
		return resultBuilder.data(data);
	}

	public static class ResultBuilder<T> {

		private Result<T> result = new Result<T>();

		public ResultBuilder<T> code(int code) {
			result.setCode(code);
			return this;
		}

		public ResultBuilder<T> msg(String msg) {
			result.setMsg(msg);
			return this;
		}

		public ResultBuilder<T> data(T data) {
			result.setData(data);
			return this;
		}

		public ResultBuilder<T> page(boolean page) {
			result.setPage(page);
			return this;
		}

		public ResultBuilder<T> pageIndex(long pageIndex) {
			result.setPageIndex(pageIndex);
			return this;
		}

		public ResultBuilder<T> pageSize(long pageSize) {
			result.setPageSize(pageSize);
			return this;
		}

		public ResultBuilder<T> total(long tatal) {
			result.setTotal(tatal);
			return this;
		}

		public ResultBuilder<T> totalPage(long totalPage) {
			result.setTotalPage(totalPage);
			return this;
		}

		public Result<T> build() {
			return result;
		}
	}

	/*------------------------ getter,setter ------------------------*/
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Boolean isPage() {
		return page;
	}

	public void setPage(boolean page) {
		this.page = page;
	}

	public Long getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(long pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public Long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}
}
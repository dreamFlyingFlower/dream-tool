package dream.flying.flower.helper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

/**
 * 工作日工具类（周末+自定义节假日+调休补班）
 *
 * @author 飞花梦影
 * @date 2026-10-15 17:17:36
 */
public interface WorkDayHelper {

	public static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Shanghai");

	/**
	 * 判断是否周末
	 */
	public static boolean isWeekend(LocalDate date) {
		if (date == null) {
			throw new IllegalArgumentException("date不能为null");
		}
		int dayOfWeek = date.getDayOfWeek().getValue();
		// 周一=1，周日=7；周六6、周日7是周末
		return dayOfWeek == 6 || dayOfWeek == 7;
	}

	/**
	 * 判断是否工作日
	 * 
	 * @param date 目标日期
	 * @param holidaySet 自定义节假日（周末之外额外放假的日期）
	 * @param workDaySet 调休补班日（原本周末，但是要上班）
	 * @return true=工作日
	 */
	public static boolean isWorkDay(LocalDate date, Set<LocalDate> holidaySet, Set<LocalDate> workDaySet) {
		if (date == null) {
			throw new IllegalArgumentException("date不能为null");
		}
		Set<LocalDate> holidays = holidaySet == null ? new HashSet<>() : holidaySet;
		Set<LocalDate> extraWorkDays = workDaySet == null ? new HashSet<>() : workDaySet;

		// 如果在补班集合里，强制是工作日
		if (extraWorkDays.contains(date)) {
			return true;
		}
		// 如果是节假日，强制休息
		if (holidays.contains(date)) {
			return false;
		}
		// 默认规则：非周末=工作日
		return !isWeekend(date);
	}

	/**
	 * 向后推 n 个工作日（往后数，n>0；往前推传负数）
	 * 
	 * @param startDate 起始日期
	 * @param n 工作日数量，>0往后，<0往前
	 * @param holidaySet 节假日
	 * @param workDaySet 补班日
	 * @return 目标工作日
	 */
	public static LocalDate plusWorkDays(LocalDate startDate, int n, Set<LocalDate> holidaySet,
			Set<LocalDate> workDaySet) {
		if (startDate == null) {
			throw new IllegalArgumentException("startDate不能为null");
		}
		LocalDate current = startDate;
		int count = 0;
		int step = n > 0 ? 1 : -1;
		int target = Math.abs(n);

		while (count < target) {
			current = current.plusDays(step);
			if (isWorkDay(current, holidaySet, workDaySet)) {
				count++;
			}
		}
		return current;
	}

	/**
	 * 计算 [start, end] 区间内工作日总数（闭区间，包含起止两天）
	 */
	public static int countWorkDays(LocalDate start, LocalDate end, Set<LocalDate> holidaySet,
			Set<LocalDate> workDaySet) {
		if (start == null || end == null) {
			throw new IllegalArgumentException("日期不能为null");
		}
		int cnt = 0;
		LocalDate curr = start;
		while (!curr.isAfter(end)) {
			if (isWorkDay(curr, holidaySet, workDaySet)) {
				cnt++;
			}
			curr = curr.plusDays(1);
		}
		return cnt;
	}

	// ========= 简易重载：无自定义节假日、只按周末判断 =========
	public static boolean isWorkDay(LocalDate date) {
		return isWorkDay(date, null, null);
	}

	public static LocalDate plusWorkDays(LocalDate startDate, int n) {
		return plusWorkDays(startDate, n, null, null);
	}

	public static int countWorkDays(LocalDate start, LocalDate end) {
		return countWorkDays(start, end, null, null);
	}
}
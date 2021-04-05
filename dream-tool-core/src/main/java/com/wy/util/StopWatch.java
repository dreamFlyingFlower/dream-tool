package com.wy.util;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * StopWatch工具类,统计代码耗时,类似System.currentTimeMillis().非线程安全
 * 
 * @author 飞花梦影
 * @date 2021-03-06 19:53:16
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class StopWatch {

	/** 对象编号,当有多个任务时,予以区分 */
	private final String id;

	/** 配置{@link TaskInfo}数组是否随时间生成,默认是 */
	private boolean keepTaskList = true;

	/** 任务信息列表 */
	private final List<TaskInfo> taskList = new ArrayList<>(1);

	/** 当前任务的开始时间 */
	private long startTimeNanos;

	/** 当前任务的名称 */
	private String currentTaskName;

	/** 当前的任务信息 */
	private TaskInfo lastTaskInfo;

	/** 任务数量 */
	private int taskCount;

	/** 总共运行时间,纳秒 */
	private long totalTimeNanos;

	public StopWatch() {
		this("");
	}

	public StopWatch(String id) {
		this.id = id;
	}

	/**
	 * 获得当前运行任务的名称
	 */
	public String currentTaskName() {
		return this.currentTaskName;
	}

	/**
	 * 返回任务编号.若构造StopWatch时没有传入id,返回""
	 * 
	 * @return 任务编号
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * 以纳秒为单位获取最后一个任务所用的时间
	 */
	public long getLastTaskTimeNanos() throws IllegalStateException {
		if (this.lastTaskInfo == null) {
			throw new IllegalStateException("No tasks run: can't get last task interval");
		}
		return this.lastTaskInfo.getTimeNanos();
	}

	/**
	 * 以毫秒为单位获取最后一个任务所用的时间
	 */
	public long getLastTaskTimeMillis() throws IllegalStateException {
		if (this.lastTaskInfo == null) {
			throw new IllegalStateException("No tasks run: can't get last task interval");
		}
		return this.lastTaskInfo.getTimeMillis();
	}

	/**
	 * 获取最后一个任务名称
	 */
	public String getLastTaskName() throws IllegalStateException {
		if (this.lastTaskInfo == null) {
			throw new IllegalStateException("No tasks run: can't get last task name");
		}
		return this.lastTaskInfo.getTaskName();
	}

	/**
	 * 获取最后一个任务的{@link TaskInfo}
	 */
	public TaskInfo getLastTaskInfo() throws IllegalStateException {
		if (this.lastTaskInfo == null) {
			throw new IllegalStateException("No tasks run: can't get last task info");
		}
		return this.lastTaskInfo;
	}

	/**
	 * 获取定时任务数
	 */
	public int getTaskCount() {
		return this.taskCount;
	}

	/**
	 * 获取所执行任务的{@link TaskInfo}数组
	 */
	public TaskInfo[] getTaskInfo() {
		if (!this.keepTaskList) {
			throw new UnsupportedOperationException("Task info is not being kept!");
		}
		return this.taskList.toArray(new TaskInfo[0]);
	}

	/**
	 * 以纳秒为单位获取所有任务的总时间
	 */
	public long getTotalTimeNanos() {
		return this.totalTimeNanos;
	}

	/**
	 * 以毫秒为单位获取所有任务的总时间
	 */
	public long getTotalTimeMillis() {
		return nanosToMillis(this.totalTimeNanos);
	}

	/**
	 * 以秒为单位获取所有任务的总时间
	 */
	public double getTotalTimeSeconds() {
		return nanosToSeconds(this.totalTimeNanos);
	}

	/**
	 * 判断当前StopWatch对象是否正在运行
	 */
	public boolean isRunning() {
		return (this.currentTaskName != null);
	}

	/**
	 * 字符串格式化输出所有执行的任务
	 */
	public String prettyPrint() {
		StringBuilder sb = new StringBuilder(shortSummary());
		sb.append('\n');
		if (!this.keepTaskList) {
			sb.append("No task info kept");
		} else {
			sb.append("---------------------------------------------\n");
			sb.append("ns         %     Task name\n");
			sb.append("---------------------------------------------\n");
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setMinimumIntegerDigits(9);
			nf.setGroupingUsed(false);
			NumberFormat pf = NumberFormat.getPercentInstance();
			pf.setMinimumIntegerDigits(3);
			pf.setGroupingUsed(false);
			for (TaskInfo task : getTaskInfo()) {
				sb.append(nf.format(task.getTimeNanos())).append("  ");
				sb.append(pf.format((double) task.getTimeNanos() / getTotalTimeNanos())).append("  ");
				sb.append(task.getTaskName()).append("\n");
			}
		}
		return sb.toString();
	}

	/**
	 * 配置{@link TaskInfo}数组是否随时间生成
	 * 
	 * @param keepTaskList true->是,false->否
	 */
	public void setKeepTaskList(boolean keepTaskList) {
		this.keepTaskList = keepTaskList;
	}

	/**
	 * 获取当前任务运行总时间的简短描述
	 */
	public String shortSummary() {
		return "StopWatch '" + getId() + "': running time = " + getTotalTimeNanos() + " ns";
	}

	/**
	 * 开始一个未指定名称的任务
	 */
	public void start() throws IllegalStateException {
		start("");
	}

	/**
	 * 开始一个指定名称的任务
	 * 
	 * @param taskName 任务名称
	 */
	public void start(String taskName) throws IllegalStateException {
		if (this.currentTaskName != null) {
			throw new IllegalStateException("Can't start StopWatch: it's already running");
		}
		this.currentTaskName = taskName;
		this.startTimeNanos = System.nanoTime();
	}

	/**
	 * 停止当前任务
	 */
	public void stop() throws IllegalStateException {
		if (this.currentTaskName == null) {
			throw new IllegalStateException("Can't stop StopWatch: it's not running");
		}
		long lastTime = System.nanoTime() - this.startTimeNanos;
		this.totalTimeNanos += lastTime;
		this.lastTaskInfo = new TaskInfo(this.currentTaskName, lastTime);
		if (this.keepTaskList) {
			this.taskList.add(this.lastTaskInfo);
		}
		++this.taskCount;
		this.currentTaskName = null;
	}

	/**
	 * 生成描述所执行的所有任务的信息字符串
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(shortSummary());
		if (this.keepTaskList) {
			for (TaskInfo task : getTaskInfo()) {
				sb.append("; [").append(task.getTaskName()).append("] took ").append(task.getTimeNanos()).append(" ns");
				long percent = Math.round(100.0 * task.getTimeNanos() / getTotalTimeNanos());
				sb.append(" = ").append(percent).append("%");
			}
		} else {
			sb.append("; no task info kept");
		}
		return sb.toString();
	}

	/**
	 * 将纳秒时间转为毫秒
	 * 
	 * @param duration 纳秒时间
	 * @return 毫秒
	 */
	private static long nanosToMillis(long duration) {
		return TimeUnit.NANOSECONDS.toMillis(duration);
	}

	/**
	 * 将纳秒时间转为秒
	 * 
	 * @param duration 纳秒时间
	 * @return 秒
	 */
	private static double nanosToSeconds(long duration) {
		return duration / 1_000_000_000.0;
	}

	/**
	 * StopWatch任务执行时的一些信息
	 * 
	 * @author 飞花梦影
	 * @date 2021-03-14 21:57:38
	 * @git {@link https://github.com/dreamFlyingFlower}
	 */
	public static final class TaskInfo {

		/** 任务名称 */
		private final String taskName;

		/** 任务时间,单位纳秒 */
		private final long timeNanos;

		TaskInfo(String taskName, long timeNanos) {
			this.taskName = taskName;
			this.timeNanos = timeNanos;
		}

		/**
		 * 获得任务名称
		 */
		public String getTaskName() {
			return this.taskName;
		}

		/**
		 * 获得任务所用时间的纳秒数
		 */
		public long getTimeNanos() {
			return this.timeNanos;
		}

		/**
		 * 获得任务所用时间的毫秒数
		 */
		public long getTimeMillis() {
			return nanosToMillis(this.timeNanos);
		}

		/**
		 * 获得任务所用时间秒数
		 */
		public double getTimeSeconds() {
			return nanosToSeconds(this.timeNanos);
		}
	}
}
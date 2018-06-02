package com.king.tooth.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 定时任务调度类
 * @author DougLei
 */
@SuppressWarnings("serial")
public class QuartzUtil implements Serializable{
	
	/**
	 * 调度器工厂对象
	 */
	private transient static final SchedulerFactory schedulerFactory = new StdSchedulerFactory();
	
	/**
	 * 存储调度器集合
	 */
	private transient static final Map<String, Scheduler> SCHEDULER_MAP = new HashMap<String, Scheduler>();
	public static Map<String, Scheduler> getSchedulerMap(){
		return SCHEDULER_MAP;
	}
	
	/**
	 * 得到scheduler实例对象
	 * @param jobName
	 * @param groupName
	 * @return
	 */
	private static Scheduler getScheduler(String jobName, String groupName){
		Scheduler scheduler = null;
		try {
			scheduler = SCHEDULER_MAP.get(jobName + "_" + groupName);
			if(scheduler == null){
				scheduler = schedulerFactory.getScheduler();
				SCHEDULER_MAP.put(jobName+"."+groupName, scheduler);
			}
		} catch (SchedulerException e) {
			Log4jUtil.debug("[QuartzUtil.getScheduler]方法出现异常：{}", ExceptionUtil.getErrMsg(e));
		}
		return scheduler;
	}
	
	/**
	 * 添加任务
	 * @param jobName 任务名称
	 * @param groupName 组名称
	 * @param clz 定时的类
	 * @param cronExpression 调用任务的时间设置表达式
	 * @param executeNow 是否立即执行
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addJob(String jobName, String groupName, Class clz, String cronExpression, boolean executeNow){
		Scheduler scheduler = getScheduler(jobName, groupName);
		JobDetail job = JobBuilder.newJob(clz).withIdentity(jobName, groupName).build();
		Trigger trigger = TriggerBuilder.newTrigger()
							.startNow()
							.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
							.build();

		try {
			scheduler.scheduleJob(job, trigger);
			scheduler.start();
			
			if(executeNow){
				executeJob(clz);
			}
		} catch (SchedulerException e) {
			Log4jUtil.debug("[QuartzUtil.addJob]方法出现异常：{}", ExceptionUtil.getErrMsg(e));
		} catch (InstantiationException e) {
			Log4jUtil.debug("[QuartzUtil.addJob]方法出现异常：{}", ExceptionUtil.getErrMsg(e));
		} catch (IllegalAccessException e) {
			Log4jUtil.debug("[QuartzUtil.addJob]方法出现异常：{}", ExceptionUtil.getErrMsg(e));
		} 
	}
	
	/**
	 * 停止任务
	 * @param jobName 任务名称
	 * @param groupName 组名称
	 */
	public static void stopJob(String jobName, String groupName){
		Scheduler scheduler = getScheduler(jobName, groupName);
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			Log4jUtil.debug("[QuartzUtil.stopJob]方法出现异常：{}", ExceptionUtil.getErrMsg(e));
		}
	}
	
	/**
	 * 立即执行
	 * @param clz 定时的类对象
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws JobExecutionException
	 */
	@SuppressWarnings("rawtypes")
	public static void executeJob(Class clz) throws InstantiationException, IllegalAccessException, JobExecutionException  {
		Job job = (Job) clz.newInstance();
		job.execute(null);
	}
}

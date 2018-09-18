package com.king.tooth.thread.operdb;

import org.hibernate.Session;

import com.king.tooth.util.Log4jUtil;

/**
 * hibernate操作数据库的线程
 * @author DougLei
 */
public abstract class HibernateOperDBThread extends OperDBThread{

	/**
	 * 和数据库连接的session对象
	 */
	protected Session session;
	
	public HibernateOperDBThread(Session session) {
		this.session = session;
		if(session == null){
			throw new NullPointerException("[HibernateOperDBThread]线程中的session对象为空，请检查程序逻辑");
		}
	}

	/**
	 * 是否继续执行
	 * <p>在线程一开始启动的时候判断</p>
	 * @return
	 */
	protected abstract boolean isGoOn();
	
	/**
	 * 进行实际的操作处理
	 * @throws Exception
	 */
	protected abstract void doRun() throws Exception;
	
	/**
	 * 进行catch块的处理
	 * @param e
	 */
	protected abstract void doCatch(Exception e);
	
	/**
	 * 进行finally块的处理
	 */
	protected abstract void doFinally();
	
	public void run(){
		Log4jUtil.debug("线程{}启动", getName());
		if(isGoOn()){
			try {
				session.beginTransaction();
				doRun();
				session.flush();
				session.getTransaction().commit();
			} catch (Exception e) {
				session.getTransaction().rollback();
				doCatch(e);
			} finally{
				session.close();
				doFinally();
			}
		}else{
			session.close();
			doFinally();
		}
	}
}

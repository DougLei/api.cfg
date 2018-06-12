package com.king.tooth.sys.service;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;

import com.king.tooth.cache.ProjectIdRefDatabaseIdMapping;
import com.king.tooth.sys.entity.IPublish;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.common.ComPublishInfo;
import com.king.tooth.sys.entity.common.ComSysResource;
import com.king.tooth.sys.service.common.ComPublishInfoService;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.database.DynamicDBUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 发布服务器的抽象类
 * @author DougLei
 */
public abstract class AbstractPublishService extends AbstractService{
	/**
	 * 发布信息的服务层
	 */
	protected ComPublishInfoService publishInfoService = new ComPublishInfoService();
	
	/**
	 * 执行远程发布操作
	 * <p>数据库id和项目id，这两个参数只要有一个值不为null即可</p>
	 * @param databaseId 
	 * @param projectId  
	 * @param publish
	 */
	protected void executeRemotePublish(String databaseId, String projectId, IPublish publish, ISysResource resource){
		if(databaseId == null){
			databaseId = ProjectIdRefDatabaseIdMapping.getDbId(projectId);
		}
		
		// 获取发布信息对象
		ComPublishInfo publishInfo = publish.turnToPublish();
		
		// 获取远程sessionFactory
		SessionFactoryImpl sessionFactory = DynamicDBUtil.getSessionFactory(databaseId);
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.save(publish.getEntityName(), publish.toEntityJson());
			if(resource != null){ // 如果资源对象不为空，同时发布资源
				ComSysResource csr = resource.turnToPublishResource();
				session.save(csr.getEntityName(), csr.toEntityJson());
			}
			session.getTransaction().commit();
			publishInfo.setIsSuccess(1);
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			publishInfo.setIsSuccess(0);
			publishInfo.setErrMsg(ExceptionUtil.getErrMsg(e));
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
		}
		// 再添加新的发布信息数据
		HibernateUtil.saveObject(publishInfo, null);
	}
	
	/**
	 * 执行远程修改操作
	 * <p>数据库id和项目id，这两个参数只要有一个值不为null即可</p>
	 * @param databaseId 
	 * @param projectId  
	 * @param hqls
	 */
	protected void executeRemoteUpdate(String databaseId, String projectId, String... hqls){
		if(databaseId == null){
			databaseId = ProjectIdRefDatabaseIdMapping.getDbId(projectId);
		}
		
		// 获取发布信息对象，对项目信息进行发布
		SessionFactoryImpl sessionFactory = DynamicDBUtil.getSessionFactory(databaseId);
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			for (String hql : hqls) {
				session.createQuery(hql).executeUpdate();
			}
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
		}
	}
}

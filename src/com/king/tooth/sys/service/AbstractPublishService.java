package com.king.tooth.sys.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.cache.ProjectIdRefDatabaseIdMapping;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.IPublish;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.cfg.ComPublishInfo;
import com.king.tooth.sys.entity.common.ComSysResource;
import com.king.tooth.sys.service.common.ComPublishInfoService;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
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
	 * @param resource
	 * @param datalinkResourceName
	 */
	protected void executeRemotePublish(String databaseId, String projectId, IPublish publish, ISysResource resource, String datalinkResourceName){
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
			
			if(datalinkResourceName != null){
				JSONObject dataLink = ResourceHandlerUtil.getDataLinksObject(projectId, publish.getId(), 1, null, null);
				dataLink.put(ResourceNameConstants.ID, ResourceHandlerUtil.getIdentity());
				session.save(datalinkResourceName, dataLink);
			}
			
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
		// 添加新的发布信息数据
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
				if(StrUtils.notEmpty(hql)){
					session.createQuery(hql).executeUpdate();
				}
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
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 执行远程批量发布操作
	 * <p>数据库id和项目id，这两个参数只要有一个值不为null即可</p>
	 * @param databaseId 
	 * @param projectId  
	 * @param publishs
	 * @param resources
	 * @param datalinkResourceName
	 */
	protected void executeRemoteBatchPublish(String databaseId, String projectId, List<? extends IPublish> publishs, List<? extends ISysResource> resources, String datalinkResourceName){
		// 记录发布时的错误信息
		String errMsg = null;
		// 获取发布信息对象
		List<ComPublishInfo> publishInfos = new ArrayList<ComPublishInfo>(publishs.size());
		
		// 获取远程sessionFactory
		SessionFactoryImpl sessionFactory = DynamicDBUtil.getSessionFactory(databaseId);
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			
			ComPublishInfo publishInfo;
			JSONObject dataLink;
			for (IPublish entity : publishs) {
				publishInfo = entity.turnToPublish();
				publishInfos.add(publishInfo);
				if(entity.getBatchPublishMsg() != null){
					publishInfo.setErrMsg(entity.getBatchPublishMsg());
					continue;
				}
				session.save(entity.getEntityName(), entity.toEntityJson());
				
				if(datalinkResourceName != null){
					dataLink = ResourceHandlerUtil.getDataLinksObject(projectId, entity.getId(), 1, null, null);
					dataLink.put(ResourceNameConstants.ID, ResourceHandlerUtil.getIdentity());
					session.save(datalinkResourceName, dataLink);
				}
			}
			
			// 如果资源对象不为空，同时发布资源
			if(resources != null && resources.size() > 0){ 
				ComSysResource csr;
				for (ISysResource entity : resources) {
					if(entity.getBatchPublishMsg() != null){
						continue;
					}
					csr = entity.turnToPublishResource();
					session.save(csr.getEntityName(), csr.toEntityJson());
				}
			}
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			errMsg = ExceptionUtil.getErrMsg(e);
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
		}
		
		// 添加新的发布信息数据
		for (ComPublishInfo publishInfo : publishInfos) {
			if(publishInfo.getErrMsg() == null && errMsg == null){
				publishInfo.setIsSuccess(1);
			}else{
				if(publishInfo.getErrMsg() == null){
					publishInfo.setErrMsg(errMsg);
				}
			}
			HibernateUtil.saveObject(publishInfo, null);
		}
		publishInfos.clear();
	}
}

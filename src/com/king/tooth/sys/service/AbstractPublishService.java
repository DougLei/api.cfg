package com.king.tooth.sys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.cache.ProjectIdRefDatabaseIdMapping;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.IPublish;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.cfg.CfgDatabase;
import com.king.tooth.sys.entity.dm.DmPublishInfo;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.sys.service.dm.DmPublishInfoService;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.database.DynamicDBUtil;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.util.httpclient.HttpClientUtil;

/**
 * 发布服务器的抽象类
 * @author DougLei
 */
public abstract class AbstractPublishService extends AbstractService{
	/**
	 * 发布信息的服务层
	 */
	protected DmPublishInfoService publishInfoService = new DmPublishInfoService();
	
	/**
	 * 获得运行系统的数据库id
	 * @param database
	 * @return
	 */
	protected String getAppSysDatabaseId(CfgDatabase database){
		if(database != null && database.getIsBuiltin() == 1){
			return database.getId();
		}
		return (String) HibernateUtil.executeUniqueQueryByHql("select "+ResourcePropNameConstants.ID+" from CfgDatabase where isBuiltin=1", null);
	}
	
	/**
	 * 执行远程发布操作
	 * @param databaseId 可以为null
	 * @param projectId 必须有值
	 * @param publish
	 * @param processSysResource
	 * @param datalinkResourceName
	 */
	protected void executeRemotePublish(String databaseId, String projectId, IPublish publish, int processSysResource, String datalinkResourceName){
		if(databaseId == null){
			databaseId = ProjectIdRefDatabaseIdMapping.getDbId(projectId);
		}
		
		// 获取发布信息对象
		DmPublishInfo publishInfo = publish.turnToPublish();
		
		// 获取远程sessionFactory
		SessionFactoryImpl sessionFactory = DynamicDBUtil.getSessionFactory(databaseId);
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			JSONObject publishEntityJson = publish.toPublishEntityJson(projectId);
			session.save(publish.getEntityName(), publishEntityJson);
			
			if(datalinkResourceName != null){
				JSONObject dataLink = ResourceHandlerUtil.getDataLinksObject(projectId, publishEntityJson.getString(ResourcePropNameConstants.ID), "1", null, null);
				dataLink.put("projectId", projectId);
				dataLink.put(ResourcePropNameConstants.ID, ResourceHandlerUtil.getIdentity());
				session.save(datalinkResourceName, dataLink);
			}
			
			if(processSysResource == 1){ // 标识需要处理资源
				SysResource csr = ((ISysResource)publish).turnToPublishResource(projectId, publishEntityJson.getString(ResourcePropNameConstants.ID));
				session.save(csr.getEntityName(), csr.toEntityJson());
			}
			session.getTransaction().commit();
			publishInfo.setIsSuccess(1);
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			publishInfo.setIsSuccess(0);
			publishInfo.setErrMsg(ExceptionUtil.getErrMsg("AbstractPublishService", "executeRemotePublish", e));
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
	 * @param databaseId 可以为null
	 * @param projectId 必须有值
	 * @param publishs
	 * @param processSysResource 
	 * @param datalinkResourceName
	 */
	protected void executeRemoteBatchPublish(String databaseId, String projectId, List<? extends IPublish> publishs, int processSysResource, String datalinkResourceName){
		if(databaseId == null){
			databaseId = ProjectIdRefDatabaseIdMapping.getDbId(projectId);
		}
		
		// 记录发布时的错误信息
		String errMsg = null;
		// 获取发布信息对象
		List<DmPublishInfo> publishInfos = new ArrayList<DmPublishInfo>(publishs.size());
		
		// 获取远程sessionFactory
		SessionFactoryImpl sessionFactory = DynamicDBUtil.getSessionFactory(databaseId);
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			
			DmPublishInfo publishInfo;
			JSONObject dataLink;
			JSONObject publishEntityJson;
			SysResource csr;
			ISysResource sysResource;
			int orderCode = 1;
			for (IPublish entity : publishs) {
				publishInfo = entity.turnToPublish();
				publishInfos.add(publishInfo);
				if(entity.getBatchPublishMsg() != null){
					publishInfo.setErrMsg(entity.getBatchPublishMsg());
					continue;
				}
				publishEntityJson = entity.toPublishEntityJson(projectId);
				session.save(entity.getEntityName(), publishEntityJson);
				
				if(datalinkResourceName != null){
					dataLink = ResourceHandlerUtil.getDataLinksObject(projectId, publishEntityJson.getString(ResourcePropNameConstants.ID), ""+(orderCode++), null, null);
					dataLink.put("projectId", projectId);
					dataLink.put(ResourcePropNameConstants.ID, ResourceHandlerUtil.getIdentity());
					session.save(datalinkResourceName, dataLink);
				}
				
				if(processSysResource == 1){
					sysResource = (ISysResource) entity;
					if(sysResource.getBatchPublishMsg() != null){
						continue;
					}
					csr = sysResource.turnToPublishResource(projectId, publishEntityJson.getString(ResourcePropNameConstants.ID));
					session.save(csr.getEntityName(), csr.toEntityJson());
				}
			}
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			errMsg = ExceptionUtil.getErrMsg("AbstractPublishService", "executeRemoteBatchPublish", e);
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
		}
		
		// 添加新的发布信息数据
		for (DmPublishInfo publishInfo : publishInfos) {
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
	
	// --------------------------------------------------------------------------------------------------------
	
	/**
	 * 运行系统处理加载/卸载数据的api路径
	 */
	private static final String appWebSysProcessPublishDataApiPath = SysConfig.getSystemConfig("app.web.sys.location") + "/monitoring/data/publish";
	private static final String publishApiToken = ResourceHandlerUtil.getIdentity();
	
	/**
	 * 获取调用加载/卸载数据的api的参数map集合
	 * @param projectId 发布数据所属的projectId
	 * @param publishDataType 发布数据类型：[publishDataType = db、project、module、oper、table、sql]
	 * @param publishType 发布类型：[publishType = 1：发布/-1：取消发布]
	 * @return
	 */
	private Map<String, String> getUrlParams(String projectId, String publishDataType, String publishType){
		Map<String, String> urlParams = new HashMap<String, String>(4);
		urlParams.put("projectId", projectId);
		urlParams.put("publishDataType", publishDataType);
		urlParams.put("publishType", publishType);
		urlParams.put("publish_api_token", publishApiToken);
		return urlParams;
	}
	
	/**
	 * 调用加载/卸载资源的api
	 * @param publishDataId
	 * @param projectId
	 * @param publishDataType
	 * @param publishType
	 * @param headerProjectId
	 * @return
	 */
	protected String usePublishResourceApi(String publishDataId, String projectId, String publishDataType, String publishType, String headerProjectId){
		return HttpClientUtil.doPostBasic(appWebSysProcessPublishDataApiPath, 
				getUrlParams(headerProjectId, publishDataType, publishType), 
				null, null, 
				HttpClientUtil.getHttpStringRequestEntity(publishDataId, "text/json"));
	}
	
	/**
	 * 修改isCreated的值
	 * @param entityName
	 * @param isCreated 0/1
	 * @param entityId
	 */
	protected void modifyIsCreatedPropVal(String entityName, int isCreated, String entityId){
		String hql = "update " + entityName + " set isCreated ="+isCreated + " where "+ResourcePropNameConstants.ID+" = '"+entityId+"'";
		HibernateUtil.executeUpdateByHql(BuiltinDatabaseData.UPDATE, hql, null);
	}
	/**
	 * 批量修改是否created的值
	 * @param entityName
	 * @param isCreated
	 * @param entityIds 在方法中被clear
	 */
	protected void batchModifyIsCreatedPropVal(String entityName, int isCreated, List<Object> entityIds) {
		StringBuilder hql = new StringBuilder("update " + entityName + " set isCreated ="+isCreated + " where id in (");
		for (Object entityId : entityIds) {
			hql.append("'").append(entityId).append("',");
		}
		hql.setLength(hql.length() - 1);
		hql.append(")");
		
		HibernateUtil.executeUpdateByHql(BuiltinDatabaseData.UPDATE, hql.toString(), null);
	}
}

<?xml version="1.0"?>
<!DOCTYPE hibernate-mapping PUBLIC 
    "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
    "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">

<!-- ${table.resourceName}.hbm.xml的配置文件版本为：${table.version} -->
<hibernate-mapping>
	<class entity-name="${table.resourceName}" table="${table.tableName}">
		<id name="id" column="id" type="string">
			<generator class="assigned" />
		</id>
		<property name="projectId" column="project_id" type="string" />
		
		<#if table.isResource == 1>
			<property name="isEnabled" column="is_enabled" type="string" />
			<property name="validDate" column="valid_date" type="timestamp" />
			<property name="isNeedDeploy" column="is_need_deploy" type="string" />
			<property name="reqResourceMethod" column="req_resource_method" type="string" />
			<property name="isBuiltin" column="is_builtin" type="string" />
			<property name="platformType" column="platform_type" type="string" />
			<property name="isCreatedResource" column="is_created_resource" type="string" />
		</#if>
		
		<#if table.isDatalinkTable == 0 && table.tableName != "COM_DATA_LINKS">
			<property name="createUserId" column="create_user_id" type="string" />
			<property name="lastUpdatedUserId" column="last_updated_user_id" type="string" />
			<property name="createTime" column="create_time" type="timestamp" />
			<property name="lastUpdateTime" column="last_update_time" type="timestamp" />
		</#if>
		
		<#list columns as column>
			<property name="${column.propName}" column="${column.columnName}" 
				<#if column.columnType == "date">
					type="timestamp"
				<#else>
					type="string"
				</#if>
			 />
		</#list>
	</class>
</hibernate-mapping>
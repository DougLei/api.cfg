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
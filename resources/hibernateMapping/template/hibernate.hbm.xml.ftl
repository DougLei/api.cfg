<?xml version="1.0"?>
<!DOCTYPE hibernate-mapping PUBLIC 
    "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
    "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">

<!-- ${table.resourceName}.hbm.xml的配置文件 -->
<hibernate-mapping>
	<class entity-name="${table.resourceName}" table="${table.tableName}" dynamic-insert="true">
		<id name="${id}" column="id" type="string">
			<generator class="assigned" />
		</id>
		
		<#list columns as column>
			<#if column.propName?lower_case != "id">
				<property name="${column.propName}" column="${column.columnName}" 
					<#if column.columnType == "date">
						type="timestamp"
					<#else>
						type="string"
					</#if>
				 />
			</#if>
		</#list>
	</class>
</hibernate-mapping>
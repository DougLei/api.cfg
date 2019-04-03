-- 建立数据库连接
/*

EXEC  sp_addlinkedserver
@server='DBMES',   --链接服务器别名
@srvproduct='',
@provider='SQLOLEDB',
@datasrc='192.168.1.111'  --要访问的的数据库所在的服务器的ip
GO

EXEC sp_addlinkedsrvlogin
'DBMES',                  --链接服务器别名
'false', 
 NULL,
'sa',                     --要访问的数据库的用户              
'123_abc'                    --要访问的数据库，用户的密码
GO

SELECT * FROM [DBMES].[数据库名].[dbo].[表名]
*/


-- 同步 cfg_project_module数据
-- 模块code，多个用,分割
select * from cfg_project_module where code in (
'MPM_PRODUCTDETAILS','MPM_PF_R','MPM_BPF'
)

/*

delete SMC_SERVICE.SmartOneCfg.dbo.cfg_project_module where code in (
'MPM_PRODUCTDETAILS','MPM_PF_R','MPM_BPF'
)

insert into SMC_SERVICE.SmartOneCfg.dbo.cfg_project_module
select * from cfg_project_module where code in (
'MPM_PRODUCTDETAILS','MPM_PF_R','MPM_BPF'
)

*/


-- 同步 cfg_sql和cfg_project_module的sys_data_links数据
-- 模块code，多个用,分割
select * from sys_data_links where left_id in (
select id from cfg_project_module where code in 
(
'MPM_PRODUCTDETAILS','MPM_PF_R','MPM_BPF'
)
)

/*

delete SMC_SERVICE.SmartOneCfg.dbo.sys_data_links where left_id in (
select id from cfg_project_module where code in 
(
'MPM_PRODUCTDETAILS','MPM_PF_R','MPM_BPF'
)
)

insert into SMC_SERVICE.SmartOneCfg.dbo.sys_data_links
select * from sys_data_links where left_id in (
select id from cfg_project_module where code in 
(
'MPM_PRODUCTDETAILS','MPM_PF_R','MPM_BPF'
)
)

*/


-- 同步 cfg_sql数据
-- 模块code，多个用,分割
select * from cfg_sql where id in (
select right_id from sys_data_links where left_id in (
select id from cfg_project_module where code in 
(
'MPM_PRODUCTDETAILS','MPM_PF_R','MPM_BPF'
)
)
)

/*

delete SMC_SERVICE.SmartOneCfg.dbo.cfg_sql where id in (
select right_id from sys_data_links where left_id in (
select id from cfg_project_module where code in 
(
'MPM_PRODUCTDETAILS','MPM_PF_R','MPM_BPF'
)
)
)

insert into SMC_SERVICE.SmartOneCfg.dbo.cfg_sql
select * from cfg_sql where id in (
select right_id from sys_data_links where left_id in (
select id from cfg_project_module where code in 
(
'MPM_PRODUCTDETAILS','MPM_PF_R','MPM_BPF'
)
)
)

*/


-- 同步 cfg_sql_parameter数据
-- 模块code，多个用,分割
select * from cfg_sql_parameter where sql_script_id in(
select id from cfg_sql where id in (
select right_id from sys_data_links where left_id in (
select id from cfg_project_module where code in 
(
'MPM_PRODUCTDETAILS','MPM_PF_R','MPM_BPF'
)
)
)
)

/*

delete SMC_SERVICE.SmartOneCfg.dbo.cfg_sql_parameter where sql_script_id in(
select id from cfg_sql where id in (
select right_id from sys_data_links where left_id in (
select id from cfg_project_module where code in 
(
'MPM_PRODUCTDETAILS','MPM_PF_R','MPM_BPF'
)
)
)
)

insert into SMC_SERVICE.SmartOneCfg.dbo.cfg_sql_parameter 
select * from cfg_sql_parameter where sql_script_id in(
select id from cfg_sql where id in (
select right_id from sys_data_links where left_id in (
select id from cfg_project_module where code in 
(
'MPM_PRODUCTDETAILS','MPM_PF_R','MPM_BPF'
)
)
)
)

*/


-- 同步 cfg_sql_resultset数据
-- 模块code，多个用,分割
select * from cfg_sql_resultset where sql_script_id in(
select id from cfg_sql where id in (
select right_id from sys_data_links where left_id in (
select id from cfg_project_module where code in 
(
'MPM_PRODUCTDETAILS','MPM_PF_R','MPM_BPF'
)
)
)
)

/*

delete SMC_SERVICE.SmartOneCfg.dbo.cfg_sql_resultset where sql_script_id in(
select id from cfg_sql where id in (
select right_id from sys_data_links where left_id in (
select id from cfg_project_module where code in 
(
'MPM_PRODUCTDETAILS','MPM_PF_R','MPM_BPF'
)
)
)
)

insert into SMC_SERVICE.SmartOneCfg.dbo.cfg_sql_resultset
select * from cfg_sql_resultset where sql_script_id in(
select id from cfg_sql where id in (
select right_id from sys_data_links where left_id in (
select id from cfg_project_module where code in 
(
'MPM_PRODUCTDETAILS','MPM_PF_R','MPM_BPF'
)
)
)
)

*/


-- 同步 cfg_sql的cfg_resource数据
-- 模块code，多个用,分割
select * from cfg_resource 
where ref_resource_id in (
select id from cfg_sql where id in (
select right_id from sys_data_links where left_id in (
select id from cfg_project_module where code in 
(
'MPM_PRODUCTDETAILS','MPM_PF_R','MPM_BPF'
)
)
)
)

/*

delete SMC_SERVICE.SmartOneCfg.dbo.cfg_resource 
where ref_resource_id in (
select id from cfg_sql where id in (
select right_id from sys_data_links where left_id in (
select id from cfg_project_module where code in 
(
'MPM_PRODUCTDETAILS','MPM_PF_R','MPM_BPF'
)
)
)
)

insert into SMC_SERVICE.SmartOneCfg.dbo.cfg_resource 
select * from cfg_resource 
where ref_resource_id in (
select id from cfg_sql where id in (
select right_id from sys_data_links where left_id in (
select id from cfg_project_module where code in 
(
'MPM_PRODUCTDETAILS','MPM_PF_R','MPM_BPF'
)
)
)
)

*/


-- 同步存储过程，将存储过程在被同步的数据库中执行
-- 模块code，多个用,分割
-- 查询出的contents值，拿到目标数据库中执行create procedure或alter procedure操作
select name, resource_name, contents, type from SMC_SERVICE.SmartOneCfg.dbo.cfg_sql where id in (
select right_id from sys_data_links where left_id in (
select id from cfg_project_module where code in 
(
'MPM_PRODUCTDETAILS','MPM_PF_R','MPM_BPF'
)
)
) and type = 'procedure'

------------------------------------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------------------------------------------

-- 同步 cfg_table数据
-- 表名，多个用,分割
select * from cfg_table 
where table_name in (
'MPM_PRODUCT','MPM_PRODUCTDETAILS','MPM_MPF','MPM_PF','MPM_SPF','MPM_STEPPROCESSCHECKELEMENT','MPM_BSPF'
)

/*

delete from SMC_SERVICE.SmartOneCfg.dbo.cfg_table  
where id in (
select id from cfg_table 
where table_name in (
'MPM_PRODUCT','MPM_PRODUCTDETAILS','MPM_MPF','MPM_PF','MPM_SPF','MPM_STEPPROCESSCHECKELEMENT','MPM_BSPF'
)
)

insert into SMC_SERVICE.SmartOneCfg.dbo.cfg_table  
select * from cfg_table 
where table_name in (
'MPM_PRODUCT','MPM_PRODUCTDETAILS','MPM_MPF','MPM_PF','MPM_SPF','MPM_STEPPROCESSCHECKELEMENT','MPM_BSPF'
)

*/


-- 同步 cfg_column数据
-- 表名，多个用,分割
select * from cfg_column 
where table_id in (
select id from cfg_table 
where table_name in (
'MPM_PRODUCT','MPM_PRODUCTDETAILS','MPM_MPF','MPM_PF','MPM_SPF','MPM_STEPPROCESSCHECKELEMENT','MPM_BSPF'
)
)

/*

delete from SMC_SERVICE.SmartOneCfg.dbo.cfg_column 
where table_id in (
select id from cfg_table 
where table_name in (
'MPM_PRODUCT','MPM_PRODUCTDETAILS','MPM_MPF','MPM_PF','MPM_SPF','MPM_STEPPROCESSCHECKELEMENT','MPM_BSPF'
)
)

insert into SMC_SERVICE.SmartOneCfg.dbo.cfg_column 
select * from cfg_column 
where table_id in (
select id from cfg_table 
where table_name in (
'MPM_PRODUCT','MPM_PRODUCTDETAILS','MPM_MPF','MPM_PF','MPM_SPF','MPM_STEPPROCESSCHECKELEMENT','MPM_BSPF'
)
)

*/


-- 同步 cfg_hibernate数据
-- 表名，多个用,分割
select * from cfg_hibernate_hbm 
where ref_table_id in (
select id from cfg_table 
where table_name in (
'MPM_PRODUCT','MPM_PRODUCTDETAILS','MPM_MPF','MPM_PF','MPM_SPF','MPM_STEPPROCESSCHECKELEMENT','MPM_BSPF'
)
)

/*

delete from SMC_SERVICE.SmartOneCfg.dbo.cfg_hibernate_hbm 
where ref_table_id in (
select id from cfg_table 
where table_name in (
'MPM_PRODUCT','MPM_PRODUCTDETAILS','MPM_MPF','MPM_PF','MPM_SPF','MPM_STEPPROCESSCHECKELEMENT','MPM_BSPF'
)
)

insert into SMC_SERVICE.SmartOneCfg.dbo.cfg_hibernate_hbm 
select * from cfg_hibernate_hbm 
where ref_table_id in (
select id from cfg_table 
where table_name in (
'MPM_PRODUCT','MPM_PRODUCTDETAILS','MPM_MPF','MPM_PF','MPM_SPF','MPM_STEPPROCESSCHECKELEMENT','MPM_BSPF'
)
)

*/


-- 同步 cfg_table的cfg_resource数据
-- 表名，多个用,分割
select * from cfg_resource 
where ref_resource_id in (
select id from cfg_table 
where table_name in (
'MPM_PRODUCT','MPM_PRODUCTDETAILS','MPM_MPF','MPM_PF','MPM_SPF','MPM_STEPPROCESSCHECKELEMENT','MPM_BSPF'
)
)

/*

delete from SMC_SERVICE.SmartOneCfg.dbo.cfg_resource 
where ref_resource_id in (
select id from cfg_table 
where table_name in (
'MPM_PRODUCT','MPM_PRODUCTDETAILS','MPM_MPF','MPM_PF','MPM_SPF','MPM_STEPPROCESSCHECKELEMENT','MPM_BSPF'
)
)

insert into SMC_SERVICE.SmartOneCfg.dbo.cfg_resource 
select * from cfg_resource 
where ref_resource_id in (
select id from cfg_table 
where table_name in (
'MPM_PRODUCT','MPM_PRODUCTDETAILS','MPM_MPF','MPM_PF','MPM_SPF','MPM_STEPPROCESSCHECKELEMENT','MPM_BSPF'
)
)

*/


-- 同步表
-- 将要同步的表，从数据库中导出，再在目标数据库中导入，即可
select 'drop table '+table_name from cfg_table 
where table_name in (
'MPM_PRODUCT','MPM_PRODUCTDETAILS','MPM_MPF','MPM_PF','MPM_SPF','MPM_STEPPROCESSCHECKELEMENT','MPM_BSPF'
)
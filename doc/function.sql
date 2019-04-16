-- 传入用户id，查询对应的角色id集合
create function F_GetRoleId(@currentUserId varchar(32))
	returns @RoldIdTable table (
		id varchar(32),
		CODE varchar(20),
		NAME varchar(30)
	)
as
begin
	insert into @RoldIdTable select id, code, name from sys_role where id in (select right_id from sys_user_role_links where left_id = @currentUserId);
	return;
end
go


-- 传入用户id，查询对应的部门id集合
create function F_GetDeptId(@currentUserId varchar(32))
	returns @DeptIdTable table (
		id varchar(32),
		NAME varchar(100),
		SHORT_NAME varchar(50),
		CODE varchar(32)
	)
as
begin
	insert into @DeptIdTable select id, name, short_name, code from sys_dept where id in (select right_id from sys_user_dept_links where left_id = @currentUserId);
	return;
end
go


-- 传入用户id，查询对应的组织id集合
create function F_GetOrgId(@currentUserId varchar(32))
	returns @OrgIdTable table (id varchar(32))
as
begin
	insert into @OrgIdTable select org_id from sys_dept where id in (select right_id from sys_user_dept_links where left_id = @currentUserId);
	return;
end
go


-- 传入用户id，查询对应的职务id集合
create function F_GetPositionId(@currentUserId varchar(32))
	returns @PositionIdTable table (id varchar(32))
as
begin
	insert into @PositionIdTable select right_id from sys_user_position_links where left_id = @currentUserId;
	return;
end
go
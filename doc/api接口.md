# api功能说明文档

## 一、系统管理

### (一)、登陆

- 功能描述: 登陆系统
- api地址: /common/login
- 请求类型: POST
- 请求头: 无
- 请求url参数: 无
- 请求体: 
```
{
  "loginName":"developer",				  --登录名
  "loginPwd":"1"					  --登陆密码
}
```

### (二)、退出

- 功能描述: 退出系统
- api地址: /common/login_out
- 请求类型: POST
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 无

### (三)、用户管理

> 资源名: SysUser

#### 1. 添加用户

- 功能描述: 添加一条用户信息 【支持批量操作】
- api地址: /common/user/add
- 请求类型: POST
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "nikeName":"",                             --昵称
  "realName":"",                             --真实姓名
  "age":"",                                  --年龄
  "sex":"",                                  --性别，0女1男
  "officePhone":"",                          --办公电话
  "tel":"",                                  --手机号
  "email":"",                                --邮箱
  "workAddr":"",                             --办公地点
  "liveAddr":"",                             --居住地点
  "idCardNo":"",                             --身份证号码
  "employedDate":"",                         --入职时间
  "userStatus":"",                           --人员状态
  "monthSalar":"",                           --月薪
  "workNo":"",                               --工号
  "secretLevel":"",                          --密级
  "descs":"",                                --描述
  "deptId":"",                               --部门id
  "positionId":"",                           --岗位id
  "isCreateAccount":""                       --是否创建账户信息，1是0否
}
```
- 备注: 如果创建账户，则账户登录名为工号(workNo)，初始密码为1

#### 2. 修改用户

- 功能描述: 修改一条用户信息
- api地址: /common/user/update
- 请求类型: PUT
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "Id":"",                                   --主键
  "nikeName":"",                             --昵称
  "realName":"",                             --真实姓名
  "age":"",                                  --年龄
  "sex":"",                                  --性别，0女1男
  "officePhone":"",                          --办公电话
  "tel":"",                                  --手机号
  "email":"",                                --邮箱
  "workAddr":"",                             --办公地点
  "liveAddr":"",                             --居住地点
  "idCardNo":"",                             --身份证号码
  "employedDate":"",                         --入职时间
  "userStatus":"",                           --人员状态
  "monthSalar":"",                           --月薪
  "workNo":"",                               --工号
  "secretLevel":"",                          --密级
  "descs":"",                                --描述
  "deptId":"",                               --部门id
  "positionId":"",                           --岗位id
  "isCreateAccount":""                       --是否创建账户信息，1是0否
}
```

#### 3. 删除用户

- 功能描述: 删除一条用户信息，同时会删除关联的账户信息 【支持批量操作】
- api地址: /common/user/delete
- 请求类型: DELETE
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 
```
?_ids=5k7f1ef06728y6016f9d10e91dcr1d39, ...             --主键，多个用,分隔
```
- 请求体: 无

#### 4. 开通账户

- 功能描述: 给指定的用户开通一个对应的账户，用户即可通过账户登陆系统 【支持批量操作】
- api地址: /common/user/open_account
- 请求类型: POST
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "Id":""                                               --主键
}
```

#### 5. 修改用户登陆密码

- 功能描述: 修改用户关联的账户的登陆密码
- api地址: /common/user/update_pwd
- 请求类型: POST
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "Id":"",                                              --主键
  "password":""                                         --新密码
}
```

### (四)、账户管理

> 资源名: SysAccount

#### 1. 添加账户

- 功能描述: 添加一条账户信息 【支持批量操作】
- api地址: /common/account/add
- 请求类型: POST
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "loginName":"",                                     --登录名
  "loginPwd":"",                                      --登陆密码
  "tel":"",                                           --电话号码
  "email":"",                                         --邮箱
  "accountType":"",                                   --账户类型
  "accountStatus":"",                                 --账户状态
  "validDate":""                                      --有效期
}
```

#### 2. 修改账户

- 功能描述: 修改一条账户信息
- api地址: /common/account/update
- 请求类型: PUT
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "Id":"",                                            --主键
  "loginName":"",                                     --登录名
  "tel":"",                                           --电话号码
  "email":"",                                         --邮箱
  "accountType":"",                                   --账户类型
  "accountStatus":"",                                 --账户状态
  "validDate":""                                      --有效期
}
```

#### 3. 删除账户

- 功能描述: 删除一条账户信息 【支持批量操作】
- api地址: /common/account/delete
- 请求类型: DELETE
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 
```
?_ids=5k7f1ef06728y6016f9d10e91dcr1d39, ...             --主键，多个用,分隔
```
- 请求体: 无

#### 4. 修改账户登陆密码

- 功能描述: 修改账户的登陆密码
- api地址: /common/account/update_pwd
- 请求类型: POST
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "Id":"",                                              --主键
  "password":""                                         --新密码
}
```

### (五)、角色管理

> 资源名: SysRole

#### 1. 添加角色

- 功能描述: 添加一条角色信息 【支持批量操作】
- api地址: /common/SysRole
- 请求类型: POST
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "name":"",                                            --角色名
  "code":"",                                            --角色编码
  "descs":"",                                           --角色描述
  "orderCode":1,                                        --排序
  "isEnabled":""                                        --是否启用，1是0否
}
```

#### 2. 修改角色

- 功能描述: 修改一条角色信息
- api地址: /common/SysRole
- 请求类型: PUT
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "Id":"",                                              --主键
  "name":"",                                            --角色名
  "code":"",                                            --角色编码
  "descs":"",                                           --角色描述
  "orderCode":1,                                        --排序
  "isEnabled":""                                        --是否启用，1是0否
}
```

#### 3. 删除角色

- 功能描述: 删除一条角色信息 【支持批量操作】
- api地址: /common/SysRole
- 请求类型: DELETE
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 
```
?_ids=5k7f1ef06728y6016f9d10e91dcr1d39, ...             --主键，多个用,分隔
```
- 请求体: 无

### (六)、权限管理

> 资源名: SysPermission

#### 1. 添加权限

- 功能描述: 添加一条权限信息 【支持批量操作】
- api地址: /common/SysPermission
- 请求类型: POST
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "refDataId":"",                                       --关联的数据id
  "refDataType":"",                                     --关联的数据类型
  "refResourceId":"",                                   --关联的资源id
  "refResourceCode":"",                                 --关联的资源code，全项目唯一
  "refParentResourceId":"",                             --关联的父资源id
  "refParentResourceCode":"",                           --关联的父资源code
  "refResourceType":"",                                 --关联的资源类型
  "isVisibility":"",                                    --是否可见(是否可读)，1是0否
  "isOper":""                                           --是否可操作(是否可写)，1是0否
}
```

#### 2. 修改权限

- 功能描述: 修改一条权限信息
- api地址: /common/SysPermission
- 请求类型: PUT
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "Id":"",                                              --主键
  "refDataId":"",                                       --关联的数据id
  "refDataType":"",                                     --关联的数据类型
  "refResourceId":"",                                   --关联的资源id
  "refResourceCode":"",                                 --关联的资源code，全项目唯一
  "refParentResourceId":"",                             --关联的父资源id
  "refParentResourceCode":"",                           --关联的父资源code
  "refResourceType":"",                                 --关联的资源类型
  "isVisibility":"",                                    --是否可见(是否可读)，1是0否
  "isOper":""                                           --是否可操作(是否可写)，1是0否
}
```

#### 3. 删除权限

- 功能描述: 删除一条权限信息 【支持批量操作】
- api地址: /common/SysPermission
- 请求类型: DELETE
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 
```
?_ids=5k7f1ef06728y6016f9d10e91dcr1d39, ...             --主键，多个用,分隔
```
- 请求体: 无

### (七)、组织管理

> 资源名: SysOrg

#### 1. 添加组织

- 功能描述: 添加一条组织信息 【支持批量操作】
- api地址: /common/SysOrg
- 请求类型: POST
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "parentId":"",                                        --父组织主键
  "name":"",                                            --组织名
  "shortName":"",                                       --组织简称
  "code":"",                                            --组织编码
  "orderCode":1                                         --排序
}
```

#### 2. 修改组织

- 功能描述: 修改一条组织信息
- api地址: /common/SysOrg
- 请求类型: PUT
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "Id":"",                                              --主键
  "parentId":"",                                        --父组织主键
  "name":"",                                            --组织名
  "shortName":"",                                       --组织简称
  "code":"",                                            --组织编码
  "orderCode":1                                         --排序
}
```

#### 3. 删除组织

- 功能描述: 删除一条组织信息 【支持批量操作】
- api地址: /common/SysOrg
- 请求类型: DELETE
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 
```
?_ids=5k7f1ef06728y6016f9d10e91dcr1d39, ...             --主键，多个用,分隔
```
- 请求体: 无

### (八)、部门管理

> 资源名: SysDept

#### 1. 添加部门

- 功能描述: 添加一条部门信息 【支持批量操作】
- api地址: /common/SysDept
- 请求类型: POST
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "orgId":"",                                          --所属组织主键
  "parentId":"",                                       --父部门主键
  "name":"",                                           --部门名
  "shortName":"",                                      --部门简称
  "code":"",                                           --部门编码
  "orderCode":1                                        --排序
}
```

#### 2. 修改部门

- 功能描述: 修改一条部门信息
- api地址: /common/SysDept
- 请求类型: PUT
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "Id":"",                                             --主键
  "orgId":"",                                          --所属组织主键
  "parentId":"",                                       --父部门主键
  "name":"",                                           --部门名
  "shortName":"",                                      --部门简称
  "code":"",                                           --部门编码
  "orderCode":1                                        --排序
}
```

#### 3. 删除部门

- 功能描述: 删除一条部门信息 【支持批量操作】
- api地址: /common/SysDept
- 请求类型: DELETE
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 
```
?_ids=5k7f1ef06728y6016f9d10e91dcr1d39, ...             --主键，多个用,分隔
```
- 请求体: 无

### (九)、职务管理

> 资源名: SysPosition

#### 1. 添加职务

- 功能描述: 添加一条职务信息 【支持批量操作】
- api地址: /common/SysPosition
- 请求类型: POST
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "name":"",                                            --职务名
  "code":"",                                            --职务编码
  "descs":"",                                           --职务描述
  "orderCode":1                                         --排序
}
```

#### 2. 修改职务

- 功能描述: 修改一条职务信息
- api地址: /common/SysPosition
- 请求类型: PUT
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "Id":"",                                              --主键
  "name":"",                                            --职务名
  "code":"",                                            --职务编码
  "descs":"",                                           --职务描述
  "orderCode":1                                         --排序
}
```

#### 3. 删除职务

- 功能描述: 删除一条职务信息 【支持批量操作】
- api地址: /common/SysPosition
- 请求类型: DELETE
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 
```
?_ids=5k7f1ef06728y6016f9d10e91dcr1d39, ...             --主键，多个用,分隔
```
- 请求体: 无

### (十)、文件/附件管理

> 资源名: SysFile

#### 1. 上传文件

- 功能描述: 上传一条文件/附件信息，系统会保存文件到服务器中，并在数据库中记录一条信息 【支持批量操作】
- api地址: /file/upload
- 请求类型: POST
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "refDataId":"",                                       --关联的业务数据主键
  "batch":"",                                           --批次，标识同一次上传的文件
  "file_1":"",                                          --上传的第一个文件
  "secretLevel_1":"",                                   --上传第一个文件的密级
  ...
  "file_x":"",                                          --上传的第x个文件
  "secretLevel_x":""                                    --上传第x个文件的密级
}
```

#### 2. 删除文件

- 功能描述: 删除一条文件/附件信息，系统会同时删除服务器上的文件和数据库中的文件信息数据 【支持批量操作】
- api地址: /file/delete
- 请求类型: DELETE
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 
```
?_ids=5k7f1ef06728y6016f9d10e91dcr1d39, ...             --主键，多个用,分隔
```
- 请求体: 无


#### 3. 下载文件

- 功能描述: 下载文件/附件，一次下载多个文件时，系统会自动压缩为.zip格式 【支持批量操作】
- api地址: /file/download
- 请求类型: GET
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 
```
?_ids=5k7f1ef06728y6016f9d10e91dcr1d39, ...             --主键，多个用,分隔
```
- 请求体: 无

### (十一)、数据字典管理

> 资源名: SysDataDictionary

#### 1. 添加数据字典

- 功能描述: 添加一条数据字典信息 【支持批量操作】
- api地址: /common/SysDataDictionary
- 请求类型: POST
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "code":"",                                            --编码
  "parentId":"",                                        --父编码主键(可为空)
  "caption":"",                                         --显示的文本
  "val":"",                                             --后台操作的值(value)
  "orderCode":1,                                        --排序值
  "isEnabled":1,                                        --是否有效，1是0否
  "comments":""                                         --备注
}
```

#### 2. 修改数据字典

- 功能描述: 修改一条数据字典信息
- api地址: /common/SysDataDictionary
- 请求类型: PUT
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "Id":"",                                              --主键
  "code":"",                                            --编码
  "parentId":"",                                        --父编码主键(可为空)
  "caption":"",                                         --显示的文本
  "val":"",                                             --后台操作的值(value)
  "orderCode":1,                                        --排序值
  "isEnabled":1,                                        --是否有效，1是0否
  "comments":""                                         --备注
}
```

#### 3. 删除数据字典

- 功能描述: 删除一条数据字典信息 【支持批量操作】
- api地址: /common/SysDataDictionary
- 请求类型: DELETE
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 
```
?_ids=5k7f1ef06728y6016f9d10e91dcr1d39, ...             --主键，多个用,分隔
```
- 请求体: 无

## 二、配置系统管理

### (一)、数据库信息管理

> 资源名: CfgDatabase

#### 1. 添加数据库

- 功能描述: 添加一条数据库信息 【支持批量操作】
- api地址: /common/database/add
- 请求类型: POST
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "dbDisplayName":"",                                   --数据库汉字名
  "dbType":"",                                          --数据库类型
  "dbInstanceName":"",                                  --数据库实例名
  "loginUserName":"",                                   --数据库登陆帐号
  "loginPassword":"",                                   --数据库登陆密码
  "dbIp":"",                                            --数据库ip
  "dbPort":""                                           --数据库端口
}
```

#### 2. 修改数据库

- 功能描述: 修改一条数据库信息
- api地址: /common/database/update
- 请求类型: PUT
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "Id":"",                                              --主键
  "dbDisplayName":"",                                   --数据库汉字名
  "dbType":"",                                          --数据库类型
  "dbInstanceName":"",                                  --数据库实例名
  "loginUserName":"",                                   --数据库登陆帐号
  "loginPassword":"",                                   --数据库登陆密码
  "dbIp":"",                                            --数据库ip
  "dbPort":""                                           --数据库端口
}
```

#### 3. 删除数据库

- 功能描述: 删除一条数据库信息 【支持批量操作】
- api地址: /common/database/delete
- 请求类型: DELETE
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 
```
?_ids=5k7f1ef06728y6016f9d10e91dcr1d39, ...             --主键，多个用,分隔
```
- 请求体: 无

#### 4. 测试数据库连接

- 功能描述: 测试系统是否能连接上指定的数据库
- api地址: /common/database/test_link
- 请求类型: DELETE
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "Id":""                                               --主键
}
```

### (二)、项目信息管理

> 资源名: ComProject

#### 1. 添加项目

- 功能描述: 添加一条项目信息 【支持批量操作】
- api地址: /common/project/add
- 请求类型: POST
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "refDatabaseId":"",                                   --关联的数据库id
  "projName":"",                                        --项目名称
  "projCode":"",                                        --项目编码
  "descs":""                                            --项目描述
}
```

#### 2. 修改项目

- 功能描述: 修改一条项目信息
- api地址: /common/project/update
- 请求类型: PUT
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "Id":"",                                              --主键
  "refDatabaseId":"",                                   --关联的数据库id
  "projName":"",                                        --项目名称
  "projCode":"",                                        --项目编码
  "descs":""                                            --项目描述
}
```

#### 3. 删除项目

- 功能描述: 删除一条项目信息 【支持批量操作】
- api地址: /common/project/delete
- 请求类型: DELETE
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 
```
?_ids=5k7f1ef06728y6016f9d10e91dcr1d39, ...             --主键，多个用,分隔
```
- 请求体: 无

### (三)、项目模块管理

> 资源名: ComProjectModule

#### 1. 添加项目模块

- 功能描述: 添加一条项目模块信息 【支持批量操作】
- api地址: /common/project_module/add
- 请求类型: POST
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "parentId":"",                                        --父模块主键
  "name":"",                                            --模块名
  "code":"",                                            --模块编码，项目唯一
  "url":"",                                             --模块url
  "icon":"",                                            --模块图标
  "orderCode":1                                         --排序值
}
```

#### 2. 修改项目模块

- 功能描述: 修改一条项目模块信息
- api地址: /common/project_module/update
- 请求类型: PUT
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "Id":"",                                              --主键
  "parentId":"",                                        --父模块主键
  "name":"",                                            --模块名
  "code":"",                                            --模块编码，项目唯一
  "url":"",                                             --模块url
  "icon":"",                                            --模块图标
  "orderCode":1                                         --排序值
}
```

#### 3. 删除账户

- 功能描述: 删除一条账户信息 【支持批量操作】
- api地址: /common/project_module/delete
- 请求类型: DELETE
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 
```
?_ids=5k7f1ef06728y6016f9d10e91dcr1d39, ...             --主键，多个用,分隔
```
- 请求体: 无

### (四)、表信息管理

> 资源名: ComTabledata

#### 1. 添加表

- 功能描述: 添加一条表信息 【支持批量操作】
- api地址: /common/table/add
- 请求类型: POST
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "name":"",                                            --表的汉字名
  "tableName":"",                                       --表名
  "comments":""                                         --注释
}
```

#### 2. 修改表

- 功能描述: 修改一条表信息
- api地址: /common/table/update
- 请求类型: PUT
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "Id":"",                                              --主键
  "name":"",                                            --表的汉字名
  "tableName":"",                                       --表名
  "comments":""                                         --注释
}
```

#### 3. 删除表

- 功能描述: 删除一条表信息 【支持批量操作】
- api地址: /common/table/delete
- 请求类型: DELETE
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 
```
?_ids=5k7f1ef06728y6016f9d10e91dcr1d39, ...             --主键，多个用,分隔
```
- 请求体: 无

#### 4. 表建模

- 功能描述: 在数据库中create表，并根据表的信息，在系统中自动生成相应的增删改查api 【支持批量操作】
- api地址: /common/table/build_model
- 请求类型: POST
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "Id":""                                               --主键
}
```

### (五)、列信息管理

> 资源名: ComColumndata

#### 1. 添加列

- 功能描述: 添加一条列信息 【支持批量操作】
- api地址: /common/column/add
- 请求类型: POST
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "tableId":"",                                          --关联的表主键
  "name":"",                                             --列的汉字名
  "columnName":"",                                       --列名
  "columnType":"",                                       --字段数据类型
  "length":"",                                           --字段长度
  "precision":"",                                        --数据精度
  "defaultValue":"",                                     --默认值
  "isUnique":"",                                         --是否唯一
  "isNullabled":"",                                      --是否可为空
  "isDataDictionary":"",                                 --是否数据字典
  "dataDictionaryCode":"",                               --数据字典编码
  "orderCode":1,                                         --排序
  "comments":""                                          --注释
}
```

#### 2. 修改列

- 功能描述: 修改一条列信息 【支持批量操作】
- api地址: /common/column/update
- 请求类型: PUT
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "Id":"",                                               --主键
  "tableId":"",                                          --关联的表主键
  "name":"",                                             --列的汉字名
  "columnName":"",                                       --列名
  "columnType":"",                                       --字段数据类型
  "length":"",                                           --字段长度
  "precision":"",                                        --数据精度
  "defaultValue":"",                                     --默认值
  "isUnique":"",                                         --是否唯一
  "isNullabled":"",                                      --是否可为空
  "isDataDictionary":"",                                 --是否数据字典
  "dataDictionaryCode":"",                               --数据字典编码
  "orderCode":1,                                         --排序
  "comments":""                                          --注释
}
```

#### 3. 删除列

- 功能描述: 删除一条列信息 【支持批量操作】
- api地址: /common/column/delete
- 请求类型: DELETE
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 
```
?_ids=5k7f1ef06728y6016f9d10e91dcr1d39, ...             --主键，多个用,分隔
```
- 请求体: 无

### (六)、sql脚本管理

> 资源名: ComSqlScript

#### 1. 添加sql脚本

- 功能描述: 添加一条sql脚本信息 【支持批量操作】
- api地址: /common/sql/add
- 请求类型: POST
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "sqlScriptCaption": "",                               --sql脚本汉字名
  "sqlScriptResourceName": "",                          --sql脚本资源名称
  "sqlScriptContent": "",                               --sql脚本内容
  "isAnalysisParameters":""                             --是否解析参数，1是0否
}
```

#### 2. 修改sql脚本

- 功能描述: 修改一条sql脚本信息
- api地址: /common/sql/update
- 请求类型: PUT
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "Id":"",                                              --主键
  "sqlScriptCaption": "",                               --sql脚本汉字名
  "sqlScriptResourceName": "",                          --sql脚本资源名称
  "sqlScriptContent": "",                               --sql脚本内容
  "isAnalysisParameters":""                             --是否解析参数，1是0否
}
```

#### 3. 删除sql脚本

- 功能描述: 删除一条sql脚本信息 【支持批量操作】
- api地址: /common/sql/delete
- 请求类型: DELETE
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 
```
?_ids=5k7f1ef06728y6016f9d10e91dcr1d39, ...             --主键，多个用,分隔
```
- 请求体: 无

### (七)、sql脚本参数管理

> 资源名: ComSqlScriptParameter

#### 1. 添加sql脚本参数

- 功能描述: 添加一条sql脚本参数信息 【支持批量操作】
- api地址: /common/sql_parameter/add
- 请求类型: POST
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "parameterName":"",                                   --参数名
  "length":"",                                          --参数的值长度
  "parameterDataType":"",                               --参数值的数据类型
  "defaultValue":"",                                    --默认值
  "orderCode":1                                         --排序
}
```

#### 2. 修改sql脚本参数

- 功能描述: 修改一条sql脚本参数信息
- api地址: /common/sql_parameter/update
- 请求类型: PUT
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 无
- 请求体: 
```
{
  "Id":"",                                              --主键
  "parameterName":"",                                   --参数名
  "length":"",                                          --参数的值长度
  "parameterDataType":"",                               --参数值的数据类型
  "defaultValue":"",                                    --默认值
  "orderCode":1                                         --排序
}
```

#### 3. 删除sql脚本参数

- 功能描述: 删除一条sql脚本参数信息 【支持批量操作】
- api地址: /common/sql_parameter/delete
- 请求类型: DELETE
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 
```
?_ids=5k7f1ef06728y6016f9d10e91dcr1d39, ...             --主键，多个用,分隔
```
- 请求体: 无




























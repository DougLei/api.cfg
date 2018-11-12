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
  "loginPwd":"1"					          --登陆密码
}
```
返回值: 
```
管理员或系统开发人员会返回: 所有系统模块信息集合
  {
      "data": {
          "token": "c14582c5551f4ae093c54ecdb07a6764",
          "modules": {
              递归显示所有模块信息
              ...
          },
          ...
      },
      "isSuccess": true,
      "message": null,
      "status": 200
  }
一般用户会返回: 具有操作权限的模块信息集合
  {
      "data": {
          "token": "c14582c5551f4ae093c54ecdb07a6764",
          "modules": {
              递归显示当前用户有权限的模块信息
              ...
          },
          ...
      },
      "isSuccess": true,
      "message": null,
      "status": 200
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
  "userStatus":"",                           --人员状态:0:其他(默认)、1.在职、2.离职、3.休假
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
  "userStatus":"",                           --人员状态:0:其他(默认)、1.在职、2.离职、3.休假
  "monthSalar":"",                           --月薪
  "workNo":"",                               --工号
  "secretLevel":"",                          --密级
  "descs":"",                                --描述
  "deptId":"",                               --部门id
  "positionId":"",                           --岗位id
  "isCreateAccount":"",                      --是否创建账户信息，1是0否
  "isSyncLoginName":""                       --如果修改了用户工号，是否同步修改账户的登录名，这个要配合上面的isCreateAccount一起使用，即当isCreateAccount=1时，该值=1才有效，1是0否
}
```

#### 3. 删除用户

- 功能描述: 逻辑删除一条用户信息，同时会逻辑删除关联的账户信息，以及所有关联关系，目前包括所属的部门、所属的职务、所属的角色、所属的用户组 【支持批量操作】
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
- api地址: /common/user/account/open
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

#### 5. 关闭账户

- 功能描述: 和开通账户相反的操作 【支持批量操作】
- api地址: /common/user/account/close
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

#### 6. 重置用户关联账户的信息

- 功能描述: 重置用户关联账户的信息，即将用户的工号、手机号、邮箱三个字段的值，重新更新到账户的帐号、手机号、邮箱三个字段的值，同时重置账户的登陆密码为初始密码
- api地址: /common/user/account/reset
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
  "Id":""                                               --主键
}
```

#### 7. 修改用户关联账户的登陆密码

- 功能描述: 修改用户关联账户的登陆密码
- api地址: /common/user/account/pwd/update
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
  "oldPassword":"",                                     --旧密码
  "password":"",                                        --新密码
  "confirmPassword":""                                  --确认密码
}
```

#### 8. 重置用户关联账户的登陆密码

- 功能描述: 重置用户关联账户的登陆密码，改为初始密码
- api地址: /common/user/account/pwd/reset
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
  "Id":""                                               --主键
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
  "loginName":"",                                    --登录名
  "loginPwd":"",                                     --登陆密码
  "tel":"",                                          --电话号码
  "email":"",                                        --邮箱
  "type":"",                                         --账户类型: 1.管理员、2.普通(默认)、3.开发者
  "status":"",                                       --账户状态: 1.启用(默认)、2.禁用
  "validDate":""                                     --有效期
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
  "Id":"",                                           --主键
  "loginName":"",                                    --登录名
  "tel":"",                                          --电话号码
  "email":"",                                        --邮箱
  "type":"",                                         --账户类型: 1.管理员、2.普通(默认)、3.开发者
  "status":"",                                       --账户状态: 1.启用(默认)、2.禁用
  "validDate":""                                     --有效期
}
```

#### 3. 删除账户

- 功能描述: 逻辑删除一条账户信息 【支持批量操作】
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
  "isEnabled":""                                        --是否启用: 1是0否
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
  "isEnabled":""                                        --是否启用: 1是0否
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
  "objId":"",                    --主体id：比如用户id，账户id，角色id，部门id，岗位id，用户组id等
  "objType":"",                  --主体类型：比如用户user，账户account，角色role，部门dept，岗位position，用户组userGroup等
  "refResourceId":"",            --关联的资源id: 比如某个模块的id，某个功能的id等
  "refResourceCode":"",          --关联的资源code，与refResourceId数据对应的code，是全项目唯一
  "refParentResourceId":"",      --关联的父资源id，同refResourceId
  "refParentResourceCode":"",    --关联的父资源code，同refResourceCode
  "refResourceType":"",          --关联的资源类型：1:模块module、2:tab、3:功能oper  等
  "isVisibility":"",             --是否可见(是否可读): 1是0否
  "isOper":""                    --是否可操作(是否可写): 1是0否
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
  "Id":"",                       --主键
  "objId":"",                    --主体id：比如用户id，账户id，角色id，部门id，岗位id，用户组id等
  "objType":"",                  --主体类型：比如用户user，账户account，角色role，部门dept，岗位position，用户组userGroup等
  "refResourceId":"",            --关联的资源id: 比如某个模块的id，某个功能的id等
  "refResourceCode":"",          --关联的资源code，与refResourceId数据对应的code，是全项目唯一
  "refParentResourceId":"",      --关联的父资源id，同refResourceId
  "refParentResourceCode":"",    --关联的父资源code，同refResourceCode
  "refResourceType":"",          --关联的资源类型：1:模块module、2:tab、3:功能oper  等
  "isVisibility":"",             --是否可见(是否可读): 1是0否
  "isOper":""                    --是否可操作(是否可写): 1是0否
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

#### 4. 根据code计算权限

- 功能描述: 计算当前用户，指定code的功能权限以及子权限集合
- api地址: /common/permission
- 请求类型: GET
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 
```
?objcode = sysManager                --要计算权限的code值
&recursive = true [true/false]       --是否递归，计算其子权限集合，值为true或false，默认为false
&deep = -1 [-1/任意数字]              --递归计算子权限的深度，默认为0，-1表示查询到底
```
- 请求体: 无
- 返回值: 
```
管理员或系统开发人员会返回: ALL标识具有权限
  {
      "data": {
          "refResourceId": "ALL",           
          "refResourceType": "ALL",         
          "refResourceCode": "ALL",         
          ...
      },
      "isSuccess": true,
      "message": null,
      "status": 200
  }
一般用户会返回: 具体的权限对象集合
  {
      "data": {
          "objId": "xxx",
          "refResourceId": "xxx",
          "isOper": 1,
          "refResourceType": "xxx",
          "refParentResourceId": "xxx",
          "secretLevels": 1,
          "objType": "xxx",
          "isVisibility": 1,
          "Id": "xxx",
          "refResourceCode": "xxx",
          "children": [...],
          ...
      },
      "isSuccess": true,
      "message": null,
      "status": 200
  }
```

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
  "orgId":"",                                          --所属组织主键，可为空
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
  "orgId":"",                                          --所属组织主键，可为空
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
  "isImport":"",                                        --是否是导入，1是0否
  "file_1":"",                                          --上传的第一个文件
  "secretLevel_1":"",                                   --上传第一个文件的密级
  ...
  "file_x":"",                                          --上传的第x个文件
  "secretLevel_x":""                                    --上传第x个文件的密级
}
```
返回值: 
```
{
    "data": [
        {
            "refDataId": "fdsafewcadsfqwefwq",
            "downloadCount": null,
            "code": "ddd85d62cbf540f3aa019d7570cfbab8",
            "projectId": "90621e37b806o6fe8538c5eb782901bb",
            "suffix": "txt",
            "saveType": "service",
            "size": "1796",
            "lastUpdateDate": "2018-08-15 11:37:46",
            "savePath": "D:\\uploadFile\\2018-08-15\\ddd85d62cbf540f3aa019d7570cfbab8.txt",
            "content": null,
            "lastUpdateUserId": "16ed21bd7a7a41f5bea2ebaa258908cf",
            "customerId": "unknow",
            "batch": "b7ed3f39797e45ee800ddcaf9e33093f",
            "Id": "93560c231ce343f5a0c828c53a1d1da6",
            "secretLevel": 4,
            "createDate": "2018-08-15 11:37:46",
            "actName": "测试上传的文本文件.txt",
            "createUserId": "16ed21bd7a7a41f5bea2ebaa258908cf"
        },
        ...
    ],
    "isSuccess": true,
    "message": null,
    "status": 200
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
- 返回值: 
```
下载出现异常时: message返回错误原因
  {
      "data": null,
      "isSuccess": false,
      "message": "下载文件时，传入的_ids参数值不能为空",
      "status": 200
  }
成功下载时: 返回文件流
  浏览器会启动自身的下载功能
```

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
  "isEnabled":1,                                        --是否有效: 1是0否
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
  "isEnabled":1,                                        --是否有效: 1是0否
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

### (十二)、用户组管理

> 资源名: SysUserGroup

#### 1. 添加用户组

- 功能描述: 添加一条用户组信息 【支持批量操作】
- api地址: /common/SysUserGroup
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
  "name":"",                                            --用户组名称
  "code":"",                                            --用户组编码
  "orderCode":1,                                        --排序值
  "descs":"",                                           --描述
  "isEnabled":1                                         --是否有效: 1是0否
}
```

#### 2. 修改用户组

- 功能描述: 修改一条用户组信息
- api地址: /common/SysUserGroup
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
  "name":"",                                            --用户组名称
  "code":"",                                            --用户组编码
  "orderCode":1,                                        --排序值
  "descs":"",                                           --描述
  "isEnabled":1                                         --是否有效: 1是0否
}
```

#### 3. 删除用户组

- 功能描述: 删除一条用户组信息 【支持批量操作】
- api地址: /common/SysUserGroup
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

### (十三)、用户组明细管理

> 资源名: SysUserGroupDetail

#### 1. 添加用户组明细

- 功能描述: 添加一条用户组明细信息 【支持批量操作】
- api地址: /common/SysUserGroupDetail
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
  "userGroupId":"",                                     --用户组主键
  "userId":""                                           --人员主键
}
```

#### 2. 修改用户组明细

- 功能描述: 修改一条用户组明细信息
- api地址: /common/SysUserGroupDetail
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
  "userGroupId":"",                                     --用户组主键
  "userId":""                                           --人员主键
}
```

#### 3. 删除用户组明细

- 功能描述: 删除一条用户组明细信息 【支持批量操作】
- api地址: /common/SysUserGroupDetail
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

### (十四)、消息推送管理

> 资源名: SysPushMessageInfo

#### 1. 消息推送功能

- 功能描述: 消息推送 【支持批量操作】
- api地址: /common/message/push
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
  "toUserId":"",                                        --接收的用户id，多个用,隔开
  "msgType":1,                                          --消息类型，不能为空，1:待办消息、2.通知消息、...
  "sendType":0,                                         --推送类型，不能为空，0:直接推送，即直接将消息原原本本推送给客户端
  "message":""                                          --推送的消息内容
}
```

#### 2. 消息阅读功能

- 功能描述: 阅读一条消息
- api地址: /common/message/read
- 请求类型: GET
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 
```
?Id=5k7f1ef06728y6016f9d10e91dcr1d39                    --主键
```
- 请求体: 无

#### 3. 修改消息的阅读状态功能

- 功能描述: 修改消息的阅读状态，改为已读或未读 【支持批量操作】
- api地址: /common/message/read_status/update
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
  "Id":"xxxxx",                                        --主键
  "isReaded":1                                         --是否已读，1是0否
}
```

### (十五)、excel管理

> 资源名: SysExcelImportExportInfo

#### 1. excel导入

- 功能描述: 导入excel 【支持批量操作】
- api地址: /common/excel/import
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
  "fileId":"",                                          --要导入的excel文件id
  "resourceName":"SysUser"                              --要导入的资源名
}
```

#### 2. 生成excel导入模版

- 功能描述: 生成excel导入模版
- api地址: /common/excel/import_template/create
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
  ".......":"........."                                 --......
}

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
  "displayName":"",                                     --数据库汉字名
  "type":"",                                            --数据库类型
  "instanceName":"",                                    --数据库实例名
  "loginUserName":"",                                   --数据库登陆帐号
  "loginPassword":"",                                   --数据库登陆密码
  "ip":"",                                              --数据库ip
  "port":""                                             --数据库端口
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
  "displayName":"",                                     --数据库汉字名
  "type":"",                                            --数据库类型
  "instanceName":"",                                    --数据库实例名
  "loginUserName":"",                                   --数据库登陆帐号
  "loginPassword":"",                                   --数据库登陆密码
  "ip":"",                                              --数据库ip
  "port":""                                             --数据库端口
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
- api地址: /common/database/test/link
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

> 资源名: CfgProject

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
  "name":"",                                            --项目名称
  "code":"",                                            --项目编码
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
  "name":"",                                            --项目名称
  "code":"",                                            --项目编码
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

> 资源名: CfgProjectModule

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

#### 3. 删除项目模块

- 功能描述: 删除一条项目模块信息 【支持批量操作】
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

> 资源名: CfgTable

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
  "type":"",                                            --表类型：1:单表、2、表类型/游标类型
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
  "type":"",                                            --表类型：1:单表、2、表类型/游标类型
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

- 功能描述: 在数据库中create表，并根据表的信息，在系统中自动生成相应的增删改查api，如果已存在，会对表进行的元数据进行修改操作(alter table modify oper...) 【支持批量操作】
- api地址: /common/table/model/create
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

#### 5. 表取消建模

- 功能描述: 在数据库中drop表，并删除相关的资源信息、hbm信息、修改表的建模状态、字段的状态、同时将已删除的字段删除掉 【支持批量操作】
- api地址: /common/table/model/drop
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

> 资源名: CfgColumn

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
  "columnType":"",                                       --字段数据类型: 字符串: string、布尔值: boolean、整型: integer、浮点型: double、日期: date、字符大字段: clob、二进制大字段: blob
  "length":"",                                           --字段长度
  "precision":"",                                        --数据精度
  "defaultValue":"",                                     --默认值
  "isUnique":"",                                         --是否唯一: 1是0否
  "isNullabled":"",                                      --是否可为空: 1是0否
  "isDataDictionary":"",                                 --是否数据字典: 1是0否
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
  "columnType":"",                                       --字段数据类型: 字符串: string、布尔值: boolean、整型: integer、浮点型: double、日期: date、字符大字段: clob、二进制大字段: blob
  "length":"",                                           --字段长度
  "precision":"",                                        --数据精度
  "defaultValue":"",                                     --默认值
  "isUnique":"",                                         --是否唯一: 1是0否
  "isNullabled":"",                                      --是否可为空: 1是0否
  "isDataDictionary":"",                                 --是否数据字典: 1是0否
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

> 资源名: CfgSql

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
  "caption": "",                                        --sql脚本汉字名
  "resourceName": "",                                   --sql脚本资源名称
  "contents": "",                                       --sql脚本内容
  "requestMethod":"",                                   --请求资源的方法,get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none表示都不支持
  "isAnalysisParameters":"",                            --是否解析参数: 1是0否
  "isImmediateCreate":""                                --是否立即创建存储过程、视图等
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
  "caption": "",                                        --sql脚本汉字名
  "resourceName": "",                                   --sql脚本资源名称
  "contents": "",                                       --sql脚本内容
  "requestMethod":"",                                   --请求资源的方法,get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none表示都不支持
  "isAnalysisParameters":"",                            --是否解析参数: 1是0否
  "isImmediateCreate":""                                --是否立即创建存储过程、视图等
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

#### 4. 创建sql脚本对象

- 功能描述: 在数据库中创建存储过程、视图等，如果已存在，会提示已经存在 【支持批量操作】
- api地址: /common/sql/object/create
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

#### 5. 删除sql脚本对象

- 功能描述: 在数据库中删除存储过程、视图等 【支持批量操作】
- api地址: /common/sql/object/drop
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

### (七)、sql脚本参数管理

> 资源名: CfgSqlParameter

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
  "name":"",                                            --参数名
  "length":"",                                          --参数的值长度
  "dataType":"",                                        --参数值的数据类型: 字符串: string、布尔值: boolean、整型: integer、浮点型: double、日期: date
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
  "name":"",                                            --参数名
  "length":"",                                          --参数的值长度
  "dataType":"",                                        --参数值的数据类型: 字符串: string、布尔值: boolean、整型: integer、浮点型: double、日期: date
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

### (八)、sql结果集信息管理

> 资源名: CfgSqlResultset

#### 1. 添加sql结果集信息

- 功能描述: 添加一条sql结果集信息信息 【支持批量操作】
- api地址: /common/CfgSqlResultset
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
  "sqlScriptId":"",                                     --关联的sql脚本id
  "sqlParameterId":"",                                  --关联的sql脚本参数id，oracle通过输出参数返回结果集,[oracle使用字段]
  "batchOrder":"",                                      --结果集批次顺序，sqlserver直接返回结果集，所以这里用批次顺序来区分返回的结果集，第几个结果集[sqlserver使用字段]
  "name":"",                                            --结果集名，sqlserver直接返回结果集，这个用来配置每个结果集的名称，前端通过该key来取，如果没有配置，则使用dataSet1、dataSet2...自增[sqlserver使用字段]
  "columnName":"",                                      --列名
  "dataType":"",                                        --存储过程传入表参数时，需要知道每个列的数据类型[sqlserver使用字段]
  "propName":"",                                        --属性名
  "orderCode":"",                                       --排序值
  "inOut":1                                             --标识是传入的结果集信息，还是传出的结果集信息，in=1、out=2
}
```

#### 2. 修改sql结果集信息

- 功能描述: 修改一条sql结果集信息信息
- api地址: /common/CfgSqlResultset
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
  "sqlScriptId":"",                                     --关联的sql脚本id
  "sqlParameterId":"",                                  --关联的sql脚本参数id，oracle通过输出参数返回结果集,[oracle使用字段]
  "batchOrder":"",                                      --结果集批次顺序，sqlserver直接返回结果集，所以这里用批次顺序来区分返回的结果集，第几个结果集[sqlserver使用字段]
  "name":"",                                            --结果集名，sqlserver直接返回结果集，这个用来配置每个结果集的名称，前端通过该key来取，如果没有配置，则使用dataSet1、dataSet2...自增[sqlserver使用字段]
  "columnName":"",                                      --列名
  "dataType":"",                                        --存储过程传入表参数时，需要知道每个列的数据类型[sqlserver使用字段]
  "propName":"",                                        --属性名
  "orderCode":"",                                       --排序值
  "inOut":1                                             --标识是传入的结果集信息，还是传出的结果集信息，in=1、out=2
}
```

#### 3. 删除sql结果集信息

- 功能描述: 删除一条sql结果集信息信息 【支持批量操作】
- api地址: /common/CfgSqlResultset
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

----

## 三、系统其他接口

### (一)、工具类接口

#### 1. 监听hibernate类元数据

- 功能描述: 监听hibernate类元数据
- api地址: /common/hibernate/classmetadata/monitor
- 请求类型: GET
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 
```
?resourceNames=SysUser, ...                             --资源名，多个用,分隔；如果不传资源名，则默认查询hibernate中的所有类的元数据
```
- 请求体: 无

#### 2. 获取指定资源的信息

- 功能描述: 获取指定资源的信息，包括资源的结构信息
- api地址: /common/resource/info/search
- 请求类型: GET
- 请求头:
```
{
  "_token":"5k7f1ef06728y6016f9d10e91dcr1d37"           --登录时返回的token值
}
```
- 请求url参数: 
```
?name=SysUser[或SYS_USER 或 sys_user]                    --资源名，或表名(表名不区分大小写)，该参数必须有，且值只能写一个资源名或表名
```
- 请求体: 无























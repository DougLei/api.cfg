# api功能说明文档

## 一、common控制器
> 实现对表资源、SQL脚本资源、code资源的调用

### (一)、GET请求
> 进行数据库的查询操作，即 [select]

	路由格式: /common/资源名
	说明: 查询指定资源的数据集合
	返回值: 数据集合
	支持调用的资源: 表资源、SQL脚本资源

	路由格式: /common/资源名/资源id
	说明: 查询指定资源和id的数据对象
	返回值: 数据对象
	支持调用的资源: 表资源、SQL脚本资源

	路由格式: /common/counter/资源名
	说明: 查询指定资源的数据量
	返回值: 数据的总数量
	支持调用的资源: 表资源、SQL脚本资源

	路由格式: /common/父资源名/父资源id或父资源筛选条件/子资源名
	说明: 查询指定父资源下对应的子资源数据集合
	返回值: 数据集合
	支持调用的资源: 表资源

	路由格式: /common/counter/父资源名/父资源id或父资源筛选条件/子资源名
	说明: 查询指定父资源下对应的子资源数据量
	返回值: 数据的总数量
	支持调用的资源: 表资源

	路由格式: /common/资源名/资源id或资源筛选条件/资源名
	说明: 递归查询指定资源的数据
	返回值: 数据集合
	支持调用的资源: 表资源、SQL脚本资源

	路由格式: /common/values/资源名/属性名
	说明: 查询指定资源的字段的数据字典值集合
	返回值: 数据集合
	支持调用的资源: 表资源

	路由格式: /common/action/资源名/方法名
	说明: 执行指定资源中的业务方法
	返回值: 不定，一般返回传入的数据
	支持调用的资源: 代码资源

	路由格式: /common/codeUri.../...
	说明: 执行指定的业务方法
	返回值: 不定，一般返回传入的数据
	支持调用的资源: 代码资源

#### 1. 内置功能参数
> 系统内置，在api地址中使用
> 可以根据实际需求，将功能参数进行任意组合使用

##### (1). 字段查询
	_select: 指定返回的查询结果的属性，默认为全部查询；多个用,分隔
	_resultType: 指定返回的查询结果值的方式，默认值为anonymous
	_split: 指定返回的查询结果值间的分隔符，和_resultType搭配使用才有效果

> _resultType的值包括:

	值: anonymous
	说明: 即默认值，对查询结果没有影响

	值: keyValues
	说明: 查询结果的值，以key=value的形式返回，其中=可以通过_split指定，默认为=

	值: strings
	说明: 查询结果的每个对象的所有值，通过,连接成一个字符串返回，其中,可以通过_split指定，默认为,

	值: text
	说明: 查询结果的所有对象的所有值，通过,和\n连接成一个字符串返回，其中,可以通过_split指定，默认为,

##### (2). 分页查询
	_limit: 指定一次限制显示多少行
	_start: 指定开始的行数

	_rows: 指定一页显示多少行
	_page: 指定第几页

> 两种分页查询方式，任意选择一种调用
> _limit + _start 的优先级高于 _rows + _page

##### (3). id定位
	_focusedId: 数据通过该参数传递到后台后，后台会查询对应_add的id数据，展示到返回列表的最前

> 该参数一般和分页查询搭配使用。目前系统也只支持该参数和分页查询搭配使用

##### (4). 生成导出文件
	_isCreateExport: 标识是否生成导出文件，值为true、false。如果不传值，或值不是true，，则该功能不启用
	_exportFileSuffix: 生成导出文件的后缀，用来判断导出的文件类型，excel、xml等

> 配合BuiltinPagerMethodProcesser中的，_rows或_limit参数使用，这两个中的任意一个参数指定一次导出的数据数量，提高系统性能
> _rows或_limit参数的搭配参数(_page或_start)可以随便传值，但是必须传值，建议传值都为0即可

##### (5). 递归查询
	_recursive: 标识是否递归查询，值为true、false。如果不传值，或值不是true，，则该功能不启用
	_deep: 递归查询的深度，默认为2。值为-1，标识递归查询到底

> 递归查询出来的数据集合，会自动挂接到其父级的对象中，属性名为"children"

##### (6). 查询排序
	_sort: 指定查询结果根据哪些属性排序，以及排列的顺序。多个属性排序，用,分隔开

> 示例: ?_sort=age ASC, createDate DESC
> 相应的SQL: ORDER BY age ASC, create_date DESC

##### (7). 父子关联查询
	_simpleModel: 是否以简单模式进行父子查询，值为true、false。如果不传值，默认为false
	_refPropName: 指定子资源的属性中，关联父资源id的属性名，默认值为parentId。只有_simpleModel=true时才有效
	
> ① 父子关联第一种方式是: 子资源的某个属性，关联父资源id。属于_simpleModel=true，这时需要指定_refPropName的值，系统会自动组装对应的SQL语句。如下:
```
SELECT sub_columns FROM sub_resource WHERE _refPropName = parent_resource_id
``` 

> ② 父子关联第二种方式是: 通过关系表关联父子资源，属于_simpleModel=false，这时需要通过关系表查询子资源数据。如下:
```
SELECT sub_columns FROM sub_resource s, relation_resource r 
        WHERE s.Id = r.rightId AND r.leftId = parent_resource_id 
```
> 目前系统中更多的都是使用第二种方式

##### (8). 子列表查询
	_subResourceName: 指定子资源的名称
	_refPropName: 指定子资源的属性中，关联父资源id的属性名，默认值为parentId
	_subSort: 指定子资源的排序规则，使用方式和_sort相同

> 查询出来的子资源集合，会自动挂接到查询结果的对象中，属性名为"children"

##### (9). 查询条件
> 任何查询结果中涉及到的属性，都可以作为查询条件
> 示例: ?name=哈哈&age=23
> 相应的SQL: where name = '哈哈' and age = 23
> 对于相对复杂的条件，系统还提供了以下内置的条件函数

	函数名: btn/between
	函数功能: 区间查询
	支持取反: 否
	示例: ?date=btn('2018-1','2018-2')
	相应的SQL: WHERE date BETWEEN '2018-1' AND '2018-2'

	函数名: ctn/contains
	函数功能: 模糊查询
	支持取反: 是
	示例: ?name=ctn('%哈哈')
	相应的SQL: WHERE name LIKE '%哈哈'
	
	函数名: in/any
	函数功能: 批量查询
	支持取反: 是
	示例: ?id=in(1,2,3)
	相应的SQL: WHERE id IN ('1','2','3')
	
	函数名: eq
	函数功能: 等于
	支持取反: 是
	示例: ?name=eq(哈哈)
	相应的SQL: WHERE name = '哈哈'
	
	函数名: ne
	函数功能: 不等于
	支持取反: 是
	示例: ?name=ne(哈哈)
	相应的SQL: WHERE name != '哈哈'
	
	函数名: ge
	函数功能: 大于或等于
	支持取反: 否
	示例: ?age=ge(20)
	相应的SQL: WHERE age >= 20
	
	函数名: gt
	函数功能: 大于
	支持取反: 否
	示例: ?age=gt(20)
	相应的SQL: WHERE age > 20
		
	函数名: le
	函数功能: 小于或等于
	支持取反: 否
	示例: ?age=le(20)
	相应的SQL: WHERE age <= 20
		
	函数名: lt
	函数功能: 小于
	支持取反: 否
	示例: ?age=lt(20)
	相应的SQL: WHERE age < 20

> ① 取反的关键字为"!"，例如: ?id=!in(1,2,3)，相应的SQL为: WHERE id NOT IN ('1','2','3')
> ② 查询条件的值，如果包含特殊字符，可以通过''或""包裹起来，系统内部会过滤掉每个值最外层的一对''或""

### (二)、POST请求
> 进行数据库的保存操作，即 [insert]

	路由格式: /common/资源名
	说明: 保存指定资源的数据对象
	返回值: 被保存的数据对象
	支持调用的资源: 表资源、SQL脚本资源

	路由格式: /common/codeUri.../...
	说明: 执行指定的业务方法
	返回值: 不定，一般返回传入的数据
	支持调用的资源: code资源

### (三)、PUT请求
> 进行数据库的修改操作，即 [update]

	路由格式: /common/资源名
	说明: 修改指定资源的数据对象
	返回值: 被修改的数据对象

	路由格式: /common/codeUri.../...
	说明: 执行指定的业务方法
	返回值: 不定，一般返回传入的数据

### (四)、DELETE请求
> 进行数据库的删除操作，即 [delete]

	路由格式: /common/资源名
	说明: 删除指定资源的数据对象
	返回值: 被删除的数据id集合
	支持调用的资源: 表资源、SQL脚本资源

	路由格式: /common/codeUri.../...
	说明: 执行指定的业务方法
	返回值: 不定，一般返回传入的数据
	支持调用的资源: code资源

> 参数: ?_ids=要删除的数据id，多个用,分隔

## 二、file控制器
> 实现对文件的操作

### (一)、GET请求
> 进行文件下载操作，即 [download]

	路由格式: /file/download
	说明: 下载文件
	返回值: 下载的文件【支持批量下载】

> 参数: ?_ids=要下载的文件数据id，多个用,分隔

### (二)、POST请求
> 进行文件上传操作，即 [upload]

	路由格式: /file/upload
	说明: 上传文件
	返回值: 被保存的文件数据对象

### (三)、DELETE请求
> 进行文件删除操作，即 [delete]

	路由格式: /file/delete
	说明: 删除文件
	返回值: 被删除的文件数据id集合

> 参数: ?_ids=要删除的文件数据id，多个用,分隔

## 三、系统其他功能

### (一)、sql脚本的内置参数
```
_Id                                                     --主键
_currentSqlDate                                         --当前时间 *
_currentCustomerId                                      --当前租户id
_currentProjectId                                       --当前项目id
_currentAccountId                                       --当前账户id
_currentAccountName                                     --当前账户名
_currentUserId                                          --当前用户id
_currentSecretLevel                                     --当前用户密级
_currentOrgId                                           --当前用户所属组织id，如果是在sql脚本中使用该参数时，请使用in操作符，并用()括起来，例org_id in ($_currentOrgId$)
_currentDeptId                                          --当前用户所属部门id，同上
_currentPositionId                                      --当前用户所属岗位id，同上
_currentRoleId                                          --当前用户所属角色id，同上
_currentUserGroupId                                     --当前用户所属用户组id，同上
_currentSecretLevel										--当前用户密级
```

### (二)、表资源的内置参数
```
_Id                                                     --主键
_currentDate                                            --当前时间 *
_currentCustomerId                                      --当前租户id
_currentProjectId                                       --当前项目id
_currentAccountId                                       --当前账户id
_currentAccountName                                     --当前账户名
_currentUserId                                          --当前用户id
_currentSecretLevel                                     --当前用户密级
_currentOrgId                                           --当前用户所属组织id
_currentDeptId                                          --当前用户所属部门id
_currentPositionId                                      --当前用户所属岗位id
_currentRoleId                                          --当前用户所属角色id，同上
_currentUserGroupId                                     --当前用户所属用户组id，同上
_currentSecretLevel										--当前用户密级
```

### (三)、消息推送系统接口
	api: ws://ipAddress:8091/api.push.message/websocket/message/push/customerId/clientIdentity

> api 内容详解:

	ipAddress
	说明: 消息推送服务器的ip地址

	customerId
	说明: 可以连接消息推送服务器的客户id，用户名[SmartOne]，密码[1QaZ2wSx,.]，(不包括[])在连接消息推送服务器时，将md5(用户名,密码)的结果值，放到customerId的位置，去连接消息推送服务器

	clientIdentity
	说明: 将业务系统用户id的值，放到clientIdentity的位置，去连接消息推送服务器

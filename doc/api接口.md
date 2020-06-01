# 国际化服务api

> 测试环境: http://192.168.1.111:10002/

> 生产环境: 未知

## 1 国际化消息管理

### 1.1 添加国际化消息

> 功能描述

```  
添加国际化消息
```

> 请求URL: 

```
/i18n/message/add
```

> 请求类型
``` 
POST
```

> 请求头参数

参数名|参数类型|是否必须|参数描述
-|-|-|-
_token|string|true|登录时系统返回的token值
_log|string|true|提供操作日志相关的信息, json格式字符串

```
{
  "_token":"string",
  "_log":"string"
}
```

> 请求体参数 

参数名|参数类型|长度范围|精度范围|是否必须|默认值|参数描述
-|-|-|-|-|-|-
CODE|string|-|-|true|-|国际化消息编码
LANUAGE|string|-|-|true|-|国际化消息对应的语言
MESSAGE|string|-|-|true|-|国际化消息内容
PRIORITY|byte|-|-|false|0|国际化消息的优先级, 值越大, 优先级越高

```
{
  "CODE":"string",
  "LANUAGE":"string",
  "MESSAGE":"string",
  "PRIORITY":0
}
```

> 响应体参数 

参数名|参数类型|参数描述
-|-|-
success|byte|响应结果, 1成功, 2部分成功, 0失败, -1异常
validation|object|返回验证失败的对象, 如果请求的是对象, 则这里为对象或null, 如果请求的是集合, 则这里为数组或空数组
data|object|返回保存成功的对象, 如果请求的是对象, 则这里为对象或null, 如果请求的是集合, 则这里为数组或空数组
error|object|返回保存失败的对象, 如果请求的是对象, 则这里为对象或null, 如果请求的是集合, 则这里为数组或空数组
exception|object|返回响应结果为异常(-1)时的信息
warn|string|返回系统警告信息

```
{
  "success":1,
  "validation":{
    "data":{

    },
    "code":"",
    "field":"",
    "message":{

    },
    "originMessage":""
  },
  
}
```

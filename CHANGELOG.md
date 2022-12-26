# Changelog

## [v1.0.8](https://github.com/ToQuery/clever-framework/tree/v1.0.8) (2019-12-24)

### fix bug:

- 修复jwt验证错误
- 增加针对Spring默认错误返回信息的处理
- 升级至Spring Boot 3


## [v1.0.7](https://github.com/ToQuery/clever-framework/tree/v1.0.7) (2019-10-17)

### fix bug:

- 删除 clever-framework-ueditor 模块，建议使用 tiny mec 编辑器
- 增加 minio 对象存储服务，修改优化配置文件提示
- 优化 files 文件服务

## [v1.0.6](https://github.com/ToQuery/clever-framework/tree/v1.0.6) (2019-10-17)

### fix bug:

- 删除角色级联删除用户
- root admin 用户，角色删除增加限制
- 修改构建树时，空指针异常
- 优化业务日志处理流程
- 拆分app dao标签处理

## [v1.0.5](https://github.com/ToQuery/clever-framework/tree/v1.0.5) (2019-07-01)

### New Features:

- 引入百度富文本编辑器模块
- 修改代码生成模板
- 增加数据日志审计功能
- 修改front、log模块自动装配设置
- 新增system模块
- 新增配置文件优化


## [v1.0.4](https://github.com/ToQuery/clever-framework/tree/v1.0.2) (2019-06-19)

### New Features:

- 引入前端模块，将前端代码打包至jar文件中
- 增加文件上传存储相关功能
- 拆分安全配置中的jwt签名认证
- 修改web，增肌公共基础类，简化业务代码


## [v1.0.3](https://github.com/ToQuery/clever-framework/tree/v1.0.2) (2019-06-15)

### New Features:

- 升级SpringBoot版本
- 增加系统配置功能
- 优化代码，修改自动配置扫描


## [v1.0.2](https://github.com/ToQuery/clever-framework/tree/v1.0.2) (2019-04-30)

### New Features:

- 移除`pac4j`模块


## [v1.0.1](https://github.com/ToQuery/clever-framework/tree/v1.0.1) (2019-01-16)

### New Features:

- 通过注解方式启用自定义JPA方法
- 添加`common`模块、`pac4j`模块
- 增加 `@JpaParam` | `@MybatisParam` 注解别名
- 抽离数据过滤的`Operator`和`Connector`类
- 增加常用时间处理类
- 增加通过参数删除数据


## [v1.0.0](https://github.com/ToQuery/clever-framework/tree/v1.0.0) (2018-11-22)

### New Features:

- 基于 JPA 的 curd 方法增强
- 基于 JPA 的数据库 audit 功能 

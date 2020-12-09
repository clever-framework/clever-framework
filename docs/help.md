
## 项目构建


```zsh
# 设置新的版本号
mvn versions:set -DnewVersion=1.0.6
mvn versions:set -DnewVersion=1.0.8-SNAPSHOT

# 更新所有模块版本号
mvn -N versions:update-child-modules

# 确定并提交版本
mvn versions:commit

# 发布jar到指定仓库
mvn deploy -Dregistry=https://maven.pkg.github.com/ToQuery -Dtoken=xxx -X -DskipTests
```


## 扩展基础 

- spring data rest 入口类方法`org.springframework.data.rest.webmvc.RepositoryEntityController.getCollectionResource`


## 存在的问题


- [x] 多个不同包路径下，扫描实体，dao存在问题
- [x] security ,在父模块中配置后，在子模块中不会被继承，会被覆盖！！！
- [x] mybatis ,在父模块中配置 编写相应xml 的Mapper，在子模块中会被覆盖，导致XML读取接口失败


## 待完成的功能

- ~~MyBatis和Jpa作用于同一个Dao时，会被Spring注册两个Bean~~
- ~~dao项目autoconfig的package和子项目的package是否冲突？~~
- spring-data和mybatis分页工具page对象的封装和转换
- spring-web 对前端实体的封装和httpstatus的转换
- JPA生成数据库时，默认值，注释的生成
- ~~pom依赖的优化~~

## 自定义注解

- 通过自定义注解实现重复名称的注解共存，（首先定义注解类并@intface加入要扩展的注解`@xxxx`，然后根据相应的属性增加`@AliasFor(annotation = XXXX.class)`。例如JPA 的param和mybatis的param可通过这个方式解决冲突）

## Other

https://github.com/javers/javers
https://yangbingdong.com/2019/spring-boot-data-jpa-learning/


- @SQLDelete
- @Where
- @SecondaryTable 做二级表，与下面的注解配合使用
- @Embedded
- @Embeddable
- @Convert


## 


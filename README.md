# spring 快速开发框架

[![GitHub License](https://img.shields.io/github/license/ToQuery/clever-framework.svg)](https://github.com/ToQuery/clever-framework)
[![Build Status](https://travis-ci.org/ToQuery/clever-framework.svg?branch=master)](https://travis-ci.org/ToQuery/clever-framework)
[![GitHub Issues](https://img.shields.io/github/issues/toquery/clever-framework.svg)](https://github.com/ToQuery/clever-framework/issues)
[![GitHub Repo Size](https://img.shields.io/github/repo-size/toquery/clever-framework.svg)](https://github.com/ToQuery/clever-framework)
[![GitHub Last Commit](https://img.shields.io/github/last-commit/ToQuery/clever-framework.svg)](https://github.com/ToQuery/clever-framework)
[![GitHub Commit Activity](https://img.shields.io/github/commit-activity/w/ToQuery/clever-framework.svg)](https://github.com/ToQuery/clever-framework)

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.toquery/clever-framework.svg)

[![Jwt Status](http://jwt.io/img/badge.svg)](https://github.com/ToQuery/clever-framework)

`clever-framework`  : 基于 Spring Boot 的拓展框架，遵循约定大于配置的原则 。

注意：snapshots 版本会及时响应，修复最新的 bug 或者必要的需求。

| 依赖         | 版本       |
| ------------ | ---------- |
| Spring       | 5.x        |
| Spring Boot  | 2.2.x      |
| Spring Cloud | Hoxton.SR1 |

## 使用方式

创建项目后，在pom.xml文件引入`parent`节点，如使用了Spring Boot的依赖，直接替换即可。目前最新版本为 ![Maven Central Version](https://img.shields.io/maven-central/v/io.github.toquery/clever-framework.svg)


```xml
<parent>
    <groupId>io.github.toquery</groupId>
    <artifactId>clever-framework</artifactId>
    <version>1.0.7-SNAPSHOT</version>
</parent>
```

## 方法命名规范

- getXXXByXXX 通过XX条件获取单个对象
- findXXXByXXX 通过XX条件获取list集合
- queryXXXByXXX  通过XX条件获取list集合,带分页


## IDE配置

- IntellJ IDEA 提示找不到 mybatis mapper 实现

使用注解 `@SuppressWarnings("MybatisMapperMethodInspection")` 忽略IDEA的 mybatis mapper 检查



## 项目构建


```zsh
# 设置新的版本号
mvn versions:set -DnewVersion=1.0.6
mvn versions:set -DnewVersion=1.0.7-SNAPSHOT

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

https://yangbingdong.com/2019/spring-boot-data-jpa-learning/

- @SQLDelete
- @Where


- @Embedded
- @Convert


## 



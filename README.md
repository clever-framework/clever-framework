# spring 快速开发框架

[![Build Status](https://travis-ci.org/ToQuery/clever-framework.svg?branch=master)](https://travis-ci.org/ToQuery/clever-framework)
![GitHub issues](https://img.shields.io/github/issues/toquery/clever-framework.svg)
![GitHub repo size](https://img.shields.io/github/repo-size/toquery/clever-framework.svg)
![Hex.pm](https://img.shields.io/hexpm/l/:package.svg)

## 扩展基础 

- spring data rest 入口类方法`org.springframework.data.rest.webmvc.RepositoryEntityController.getCollectionResource`

## 使用说明

- getXXXByXXX 通过XX条件获取单个对象
- findXXXByXXX 通过XX条件获取list集合
- queryXXXByXXX  通过XX条件获取list集合,带分页
- 使用注解`@SuppressWarnings("MybatisMapperMethodInspection")`忽略IDEA的mybatis mapper检查

## 存在的问题

- 多个不同包路径下，扫描实体，dao存在问题

## 待完成的功能

- ~~MyBatis和Jpa作用于同一个Dao时，会被Spring注册两个Bean~~
- ~~dao项目autoconfig的package和子项目的package是否冲突？~~
- spring-data和mybatis分页工具page对象的封装和转换
- spring-web 对前端实体的封装和httpstatus的转换
- JPA生成数据库时，默认值，注释的生成
- pom依赖的优化

## 自定义注解

- 通过自定义注解实现重复名称的注解共存，（首先定义注解类并@intface加入要扩展的注解`@xxxx`，然后根据相应的属性增加`@AliasFor(annotation = XXXX.class)`。例如JPA 的param和mybatis的param可通过这个方式解决冲突）



## 

```bash
mvn versions:set -DnewVersion=1.0.2
```

```bash
mvn -N versions:update-child-modules
```

```bash
mvn versions:commit
```


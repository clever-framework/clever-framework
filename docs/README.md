# spring 快速开发框架

[![Build Status](https://travis-ci.org/ToQuery/clever-framework.svg?branch=master)](https://travis-ci.org/ToQuery/clever-framework)
![GitHub issues](https://img.shields.io/github/issues/toquery/clever-framework.svg)
![GitHub repo size](https://img.shields.io/github/repo-size/toquery/clever-framework.svg)
![Hex.pm](https://img.shields.io/hexpm/l/:package.svg)


## CURD 方法命名规则

- getXXXByXXX 通过XX条件获取单个对象
- findXXXByXXX 通过XX条件获取list集合
- queryXXXByXXX  通过XX条件获取list集合,带分页


## 待完成的功能

- ~~MyBatis和Jpa作用于同一个Dao时，会被Spring注册两个Bean~~
- ~~dao项目autoconfig的package和子项目的package是否冲突？~~
- spring-data和mybatis分页工具page对象的封装和转换
- spring-web 对前端实体的封装和httpstatus的转换
- JPA生成数据库时，默认值，注释的生成
- pom依赖的优化
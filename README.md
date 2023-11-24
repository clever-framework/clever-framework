# spring 快速开发框架

[![GitHub License](https://img.shields.io/github/license/ToQuery/clever-framework.svg)](https://github.com/ToQuery/clever-framework)
[![Build Status](https://travis-ci.org/ToQuery/clever-framework.svg?branch=master)](https://travis-ci.org/ToQuery/clever-framework)
[![GitHub Issues](https://img.shields.io/github/issues/toquery/clever-framework.svg)](https://github.com/ToQuery/clever-framework/issues)
[![GitHub Repo Size](https://img.shields.io/github/repo-size/toquery/clever-framework.svg)](https://github.com/ToQuery/clever-framework)
[![GitHub Last Commit](https://img.shields.io/github/last-commit/ToQuery/clever-framework.svg)](https://github.com/ToQuery/clever-framework)
[![GitHub Commit Activity](https://img.shields.io/github/commit-activity/w/ToQuery/clever-framework.svg)](https://github.com/ToQuery/clever-framework)

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.toquery/clever-framework.svg)


`clever-framework`  : 基于 Spring Boot 的拓展框架，遵循约定大于配置的原则 。

注意：snapshots 版本会及时响应，修复最新的 bug 或者必要的需求。

| 依赖         | 版本       |
| ------------ |----------|
| Spring       | 6.2.0    |
| Spring Boot  | 3.2.0    |
| Spring Cloud | 2022.0.0 |


[![ReadMe Card](https://github-readme-stats.vercel.app/api/pin/?username=toquery&repo=clever-framework)](https://github.com/toquery/clever-framework)


## 使用方式

修改项目的pom.xml文件，引入`parent`节点，如使用了 Spring Boot 的依赖，直接替换即可。

目前最新版本为: ![Maven Central Version](https://img.shields.io/maven-central/v/io.github.toquery/clever-framework.svg)

```xml
<parent>
    <groupId>io.github.toquery</groupId>
    <artifactId>clever-framework</artifactId>
    <version>1.0.8-SNAPSHOT</version>
</parent>
```

## 方法命名规范

- getXXXByXXX 通过XX条件获取单个对象
- findXXXByXXX 通过XX条件获取list集合
- queryXXXByXXX  通过XX条件获取list集合,带分页


## IDE配置

- IntellJ IDEA 提示找不到 mybatis mapper 实现

使用注解 `@SuppressWarnings("MybatisMapperMethodInspection")` 忽略IDEA的 mybatis mapper 检查

`ResponseBodyAdvice`


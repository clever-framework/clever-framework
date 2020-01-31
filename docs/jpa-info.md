# JPA-info

- [JPA注解参考 v5.1](http://www.datanucleus.org/products/accessplatform_5_1/jpa/annotations.html)
> https://yangbingdong.com/2019/spring-boot-data-jpa-learning/


- @PersistenceContext 获取 EntityManager


- @SQLDelete
- @Where

两个注解可以配合完成逻辑删除？？？

## 主键

- @Id
- @IdClass
- @EmbeddedId 联合主键？

## 关联映射

- @JoinColumn
- @OneToOne
- @OneToMany @ManyToOne
- @ManyToMany @JoinTable

- @Embedded 关联一对一的值对象：将两个实体的公共字段抽离形成新的实体，在原两个实体内引入新的实体
- @Embeddable

> https://blog.csdn.net/lmy86263/article/details/52108130


- @AttributeOverrides
- @AttributeOverride



- @Convert 关联一对多的值对象

使用场景：数据库保存数据时为一个字段，但对应一个实体或集合数据，

- @Converts
- @Converter

## 领域事件

- @EntityListeners

- @PrePersist
- @PreUpdate
- @PreRemove
- @PostLoad
- @PostPersist
- @PostRemove
- @PostUpdate

- @DomainEvents
- @AfterDomainEventPublication
- @EventListener    事务之前执行
- @TransactionalEventListener   事务之后执行

# Spring事务实例
- 配置事务无法使用.yml格式配置文件（因为spingxml文件无法读取.yml格式文件），
请使用.properties文件
- springboot项目请在启动类中添加
@ImportResource({"classpath*:spring/spring-mybatis-*.xml"})
引入xml配置文件

编程式事务配置(在 Spring 出现以前，编程式事务管理对基于 POJO 的应用来说是唯一选择。用过 Hibernate 的人都知道，我们需要在代码中显式调用beginTransaction()、commit()、rollback()等事务管理相关的方法，这就是编程式事务管理。通过 Spring 提供的事务管理 API，我们可以在代码中灵活控制事务的执行。在底层，Spring 仍然将事务操作委托给底层的持久化框架来执行。)

声明式事务的配置(底层是建立在 AOP 的基础之上的。其本质是对方法前后进行拦截，然后在目标方法开始之前创建或者加入一个事务，在执行完目标方法之后根据执行情况提交或者回滚事务。)
- 1.spring-mybatis-anno 注解形式配置
- 2.spring-mybatis-tx 使用tx标签配置的拦截器
注意添加aop maven依赖
- 3.spring-mybatis-proxy 所有Bean共享一个代理基类
- 4.spring-mybatis-single-proxy 每个Bean都有一个代理
- 5.spring-mybatis-Interceptor 拦截器配置


### 使用场景：
- springboot中优先使用第一种
- 第二种使用较多
- 3，4，5很少使用

> 参考：https://www.cnblogs.com/longshiyVip/p/5061547.html

> 参考：https://github.com/Snailclimb/JavaGuide/blob/main/docs/database/mysql/transaction-isolation-level.md

### 事务的四大特性和mysql是如何保证

- 原子性：

事务被视为不可分割的最小单元，事务的所有操作要么全部提交成功，要么全部失败回滚。

回滚可以用回滚日志（Undo Log）来实现，回滚日志记录着事务所执行的修改操作，在回滚时反向执行这些修改操作即可。


- 一致性:

数据库在事务执行前后都保持一致性状态。在一致性状态下，所有事务对同一个数据的读取结果都是相同的。

- 隔离性:

一个事务所做的修改在最终提交以前，对其它事务是不可见的。

- 持久性:

一旦事务提交，则其所做的修改将会永远保存到数据库中。即使系统发生崩溃，事务执行的结果也不能丢失。

系统发生崩溃可以用重做日志（Redo Log）进行恢复，从而实现持久性。与回滚日志记录数据的逻辑修改不同，重做日志记录的是数据页的物理修改。

### 并发一致性问题（）

- 脏读（Dirty read）

- 丢失修改（Lost to modify)

- 不可重复读（Unrepeatableread）

- 幻读（Phantom read）

### 事务隔离级别（定义了一个事务可能受其他并发事务影响的程度）

多版本并发控制（Multi-Version Concurrency Control, MVCC）是 MySQL 的 InnoDB 存储引擎实现隔离级别的一种具体方式，用于实现提交读和可重复读这两种隔离级别。
而未提交读隔离级别总是读取最新的数据行，要求很低，无需使用 MVCC。
可串行化隔离级别需要对所有读取的行都加锁，单纯使用 MVCC 无法实现。

- TransactionDefinition.ISOLATION_DEFAULT=-1 ：使用后端数据库默认的隔离级别，Mysql 默认采用的 REPEATABLE_READ隔离级别 Oracle 默认采用的 READ_COMMITTED隔离级别. 
- TransactionDefinition.ISOLATION_READ_UNCOMMITTED: 最低的隔离级别，允许读取尚未提交的数据变更，可能会导致**脏读**、**幻读**或**不可重复读**
- TransactionDefinition.ISOLATION_READ_COMMITTED: 允许读取并发事务已经提交的数据，可以阻止**脏读**，但是**幻读**或**不可重复读**仍有可能发生
- TransactionDefinition.ISOLATION_REPEATABLE_READ: 对同一字段的多次读取结果都是一致的，除非数据是被本身事务自己所修改，可以阻止**脏读**和**不可重复读**，但**幻读**仍有可能发生。
- TransactionDefinition.ISOLATION_SERIALIZABLE: 最高的隔离级别，完全服从ACID的隔离级别。所有的事务依次逐个执行，这样事务之间就完全不可能产生干扰，也就是说，该级别可以防止**脏读**、**不可重复读**以及**幻读**。但是这将严重影响程序的性能。通常情况下也不会用到该级别。

### 事务传播行为（为了解决业务层方法之间互相调用的事务问题）

支持当前事务的情况：

- TransactionDefinition.PROPAGATION_REQUIRED： 如果当前存在事务，则加入该事务；如果当前没有事务，则**创建一个新的事务**。
- TransactionDefinition.PROPAGATION_SUPPORTS： 如果当前存在事务，则加入该事务；如果当前没有事务，则以**非事务的方式继续运行**。 
- TransactionDefinition.PROPAGATION_MANDATORY： 如果当前存在事务，则加入该事务；如果当前没有事务，则**抛出异常**。

不支持当前事务的情况：

- TransactionDefinition.PROPAGATION_REQUIRES_NEW： 创建一个新的事务，如果当前存在事务，则把当前事务挂起。
- TransactionDefinition.PROPAGATION_NOT_SUPPORTED： 以**非事务方式**运行，如果当前存在事务，则把当前事务挂起。
- TransactionDefinition.PROPAGATION_NEVER： **以非事务方式运行**，如果当前**存在事务，则抛出异常**。

其他情况：嵌套事务

- TransactionDefinition.PROPAGATION_NESTED： 如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行；如果当前没有事务，则该取值等价于TransactionDefinition.PROPAGATION_REQUIRED。

嵌套事务理解：嵌套是子事务在父事务中执行，子事务是父事务的一部分。在进入子事物时，在父事务中建立一个回滚点，save point，然后执行子事物，子事务是父事务的一部分，子事务执行完毕再执行父事务。

- 子事务回滚，父事务会回滚到save point 之前的状态，然后继续其他的操作，之前的操作不会影响。
- 父事务回滚，子事务也回滚。因为**在父事务结束之前子事务不会提交**。
- 在父事务结束后，先提交子事务，再提交父事务。

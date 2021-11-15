# java 里一些测试和基础类

[array]():

- ArrayDemo：一维，二维数组的定义和使用
- ArrayUtilsDemo：数组工具类使用



# spring 学习测试和基础类

## @Lazy

1.当把@Lazy注解写在某个类上（该类上有@Component注解）时，表示该Bean是一个懒加载的Bean，表示该Bean是在用到时才去创建，而不是Spring启动时创建

2.当把@Lazy注解写在一个@Configuration注解所在类上时，表示该类内部所有@Bean所定义的Bean都是懒加载的Bean

3.当把@Lazy注解加在一个字段上时，Spring会给该属性赋值一个由CGLIB所生成的一个代理对象，当该代理对象执行某个方法时，才会真正的根据字段的类型和名字从Spring容器找去找到某个Bean对象，并执行该Bean对象所对应的方法

4.当把@Lazy注解写在一个@Autowired注解所在方法上时，那么Spring会给该方法的所有入参赋值一个代理对象

5.当把@Lazy注解写在一个@Autowired注解所在方法中的某个参数前时，那么Spring会给该入参赋值一个代理对象

6.当把@Lazy注解写在一个@Autowired注解所在构造方法上时，那么Spring会给该方法的所有入参赋值一个代理对象，可以解决循环依赖

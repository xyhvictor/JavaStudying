# 控制反转
&emsp;&emsp;控制反转（IOC，Inversion of Control）是依赖倒置原则（Dependency Inversion Principle）的一种代码设计思路，在Spring中具体采用的方法是依赖注入（Dependency Injection），上述概念的关系如图所示。
<div align="center">  
<img src="/Users/victor/Desktop/study/java/pic/Spring/IOC/IOC_framework.png"  width="400" height="">  
</div>

## Bean
&emsp;&emsp;Spring官方文档中对bean的定义如下：
```
In Spring, the objects that form the backbone of your application and that are managed by the Spring IoC container are called beans. A bean is an object that is instantiated, assembled, and otherwise managed by a Spring IoC container.
```
&emsp;&emsp;即在Spring中，构成应用程序主干并由Spring IoC container管理的对象称为bean。bean是一个由Spring IoC容器实例化、组装和管理的对象，Spring程序由一个个bean组成。

## IoC container
&emsp;&emsp;在IoC container中，主要包括BeanFactory和ApplicationContext两个重要的接口。其中BeanFactory是比较原始的Factory，无法支持Spring的许多插件，如AOP功能等。ApplicationContext派生自BeanFactory，包含BeanFactory的所有功能，建议使用ApplicationContext。
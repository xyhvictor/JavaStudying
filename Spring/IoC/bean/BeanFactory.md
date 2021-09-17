# BeanFactory
&emsp;&emsp;在Spring中，对BeanFactory的介绍如下：
```
The root interface for accessing a Spring bean container.
```
&emsp;&emsp;即BeanFactory是所有Ioc容器的根接口。注释中对BeanFactory的介绍如下：
```
Depending on the bean definition, the factory will return either an independent instance of a contained object (the Prototype design pattern), or a single shared instance (a superior alternative to the Singleton design pattern, in which the instance is a singleton in the scope of the factory).
// 工厂可以返回一个单例的实例（默认）或者原型的实例。
```
```
The point of this approach is that the BeanFactory is a central registry of application components, and centralizes configuration of application components.
// BeanFactory是应用程序组件的中央注册中心，并集中应用程序组件的配置。
```
```
Note that it is generally better to rely on Dependency Injection ("push" configuration) to configure application objects through setters or constructors, rather than use any form of "pull" configuration like a BeanFactory lookup.
// 通过setters进行依赖注入配置对象优于传统的“pull”操作。
```
&emsp;&emsp;在BeanFactory中定义了许多操作，主要包括获取实例、判断类型和给bean起别名，如下所示。
```
public interface BeanFactory {
 
    String FACTORY_BEAN_PREFIX = "&";
 
    Object getBean(String name) throws BeansException;
 
    <T> T getBean(String name, Class<T> requiredType) throws BeansException;
 
    <T> T getBean(Class<T> requiredType) throws BeansException;
 
    Object getBean(String name, Object... args) throws BeansException;
 
    boolean containsBean(String name);
 
    boolean isSingleton(String name) throws NoSuchBeanDefinitionException;
 
    boolean isPrototype(String name) throws NoSuchBeanDefinitionException;
 
    boolean isTypeMatch(String name, Class<?> targetType) throws NoSuchBeanDefinitionException;
 
    boolean isTypeMatch(String name, Class<?> targetType) throws NoSuchBeanDefinitionException;
 
    String[] getAliases(String name);
 
}
```

### ApplicationContext
&emsp;&emsp;ApplicationContext派生于BeanFactory接口，利用MessageSource提供消息国际化，并且支持事件机制发布ApplicationEvent，可以获取上下文环境Environment，支持加载资源配置文件。 


## 循环依赖
## Spring扩展点
log4j2
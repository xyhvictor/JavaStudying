# Bean生命周期
&emsp;&emsp;bean生命周期的简单表示如图所示，首先通过new关键字进行实例化，然后该bean就能够使用了。一旦bean不再被使用，则由Java自动进行垃圾回收。
<div align="center">  
<img src="/Users/victor/Desktop/study/java/pic/Spring/IOC/Bean_life1.png"  width="400" height="">  
</div>

## Bean完整生命周期
&emsp;&emsp;BeanFactory实现类需要尽可能的支持标准bean生命周期相关的接口（BeanFactory注释），完整的初始化方法及其顺序如下所示：
```
Bean factory implementations should support the standard bean lifecycle interfaces
as far as possible. The full set of initialization methods and their standard order is:
<ol>
<li>BeanNameAware's {@code setBeanName}
<li>BeanClassLoaderAware's {@code setBeanClassLoader}
<li>BeanFactoryAware's {@code setBeanFactory}
<li>EnvironmentAware's {@code setEnvironment}
<li>EmbeddedValueResolverAware's {@code setEmbeddedValueResolver}
<li>ResourceLoaderAware's {@code setResourceLoader}
(only applicable when running in an application context)
<li>ApplicationEventPublisherAware's {@code setApplicationEventPublisher}
(only applicable when running in an application context)
<li>MessageSourceAware's {@code setMessageSource}
(only applicable when running in an application context)
<li>ApplicationContextAware's {@code setApplicationContext}
(only applicable when running in an application context)
<li>ServletContextAware's {@code setServletContext}
(only applicable when running in a web application context)
<li>{@code postProcessBeforeInitialization} methods of BeanPostProcessors
<li>InitializingBean's {@code afterPropertiesSet}
<li>a custom init-method definition
<li>{@code postProcessAfterInitialization} methods of BeanPostProcessors
</ol>

<p>On shutdown of a bean factory, the following lifecycle methods apply:
<ol>
<li>{@code postProcessBeforeDestruction} methods of DestructionAwareBeanPostProcessors
<li>DisposableBean's {@code destroy}
<li>a custom destroy-method definition
</ol>
```
&emsp;&emsp;其完整的生命周期如图所示。
<div align="center">  
<img src="/Users/victor/Desktop/study/java/pic/Spring/IOC/Bean_life3.png"  width="900" height="">  
</div>

## Bean核心生命周期
&emsp;&emsp;在此基础上，bean的核心生命周期如图所示，下面分别对其进行文字描述。
<div align="center">  
<img src="/Users/victor/Desktop/study/java/pic/Spring/IOC/Bean_life2.png"  width="900" height="">  
</div>

1) Spring启动，查找并加载需要被Spring管理的bean，进行bean的实例化。
2) bean实例化后将bean的引用和值注入到Bean的属性中。
3) 如果bean实现了BeanNameAware接口的话，Spring将bean的Id传递给setBeanName()方法。
4) 如果bean实现了BeanFactoryAware接口的话，Spring将调用setBeanFactory()方法，将BeanFactory容器实例传入。
5) 如果Bean实现了ApplicationContextAware接口的话，Spring将调用bean的setApplicationContext()方法，将bean所在应用上下文引用传入进来。
6) 如果bean实现了BeanPostProcessor接口，Spring就将调用他们的postProcessBeforeInitialization()方法。
7) 如果bean实现了InitializingBean接口，Spring将调用他们的afterPropertiesSet()方法。类似的，如果bean使用init-method声明了初始化方法，该方法也会被调用。
8) 如果bean实现了BeanPostProcessor接口，Spring就将调用他们的postProcessAfterInitialization()方法。
9) 此时，bean已经准备就绪，可以被应用程序使用了。他们将一直驻留在应用上下文中，直到应用上下文被销毁。
10) 如果bean实现了DisposableBean接口，Spring将调用它的destory()接口方法，同样，如果bean使用了destory-method声明销毁方法，该方法也会被调用。

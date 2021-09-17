# BeanDefinitionReader
## BeanDefinition
&emsp;&emsp;BeanDefinition包含了构造bean的相关信息，Spring中注释如下所示。
```
A BeanDefinition describes a bean instance, which has property values, constructor argument values, and further information supplied by concrete implementations.
```
## BeanDefinitionReader
&emsp;&emsp;BeanDefinitionReader用于读取Spring中配置的bean信息（即BeanDefinition），将其转换为IoC container内部的数据。BeanDefinitionReader即其实现类如图所示。
<div align="center">  
<img src="/Users/victor/Desktop/study/java/pic/Spring/IOC/BeanDefinitionReader_classes.png"  width="800" height="">  
</div>
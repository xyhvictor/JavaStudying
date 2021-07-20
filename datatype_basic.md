# 数据类型
## 简介
八大基础类型：int、short、byte、long、boolean、char、float、double  
本文重点介绍以下内容：  
&emsp;&emsp; 1）box & unbox   
&emsp;&emsp; 2）数值比较和溢出问题  
### 1）box & unbox 
box：
```
Integer total = 99;
```
unbox：
```
int totalPrim = total;
```
在使用自动拆箱的时候要注意空指针问题（NPE）：
```
// 会出现空指针异常
Integer a = null;
int b = a.intValue();
``` 
### 2）数值比较和溢出问题
#### a.溢出问题
```
// 未加L，因此等号右边为int类型。因为结果数值超过整数范围，将溢出的整型赋值给长整形也是溢出。
public void intOverFlow(){
    long millisOfYears = 5000 * 365 * 24 * 3600 * 1000;
}
```
```
// 加了L，等号右侧为长整型，不会溢出。L最好加在第一个数值后，避免前面的结果就已经溢出了。
public void intOverFlow(){
    long millisOfYears = 5000L * 365 * 24 * 3600 * 1000;
}
```
#### b.精度丢失问题
在计算机实际存储中，浮点型是不准确的。  
示例1:
```
public void accuracyLosing(){
    System.out.println(new BigDecimal(0.58)); //0.57999999...123149645...
}
```
示例2
```
public void accuracyLosing(){
    double a = 0.58;
    long b = (long)(a * b);
    System.out.println(b); //0.57
}
```
BigDecimal提供了两种准确存储的方式，分别是BigDecimal.valueOf()和通过构造方法传入字符串，如下所示
```
public void bidDecimalConstruct(){
    BigDecimal a = BigDecimal.valueOf(0.580);
    BigDecimal b = new BigDecimal("0.580");
    System.out.println(a);                  //0.58
    System.out.println(b);                  //0.58
    System.out.println(a.equals(b));        //false，equals会比较精度和数值两部分
    System.out.println(a.compareTo(b));     //0，compareTo仅比较数值
}
```
BigDecimal无法处理无限循环小数，因此在使用BigDecimal做除法运算的时候需要指定精度。
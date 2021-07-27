# SQL语句
&emsp;&emsp;SQL语句分为数据操纵语言（DML）、数据定义语言（DDL）、数据控制语言（DCL）。
## DML
### 隐式类型转换
&emsp;&emsp;通过关键字 ***desc/explain*** 可以查看SQL语句的具体执行情况，其中列 ***key*** 表示该查询语句实际使用的索引；***key_len*** 为使用索引的长度，在不损失精度的情况下长度越短越好；***rows***为必须检索的用来返回请求数据的行数（即实际扫描行数，越少越好）；Extra中参数较多，Using index表明实现了覆盖索引，即不需要回表。  
&emsp;&emsp;字段类型的隐式转换会导致索引失效和逻辑错误，如下所示。示例1发生了隐式类型转换，导致了索引失效，采用全表扫描的方式得到结果，扫描了6749025行数据；示例2使用了索引，扫描了1行数据。
```
// 示例1
desc select * from delay_message where message_id=2005261221531086216160969224 limit 10;
```
<div align="center">  
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/MySQL/SQL/DDL1.png"  width="800" height="">  
</div>

```
// 示例2
desc select * from delay_message where message_id='2005261221531086216160969224' limit 10;
```
<div align="center">  
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/MySQL/SQL/DDL2.png"  width="800" height="">  
</div>

### join select
#### 内连接
&emsp;&emsp;连接结果仅包含符合连接条件的行，参与连接的两个表都应该符合连接条件。
#### 外联接
&emsp;&emsp;连接结果不仅包含符合连接条件的行，而且包含自身不符合条件的行。通常包括左外连接、右外连接和全外连接。
#### 示例
&emsp;&emsp;通过集合表示各个连接的详细情况，如图所示。
<div align="center">  
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/MySQL/SQL/join_select.png"  width="800" height="">  
</div>

### truncate与delete
&emsp;&emsp;truncate与delete在进行全表操作的时候，逻辑结果相同，但是其处理方式却不相同。delete属于DML，DML会产生大量的binlog日志，在IO方面执行时间长，会占用锁资源，堵塞写入；truncate属于DDL原子事物，日志量极少，速度极快。
## DDL
### 字段类型
<div align="center">  
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/MySQL/SQL/col_type.png"  width="800" height="">  
</div>

### 数值型
<div align="center">  
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/MySQL/SQL/number_type.png"  width="800" height="">  
</div>

### 字符串型
<div align="center">  
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/MySQL/SQL/char_type.png"  width="800" height="">  
</div>

#### varchar限制
1) 存储限制：开头会占用1到2个字节存储字符串实际长度，最大长度不超过65535。
2) 编码长度限制：字符类型若为UTF8，每个字符最多占3个字节，最大长度不能超过21845，若为utf8mb4，每个字符最多占4个字节，最大长度不能超过16383。
3) 行长度限制：MySQL要求一个行的定义长度不能超过65535。
#### 大字段使用（text/blob）
1) 预期长度varchar满足，则避免使用TEXT。
2) 应用程序里压缩后再存到db。
3) 列值很长考虑用单独拆表存放。
### 时间日期类型
<div align="center">  
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/MySQL/SQL/date_type.png"  width="800" height="">  
</div>

## DCL
&emsp;&emsp;使用较少，不做陈述。
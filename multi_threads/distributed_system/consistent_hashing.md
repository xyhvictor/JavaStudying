# 一致性哈希
&emsp;&emsp;相比于一致性哈希，还有非一致性哈希（即普通哈希），下面分别对其进行介绍。
## 非一致性哈希
&emsp;&emsp;设资源集合为$RS$，$RS=\{R_{n},n=1,2,3,...,N\}$，其中$N$为资源的数量，$R_{n}$为资源，设服务器集合为$SS$，$SS=\{S_{m},m=1,2,3,...,M\}$，其中$M$为需要存储的服务器的数量。现将$RS$中的资源通过哈希的方法映射存储到$SS$中，具体计算过程如下：对任意资源$R_{i},i\in\{1,2,3,...N\}$，其需要存储的服务器编号$m=hash(R_{i})\ mod\ M$。
  
&emsp;&emsp;该方法优点在于，当查找服务器的时候，不需要遍历每台服务器，可以通过计算其哈希值并取模直接找到对应服务器。  
&emsp;&emsp;其缺陷在于，当需要改变服务器数量的时候（增加服务器或因故障减少服务器），都需要对所有资源进行重新计算并存储。  
&emsp;&emsp;针对上述缺陷，一致性哈希改进了其对服务器数量取模的方式，对$2^{32}$取模。
## 一致性哈希
### 算法原理
#### 环空间
&emsp;&emsp;一致性哈希算法所采用$hash$函数的值空间$R\in[0,2^{32}-1],R\in Z$，可以采用32位的无符号整形进行表示。整个哈希空间可以组成一个虚拟的圆环，如下所示。整个圆环以顺时针方向组织，圆环正上方的点代表0，0点右侧的第一个点代表1，以此类推，最后一个点为$2^{32}-1$。

<div align="center">  
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/distributed_system/consistent_hashing/consisten_hashing_1.png"  width="500" height="500">  
</div>

#### 服务器映射
&emsp;&emsp;接下来，可以选择服务器的IP或主机名作为关键字进行哈希，将所选取服务器的关键字记为$K$，得到哈希结果$h=hash(K)$，通过$h$就确定在该服务器在哈希环上位置上。  
&emsp;&emsp;设有三台机器，使用IP地址哈希后在环空间的位置如图所示。
<div align="center">  
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/distributed_system/consistent_hashing/consisten_hashing_2.png"  width="600" height="350">  
</div>

#### 资源定位
&emsp;&emsp;设资源集合为$RS$，$RS=\{R_{n},n=1,2,3,...,N\}$，其中$N$为资源的数量，$R_{n}$为资源，将资源$R_{n}$使用相同的$hash$函数计算出其哈希值$h_{n}=hash(R_{n})$，通过$h_{n}$确定$R_{n}$在环上的位置，从此位置沿环顺时针查找，查找到的第一个服务器就是其应该定位到的服务器。设有ObjectA，ObjectB，ObjectC三个资源，经过哈希计算后，在环空间上的位置如图所示。
<div align="center">  
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/distributed_system/consistent_hashing/consisten_hashing_3.png"  width="600" height="450">
</div>

### 容错性和可扩展性
&emsp;&emsp;一致性Hash算法对于节点的增减都只需重定位环空间中的一小部分数据，有很好的容错性和可扩展性。
#### 服务器减少
&emsp;&emsp;假设NodeC宕机了，只有ObjectC对象被重新定位到NodeA。即在一致性Hash算法中，***若一台服务器不可用，受影响的数据仅仅是此服务器到其环空间前一台服务器之间的数据***（这里为NodeC到NodeB之间的数据），其他不会受到影响，如图所示。
<div align="center">  
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/distributed_system/consistent_hashing/consisten_hashing_4.png"  width="600" height="450">
</div>

#### 服务器增加
&emsp;&emsp;系统增加了一台服务器NodeX，如图所示。此时对象ObjectA、ObjectB没有受到影响，只有ObjectC重新定位到了新的节点X上。***即受影响的数据只有新增服务器到其环空间前一台服务器之间的数据。***
<div align="center">  
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/distributed_system/consistent_hashing/consisten_hashing_5.png"  width="600" height="450">
</div>

### 数据倾斜
&emsp;&emsp;数据倾斜即 ***被缓存的对象大部分缓存在某一台服务器上***，通常由 ***服务器太少*** 造成，如图所示，此时大部分数据集中在服务器A上，而此时B仅有较少的数据。
<div align="center">  
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/distributed_system/consistent_hashing/consisten_hashing_6.png"  width="450" height="450">
</div>

#### 虚拟结点机制
&emsp;&emsp;一致性哈希算法引入了虚拟节点机制，即对每一个服务器节点计算多个哈希，每个计算结果位置都放置一个此服务节点，称为虚拟节点。通常可以将服务器IP或主机名后加入编号来作为$hash$函数的输入，如图所示。在将资源映射到虚拟结点后，再将虚拟结点映射为实际结点，就可以保证数据分布的均匀。
<div align="center">  
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/distributed_system/consistent_hashing/consisten_hashing_7.png"  width="600" height="450">
</div>

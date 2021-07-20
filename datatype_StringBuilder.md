# StringBuilder
## 简介
public final class StringBuilder  
extends Object  
implements Serializable, CharSequence  
* A ***mutable*** sequence of characters.This class provides an API compatible with StringBuffer, but with ***no guarantee of synchronization***. This class is designed for use as a drop-in ***replacement for StringBuffer*** in places where the string buffer was being used by a ***single thread*** (as is generally the case). Where possible, it is recommended that this class be used in preference to String Buffer as it will be ***faster under most implementations***.
## append方法
append方法源码如下(jdk11),在追加之前要确保存放数据的数组大小够用
```
    public AbstractStringBuilder append(String str) {
        if (str == null) {
            return appendNull();
        }
        int len = str.length();
        ensureCapacityInternal(count + len);
        putStringAt(count, str);
        count += len;
        return this;
    }
```
## 扩容方式
扩容即append源码中的ensureCapacityInternal函数，依次调用了ensureCapacityInternal、newCapacity和huguCapacity，源码如下
```
    private void ensureCapacityInternal(int minimumCapacity) {
        // overflow-conscious code
        int oldCapacity = value.length >> coder;
        if (minimumCapacity - oldCapacity > 0) {
            value = Arrays.copyOf(value,
                    newCapacity(minimumCapacity) << coder);
        }
    }
    private int newCapacity(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = value.length >> coder;
        int newCapacity = (oldCapacity << 1) + 2;
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        int SAFE_BOUND = MAX_ARRAY_SIZE >> coder;
        return (newCapacity <= 0 || SAFE_BOUND - newCapacity < 0)
            ? hugeCapacity(minCapacity)
            : newCapacity;
    }
    private int hugeCapacity(int minCapacity) {
        int SAFE_BOUND = MAX_ARRAY_SIZE >> coder;
        int UNSAFE_BOUND = Integer.MAX_VALUE >> coder;
        if (UNSAFE_BOUND - minCapacity < 0) { // overflow
            throw new OutOfMemoryError();
        }
        return (minCapacity > SAFE_BOUND)
            ? minCapacity : SAFE_BOUND;
    }
```
## StringBuilder与StringBuffer的区别
StringBuilder与StringBuffer都继承自AbstractStringBuilder，但是在StringBuffer中对每个方法都添加了synchronized关键字以保证线程安全。
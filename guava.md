# Strings
Strings常用的方法如下所示
```
// 判断是否为空指针或者字符串为空
Strings.isNullOrEmpty("");      // true
Strings.isNullOrEmpty(null);    // true

// 将空指针转换为空字符串
Strings.nullToEmpty(null);      // ""
Strings.nullToEmpty("victor");  // "victor"

// 将空字符串转换为null
Strings.emptyToNull("");        // null
Strings.emptyToNull("victor");  // "victor"

// 获取相同前缀
Strings.commonPrefix("aaabbb", "aabbc");    // "aa"

// 获取相同后缀
Strings.commonSuffix("asdzzz","kojdzzz");   // "dzzz"

// 在后面padding
Strings.padEnd("victor", 9, 'z');   // "victorzzz"

// 在前面padding
Strings.padStart("victor", 9, 'z'); // "zzzvictor"
```
# Ints
Ints常用方法如下
```
// 返回由指定数组支持的固定大小的列表，类似Arrays.asList(Object[])。
static List<Integer> asList(int... backingArray);

// 返回int值等于值，如果可能的话。
static int checkedCast(long value);

// 比较两个指定的int值。
static int compare(int a, int b);

// 每个阵列提供组合成一个单一的阵列，则返回值。
static int[] concat(int[]... arrays);
int[] res = Ints.concat(new int[]{1,2,3}, new int[]{2,3,4});    // [1,2,3,2,3,4]

// 若数组array中存在值为target的元素，返回true
static boolean contains(int[] array, int target);

// 返回一个包含相同的值数组的数组，但保证是一个规定的最小长度。
static int[] ensureCapacity(int[] array, int minLength, int padding);

// 返回int值，其大端表示存储在第一个4字节的字节；相当于ByteBuffer.wrap(bytes).getInt()。
static int fromByteArray(byte[] bytes);

// 返回值的哈希码，相当于调用((Integer)value).hashCode()的结果
static int hashCode(int value);

// 返回值目标数组的第一次出现的索引。
static int indexOf(int[] array, int target);

//返回指定数组的在目标数组中第一个匹配的位置，若不存在为-1
static int indexOf(int[] array, int[] target)
Ints.indexOf(new int[]{1,2,3,4,5}, new int[]{2,3,4}); // 1
Ints.indexOf(new int[]{1,2,3,4,5}, new int[]{2,6,7}); // -1

// 将数字用separator作为链接符拼接成字符串
static String join(String separator, int... array);
Ints.join("*",1,2,3); // 1*2*3

// 返回target在数组中最后出现的索引
static int lastIndexOf(int[] array, int target);

// 返回一个Comparator，用于比较两个int数组的字典顺序
static Comparator<int[]> lexicographicalComparator();
Comparator<int[]> comparator = Ints.lexicographicalComparator();
int res = comparator.compare(new int[]{1,2,3}, new int[]{1,2,2});   // 1

// 返回数组中的最大值
static int max(int... array);

// 返回数组中的最小值
static int min(int... array);

// 返回最接近的整数值
static int saturatedCast(long value);

// 返回包含集合的每个值的数组，转换为int值的方式为Number.intValue()
static int[] toArray(Collection<? extends Number> collection);

// 解析指定的字符串作为符号十进制整数
static Integer tryParse(String string);
```
# joiner
# spliter
# objects
# charMatcher
# Optional
# Function
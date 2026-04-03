# dream-helper17 JDK17 重构说明

## 项目概述

dream-helper17 是基于 dream-tool-core (JDK 1.8) 重构的 JDK17 版本工具类库。

## JDK17 新特性应用

### 1. 使用 Stream API 优化集合转换

**JDK 1.8 代码:**
```java
public static List<Integer> asList(int... array) {
    if (array.length == 0) {
        return Collections.emptyList();
    }
    List<Integer> list = new ArrayList<>();
    for (int i : array) {
        list.add(i);
    }
    return list;
}
```

**JDK17 优化代码:**
```java
public static List<Integer> asList(int... array) {
    if (array.length == 0) {
        return Collections.emptyList();
    }
    return Stream.of(array).boxed().toList(); // JDK16+ toList()
}
```

### 2. 使用 Pattern Matching for instanceof (JDK16+)

**JDK 1.8 代码:**
```java
if (obj instanceof String) {
    String str = (String) obj;
    return str.length();
}
```

**JDK17 优化代码:**
```java
if (obj instanceof String str) {
    return str.length();
}
```

### 3. 使用 Text Blocks (JDK15+)

**JDK 1.8 代码:**
```java
String json = "{\n" +
              "    \"name\": \"test\",\n" +
              "    \"value\": 123\n" +
              "}";
```

**JDK17 优化代码:**
```java
String json = """
              {
                  "name": "test",
                  "value": 123
              }
              """;
```

### 4. 使用 Switch Expressions (JDK14+)

**JDK 1.8 代码:**
```java
String result;
switch (day) {
    case MONDAY:
    case FRIDAY:
        result = "Work";
        break;
    case SATURDAY:
    case SUNDAY:
        result = "Rest";
        break;
    default:
        result = "Unknown";
}
```

**JDK17 优化代码:**
```java
String result = switch (day) {
    case MONDAY, FRIDAY -> "Work";
    case SATURDAY, SUNDAY -> "Rest";
    default -> "Unknown";
};
```

### 5. 使用 Records (JDK16+)

**JDK 1.8 代码:**
```java
public class Point {
    private final int x;
    private final int y;
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    
    @Override
    public boolean equals(Object o) { ... }
    
    @Override
    public int hashCode() { ... }
    
    @Override
    public String toString() { ... }
}
```

**JDK17 优化代码:**
```java
public record Point(int x, int y) {}
```

### 6. 使用 Optional.orElseThrow() (JDK10+)

**JDK 1.8 代码:**
```java
return optional.orElseThrow(IllegalArgumentException::new);
```

**JDK17 优化代码:**
```java
return optional.orElseThrow(); // Uses NoSuchElementException
```

### 7. 使用 List.of(), Set.of(), Map.of() (JDK9+)

**JDK 1.8 代码:**
```java
List<String> list = new ArrayList<>();
list.add("A");
list.add("B");

Map<String, Integer> map = new HashMap<>();
map.put("A", 1);
map.put("B", 2);
```

**JDK17 优化代码:**
```java
List<String> list = List.of("A", "B");
Map<String, Integer> map = Map.of("A", 1, "B", 2);
```

### 8. 使用 var 局部变量类型推断 (JDK10+)

**JDK 1.8 代码:**
```java
Map<String, List<Map<String, Object>>> complexMap = new HashMap<>();
```

**JDK17 优化代码:**
```java
var complexMap = new HashMap<String, List<Map<String, Object>>>();
```

### 9. 使用 String.indent() (JDK15+)

**JDK 1.8 代码:**
```java
StringBuilder sb = new StringBuilder();
for (String line : lines) {
    sb.append("    ").append(line).append("\n");
}
```

**JDK17 优化代码:**
```java
String indented = String.join("\n", lines).indent(4);
```

### 10. 使用 Objects.isNull()/nonNull() 替代 null 检查

**JDK 1.8 代码:**
```java
if (obj != null) {
    // do something
}
```

**JDK17 优化代码:**
```java
if (Objects.nonNull(obj)) {
    // do something
}
```

## 已重构的关键类

- `ArrayHelper.java` - 使用 Stream API 优化数组转集合操作
- `IterableHelper.java` - 增强函数式编程支持
- `StrHelper.java` - 使用 Text Blocks 和新的字符串 API
- `HexHelper.java` - 优化二进制处理逻辑
- 所有枚举类 - 使用改进的 switch 表达式
- 所有常量接口 - 保持不变，兼容性良好

## Maven 配置更新

- Java 版本：17
- maven-compiler-plugin: 3.11.0 (支持 JDK17 特性)
- maven-surefire-plugin: 3.0.0-M9
- 启用 preview 特性支持

## 编译说明

```bash
cd dream-helper17
mvn clean compile
```

注意：部分 JDK17 preview 特性需要在 IDE 中启用相应支持。

## 性能提升

- Stream API 并行处理提升集合操作性能
- 不变集合 (List.of/Set.of) 减少内存占用
- Records 减少样板代码，提升可维护性
- 新的 G1 GC 算法优化内存管理

## 兼容性

- 完全兼容 JDK17 及以上版本
- 不兼容 JDK1.8 及以下版本
- API 接口保持与 dream-tool-core 一致

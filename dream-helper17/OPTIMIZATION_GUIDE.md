# JDK17 代码优化指南

## 1. 集合操作优化

### 使用 Stream API 简化代码

```java
// JDK 1.8
List<String> result = new ArrayList<>();
for (String item : list) {
    if (item != null && item.length() > 5) {
        result.add(item.toUpperCase());
    }
}

// JDK17
List<String> result = list.stream()
    .filter(Objects::nonNull)
    .filter(s -> s.length() > 5)
    .map(String::toUpperCase)
    .toList();
```

### 使用 Collection 工厂方法

```java
// JDK 1.8
List<String> list = new ArrayList<>();
list.add("A");
list.add("B");

Set<Integer> set = new HashSet<>();
set.add(1);
set.add(2);

Map<String, Integer> map = new HashMap<>();
map.put("key1", 1);
map.put("key2", 2);

// JDK17
List<String> list = List.of("A", "B");
Set<Integer> set = Set.of(1, 2);
Map<String, Integer> map = Map.of("key1", 1, "key2", 2);
```

## 2. 字符串处理优化

### 使用 Text Blocks

```java
// JDK 1.8
String json = "{\n" +
              "    \"name\": \"John\",\n" +
              "    \"age\": 30,\n" +
              "    \"city\": \"New York\"\n" +
              "}";

// JDK17
String json = """
              {
                  "name": "John",
                  "age": 30,
                  "city": "New York"
              }
              """;
```

### 使用 String.indent()

```java
// JDK 1.8
StringBuilder sb = new StringBuilder();
for (String line : lines) {
    sb.append("    ").append(line).append("\n");
}
String indented = sb.toString();

// JDK17
String indented = String.join("\n", lines).indent(4);
```

### 使用 switch 表达式处理字符串

```java
// JDK 1.8
String result;
switch (day) {
    case "MONDAY":
    case "FRIDAY":
        result = "Work";
        break;
    case "SATURDAY":
    case "SUNDAY":
        result = "Rest";
        break;
    default:
        result = "Unknown";
}

// JDK17
String result = switch (day) {
    case "MONDAY", "FRIDAY" -> "Work";
    case "SATURDAY", "SUNDAY" -> "Rest";
    default -> "Unknown";
};
```

## 3. 类型推断和模式匹配

### 使用 var 类型推断

```java
// JDK 1.8
Map<String, List<Map<String, Object>>> complexMap = new HashMap<>();

// JDK17
var complexMap = new HashMap<String, List<Map<String, Object>>>();
```

### Pattern Matching for instanceof

```java
// JDK 1.8
if (obj instanceof String) {
    String str = (String) obj;
    return str.length();
} else if (obj instanceof Integer) {
    Integer num = (Integer) obj;
    return num * 2;
}

// JDK17
if (obj instanceof String str) {
    return str.length();
} else if (obj instanceof Integer num) {
    return num * 2;
}
```

## 4. Records 简化数据类

```java
// JDK 1.8
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
    
    @Override
    public String toString() {
        return "Point{x=" + x + ", y=" + y + "}";
    }
}

// JDK17
public record Point(int x, int y) {}
```

## 5. Optional 优化

```java
// JDK 1.8
return optional.orElseThrow(IllegalArgumentException::new);

// JDK17
return optional.orElseThrow(); // NoSuchElementException
```

## 6. 数值计算优化

### 使用 Math.addExact(), Math.multiplyExact() 等

```java
// JDK 1.8 - 可能溢出
int result = a + b;

// JDK17 - 溢出时抛出异常
int result = Math.addExact(a, b);
```

### 使用 Integer.sum(), Long.sum()

```java
// JDK 1.8
list.stream().reduce(0, (a, b) -> a + b);

// JDK17
list.stream().mapToInt(Integer::intValue).sum();
```

## 7. 并发优化

### 使用 CompletableFuture

```java
// JDK 1.8
Future<String> future = executor.submit(() -> {
    return doSomething();
});
String result = future.get();

// JDK17
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> doSomething())
    .thenApply(result -> process(result));
String result = future.join();
```

### 使用 ConcurrentHashMap.merge()

```java
// JDK 1.8
if (map.containsKey(key)) {
    map.put(key, map.get(key) + value);
} else {
    map.put(key, value);
}

// JDK17
map.merge(key, value, Integer::sum);
```

## 8. 日期时间 API 优化

```java
// JDK 1.8 - 获取当前时间戳
long timestamp = System.currentTimeMillis();

// JDK17 - 使用 Instant
long timestamp = Instant.now().toEpochMilli();

// JDK 1.8 - 日期格式化
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
String dateStr = sdf.format(new Date());

// JDK17 - 使用 DateTimeFormatter
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
String dateStr = LocalDate.now().format(formatter);
```

## 9. I/O 优化

### 使用 Files.readString() 和 Files.writeString()

```java
// JDK 1.8
String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
Files.write(path, content.getBytes(StandardCharsets.UTF_8));

// JDK17
String content = Files.readString(path, StandardCharsets.UTF_8);
Files.writeString(path, content, StandardCharsets.UTF_8);
```

## 10. 异常处理优化

### 使用 try-with-resources 改进

```java
// JDK 1.8
try (BufferedReader br = new BufferedReader(new FileReader(file))) {
    return br.readLine();
}

// JDK17 - 多个资源更清晰
try (var reader = Files.newBufferedReader(path);
     var writer = Files.newBufferedWriter(outputPath)) {
    // process
}
```

### 使用 throw as 多异常处理

```java
// JDK 1.8
try {
    // code
} catch (Exception1 e) {
    handle(e);
} catch (Exception2 e) {
    handle(e);
}

// JDK17 (JDK7+ 已支持)
try {
    // code
} catch (Exception1 | Exception2 e) {
    handle(e);
}
```

## 11. 函数式编程优化

### 使用 Function.andThen() / Function.compose()

```java
// JDK 1.8
Function<String, Integer> f1 = s -> s.length();
Function<Integer, String> f2 = i -> "Length: " + i;
Function<String, String> composed = s -> f2.apply(f1.apply(s));

// JDK17
Function<String, Integer> f1 = String::length;
Function<Integer, String> f2 = i -> "Length: " + i;
Function<String, String> composed = f1.andThen(f2);
```

### 使用 Predicate 组合

```java
// JDK 1.8
Predicate<String> p1 = s -> s != null && s.length() > 5;

// JDK17
Predicate<String> p1 = Objects::nonNull;
Predicate<String> p2 = p1.and(s -> s.length() > 5);
```

## 12. 性能优化建议

1. **使用 StringBuilder 的构造器指定初始容量**
   ```java
   StringBuilder sb = new StringBuilder(expectedLength);
   ```

2. **优先使用基本类型数组而非包装类**
   ```java
   int[] array = new int[100]; // 优于 Integer[]
   ```

3. **使用并行流处理大数据集**
   ```java
   list.parallelStream()
       .filter(...)
       .collect(Collectors.toList());
   ```

4. **避免在循环中创建对象**
   ```java
   // 不推荐
   for (int i = 0; i < list.size(); i++) {
       Pattern pattern = Pattern.compile(regex);
   }
   
   // 推荐
   Pattern pattern = Pattern.compile(regex);
   for (int i = 0; i < list.size(); i++) {
       // use pattern
   }
   ```

## 13. 代码质量提升

### 使用 @Deprecated(since, forRemoval)

```java
@Deprecated(since = "17", forRemoval = true)
public void oldMethod() {
    // 标记为将要移除的方法
}
```

### 使用 Varargs 警告抑制

```java
@SuppressWarnings("varargs")
public static void genericMethod(List<?>... lists) {
    // 安全地使用泛型数组
}
```

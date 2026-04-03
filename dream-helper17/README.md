# Dream Helper 17 (dream-helper17)

Simple helper tool library based on JDK 17.

## Project Information

- **Group ID**: dream.flying.flower
- **Artifact ID**: dream-helper17
- **Version**: 0.0.1
- **Java Version**: 17
- **Encoding**: UTF-8

## Features

This project is refactored from [dream-tool-core](https://github.com/dreamFlyingFlower/dream-tool-core) with JDK 17 features:

### JDK 17 New Features Applied

1. **Stream API Enhancements** - Using `Stream.toList()` for efficient collection operations
2. **Pattern Matching for instanceof** - Cleaner type casting code
3. **Text Blocks** - Multi-line string literals for JSON, SQL, etc.
4. **Switch Expressions** - Modern switch syntax with arrow notation
5. **Records** - Concise immutable data carriers
6. **Var Type Inference** - Local variable type inference
7. **String.indent()** - Improved string formatting
8. **Optional.orElseThrow()** - Simplified exception throwing
9. **Collection Factory Methods** - `List.of()`, `Set.of()`, `Map.of()`
10. **Objects.isNull()/nonNull()** - Functional null checks

## Build Requirements

- JDK 17 or higher
- Maven 3.6+

## Building the Project

```bash
cd dream-helper17
mvn clean install
```

## Usage

Add dependency to your pom.xml:

```xml
<dependency>
    <groupId>dream.flying.flower</groupId>
    <artifactId>dream-helper17</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Package Structure

```
dream.flying.flower
├── annotation          # Custom annotations
├── bean                # Bean utilities
├── binary              # Binary data helpers (Hex, Base64)
├── collection          # Collection utilities
├── common              # Common utilities
├── db                  # Database utilities
├── digest              # Cryptographic helpers
├── enums               # Enumerations
├── function            # Functional interfaces
├── generator           # ID generators
├── helper              # Core helper classes
├── io                  # I/O utilities
├── lang                # Language utilities
├── net                 # Network utilities
├── reflect             # Reflection utilities
├── result              # Result handling
└── thread              # Threading utilities
```

## Key Classes

### Array Helpers
- `ArrayHelper` - Array operations with Stream API optimization
- `ListHelper` - List manipulation utilities
- `MapHelper` - Map operation utilities

### String Helpers
- `StrHelper` - String manipulation with Text Blocks support
- `CharsetHelper` - Character encoding utilities

### Binary Helpers
- `HexHelper` - Hexadecimal encoding/decoding
- `Base64Helper` - Base64 encoding/decoding

### Cryptographic Helpers
- `DigestHelper` - Message digest operations
- `RsaHelper` - RSA encryption/decryption
- `BCryptHelper` - BCrypt password hashing

### Generators
- `UuidGenerator` - UUID generation
- `SnowFlakeGenerator` - Distributed ID generation
- `StringGenerator` - Random string generation

## Migration from dream-tool-core (JDK 1.8)

All APIs are backward compatible. Simply update your dependency and ensure you're running on JDK 17+.

## Documentation

See [REFACTOR_JDK17.md](REFACTOR_JDK17.md) for detailed JDK 17 migration guide.

## License

Same as dream-tool-core project.

## Author

飞花梦影

## Links

- [GitHub](https://github.com/dreamFlyingFlower)
- [Gitee](https://gitee.com/dreamFlyingFlower)
- [Original Project](https://github.com/dreamFlyingFlower/dream-tool-core)

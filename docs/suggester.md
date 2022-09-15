# Suggester

> Better representation of boolean expressions

## Old Way

```kotlin
// ...
val couldBeDisposed = money > 0f || (
        admin in allowedAdmins &&
                (foods.isNotEmpty() || foods.first().type != Food.Water)
        )

if (couldBeDisposed) {
    // do something...
}
```

## How to use?

```kotlin
// ...
val couldBeDisposed = any {
    suggest { money > 0f }
    suggestAll { all ->
        all.suggest { admin in allowedAdmins }
        all.suggestAny {
            it.suggest { foods.isNotEmpty() }
            it.suggest { foods.first().type != Food.Water }
        }
    }
}
if (couldBeDisposed) {
    // do something...
}
```

[More Examples](../src/test/kotlin/com/thxbrop/suggester/SuggesterTest.kt)

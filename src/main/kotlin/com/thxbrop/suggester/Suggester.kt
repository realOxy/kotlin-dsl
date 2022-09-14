package com.thxbrop.suggester

abstract class Suggester {
    var result: Boolean? = null
    abstract fun suggest(block: () -> Boolean)
    protected open var completed: Boolean = false
    abstract fun complete()

    companion object {
        fun newAllSuggester() = AllSuggester()
        fun newAnySuggester() = AnySuggester()
    }

    class AllSuggester internal constructor() : Suggester() {
        override fun suggest(block: () -> Boolean) {
            if (!completed) block().also {
                if (!it) {
                    result = false
                    completed = true
                }
            }
        }

        override fun complete() {
            completed = true
            result = result ?: true
        }
    }

    class AnySuggester internal constructor() : Suggester() {
        override fun suggest(block: () -> Boolean) {
            if (!completed) block().also {
                if (it) {
                    result = true
                    completed = true
                }
            }
        }

        override fun complete() {
            completed = true
            result = result ?: false
        }
    }
}

inline fun Suggester.suggestAll(crossinline block: (Suggester) -> Unit) = suggest { all(block) }

inline fun Suggester.suggestAny(crossinline block: (Suggester) -> Unit) = suggest { any(block) }

inline fun suggest(suggester: Suggester, block: Suggester.() -> Unit): Boolean {
    block(suggester)
    suggester.complete()
    return suggester.result!!
}

inline fun all(block: Suggester.() -> Unit): Boolean = suggest(Suggester.newAllSuggester(), block)

inline fun any(block: Suggester.() -> Unit): Boolean = suggest(Suggester.newAnySuggester(), block)
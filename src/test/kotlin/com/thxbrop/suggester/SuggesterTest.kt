package com.thxbrop.suggester

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

internal class SuggesterTest {
    @Test
    fun `Basic All`() {
        val actual = all {
            suggest { true }
        }
        val expected = true
        assertEquals(expected, actual)
    }

    @Test
    fun `Basic Any`() {
        val actual = any {
            suggest { true }
        }
        val expected = true
        assertEquals(expected, actual)
    }

    @Test
    fun `Multiple All`() {
        val actual = all {
            suggest { false }
            suggest { false }
        }
        val expected = false
        assertEquals(expected, actual)
    }

    @Test
    fun `Multiple Any`() {
        val actual = any {
            suggest { false }
            suggest { false }
        }
        val expected = false
        assertEquals(expected, actual)
    }

    @Test
    fun `Diff Multiple All`() {
        val actual = all {
            suggest { true }
            suggest { false }
        }
        val expected = false
        assertEquals(expected, actual)
    }

    @Test
    fun `Diff Multiple Any`() {
        val actual = any {
            suggest { true }
            suggest { false }
        }
        val expected = true
        assertEquals(expected, actual)
    }

    @Test
    fun `Nested All`() {
        val actual = all {
            suggest { true }
            suggestAll {
                suggest { false }
            }
        }
        val expected = false
        assertEquals(expected, actual)
    }

    @Test
    fun `Nested Any`() {
        val actual = any {
            suggest { false }
            suggestAny {
                suggest { false }
                suggest { true }
            }
        }
        val expected = true
        assertEquals(expected, actual)
    }

    @Suppress("UNREACHABLE_CODE")
    @Test
    fun `Unreachable All`() {
        assertDoesNotThrow {
            val actual = all {
                suggest { false }
                suggest {
                    throw RuntimeException("Unreachable!")
                    true
                }
            }
            val expected = false
            assertEquals(expected, actual)
        }
    }

    @Suppress("UNREACHABLE_CODE")
    @Test
    fun `Unreachable Any`() {
        assertDoesNotThrow {
            val actual = any {
                suggest { true }
                suggest {
                    throw RuntimeException("Unreachable!")
                    true
                }
            }
            val expected = true
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `Complex Expression`() {
        val actual = any {
            suggest { false }
            suggestAll { all ->
                all.suggest { true }
                suggestAny {
                    it.suggest { false }
                    it.suggest { true }
                }
            }
        }
        val expected = true
        assertEquals(expected, actual)
    }
}

inline fun <R, T> foo(xFoo : R, yFoo : R, block : (R, R) -> T) : T {
    return block(xFoo, yFoo)
}

fun bar() {
    foo(1, 2) { x, y -> x + y }
    foo(1L, 2L) { x, y -> x + y }
    foo(1f, 2f) { x, y -> x + y }
    foo(1.toDouble(), 2.toDouble()) { x, y -> x + y }
    foo(1.toByte(), 2.toByte()) { x, y -> x + y }
    foo(1.toShort(), 2.toShort()) { x, y -> x + y }
    foo('a', 'b') { x, y -> x == y }
    foo(true, false) { x, y -> x || y }
}

// 0 valueOf
// 0 Value\s\(\)
// 1 LOCALVARIABLE x I (.*) (.*)
// 1 LOCALVARIABLE y I (.*) (.*)
// 1 LOCALVARIABLE x J (.*) (.*)
// 1 LOCALVARIABLE y J (.*) (.*)
// 1 LOCALVARIABLE x F (.*) (.*)
// 1 LOCALVARIABLE y F (.*) (.*)
// 1 LOCALVARIABLE x D (.*) (.*)
// 1 LOCALVARIABLE y D (.*) (.*)
// 1 LOCALVARIABLE x B (.*) (.*)
// 1 LOCALVARIABLE y B (.*) (.*)
// 1 LOCALVARIABLE x S (.*) (.*)
// 1 LOCALVARIABLE y S (.*) (.*)
// 1 LOCALVARIABLE x C (.*) (.*)
// 1 LOCALVARIABLE y C (.*) (.*)
// 1 LOCALVARIABLE x Z (.*) (.*)
// 1 LOCALVARIABLE y Z (.*) (.*)

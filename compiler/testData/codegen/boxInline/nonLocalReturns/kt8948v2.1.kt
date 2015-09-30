import test.*

fun test() {
    foo {
        val b = "aa"
    }
}

fun box(): String {
    test()
    return "OK1"
}
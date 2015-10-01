class A {
    fun test(a: String, b: String): String {
        return a + b;
    }

    fun box(): String {
        test(a = "K", b = "O")
        return "OK"
    }

}
// 6 LOAD
// 0 STORE
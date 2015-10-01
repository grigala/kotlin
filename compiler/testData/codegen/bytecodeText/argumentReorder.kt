class A {
    fun test(a: String, b: String): String {
        return a + b;
    }

    fun box(): String {
        test(b = "K", a = "O")
        return "OK"
    }

}
// 8 LOAD
// 2 STORE
// LANGUAGE_VERSION: 1.3

interface A {
    fun test() = ok()

    private companion object {
        fun ok() = "OK"
    }
}

class C : A

fun box() = C().test()

interface A {

    private val prop: String
        get() = "1"

    private fun foo() {

    }

    private fun defaultFun(p: String = "OK") {

    }
}

// There's no 'foo' in A$$TImpl, proguard and other tools may fail if we generate calls to it
// 1 foo
// 1 getProp
// 1 defaultFun\$
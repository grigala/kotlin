@file:JvmName("PrivateFun")
@file:JvmMultifileClass
package test

private fun foo() = "foo"

class ClassA {
    fun bar(): String = foo()
}

private open class PrivateClass

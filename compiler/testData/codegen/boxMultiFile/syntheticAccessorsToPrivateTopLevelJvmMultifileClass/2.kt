@file:JvmName("PrivateVal")
@file:JvmMultifileClass
package test

private val x = "foo"

class ClassB {
    fun bar() = x
}

@file:JvmName("PrivateVal")
package test

private val x = "foo"

class ClassB {
    fun bar() = x
}

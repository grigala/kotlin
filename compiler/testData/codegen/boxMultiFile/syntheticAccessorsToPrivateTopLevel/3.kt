@file:JvmName("PrivateVar")
package test

private var y = "foo"

class ClassC {
    fun bar(value: String? = null): String {
        if (value != null) {
            y = value
        }

        return y
    }
}

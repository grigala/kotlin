package test

fun check(klass: Class<*>) {
    for (method in klass.getDeclaredMethods()) {
        if (method.isSynthetic() && method.getName().startsWith("access$")) return
    }

    throw AssertionError("No synthetic methods starting with 'access$' found in class $klass")
}

fun checkMethodForPrivate(klass: Class<*>, methodName: String) {
    val method = klass.getDeclaredMethod(methodName)
}

fun box(): String {
    assert("foo" ==ClassA().bar())
    assert("foo" ==ClassB().bar())

    assert("foo" ==ClassC().bar())
    ClassC().bar("bar")
    assert("bar" ==ClassC().bar())

    val classLoader = ClassB().javaClass.classLoader
    val privateFunClass = classLoader.loadClass("test.PrivateFun")
    val privateValClass = classLoader.loadClass("test.PrivateVal")
    val privateVarClass = classLoader.loadClass("test.PrivateVar")

    assert(classLoader.loadClass("test.PrivateClass").modifiers == 0) // package visibility

    val foo = privateFunClass.getDeclaredMethod("foo")
    assert(foo != null) { "method foo is not found in PrivateFun class" }
    assert(foo.modifiers and 2 == 2) { "foo should be private in PrivateFun class, but foo.modifiers=${foo.modifiers}"}

    check(classLoader.loadClass("test.PrivateFun"))
    check(classLoader.loadClass("test.PrivateVal"))
    check(classLoader.loadClass("test.PrivateVar"))

    return "OK"
}

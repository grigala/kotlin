package smartStepIntoInlinedFunctionalExpression

fun main(args: Array<String>) {
    val array = arrayOf(1, 2)
    //Breakpoint!
    val myClass = MyClass()

    // smart step into f2.invoke(), one-line lambda
    myClass.f1(fun () { test() })
           .f2(fun () { test() })

    // smart step into map.invoke(), multiline lambda
    array.map(fun (it): Int {
        return it * 2
    })

    // smart step into filter.invoke()
    array.map(fun (it): Int { return it * 2 })
         .filter(fun (it): Boolean {
            return it > 2
         })
}

class MyClass {
    inline fun f1(f1Param: () -> Unit): MyClass {
        test()
        f1Param()
        return this
    }

    inline fun f2(f1Param: () -> Unit): MyClass {
        test()
        f1Param()
        return this
    }
}

fun test() {}

// STEP_OVER: 1
// SMART_STEP_INTO_BY_INDEX: 4
// STEP_OVER: 1
// SMART_STEP_INTO_BY_INDEX: 2
// STEP_OVER: 1
// SMART_STEP_INTO_BY_INDEX: 4
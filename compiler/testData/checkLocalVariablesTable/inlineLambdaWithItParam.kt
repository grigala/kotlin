class A {
    inline fun inlineFun(s: (s: Int) -> Unit) {
        s(11)
    }

    fun foo() {
        inlineFun ({
                       var zzz = it;
                       zzz++
                   })
    }
}

// METHOD : A.foo()V
// VARIABLE : NAME=zzz TYPE=I INDEX=5
// VARIABLE : NAME=it TYPE=I INDEX=3
// VARIABLE : NAME=$$inline$argument$ordinal$1$index$2 TYPE=I INDEX=4
// VARIABLE : NAME=$$inline$function$inlineFun$index$2 TYPE=I INDEX=2
// VARIABLE : NAME=this TYPE=LA; INDEX=0

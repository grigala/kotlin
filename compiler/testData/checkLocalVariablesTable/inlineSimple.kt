class A {
    inline fun inlineFun(s: () -> Unit) {
        s()
    }

    fun foo() {
        var s = 1;
        inlineFun ({
                       var zzz = 2;
                       zzz++
                   })
    }
}

// METHOD : A.foo()V
// VARIABLE : NAME=zzz TYPE=I INDEX=3
// VARIABLE : NAME=$$inline$argument$1 TYPE=I INDEX=4
// VARIABLE : NAME=$$inline$function$inlineFun TYPE=I INDEX=3
// VARIABLE : NAME=s TYPE=I INDEX=1
// VARIABLE : NAME=this TYPE=LA; INDEX=0

class A {
    inline fun inlineFun(s: () -> Unit) {
        s()
    }

    fun foo() {
        var s = 0;
        inlineFun {
            var z = 1;
            z++

            inlineFun {
                var zz2 = 2;
                zz2++
            }
        }
    }
}

// METHOD : A.foo()V
// VARIABLE : NAME=zz2 TYPE=I INDEX=9
// VARIABLE : NAME=$$inline$argument$ordinal$1$index$1 TYPE=I INDEX=8
// VARIABLE : NAME=$$inline$function$inlineFun$index$2 TYPE=I INDEX=7
// VARIABLE : NAME=z TYPE=I INDEX=5
// VARIABLE : NAME=$$inline$argument$ordinal$1$index$1 TYPE=I INDEX=4
// VARIABLE : NAME=$$inline$function$inlineFun$index$2 TYPE=I INDEX=3
// VARIABLE : NAME=s TYPE=I INDEX=1
// VARIABLE : NAME=this TYPE=LA; INDEX=0

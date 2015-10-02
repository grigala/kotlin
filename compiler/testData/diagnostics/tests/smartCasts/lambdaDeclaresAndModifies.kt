// !DIAGNOSTICS: -UNUSED_PARAMETER
fun run(f: () -> Unit) = 0

fun foo(arg: Int?) {
    run {
        var x = arg
        if (x == null) return@run
        // Not safe: x = null later in the owner
        <!DEBUG_INFO_SMARTCAST!>x<!>.hashCode()
    }
}
// WITH_RUNTIME

fun box(): String {
    var str = "OK"
    var r = ""
    for (ch in str) {
        r += ch
        str = "zzz"
    }
    return r
}
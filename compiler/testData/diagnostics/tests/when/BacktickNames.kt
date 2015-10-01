// TODO: Uncomment after fixing KT-9416
//import kotlin.Deprecated as `deprecate\entity`

//@`deprecate\entity`("") data class Pair(val x: Int, val y: Int)

class <!INVALID_CHARACTERS!>`class.name`<!>
class <!INVALID_CHARACTERS!>`class;name`<!>
class <!INVALID_CHARACTERS!>`class[name`<!>
class <!INVALID_CHARACTERS!>`class]name`<!>
class <!INVALID_CHARACTERS!>`class/name`<!>
class <!INVALID_CHARACTERS!>`class<name`<!>
class <!INVALID_CHARACTERS!>`class>name`<!>
class <!INVALID_CHARACTERS!>`class:name`<!>
class <!INVALID_CHARACTERS!>`class\name`<!>

class <!INVALID_CHARACTERS!>`  `<!>

val <!INVALID_CHARACTERS!>`val:X`<!> = 10

fun <!INVALID_CHARACTERS!>`foo:bar`<!>(<!INVALID_CHARACTERS!>`\arg`<!>: Int): Int {
    return `\arg`
}
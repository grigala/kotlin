// "Make 'Foo' data class" "true"
class Foo(internal val bar: String, var baz: Int)

fun test() {
    var (bar, baz) = Foo("A", 1)<caret>
}
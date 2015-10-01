// "Import" "true"
// ERROR: Unresolved reference: foo()["other"]
// ERROR: No set method providing array access

package testing

import some.Some

fun foo(): Some = Some()

fun testing() {
    foo()<caret>["other"] = 1
}

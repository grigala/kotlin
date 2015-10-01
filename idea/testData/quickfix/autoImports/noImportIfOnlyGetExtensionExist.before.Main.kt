// "Import" "false"
// ERROR: Unresolved reference: foo()["str"]
// ERROR: No set method providing array access

package testing

import some.Some

fun foo(): Some = Some()

fun testing() {
    foo()<caret>["str"] = 1
}

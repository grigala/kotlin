// "Import" "true"
// ERROR: Unresolved reference: foo()["str"]
// ERROR: No get method providing array access

package testing

import some.Some

fun foo(): Some = Some()

fun testing() {
    foo()<caret>["other"] = 1
}

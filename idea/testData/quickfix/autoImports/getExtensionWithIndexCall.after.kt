// "Import" "true"
// ERROR: Unresolved reference: foo()["str"]
// ERROR: No get method providing array access

package testing

import some.Some
import some.get

fun foo(): Some = Some()

fun testing() {
    foo()["str"]
}

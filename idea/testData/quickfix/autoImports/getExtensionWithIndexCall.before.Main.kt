// "Import" "true"
// ERROR: Unresolved reference: Some()["str"]
// ERROR: No get method providing array access

package testing

import some.Some

fun testing() {
    Some()<caret>["str"]
}

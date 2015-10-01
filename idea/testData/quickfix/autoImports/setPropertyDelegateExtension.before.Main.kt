// "Import" "true"
// ERROR: Missing 'set(testing.BigTest, kotlin.PropertyMetadata, kotlin.Int)' method on delegate of type 'some.DelegateImpl<kotlin.Int>'

package testing

import some.DelegateImpl
import some.get

class BigTest {
    var a by <caret>DelegateImpl<Int>()
}
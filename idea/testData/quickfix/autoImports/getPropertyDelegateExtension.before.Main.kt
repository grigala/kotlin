// "Import" "true"
// ERROR: Missing 'get(testing.BigTest, kotlin.PropertyMetadata)' method on delegate of type 'some.DelegateImpl<kotlin.Int>'

package testing

import some.DelegateImpl

class BigTest {
    val a by <caret>DelegateImpl<Int>()
}
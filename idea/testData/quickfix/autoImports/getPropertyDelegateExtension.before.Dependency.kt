package some

class DelegateImpl<T> {
    val value: T = null!!
}

public fun <T> DelegateImpl<T>.get(thisRef: Any?, property: PropertyMetadata): T = value

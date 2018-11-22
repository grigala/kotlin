// TODO: muted automatically, investigate should it be ran for JVM_IR or not
// IGNORE_BACKEND: JVM_IR

// Auto-generated by org.jetbrains.kotlin.generators.tests.GenerateRangesCodegenTestData. DO NOT EDIT!
// WITH_RUNTIME



fun box(): String {
    val list1 = ArrayList<Int>()
    val range1 = 3..9
    for (i in range1) {
        list1.add(i)
        if (list1.size > 23) break
    }
    if (list1 != listOf<Int>(3, 4, 5, 6, 7, 8, 9)) {
        return "Wrong elements for 3..9: $list1"
    }

    val list2 = ArrayList<Int>()
    val range2 = 3.toByte()..9.toByte()
    for (i in range2) {
        list2.add(i)
        if (list2.size > 23) break
    }
    if (list2 != listOf<Int>(3, 4, 5, 6, 7, 8, 9)) {
        return "Wrong elements for 3.toByte()..9.toByte(): $list2"
    }

    val list3 = ArrayList<Int>()
    val range3 = 3.toShort()..9.toShort()
    for (i in range3) {
        list3.add(i)
        if (list3.size > 23) break
    }
    if (list3 != listOf<Int>(3, 4, 5, 6, 7, 8, 9)) {
        return "Wrong elements for 3.toShort()..9.toShort(): $list3"
    }

    val list4 = ArrayList<Long>()
    val range4 = 3L..9L
    for (i in range4) {
        list4.add(i)
        if (list4.size > 23) break
    }
    if (list4 != listOf<Long>(3, 4, 5, 6, 7, 8, 9)) {
        return "Wrong elements for 3L..9L: $list4"
    }

    val list5 = ArrayList<Char>()
    val range5 = 'c'..'g'
    for (i in range5) {
        list5.add(i)
        if (list5.size > 23) break
    }
    if (list5 != listOf<Char>('c', 'd', 'e', 'f', 'g')) {
        return "Wrong elements for 'c'..'g': $list5"
    }

    val list6 = ArrayList<UInt>()
    val range6 = 3u..5u
    for (i in range6) {
        list6.add(i)
        if (list6.size > 23) break
    }
    if (list6 != listOf<UInt>(3u, 4u, 5u)) {
        return "Wrong elements for 3u..5u: $list6"
    }

    val list7 = ArrayList<UInt>()
    val range7 = 3.toUByte()..5.toUByte()
    for (i in range7) {
        list7.add(i)
        if (list7.size > 23) break
    }
    if (list7 != listOf<UInt>(3u, 4u, 5u)) {
        return "Wrong elements for 3.toUByte()..5.toUByte(): $list7"
    }

    val list8 = ArrayList<UInt>()
    val range8 = 3.toUShort()..5.toUShort()
    for (i in range8) {
        list8.add(i)
        if (list8.size > 23) break
    }
    if (list8 != listOf<UInt>(3u, 4u, 5u)) {
        return "Wrong elements for 3.toUShort()..5.toUShort(): $list8"
    }

    val list9 = ArrayList<ULong>()
    val range9 = 3uL..5uL
    for (i in range9) {
        list9.add(i)
        if (list9.size > 23) break
    }
    if (list9 != listOf<ULong>(3u, 4u, 5u)) {
        return "Wrong elements for 3uL..5uL: $list9"
    }

    return "OK"
}

// Auto-generated by org.jetbrains.kotlin.generators.tests.GenerateRangesCodegenTestData. DO NOT EDIT!
// WITH_RUNTIME


const val MaxI = Int.MAX_VALUE
const val MaxL = Long.MAX_VALUE
const val MaxUI = UInt.MAX_VALUE
const val MaxUL = ULong.MAX_VALUE

fun box(): String {
    val list1 = ArrayList<Int>()
    for (i in 0 downTo MaxI step 3) {
        list1.add(i)
        if (list1.size > 23) break
    }
    if (list1 != listOf<Int>()) {
        return "Wrong elements for 0 downTo MaxI step 3: $list1"
    }

    val list2 = ArrayList<Long>()
    for (i in 0 downTo MaxL step 3) {
        list2.add(i)
        if (list2.size > 23) break
    }
    if (list2 != listOf<Long>()) {
        return "Wrong elements for 0 downTo MaxL step 3: $list2"
    }

    val list3 = ArrayList<UInt>()
    for (i in 0u downTo MaxUI step 3) {
        list3.add(i)
        if (list3.size > 23) break
    }
    if (list3 != listOf<UInt>()) {
        return "Wrong elements for 0u downTo MaxUI step 3: $list3"
    }

    val list4 = ArrayList<ULong>()
    for (i in 0uL downTo MaxUL step 3) {
        list4.add(i)
        if (list4.size > 23) break
    }
    if (list4 != listOf<ULong>()) {
        return "Wrong elements for 0uL downTo MaxUL step 3: $list4"
    }

    return "OK"
}

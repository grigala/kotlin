import test.*

fun test1(): String {
    try {
        doCall ext@ {
            try {
                doCall {
                    if (1 == 1) {
                        return@test1 "a"
                    }
                    else if (2 == 2) {
                        return@ext "b"
                    }
                }

                if (3 == 3) {
                    return "d"
                }

                "e"
            }
            finally {
                val aaa = 1
            }
        }

        return "f"
    }
    finally {
        val bbb = 2
    }
}

fun box(): String {
    test1()

    return "OK"
}

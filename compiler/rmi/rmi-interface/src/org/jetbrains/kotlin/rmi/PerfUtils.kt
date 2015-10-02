/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.rmi

import java.lang.management.ManagementFactory
import java.lang.management.ThreadMXBean

interface PerfCounters {
    public val count: Long
    public val time: Long
    public val threadTime: Long
    public val threadUserTime: Long
    public val memory: Long

    public fun addMeasurement(time: Long = 0, thread: Long = 0, threadUser: Long = 0, memory: Long = 0)
}

interface Profiler {
    public fun getCounters(obj: Any): PerfCounters
    public fun getTotalCounters(): PerfCounters

    public fun<R> withMeasure(obj: Any?, body: () -> R): R
}


class SimplePerfCounters : PerfCounters {
    private @Volatile var _count: Long = 0
    private @Volatile var _time: Long = 0L
    private @Volatile var _threadTime: Long = 0L
    private @Volatile var _threadUserTime: Long = 0L
    private @Volatile var _memory: Long = 0

    override val count: Long get() = _count
    override val time: Long get() = _time
    override val threadTime: Long get() = _threadTime
    override val threadUserTime: Long get() = _threadUserTime
    override val memory: Long get() = _memory

    public override fun addMeasurement(time: Long, thread: Long, threadUser: Long, memory: Long) {
        _count++
        _time += time
        _threadTime += thread
        _threadUserTime += threadUser
        _memory += memory
    }
}


@Suppress("NOTHING_TO_INLINE")
inline fun ThreadMXBean.threadCpuTime() = if (isCurrentThreadCpuTimeSupported) currentThreadCpuTime else 0L

@Suppress("NOTHING_TO_INLINE")
inline fun ThreadMXBean.threadUserTime() = if (isCurrentThreadCpuTimeSupported) currentThreadUserTime else 0L

@Suppress("NOTHING_TO_INLINE")
inline fun usedMemory(withGC: Boolean): Long {
    if (withGC) {
        System.gc()
    }
    val rt = Runtime.getRuntime()
    return (rt.totalMemory() - rt.freeMemory())
}


inline fun<R> withMeasureWallTime(perfCounters: PerfCounters, body: () -> R): R {
    val startTime = System.nanoTime()
    val res = body()
    perfCounters.addMeasurement(time = System.nanoTime() - startTime) // TODO: add support for wrapping
    return res
}


inline fun<R> withMeasureWallAndThreadTimes(perfCounters: PerfCounters, threadMXBean: ThreadMXBean, body: () -> R): R {
    val startTime = System.nanoTime()
    val startThreadTime = threadMXBean.threadCpuTime()
    val startThreadUserTime = threadMXBean.threadUserTime()
    val res = body()
    // TODO: add support for wrapping
    perfCounters.addMeasurement(time = System.nanoTime() - startTime,
                                thread = threadMXBean.threadCpuTime() - startThreadTime,
                                threadUser = threadMXBean.threadUserTime() - startThreadUserTime)
    return res
}

inline fun<R> withMeasureWallAndThreadTimes(perfCounters: PerfCounters, body: () -> R): R = withMeasureWallAndThreadTimes(perfCounters, ManagementFactory.getThreadMXBean(), body)


inline fun<R> withMeasureWallAndThreadTimesAndMemory(perfCounters: PerfCounters, withGC: Boolean = false, threadMXBean: ThreadMXBean, body: () -> R): R {
    val startMem = usedMemory(withGC)
    val startTime = System.nanoTime()
    val startThreadTime = threadMXBean.threadCpuTime()
    val startThreadUserTime = threadMXBean.threadUserTime()
    val res = body()
    // TODO: add support for wrapping
    perfCounters.addMeasurement(time = System.nanoTime() - startTime,
                                thread = threadMXBean.threadCpuTime() - startThreadTime,
                                threadUser = threadMXBean.threadUserTime() - startThreadUserTime,
                                memory = usedMemory(withGC) - startMem)
    return res
}


inline fun<R> withMeasureWallAndThreadTimesAndMemory(perfCounters: PerfCounters, withGC: Boolean, body: () -> R): R =
        withMeasureWallAndThreadTimesAndMemory(perfCounters, withGC, ManagementFactory.getThreadMXBean(), body)


class DummyProfiler : Profiler {
    override fun getCounters(obj: Any): PerfCounters = SimplePerfCounters()
    override fun getTotalCounters(): PerfCounters = SimplePerfCounters()

    override final inline fun <R> withMeasure(obj: Any?, body: () -> R): R = body()
}


class WallTotalProfiler : Profiler {

    val total = SimplePerfCounters()

    override fun getCounters(obj: Any): PerfCounters = total
    override fun getTotalCounters(): PerfCounters = total

    override final inline fun <R> withMeasure(obj: Any?, body: () -> R): R = withMeasureWallTime(total, body)
}


class WallAndThreadTotalProfiler : Profiler {

    val total = SimplePerfCounters()
    val threadMXBean = ManagementFactory.getThreadMXBean()

    override fun getCounters(obj: Any): PerfCounters = total
    override fun getTotalCounters(): PerfCounters = total

    override final inline fun <R> withMeasure(obj: Any?, body: () -> R): R = withMeasureWallAndThreadTimes(total, threadMXBean, body)
}


class WallAndThreadAndMemoryTotalProfiler(val withGC: Boolean) : Profiler {

    val total = SimplePerfCounters()
    val threadMXBean = ManagementFactory.getThreadMXBean()

    override fun getCounters(obj: Any): PerfCounters = total
    override fun getTotalCounters(): PerfCounters = total

    override final inline fun <R> withMeasure(obj: Any?, body: () -> R): R = withMeasureWallAndThreadTimesAndMemory(total, withGC, threadMXBean, body)
}

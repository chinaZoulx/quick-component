package org.quick.component

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors

/**
 * @describe 方便的使用简单异步，此类只做简单异步封装，推荐使用RxAndroid
 * @author ChrisZou
 * @date 2018/08/03-10:16
 * @from https://github.com/SpringSmell/quick.library
 * @email chrisSpringSmell@gmail.com
 */
object QuickASync {

    private val executorService = Executors.newFixedThreadPool(10)
    private val mainHandler: Handler by lazy { return@lazy Handler(Looper.getMainLooper()) }

    /**
     * 异常线程处理数据，然后返回值
     */
    fun <T> async(onASyncListener: OnASyncListener<T>) {
        executorService.submit {
            val value = onASyncListener.onASync()
            mainHandler.post { onASyncListener.onAccept(value) }
        }
    }

    /**
     * 秒表计步，for example:60秒（1....60）
     * @param interval 间隔时间，单位：毫秒
     * @param maxTime 最大时间，单位：毫秒
     */
    fun <T> async(onIntervalListener: OnIntervalListener<T>, interval: Long, maxTime: Long, isReversal: Boolean = false) {
        executorService.submit {
            var steps = if (isReversal) maxTime else 0
            if (isReversal)
                while (steps > 0) {
                    Thread.sleep(interval)
                    steps--
                    mainHandler.post { onIntervalListener.onNext(steps as T) }
                }
            else
                while (steps < maxTime) {
                    Thread.sleep(interval)
                    steps++
                    mainHandler.post { onIntervalListener.onNext(steps as T) }
                }
            mainHandler.post { onIntervalListener.onAccept(steps as T) }
        }
    }


    interface OnIntervalListener<T> : Consumer<T> {
        fun onNext(value: T)
    }

    interface OnASyncListener<T> : Consumer<T> {
        fun onASync(): T
    }

    interface Consumer<T> {
        /**
         * Consume the given value.
         * @param value the value
         * @throws Exception on error
         */
        @Throws(Exception::class)
        fun onAccept(value: T)
    }
}
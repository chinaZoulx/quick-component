package org.quick.component.http.callback

import java.io.File
import java.io.IOException

/**
 * 请求进度监听
 */
interface OnProgressListener : OnProgressCallBack {
    fun onStart() {}
    fun onFailure(e: IOException, isNetworkError: Boolean)
    fun onResponse(file: File)
    fun onEnd() {}
}
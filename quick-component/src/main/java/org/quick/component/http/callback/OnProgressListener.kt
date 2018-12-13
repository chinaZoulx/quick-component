package org.quick.component.http.callback

import java.io.File

/**
 * 请求进度监听
 */
interface OnProgressListener : OnProgressCallBack {
    fun onStart() {}
    fun onFailure(e: Exception, isNetworkError: Boolean)
    fun onResponse(file: File)
    fun onEnd() {}
}
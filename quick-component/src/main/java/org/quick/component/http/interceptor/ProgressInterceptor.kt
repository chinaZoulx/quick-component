package org.quick.component.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import org.quick.component.http.HttpService
import org.quick.component.http.callback.OnProgressCallBack
import org.quick.component.http.ProgressResponseBody


/**
 * 下载进度监听-拦截器
 */
class ProgressInterceptor(var builder: HttpService.Builder, var onProgressCallBack: OnProgressCallBack) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        return originalResponse.newBuilder()
                .body(ProgressResponseBody(builder, originalResponse.body()!!, onProgressCallBack))
                .build()
    }
}
package com.androidavid.credibanco.api

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Content-type","application/json")
            .addHeader("authorization","Basic ")
            .build()
        return chain.proceed(request)
    }
}
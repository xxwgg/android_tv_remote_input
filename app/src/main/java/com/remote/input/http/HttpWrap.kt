package com.remote.input.http

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Proxy
import java.util.concurrent.TimeUnit


object HttpWrap {

    fun getInstance(): Api {

        val okHttpClientBuilder = OkHttpClient.Builder()
            .proxy(Proxy.NO_PROXY)
            .retryOnConnectionFailure(true)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)

        val okHttpClient = okHttpClientBuilder.build()

        val builder = Retrofit.Builder().client(okHttpClient).baseUrl(Api.base_url)
        builder.addConverterFactory(GsonConverterFactory.create())

        return builder.build().create(Api::class.java)
    }

}
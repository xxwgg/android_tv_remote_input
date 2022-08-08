package com.remote.input.http

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface Api {

    companion object {
        const val base_url = "https://www.remote.com/"
    }

    //获取微信 access_token
    //https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=appId$secrect=123
    //根目录里附了php版本的
    @GET("mpToken.php")
    suspend fun getAccessToken(): AccessToken

    //获取二维码
    @Streaming
    @POST
    suspend fun getQrImage(@Url url: String, @Body scene: RequestBody): ResponseBody


}
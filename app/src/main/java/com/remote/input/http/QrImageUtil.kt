package com.remote.input.http

import android.content.Context
import android.widget.ImageView
import cn.jpush.android.api.JPushInterface
import coil.load
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import com.remote.input.MyApp
import com.remote.input.log

object QrImageUtil {

    // key secret
    private const val pushKey = "key"
    private const val pushSecret = "secret"

    /**
     *
     */
    suspend fun showAndDownQrImage(imageView: ImageView) {
        if (JPushInterface.getRegistrationID(MyApp.app).isEmpty()) {
            "id 为空".log()
            return
        }
        JPushInterface.getRegistrationID(MyApp.app).let { channelId ->
            val fileName = "$channelId.jpeg"

            fileName.log()

            val qrFile = MyApp.app.getFileStreamPath(fileName)

            if (qrFile.exists()) {
                withContext(Dispatchers.Main) {
                    try {
                        imageView.load(qrFile)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                "showAndDownQrImage 取缓存的二维码 channelId: $channelId".log()
                return@let
            }
            //
            "showAndDownQrImage 需要获取二维码 channelId: $channelId".log()

            val token = withContext(Dispatchers.IO) {
                HttpWrap.getInstance().getAccessToken()
            }

            token.access_token.let { access_token ->
                withContext(Dispatchers.IO) {
                    //
                    try {
                        //Gson转的话 会把=号重新编码
                        val bodyString = RequestBody.create(
                            MediaType.parse("text/plain"),
                            "{\"scene\":\"cid=${channelId}&s=2\"}"
                        )

//                        "{\"scene\":\"cid=${channelId}&pk=$pushKey&ps=$pushSecret\"}"
//scene只能传32个字符
                        val buffer = HttpWrap.getInstance().getQrImage(
                            "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=$access_token",
                            bodyString
                        )

                        access_token.log()

                        "qrimage url: https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=$access_token".log()
                        "{\"scene\":\"c=${channelId}&s=2\"}".log()


                        val fileOutput = MyApp.app.openFileOutput(fileName, Context.MODE_PRIVATE)
                        fileOutput.write(buffer.bytes())
                        fileOutput.flush()
                        fileOutput.close()

                        val file = MyApp.app.getFileStreamPath(fileName)
                        withContext(Dispatchers.Main) {
                            imageView.load(file)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        }

    }
}
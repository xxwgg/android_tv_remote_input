package com.remote.input

import android.os.Build
import androidx.multidex.MultiDexApplication
import cn.jpush.android.api.JPushInterface
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder

class MyApp : MultiDexApplication(), ImageLoaderFactory {

    companion object {
        lateinit var app: MyApp
    }

    override fun onCreate() {
        super.onCreate()
        app = this

        JPushInterface.setDebugMode(BuildConfig.DEBUG)
        JPushInterface.init(this)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }.crossfade(true)
            .build()
    }

}
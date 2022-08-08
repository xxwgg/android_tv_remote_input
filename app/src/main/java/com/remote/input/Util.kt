package com.remote.input

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils.SimpleStringSplitter
import android.util.Log
import android.view.View
import com.google.gson.Gson
import timber.log.Timber

private const val EXTRA_FRAGMENT_ARG_KEY = ":settings:fragment_args_key";
private const val EXTRA_SHOW_FRAGMENT_ARGUMENTS = ":settings:show_fragment_args";

fun jumpAccessibilitySetting(activity: Activity) {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    val bundle = Bundle()
    val componentName = ComponentName(
        BuildConfig.APPLICATION_ID,
        MyAccessibilityService::class.java.name
    ).flattenToString()
    bundle.putString(EXTRA_FRAGMENT_ARG_KEY, componentName)
    intent.putExtra(EXTRA_FRAGMENT_ARG_KEY, componentName)

    intent.putExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS, bundle)

    try {
        activity.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

fun isAccessibilitySettingsOn(context: Context): Boolean {
    var accessibilityEnable = 0

    //todo
    val serviceName = "com.remote.input/com.remote.input.MyAccessibilityService"
    try {
        accessibilityEnable = Settings.Secure.getInt(
            context.contentResolver,
            Settings.Secure.ACCESSIBILITY_ENABLED
        )
    } catch (e: Exception) {
        Timber.tag("Accessibility").e("get accessibility enable failed, the err:%s", e.message)
    }
    if (1 == accessibilityEnable) {
        val mStringColonSplitter = SimpleStringSplitter(':')
        val settingValue: String = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        mStringColonSplitter.setString(settingValue)
        while (mStringColonSplitter.hasNext()) {
            val accessibilityService = mStringColonSplitter.next()
            if (accessibilityService.equals(serviceName, ignoreCase = true)) {
                Timber.tag("Accessibility").e("accessibility service: $serviceName  is on.")
                return true
            }
        }
    } else {
        Timber.tag("Accessibility").e("accessibility service disable.")
    }
    return false
}

//

fun enableAccessibility(context: Context) {
    try {
        Settings.Secure.putString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
            "re.mote.input/com.remote.input.MyAccessibilityService"
        )
        Settings.Secure.putString(
            context.contentResolver,
            Settings.Secure.ACCESSIBILITY_ENABLED, "1"
        )
        Log.e("Accessibility", "enableAccessibility 1.")
    } catch (e: Exception) {
        Log.e("Accessibility", "enableAccessibility ${e.message}")
    }
}

fun Any.log() {
    Log.d("remote_input", this.toString())
}

fun <T : Any> String.toObj(clazz: Class<T>): T {
    return Gson().fromJson(this, clazz)
}

fun View.show() {
    this.visibility = View.VISIBLE
}


fun View.gone() {
    this.visibility = View.GONE
}

fun String.copy2Clip() {
    try {
        val text = this
        // 获取系统剪贴板
        try {
            Looper.prepare()
        } catch (e: Exception) {
        }

        val clipboard = MyApp.app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("remote_input", text)
        clipboard.setPrimaryClip(clipData)

    } catch (e: Exception) {
        e.printStackTrace()
    }
}
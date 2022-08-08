package com.remote.input

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.remote.input.ui.login.InputFinder

class MyAccessibilityService : AccessibilityService() {

    /**
     * 当用户开启辅助功能会回调该函数
     */
    override fun onServiceConnected() {
        super.onServiceConnected()
    }

    /**
     * 当收到通过配置过滤的相关事件
     */
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.d("MyAccessibilityService", "eventType: ${event?.eventType}")

        InputFinder.findInput(rootInActiveWindow)

    }

    /**
     * 服务断开时回调
     */
    override fun onInterrupt() {
        Log.d("MyAccessibilityService", "onInterrupt")
    }
}
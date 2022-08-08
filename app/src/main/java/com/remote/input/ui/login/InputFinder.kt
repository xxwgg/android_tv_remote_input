package com.remote.input.ui.login

import android.view.accessibility.AccessibilityNodeInfo
import com.remote.input.copy2Clip
import com.remote.input.log


object InputFinder {

    private var currentFocusInput: AccessibilityNodeInfo? = null

    fun findInput(nodeInfo: AccessibilityNodeInfo?): AccessibilityNodeInfo? {
        val focusInput = nodeInfo?.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)

        if (focusInput?.isEditable == true) {
            currentFocusInput = focusInput

            "获取到focus节点：${focusInput.className} ${focusInput.text}".log()

        } else if (currentFocusInput == null && nodeInfo != null && nodeInfo.childCount > 0) {
            //
            kkk@ for (index in 0 until nodeInfo.childCount) {
                val item = nodeInfo.getChild(index)
                if (item.isEditable) {
                    currentFocusInput = item

                    "获取到节点：${item.className} ${item.text}".log()
                    break@kkk
                }
            }
        }

        return currentFocusInput
    }

    fun setText(string: String) {
        try {
            //兼容21以下 使用剪贴板传递

            string.copy2Clip()
            currentFocusInput?.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
            currentFocusInput?.performAction(AccessibilityNodeInfo.ACTION_PASTE)
            //
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
package com.remote.input.push

import android.content.Context
import cn.jpush.android.api.CustomMessage
import cn.jpush.android.api.JPushInterface
import cn.jpush.android.service.JPushMessageReceiver
import org.greenrobot.eventbus.EventBus
import com.remote.input.log
import com.remote.input.toObj
import com.remote.input.ui.login.InputFinder

class PushReceiver : JPushMessageReceiver() {

    override fun onConnected(p0: Context?, p1: Boolean) {
        super.onConnected(p0, p1)
        ("push onConnected $p1 rid: ${JPushInterface.getRegistrationID(p0)}").log()

        val msg = PushConnectedEvent()
        EventBus.getDefault().post(msg)
    }

    override fun onMessage(p0: Context?, message: CustomMessage?) {
        super.onMessage(p0, message)
        "push onMessage $message".log()

        message?.message?.let {
            try {
                val msg = it.toObj(PushMsgEvent::class.java)
                EventBus.getDefault().post(msg)

                InputFinder.setText(msg.msg)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }
}
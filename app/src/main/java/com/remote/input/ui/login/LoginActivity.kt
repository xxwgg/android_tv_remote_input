package com.remote.input.ui.login

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.permissionx.guolindev.PermissionX
import com.remote.input.enableAccessibility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.remote.input.*
import com.remote.input.databinding.ActivityLoginBinding
import com.remote.input.http.QrImageUtil
import com.remote.input.isAccessibilitySettingsOn
import com.remote.input.jumpAccessibilitySetting
import com.remote.input.log
import com.remote.input.push.PushConnectedEvent
import com.remote.input.push.PushMsgEvent

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch(Dispatchers.IO) {
            QrImageUtil.showAndDownQrImage(binding.qrImg)
        }

        binding.permissionText.setOnClickListener {
            jumpAccessibilitySetting(this@LoginActivity)
        }

        binding.copy.setOnClickListener {

            binding.input.text.log()

            binding.input.text.toString().copy2Clip()

            Toast.makeText(this@LoginActivity, "已复制", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        requestPermissions()

        if (isAccessibilitySettingsOn(this)) {
            binding.permissionText.gone()
        } else {
            binding.permissionText.show()
        }

        binding.copy.requestFocus()
    }


    private fun requestPermissions() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.WRITE_SECURE_SETTINGS
            )
            .onExplainRequestReason { _, _ ->
//                scope.showRequestReasonDialog(deniedList, "需要开启服务", "同意", "拒绝")
            }
            .onForwardToSettings { _, _ ->

//                val dialog = AlertDialog.Builder(this@LoginActivity)
//                    .setMessage("需要开启服务才能更好的使用")
//                    .setNegativeButton("设置") { dialog, which ->
//                        jumpAccessibilitySetting(this@LoginActivity)
//                    }
//                    .setPositiveButton("取消") { dialog, which ->
//                        dialog.dismiss()
//                    }
//                dialog.show()
            }
            .request { allGranted, _, _ ->
                if (allGranted) {
                    //
                    enableAccessibility(this)
                } else {
                }
            }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(pushMsgEvent: PushMsgEvent) {
        "SearchResultFragment onEvent $pushMsgEvent".log()

        if (!isAccessibilitySettingsOn(this)) {
            binding.input.text.clear()
            binding.input.setText(pushMsgEvent.msg)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(pushMsgEvent: PushConnectedEvent) {
        "SearchResultFragment onEvent $pushMsgEvent".log()

        lifecycleScope.launchWhenStarted {
            withContext(Dispatchers.IO) {
                QrImageUtil.showAndDownQrImage(binding.qrImg)
            }
        }

    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)
    }

}
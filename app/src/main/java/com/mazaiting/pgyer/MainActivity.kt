package com.mazaiting.pgyer

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mazaiting.common.LOG_DEBUG
import com.mazaiting.common.browse
import com.mazaiting.common.debug
import com.mazaiting.common.toast
import com.mazaiting.pgyer.update.UpdateManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LOG_DEBUG = true
        // api key
        val apiKey = "c65f609fd16e7d4834a1f7e46e105ca2"
        // app key
        val appKey = "a370d65d7e2eb84cb659ad1ed079799b"

        UpdateManager.getInstance()
            .check(this, apiKey, appKey, BuildConfig.VERSION_NAME) {
                debug(it.toString())
                // 更新描述
                val description = if (it.buildUpdateDescription.isEmpty()) {
                    "无"
                } else {
                    it.buildUpdateDescription
                }
                val msg = StringBuilder()
                    .append("最新构建版本: ${it.buildBuildVersion} \n")
                    .append("更新内容: $description")
                AlertDialog
                    .Builder(this)
                    .setTitle("更新提示")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage(msg)
                    .setPositiveButton(
                        "确定"
                    ) { _, _ ->
                        toast("确定")
                        browse(it.buildShortcutUrl)
                    }
                    .setNegativeButton("取消") { _, _ ->
                        toast("取消")
                    }
                    .setCancelable(false)
                    .create()
                    .show()
            }
    }

}


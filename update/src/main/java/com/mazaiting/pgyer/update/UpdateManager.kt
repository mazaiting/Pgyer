package com.mazaiting.pgyer.update

import android.content.Context
import com.mazaiting.common.LOG_DEBUG
import com.mazaiting.common.debug
import com.mazaiting.common.response
import com.mazaiting.common.toast
import com.mazaiting.common.util.SpUtil
import com.mazaiting.pgyer.bean.CheckBean
import com.mazaiting.pgyer.util.RxUtil
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/***
 *
 *
 *                                                    __----~~~~~~~~~~~------___
 *                                   .  .   ~~//====......          __--~ ~~
 *                   -.            \_|//     |||\\  ~~~~~~::::... /~
 *                ___-==_       _-~o~  \/    |||  \\            _/~~-
 *        __---~~~.==~||\=_    -_--~/_-~|-   |\\   \\        _/~
 *    _-~~     .=~    |  \\-_    '-~7  /-   /  ||    \      /
 *  .~       .~       |   \\ -_    /  /-   /   ||      \   /
 * /  ____  /         |     \\ ~-_/  /|- _/   .||       \ /
 * |~~    ~~|--~~~~--_ \     ~==-/   | \~--===~~        .\
 *          '         ~-|      /|    |-~\~~       __--~~
 *                      |-~~-_/ |    |   ~\_   _-~            /\
 *                           /  \     \__   \/~                \__
 *                       _--~ _/ | .-~~____--~-/                  ~~==.
 *                      ((->/~   '.|||' -_|    ~~-/ ,              . _||
 *                                 -_     ~\      ~~---l_
 *                                 _i__i__i--~~_/
 *                                 _-~-__   ~)  \--______________--~~
 *                               //.-~~~-~_--~- |-------~~~~~~~~
 *                                      //.-~~~--\
 *                               神兽保佑
 *                              代码无BUG!
 * @author mazaiting
 * @date 2020/5/18
 * @description 更新管理
 */
class UpdateManager private constructor() {

    companion object {

        /**
         * 更新管理者
         */
        private var instance: UpdateManager? = null

        /**
         * 更新管理者实例
         * @return 更新管理者
         */
        fun getInstance(): UpdateManager {
            if (null == instance)
                synchronized(this) {
                    if (null == instance) {
                        instance = UpdateManager()
                        LOG_DEBUG = true
                    }
                }
            return instance!!
        }
    }

    /**
     * 检测是否更新
     * @param apiKey API KEY
     * @param appKey APP KEY
     * @param buildVersion 当前版本
     * @param block 更新 block
     */
    fun check(
        context: Context,
        apiKey: String,
        appKey: String,
        buildVersion: String = "",
        block: ((CheckBean) -> Unit)? = null
    ) {
        if (apiKey.isEmpty()) {
            context.toast("蒲公英 API KEY 为空! 请设置")
            return
        }
        if (appKey.isEmpty()) {
            context.toast("蒲公英 API KEY 为空! 请设置")
            return
        }
        // 获取蒲公英版本
        val buildBuildVersion = getBuildVersion(context)
        // 获取是否更新
        response({ RxUtil.checkAsync(apiKey, appKey, buildVersion, buildBuildVersion) }, {
            debug(it.message)
            // 判断是否成功
            if (it.isSuccess()) {
                debug(it.data)
                val data = it.data
                // 构建版本
                val newBuildBuildVersion = data.buildBuildVersion
                // 为 0 时直接保存
                if (newBuildBuildVersion > 1 && newBuildBuildVersion > buildBuildVersion) {
                    block?.let { block(data) }
                }
            } else {
                context.toast(it.getFailedMessage())
            }
        }, {
            debug(it)
        })
    }


    /**
     * 读取蒲公英配置内容
     * @param context 上下文
     */
    private fun getBuildVersion(context: Context): Int {
        try {
            // 获取文件流
            val inputStream = context.assets.open("pgyer.json")
            // 读取内容
            val pgyerContent = String(inputStream.readBytes())
            // 解析
            val jsonObject = JSONObject(pgyerContent)
            // 获取版本
            val buildVersion = jsonObject.getInt("build_version")
            debug("buildVersion: $buildVersion")
            return buildVersion
        } catch (e: IOException) {
            debug("解析异常")
        }
        return 1
    }
}
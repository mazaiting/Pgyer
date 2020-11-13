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
import java.io.File
import java.io.FileOutputStream
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
         * 文件名
         */
        private const val FILE_NAME = "pgyer.json"

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
//                        LOG_DEBUG = true
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
//        // 拷贝文件
//        val isCopyAssetsToFile = copyAssetsToFile(context)
//        // 判断是否复制成功
//        if (isCopyAssetsToFile) {
        // 获取蒲公英版本
        val buildBuildVersion = getBuildVersionByAssets(context)
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
                if (newBuildBuildVersion > 1 && newBuildBuildVersion != buildBuildVersion) {
                    block?.let { block(data) }
                }
//                    else if (buildBuildVersion > newBuildBuildVersion) {
//                        writeBuildVersion(context, data.buildBuildVersion)
//                    }
            } else {
                context.toast(it.getFailedMessage())
            }
        }, {
            debug(it)
        })
//        } else {
//            context.toast("复制文件失败")
//        }
    }

    /**
     * 拷贝 Assets 中的 pgyer.json 到 file文件夹
     * @return true: 成功; false: 失败
     */
    private fun copyAssetsToFile(context: Context): Boolean {
        // 创建文件
        val file = File(context.filesDir.absolutePath, FILE_NAME)
        // 如果文件存在, 则删除
        if (file.exists()) {
            return true
        }
        // 获取文件流
        val inputStream = context.assets.open(FILE_NAME)
        // 输出流
        val fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
        return try {
            fos.write(inputStream.readBytes())
            fos.close()
            inputStream.close()
            true
        } catch (e: IOException) {
            debug("解析异常")
            fos.close()
            inputStream.close()
            false
        }
    }

    /**
     * 写入蒲公英配置内容
     * @param context 上下文
     * @param buildBuildVersion 当前版本
     */
    private fun writeBuildVersion(context: Context, buildBuildVersion: Int) {
        try {
            // 获取文件流
            val inputStream = context.openFileInput(FILE_NAME)
            // 读取内容
            val pgyerContent = String(inputStream.readBytes())
            inputStream.close()
            // 解析
            val jsonObject = JSONObject(pgyerContent)
            jsonObject.put("build_version", buildBuildVersion)
            // 输出流
            val fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            fos.write(jsonObject.toString().toByteArray())
            fos.close()
        } catch (e: IOException) {
            debug("解析异常")
        }
    }

    /**
     * 读取蒲公英配置内容
     * @param context 上下文
     */
    private fun getBuildVersionByAssets(context: Context): Int {
        try {
            // 获取文件流
            val inputStream = context.assets.open(FILE_NAME)
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

    /**
     * 读取蒲公英配置内容
     * @param context 上下文
     */
    private fun getBuildVersion(context: Context): Int {
        try {
            // 获取文件流
            val inputStream = context.openFileInput(FILE_NAME)
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
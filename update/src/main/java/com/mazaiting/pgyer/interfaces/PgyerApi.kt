package com.mazaiting.pgyer.interfaces

import com.mazaiting.pgyer.bean.CheckBean
import com.mazaiting.pgyer.bean.ResponseBean
import kotlinx.coroutines.Deferred
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
 *                                 -_     ~\      ~~---l__i__i__i--~~_/
 *                                 _-~-__   ~)  \--______________--~~
 *                               //.-~~~-~_--~- |-------~~~~~~~~
 *                                      //.-~~~--\
 *                               神兽保佑
 *                              代码无BUG!
 * @author mazaiting
 * @date 2020/5/18
 * @description 蒲公英接口
 */
public interface PgyerApi {
    /**
     * 检查 APP 是否更新
     */
    @FormUrlEncoded
    @POST("app/check")
    fun checkAsync(
        /**
         * (必填) API Key
         */
        @Field("_api_key") apiKey: String,
        /**
         * (必填) 表示一个App组的唯一Key。例如，名称为'微信'的App上传了三个版本，那么这三个版本为一个App组，该参数表示这个组的Key。这个值显示在应用详情--应用概述--App Key。
         */
        @Field("appKey") appKey: String,
        /**
         * (选填) 使用 App 本身的 Build 版本号，Android 对应字段为 versionname ， iOS 对应字段为 version
         */
        @Field("buildVersion") buildVersion: String = "",
        /**
         * (选填) 使用蒲公英生成的自增 Build 版本号
         */
        @Field("buildBuildVersion") buildBuildVersion: Int = 0
    ): Deferred<ResponseBean<CheckBean>>
}

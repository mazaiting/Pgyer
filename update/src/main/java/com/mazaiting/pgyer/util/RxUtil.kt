package com.mazaiting.pgyer.util

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mazaiting.pgyer.bean.CheckBean
import com.mazaiting.pgyer.bean.ResponseBean
import java.util.concurrent.TimeUnit
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.mazaiting.pgyer.interfaces.PgyerApi
import com.mazaiting.pgyer.update.BuildConfig
import kotlinx.coroutines.Deferred

/**
 * 网络工具类
 * @author mazaiting
 * @date 2018/2/6
 */

object RxUtil {
    /**
     * 蒲公英接口
     */
    private const val URL_PYGER = "https://www.pgyer.com/apiv2/"

    /**
     * 获取日志拦截器
     * @return Http日志拦截器
     */
    private val httpLoggingInterceptor: HttpLoggingInterceptor
        get() {
            // 日志显示级别
            val level = HttpLoggingInterceptor.Level.BODY
            // 新建拦截器
            val loggingInterceptor =
                HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                    if (!TextUtils.isEmpty(message)) {
                        Log.d("Smit", message)
                    }
                })
            // 设置显示级别
            loggingInterceptor.level = level
            return loggingInterceptor
        }

    /**
     * 获取
     * @return OkHttpClient.Builder
     */
    private fun getOkHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            // 设置网络请求拦截器
            .addInterceptor(httpLoggingInterceptor)
            // 设置连接超时时间
            .connectTimeout(15, TimeUnit.SECONDS)
            // 设置读取超时时间
            .readTimeout(15, TimeUnit.SECONDS)
            .callTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
    }

    /**
     * 获取 Retrofit
     */
    private fun getRetrofitCoroutine(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(URL_PYGER) //设置网络请求的Url地址
            .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
            .addCallAdapterFactory(CoroutineCallAdapterFactory())//支持协程
            .client(getOkHttpClientBuilder().build())
            .build()
    }

    /**
     * 获取 PgyerApi
     */
    private fun getCardApiCoroutine(): PgyerApi =
        getRetrofitCoroutine().create(PgyerApi::class.java)

    /**
     * 检测 APP 是否更新
     * @param apiKey API KEY
     * @param appKey APP KEY
     * @param buildVersion 当前版本
     * @param buildBuildVersion 蒲公英构建版本
     */
    fun checkAsync(
        apiKey: String,
        appKey: String,
        buildVersion: String = "",
        buildBuildVersion: Int = 0
    ): Deferred<ResponseBean<CheckBean>> {
        return getCardApiCoroutine().checkAsync(apiKey, appKey, buildVersion, buildBuildVersion)
    }
}

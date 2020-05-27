# Pgyer

#### 介绍
 蒲公英相关操作

#### 使用说明

1. 在项目模块目录下的 build.gradle 添加依赖

```
// 自定义插件
classpath 'com.mazaiting.pgyer:update:<版本号>'
```

2. 在启动 Activity 中添加更新检测

```
// api key
val apiKey = ""
// app key
val appKey = ""
UpdateManager.getInstance()
    .check(this, apiKey, appKey) {
        Log.d("MainActivity", it.toString())
    }
```

### 混淆配置

```
##---------------Begin: proguard configuration for Pgyer  ----------
-keep class com.mazaiting.pgyer.bean.** { *; }
##---------------End:   proguard configuration for Pgyer  ----------
```

### 版本

- v1.0.3
1. 修复首次安装提示更新问题
2. 增加混淆配置

- v1.0.2
1. 新增更新block, 将更新处理让使用者自己处理
2. 增加安装包广播

- v1.0.1
1. 新增动态设置 API_KEY 和 APP_KEY

- v1.0.0
1. 完成蒲公英检测更新

#### Contribution

1. [简书地址](https://www.jianshu.com/u/5d2cb4bfeb15)
2. [码云地址](https://gitee.com/)
3. [邮箱](mailto:zaitingma@foxmail.com)
4. [新浪微博](http://blog.sina.com.cn/mazaiting)

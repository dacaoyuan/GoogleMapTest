package com.pactera.googlemaptest

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.google.android.libraries.places.api.Places
import com.vise.xsnow.http.ViseHttp

public class InitApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ViseHttp.init(this);
        ViseHttp.CONFIG()
            //配置请求主机地址
            .baseUrl(Config.GOOGLE_MAP_BASE_URL);
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, Config.API_KAY)
        }

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}
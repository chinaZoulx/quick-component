package org.quick.component.sample

import android.app.Application
import org.quick.component.Log2
import org.quick.component.QuickAndroid

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        QuickAndroid.init(this)
        QuickAndroid.appBaseName = "dfds"
        Log2.isDebug = true
    }
}
package ru.romanoval.myhabits_ca_modules.core

import android.content.Context
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import ru.romanoval.myhabits_ca_modules.di.component.DaggerCoreComponent

class App : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {

        return DaggerCoreComponent
            .builder()
            .application(this)
            .build()

    }

    override fun onCreate() {
        super.onCreate()

    }
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }
}
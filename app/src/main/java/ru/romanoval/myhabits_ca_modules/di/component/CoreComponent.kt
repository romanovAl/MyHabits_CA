package ru.romanoval.myhabits_ca_modules.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ru.romanoval.myhabits_ca_modules.core.App
import ru.romanoval.myhabits_ca_modules.di.builder.ActivityBuilder
import ru.romanoval.myhabits_ca_modules.di.module.ContextModule
import ru.romanoval.myhabits_ca_modules.di.module.DatabaseModule
import ru.romanoval.myhabits_ca_modules.di.module.NetworkModule
import ru.romanoval.myhabits_ca_modules.di.module.TempSortModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    NetworkModule::class,
    ActivityBuilder::class,
    ContextModule::class,
    DatabaseModule::class,
    TempSortModule::class
])
interface CoreComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(application: Application) : Builder

        fun build(): CoreComponent
    }

}
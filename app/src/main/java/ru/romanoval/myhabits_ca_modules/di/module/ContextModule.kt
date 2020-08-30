package ru.romanoval.myhabits_ca_modules.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.romanoval.myhabits_ca_modules.di.builder.ViewModelBuilder
import javax.inject.Singleton

@Module(includes = [ViewModelBuilder::class])
class ContextModule {

    @Provides
    @Singleton
    fun provideContext(application: Application) : Context {
        return application
    }

}
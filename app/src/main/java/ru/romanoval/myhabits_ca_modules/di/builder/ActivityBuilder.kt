package ru.romanoval.myhabits_ca_modules.di.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.romanoval.myhabits_ca_modules.ui.MainActivity

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MainActivityProviders::class])
    abstract fun bindMainActivity() : MainActivity

}
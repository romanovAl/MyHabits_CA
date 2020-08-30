package ru.romanoval.myhabits_ca_modules.di.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.romanoval.myhabits_ca_modules.ui.fragments.AddEditFragment
import ru.romanoval.myhabits_ca_modules.ui.fragments.BadHabitsFragment
import ru.romanoval.myhabits_ca_modules.ui.fragments.GoodHabitsFragment
import ru.romanoval.myhabits_ca_modules.ui.fragments.MainFragment

@Module
abstract class MainActivityProviders {

    @ContributesAndroidInjector
    abstract fun provideMainFragment() : MainFragment

    @ContributesAndroidInjector
    abstract fun provideGoodHabitsFragment(): GoodHabitsFragment

    @ContributesAndroidInjector
    abstract fun provideBadHabitsFragment(): BadHabitsFragment

    @ContributesAndroidInjector
    abstract fun provideAddEditFragment() : AddEditFragment

}
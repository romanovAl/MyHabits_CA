package ru.romanoval.myhabits_ca_modules.di.module

import dagger.Module
import dagger.Provides
import ru.romanoval.data.source.tempSort.SortedHabits
import javax.inject.Singleton

@Module
class TempSortModule {

    @Provides
    @Singleton
    fun provideSortedHabits() : SortedHabits {
        return SortedHabits
    }

}
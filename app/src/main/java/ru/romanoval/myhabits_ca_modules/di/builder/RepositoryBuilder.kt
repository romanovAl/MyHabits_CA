package ru.romanoval.myhabits_ca_modules.di.builder

import dagger.Binds
import dagger.Module
import ru.romanoval.data.repository.AppRepoImpl
import ru.romanoval.domain.repositories.AppRepository

@Module
abstract class RepositoryBuilder {

    @Binds
    abstract fun bindsHabitsRepository(repoImpl: AppRepoImpl) : AppRepository

}
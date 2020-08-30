package ru.romanoval.myhabits_ca_modules.di.builder

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import ru.romanoval.myhabits_ca_modules.ui.ViewModelFactory

@Module(includes = [
    (RepositoryBuilder::class),
    (AppViewModelBuilder::class)
])
abstract class ViewModelBuilder {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory) : ViewModelProvider.Factory

}
package ru.romanoval.myhabits_ca_modules.di.builder

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.romanoval.myhabits_ca_modules.di.qualifier.ViewModelKey
import ru.romanoval.myhabits_ca_modules.ui.viewModels.AddEditViewModel
import ru.romanoval.myhabits_ca_modules.ui.viewModels.BadHabitsViewModel
import ru.romanoval.myhabits_ca_modules.ui.viewModels.GoodHabitsViewModel
import ru.romanoval.myhabits_ca_modules.ui.viewModels.MainViewModel

@Module
abstract class AppViewModelBuilder {

    @Binds
    @IntoMap
    @ViewModelKey(GoodHabitsViewModel::class)
    abstract fun bindGoodHabitsFragment(goodHabitsViewModel: GoodHabitsViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BadHabitsViewModel::class)
    abstract fun bindBadHabitsFragment(badHabitsViewModel: BadHabitsViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainFragment(mainViewModel: MainViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddEditViewModel::class)
    abstract fun bindAddEditViewModel(addEditViewModel: AddEditViewModel) : ViewModel

}
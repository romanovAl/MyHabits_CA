package ru.romanoval.myhabits_ca_modules.core

import ru.romanoval.myhabits_ca_modules.ui.fragments.AddEditFragment
import ru.romanoval.myhabits_ca_modules.ui.fragments.BadHabitsFragment
import ru.romanoval.myhabits_ca_modules.ui.fragments.GoodHabitsFragment

object FragmentFactory {

    fun getGoodHabitsFragment(): GoodHabitsFragment {
        return GoodHabitsFragment.newInstance()
    }

    fun getBadHabitsFragment(): BadHabitsFragment {
        return BadHabitsFragment.newInstance()
    }

    fun getAddEditFragment(): AddEditFragment {
        return AddEditFragment.newInstance()
    }
}
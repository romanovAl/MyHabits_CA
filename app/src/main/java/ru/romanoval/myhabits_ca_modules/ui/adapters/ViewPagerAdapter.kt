package ru.romanoval.myhabits_ca_modules.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.romanoval.myhabits_ca_modules.core.FragmentFactory

class ViewPagerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when(position){
        0 -> FragmentFactory.getGoodHabitsFragment()
        else -> FragmentFactory.getBadHabitsFragment()
    }

}
package ru.romanoval.myhabits_ca_modules.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_good_habits.*
import ru.romanoval.domain.model.Habit
import ru.romanoval.myhabits_ca_modules.R
import ru.romanoval.myhabits_ca_modules.core.Lists
import ru.romanoval.myhabits_ca_modules.ui.adapters.RecyclerAdapter
import ru.romanoval.myhabits_ca_modules.ui.viewModels.GoodHabitsViewModel
import javax.inject.Inject

class GoodHabitsFragment : DaggerFragment(R.layout.fragment_good_habits) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: GoodHabitsViewModel
    private lateinit var adapter: RecyclerAdapter
    private lateinit var curView: View


    companion object {
        @JvmStatic
        fun newInstance() = GoodHabitsFragment()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        curView = view

        init()
    }

    private fun init(){

        viewModel = ViewModelProvider(this, viewModelFactory).get(GoodHabitsViewModel::class.java)

        progressBarGood.visibility = View.VISIBLE
        recyclerGoodHabits.visibility = View.INVISIBLE

        var habitFromAdapter : Habit? = null

        adapter = RecyclerAdapter(emptyList(), requireContext()) {habit ->
            viewModel.addDoneTimes(habit, Lists.getPeriods(requireContext()))

            habitFromAdapter = habit
        }
        recyclerGoodHabits.adapter = adapter

        viewModel.sortedHabits.observe(viewLifecycleOwner, Observer{sortedHabits ->

            viewModel.habits.observe(viewLifecycleOwner, Observer { habits ->

                if(sortedHabits.isNotEmpty()){
                    adapter.updateHabits(sortedHabits.filter { it.type.toBoolean() })
                    recyclerGoodHabits.visibility = View.VISIBLE
                    progressBarGood.visibility = View.INVISIBLE
                }else{
                    adapter.updateHabits(habits)
                    recyclerGoodHabits.visibility = View.VISIBLE
                    progressBarGood.visibility = View.INVISIBLE
                }

            })

        })

        viewModel.message.observe(viewLifecycleOwner, Observer{message ->
            if (message != null){

                Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        if(habitFromAdapter != null){
                            viewModel.removeLastDoneTimes(habitFromAdapter!!, Lists.getPeriods(requireContext()) )
                        }
                    }.show()


            }
        })

        viewModel.messageWithUndo.observe(viewLifecycleOwner, Observer{message ->
            if(message != null){
                Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
            }
        })

    }

    private fun Int.toBoolean(): Boolean {
        return this == 1
    }
}
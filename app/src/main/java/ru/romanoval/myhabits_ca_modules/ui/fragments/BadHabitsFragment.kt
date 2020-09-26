package ru.romanoval.myhabits_ca_modules.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_bad_habits.*
import ru.romanoval.domain.model.Habit
import ru.romanoval.myhabits_ca_modules.R
import ru.romanoval.myhabits_ca_modules.core.Lists
import ru.romanoval.myhabits_ca_modules.ui.adapters.RecyclerAdapter
import ru.romanoval.myhabits_ca_modules.ui.viewModels.BadHabitsViewModel
import javax.inject.Inject

class BadHabitsFragment : DaggerFragment(R.layout.fragment_bad_habits) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: BadHabitsViewModel
    private lateinit var adapter: RecyclerAdapter
    private lateinit var curView: View

    companion object {
        @JvmStatic
        fun newInstance() = BadHabitsFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        curView = view

        init()
    }

    private fun init(){

        val filterTypes = Lists.getFilterTypes(requireContext())

        progressBarBad.visibility = View.VISIBLE
        recyclerBadHabits.visibility = View.INVISIBLE

        viewModel = ViewModelProvider(this, viewModelFactory).get(BadHabitsViewModel::class.java)

        var habitFromAdapter: Habit? = null

        adapter = RecyclerAdapter(emptyList(), requireContext()) { habit ->
            viewModel.addDoneTimes(habit, Lists.getPeriods(requireContext()))
            habitFromAdapter = habit
        }
        recyclerBadHabits.adapter = adapter

        viewModel.sortedHabits.observe(viewLifecycleOwner, Observer {sortedHabits ->

            viewModel.habits.observe(viewLifecycleOwner, Observer{habits ->

                if(sortedHabits.isNotEmpty()){
                    adapter.updateHabits(sortedHabits.filter { !it.type.toBoolean() })
                    progressBarBad.visibility = View.INVISIBLE
                    recyclerBadHabits.visibility = View.VISIBLE
                }else{
                    adapter.updateHabits(habits)
                    progressBarBad.visibility = View.INVISIBLE
                    recyclerBadHabits.visibility = View.VISIBLE
                }

            })

        })

        viewModel.message.observe(viewLifecycleOwner, Observer{message ->
            if(message != null){

                Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        if(habitFromAdapter != null){
                            viewModel.removeLastDoneTimes(habitFromAdapter!!, Lists.getPeriods(requireContext()) )
                        }
                    }.show()
            }
        })

        viewModel.messageWithoutUndo.observe(viewLifecycleOwner, Observer{message ->
            if(message != null){
                Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
            }
        })

    }

    private fun undoAction(){
        viewModel
    }

    private fun Int.toBoolean(): Boolean {
        return this == 1
    }


}
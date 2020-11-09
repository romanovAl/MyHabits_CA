package ru.romanoval.myhabits_ca_modules.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.main_recycler_element.view.*
import ru.romanoval.domain.model.Habit
import ru.romanoval.myhabits_ca_modules.R
import ru.romanoval.myhabits_ca_modules.core.Lists
import ru.romanoval.myhabits_ca_modules.ui.fragments.MainFragmentDirections
import javax.inject.Inject

class RecyclerAdapter(private var habits: List<Habit>, var context: Context, val adapterOnClickAdd: (Habit) -> Unit    ) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    private val priorities = context.resources.getStringArray(R.array.priorities)
    private val periods = context.resources.getStringArray(R.array.periods)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ViewHolder((inflater.inflate(R.layout.main_recycler_element, parent, false)))
    }

    fun updateHabits(newHabits: List<Habit>){
        habits = newHabits
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = habits.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(habits[position], position)
    }

    inner class ViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(habit: Habit, position: Int){
            
            containerView.run {
                constraintMainRecyclerElement.setOnClickListener {
                    val action = MainFragmentDirections.actionMainFragment3ToAddEditFragment(
                        context.resources.getString(R.string.label_edit)
                    )

                    action.habitToEdit = habit

                    Navigation.findNavController(it).navigate(action)
                }

                habitNameRecyclerElement.text = habit.title
                habitDescriptionRecyclerElement.text = habit.description
                habitPeriodRecyclerElement.text = when(habit.frequency){
                    0 -> "${habit.count} ${resources.getString(R.string.times)} ${periods[0]}"
                    1 -> "${habit.count} ${resources.getString(R.string.times)} ${periods[1]}"
                    2 -> "${habit.count} ${resources.getString(R.string.times)} ${periods[2]}"
                    3 -> "${habit.count} ${resources.getString(R.string.times)} ${periods[3]}"
                    4 -> "${habit.count} ${resources.getString(R.string.times)} ${periods[4]}"
                    else -> this.resources.getText(R.string.period_is_not_chosen)
                }
                habitPriorityRecyclerElement.text = when(habit.priority){
                    1 -> "${priorities[1]} ${this.resources.getString(R.string.priority)}"
                    2 -> "${priorities[2]} ${this.resources.getString(R.string.priority)}"
                    else -> priorities[0]
                }

                habitTypeRecyclerElement.text = if (habit.type == 1) {
                    this.resources.getText(R.string.good_habit)
                } else {
                    this.resources.getText(R.string.bad_habit)
                }

                habitNameRecyclerElement.setBackgroundColor(resources.getColor(R.color.colorPrimaryCustom))

                buttonDone.setOnClickListener {
                    adapterOnClickAdd(habit)
                }

            }

        }

    }



}
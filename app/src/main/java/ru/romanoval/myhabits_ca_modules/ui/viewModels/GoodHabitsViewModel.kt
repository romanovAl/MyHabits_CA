package ru.romanoval.myhabits_ca_modules.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import ru.romanoval.domain.model.Habit
import ru.romanoval.domain.useCases.db.GetGoodHabitsDBUseCase
import ru.romanoval.domain.useCases.db.UpdateHabitDBUseCase
import ru.romanoval.domain.useCases.sort.GetSortedHabitsUseCase
import java.util.*
import javax.inject.Inject

class GoodHabitsViewModel @Inject constructor(
    private val getGoodHabitsFromDbUseCase: GetGoodHabitsDBUseCase,
    getSortedHabitsUseCase: GetSortedHabitsUseCase,
    private val updateHabitUseCase: UpdateHabitDBUseCase
) : ViewModel() {

    var habits: LiveData<List<Habit>> = getGoodHabitsFromDbUseCase.getGoodHabits().asLiveData()

    var sortedHabits: LiveData<List<Habit>> = getSortedHabitsUseCase.getSortedHabits().asLiveData()

    val message: MutableLiveData<String> = MutableLiveData()

    val messageWithUndo: MutableLiveData<String> = MutableLiveData()


    fun addDoneTimes(habit: Habit, periods: List<String>) {
        habit.done_dates.add(Calendar.getInstance().time.time)
        calculate(habit, periods, true)
        updateHabitUseCase.habit = habit
        updateHabitUseCase.execute {}
    }

    fun removeLastDoneTimes(habit: Habit, periods: List<String>) {
        habit.done_dates.removeAt(habit.done_dates.lastIndex)
        calculate(habit, periods, false)
        updateHabitUseCase.habit = habit
        updateHabitUseCase.execute {}
    }

    private fun calculate(habit: Habit, periods: List<String>, withUndo: Boolean) {

        val currentTime = Calendar.getInstance().time.time

        when (habit.frequency) {
            0 -> { //per hour
                val from = currentTime - 3600000
                val curCount = habit.done_dates.filter {
                    (it in from..currentTime)
                }.size
                displayMessage(habit.count, curCount, withUndo)

            }
            1 -> { //per day
                val from = currentTime - 86400000
                val curCount = habit.done_dates.filter {
                    (it in from..currentTime)
                }.size
                displayMessage(habit.count, curCount, withUndo)
            }
            2 -> {
                val from = currentTime - 604800000
                val curCount = habit.done_dates.filter {
                    (it in from..currentTime)
                }.size
                displayMessage(habit.count, curCount, withUndo)
            } //per week
            3 -> {
                val from = currentTime - 2592000000
                val curCount = habit.done_dates.filter {
                    (it in from..currentTime)
                }.size
                displayMessage(habit.count, curCount, withUndo)
            } //per month
            4 -> {
                val from = currentTime - 31536000000
                val curCount = habit.done_dates.filter {
                    (it in from..currentTime)
                }.size
                displayMessage(habit.count, curCount, withUndo)
            } //per year
        }
    }

    private fun displayMessage(count: Int, curCount: Int, withUndo: Boolean) {
        when {
            curCount < count -> {
                if (withUndo) {
                    message.value =
                        "Вам стоит выполнить эту привычку еще ${count - curCount} раз(а)"
                } else {
                    messageWithUndo.value =
                        "Вам стоит выполнить эту привычку еще ${count - curCount} раз(а)"
                }
            }
            curCount > count -> {
                if (withUndo) {
                    message.value = "Вы уже выполнили достаточно!"
                } else {
                    messageWithUndo.value = "Вы уже выполнили достаточно!"
                }
            }
            else -> {
                if (withUndo) {
                    message.value = "You're breathtaking!"
                } else {
                    messageWithUndo.value = "You're breathtaking!"
                }


            }
        }
        message.value = null
        messageWithUndo.value = null

    }


}
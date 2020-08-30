package ru.romanoval.myhabits_ca_modules.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.romanoval.domain.model.Habit
import ru.romanoval.domain.model.response.ErrorModel
import ru.romanoval.domain.useCases.db.DeleteHabitDBUseCase
import ru.romanoval.domain.useCases.db.InsertHabitDBUseCase
import ru.romanoval.domain.useCases.db.UpdateHabitDBUseCase
import javax.inject.Inject

class AddEditViewModel @Inject constructor(
    private val insertHabitUseCase: InsertHabitDBUseCase,
    private val deleteHabitUseCase: DeleteHabitDBUseCase,
    private val updateHabitUseCase: UpdateHabitDBUseCase
) : ViewModel(){

    val error: MutableLiveData<ErrorModel> = MutableLiveData<ErrorModel>()

    fun insertHabit(habit: Habit){
        insertHabitUseCase.habit = habit
        insertHabitUseCase.execute {
            onError {
                error.value = it
            }
        }
    }

    fun deleteHabit(habit: Habit){
        deleteHabitUseCase.habit = habit
        deleteHabitUseCase.execute {
            onError {
                error.value = it
            }
        }
    }

    fun updateHabit(habit: Habit){
        updateHabitUseCase.habit = habit
        updateHabitUseCase.execute {
            onError {
                error.value = it
            }
        }
    }

}
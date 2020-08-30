package ru.romanoval.domain.useCases.sort

import ru.romanoval.domain.model.Habit
import ru.romanoval.domain.repositories.AppRepository
import javax.inject.Inject

class SetSortedHabitsUseCase @Inject constructor(
    private val appRepository: AppRepository
) {

    fun setSortedHabits(newHabits: List<Habit>){
        return appRepository.setSortedHabits(newHabits)
    }

}
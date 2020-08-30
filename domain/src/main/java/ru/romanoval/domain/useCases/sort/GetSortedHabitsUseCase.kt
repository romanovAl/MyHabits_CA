package ru.romanoval.domain.useCases.sort

import kotlinx.coroutines.flow.Flow
import ru.romanoval.domain.model.Habit
import ru.romanoval.domain.repositories.AppRepository
import javax.inject.Inject

class GetSortedHabitsUseCase @Inject constructor(
    private val appRepository: AppRepository
) {
    fun getSortedHabits() : Flow<List<Habit>> {
        return appRepository.getSortedHabits()
    }
}
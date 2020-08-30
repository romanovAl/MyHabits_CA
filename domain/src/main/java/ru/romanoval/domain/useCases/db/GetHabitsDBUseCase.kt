package ru.romanoval.domain.useCases.db

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.romanoval.domain.mapper.CloudErrorMapper
import ru.romanoval.domain.model.Habit
import ru.romanoval.domain.repositories.AppRepository
import javax.inject.Inject

class GetHabitsDBUseCase @Inject constructor(
    errorUtil: CloudErrorMapper,
    private val appRepository: AppRepository
)  {

    fun getHabits() : Flow<List<Habit>> {
        return appRepository.selectAllHabits()
    }

}
package ru.romanoval.domain.useCases.db

import kotlinx.coroutines.flow.Flow
import ru.romanoval.domain.mapper.CloudErrorMapper
import ru.romanoval.domain.model.Habit
import ru.romanoval.domain.repositories.AppRepository
import javax.inject.Inject

open class GetGoodHabitsDBUseCase @Inject
constructor(
    errorUtil: CloudErrorMapper,
    private val appRepository: AppRepository
)  {
    open fun getGoodHabits() : Flow<List<Habit>> {
        return appRepository.selectGoodHabitsFromDb()
    }

}
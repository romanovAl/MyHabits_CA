package ru.romanoval.domain.useCases.api

import ru.romanoval.domain.mapper.CloudErrorMapper
import ru.romanoval.domain.model.Habit
import ru.romanoval.domain.repositories.AppRepository
import ru.romanoval.domain.useCases.base.UseCase
import javax.inject.Inject

class PostHabitsDoneApiUseCase @Inject constructor(
    errorUtil : CloudErrorMapper,
    private val appRepository: AppRepository
) : UseCase<Unit>(errorUtil){

    var habitsFromDb: List<Habit> = emptyList()
    var habitsFromApi: List<Habit> = emptyList()
    override suspend fun executeOnBackground() {
        return appRepository.postHabitsDoneInApi(habitsFromApi, habitsFromDb)
    }
}
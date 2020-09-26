package ru.romanoval.domain.useCases.api

import ru.romanoval.domain.mapper.CloudErrorMapper
import ru.romanoval.domain.model.Habit
import ru.romanoval.domain.repositories.AppRepository
import ru.romanoval.domain.useCases.base.UseCase
import javax.inject.Inject

class GetHabitsApiUseCase @Inject constructor(
    errorUtil: CloudErrorMapper,
    private val appRepository: AppRepository
) : UseCase<List<Habit>>(errorUtil) {

    override suspend fun executeOnBackground(): List<Habit> {
        return appRepository.getHabitsFromApi()
    }

}
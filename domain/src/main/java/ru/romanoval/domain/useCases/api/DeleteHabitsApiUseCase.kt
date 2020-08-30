package ru.romanoval.domain.useCases.api

import ru.romanoval.domain.mapper.CloudErrorMapper
import ru.romanoval.domain.model.Habit
import ru.romanoval.domain.repositories.AppRepository
import ru.romanoval.domain.useCases.base.UseCase
import javax.inject.Inject

class DeleteHabitsApiUseCase @Inject constructor(
    errorUtil: CloudErrorMapper,
    private val appRepository: AppRepository
) : UseCase<Unit>(errorUtil) {

    var habits: List<Habit> = emptyList()
    override suspend fun executeOnBackground() {
        return appRepository.deleteHabitsInApi(habits)
    }
}
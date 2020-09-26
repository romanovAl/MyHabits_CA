package ru.romanoval.domain.useCases.db


import ru.romanoval.domain.mapper.CloudErrorMapper
import ru.romanoval.domain.repositories.AppRepository
import ru.romanoval.domain.useCases.base.UseCase
import javax.inject.Inject

class DeleteAllHabitsDBUseCase @Inject constructor(
    errorUtil : CloudErrorMapper,
    private val appRepository: AppRepository
) : UseCase<Unit>(errorUtil){

    override suspend fun executeOnBackground() {
        return appRepository.deleteAllHabitsFromDb()
    }

}
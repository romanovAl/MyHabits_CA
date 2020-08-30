package ru.romanoval.domain.useCases.db

import ru.romanoval.domain.mapper.CloudErrorMapper
import ru.romanoval.domain.model.Habit
import ru.romanoval.domain.repositories.AppRepository
import ru.romanoval.domain.useCases.base.UseCase
import javax.inject.Inject

class InsertHabitDBUseCase @Inject constructor(
    errorUtil: CloudErrorMapper,
    private val appRepository: AppRepository
) : UseCase<Any>(errorUtil) {

    lateinit var habit : Habit
    override suspend fun executeOnBackground(): Any {
        return appRepository.insertHabit(habit)
    }
}
package ru.romanoval.data.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import ru.romanoval.data.source.cloud.BaseCloudRepository
import ru.romanoval.data.source.db.HabitsDao
import ru.romanoval.data.source.tempSort.SortedHabits
import ru.romanoval.domain.model.Habit
import ru.romanoval.domain.repositories.AppRepository
import javax.inject.Inject

class AppRepoImpl @Inject constructor(
    private val cloudRepository: BaseCloudRepository,
    private val habitsDao: HabitsDao,
    private val sortedHabits: SortedHabits
) : AppRepository{

    //--------------DATABASE--------------------
    override fun selectAllHabits(): Flow<List<Habit>> {
        return habitsDao.selectAllHabits()
    }

    override fun selectGoodHabitsFromDb(): Flow<List<Habit>> {
        return habitsDao.selectGoodHabits()
    }

    override fun selectBadHabitsFromDb(): Flow<List<Habit>> {
        return habitsDao.selectBadHabits()
    }

    override suspend fun insertHabits(habits: List<Habit>) {
        if(habits.isNotEmpty()){
            habits.forEach {
                habitsDao.insertHabit(it)
            }
        }
        return
    }

    override suspend fun insertHabit(habit: Habit) {
        habitsDao.insertHabit(habit)
    }

    override suspend fun updateHabit(habit: Habit) {
        habitsDao.updateHabit(habit)
    }

    override suspend fun deleteHabit(habit: Habit) {
        habitsDao.deleteHabit(habit)
    }

    override suspend fun deleteAllHabitsFromDb() {
        habitsDao.deleteAllHabits()
    }
    //--------------DATABASE--------------------

    //-----------------API---------------------
    override suspend fun getHabitsFromApi(): List<Habit> {
        return cloudRepository.getAllHabits()
    }

    override suspend fun deleteHabitInApi(habit: Habit) {
        return cloudRepository.deleteHabit(habit)
    }

    override suspend fun deleteHabitsInApi(habits: List<Habit>) {
        return cloudRepository.deleteHabits(habits)
    }

    override suspend fun putHabitsInApi(habits: List<Habit>) {
        return cloudRepository.putHabits(habits)
    }

    override suspend fun putHabitInApi(habit: Habit) {
        return cloudRepository.putHabit(habit)
    }

    override suspend fun postHabitsDoneInApi(
        habitsFromApi: List<Habit>,
        habitsFromDb: List<Habit>
    ) {
        return cloudRepository.postHabitsDone(habitsFromApi, habitsFromDb)
    }
    //-----------------API---------------------

    //----------------SORTED-------------------
    @ExperimentalCoroutinesApi
    override fun getSortedHabits(): Flow<List<Habit>> {
        return sortedHabits.sortedHabits
    }

    @ExperimentalCoroutinesApi
    override fun setSortedHabits(newHabits: List<Habit>) {
        sortedHabits.sortedHabits.value = newHabits
    }
    //----------------SORTED-------------------
}
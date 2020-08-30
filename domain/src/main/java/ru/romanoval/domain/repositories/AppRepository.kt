package ru.romanoval.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.romanoval.domain.model.Habit

interface AppRepository {
    //---------------DATABASE-------------------//
    fun selectAllHabits(): Flow<List<Habit>>
    fun selectGoodHabitsFromDb(): Flow<List<Habit>>
    fun selectBadHabitsFromDb(): Flow<List<Habit>>
    suspend fun insertHabits(habits: List<Habit>)
    suspend fun insertHabit(habit: Habit)
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(habit: Habit)
    suspend fun deleteAllHabitsFromDb()
    //---------------DATABASE-------------------//

    //---------------TEMP_SORT------------------//
    fun getSortedHabits() : Flow<List<Habit>>
    fun setSortedHabits(newHabits :List<Habit>)
    //---------------TEMP_SORT------------------//

    //-----------------API----------------------//
    suspend fun getHabitsFromApi(): List<Habit>
    suspend fun deleteHabitInApi(habit: Habit)
    suspend fun deleteHabitsInApi(habits: List<Habit>)
    suspend fun putHabitsInApi(habits: List<Habit>)
    suspend fun putHabitInApi(habit: Habit)
    suspend fun postHabitsDoneInApi(habitsFromApi: List<Habit>, habitsFromDb: List<Habit>)
    //-----------------API----------------------//

}
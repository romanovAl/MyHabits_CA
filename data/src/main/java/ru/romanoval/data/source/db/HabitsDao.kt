package ru.romanoval.data.source.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.romanoval.data.model.Habit

@Dao
interface HabitsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit)

    @Query("SELECT * from Habit ORDER BY bdId DESC")
    fun selectAllHabits(): Flow<List<Habit>>

    @Query("SELECT * from Habit WHERE type = 0 ORDER BY bdId DESC")
    fun selectBadHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM Habit WHERE type = 1 ORDER BY bdId DESC")
    fun selectGoodHabits() : Flow<List<Habit>>

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Update
    suspend fun updateHabit(habit: Habit)

    @Query("DELETE FROM Habit")
    suspend fun deleteAllHabits()

}